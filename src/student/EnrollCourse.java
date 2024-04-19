package student;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import connection.ConnectionManager;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.RoundButton;

public class EnrollCourse extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField studentNameField;
	private JTextField studentIdField;
	private JTextField emailField;
	private Enrollment studentPanel;
	private JTextField phoneNumberField;
	private JTextField addressField;
	private JComboBox<Integer> levelComboBox;

	public EnrollCourse(JFrame parent, Enrollment studentPanel) {
		super(parent, "Enroll Course", true);
		getContentPane().setBackground(Color.WHITE);
		setSize(479, 647);
		setUndecorated(true);

		studentNameField = new JTextField(20);
		studentNameField.setBounds(182, 148, 244, 32);
		studentIdField = new JTextField(20);
		studentIdField.setBounds(182, 201, 250, 32);
		emailField = new JTextField(20);
		emailField.setBounds(182, 257, 250, 32);
		phoneNumberField = new JTextField(20);
		phoneNumberField.setBounds(182, 320, 250, 32);
		addressField = new JTextField(20);
		addressField.setBounds(182, 375, 250, 32);
		levelComboBox = new JComboBox<>(new Integer[] { 4, 5, 6 });
		levelComboBox.setBounds(182, 408, 261, 81);

		JButton enrollButton = new RoundButton("Enroll", Color.WHITE, new Color(50, 144, 255), 30);
		enrollButton.setBounds(119, 511, 100, 37);
		enrollButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enrollCourse();

			}
		});
		getContentPane().setLayout(null);
		JLabel label = new JLabel("Student Name:");
		label.setBounds(61, 145, 121, 38);
		getContentPane().add(label);
		getContentPane().add(studentNameField);
		JLabel label_1 = new JLabel("Student ID:");
		label_1.setBounds(61, 194, 81, 47);
		getContentPane().add(label_1);
		getContentPane().add(studentIdField);
		JLabel label_2 = new JLabel("Email:");
		label_2.setBounds(62, 254, 59, 38);
		getContentPane().add(label_2);
		getContentPane().add(emailField);
		JLabel label_3 = new JLabel("Phone Number:");
		label_3.setBounds(62, 320, 105, 32);
		getContentPane().add(label_3);
		getContentPane().add(phoneNumberField);
		JLabel label_4 = new JLabel("Address:");
		label_4.setBounds(62, 368, 68, 47);
		getContentPane().add(label_4);
		getContentPane().add(addressField);
		JLabel label_5 = new JLabel("Level:");
		label_5.setBounds(62, 436, 59, 22);
		getContentPane().add(label_5);
		getContentPane().add(levelComboBox);
		getContentPane().add(enrollButton);

		RoundButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(273, 509, 100, 37);
		getContentPane().add(cancelButton);

		JLabel lblEnrollCourse = new JLabel("Enroll Course");
		lblEnrollCourse.setForeground(new Color(50, 144, 255));
		lblEnrollCourse.setFont(new Font("Oriya MN", Font.PLAIN, 28));
		lblEnrollCourse.setBounds(61, 59, 250, 38);
		getContentPane().add(lblEnrollCourse);
		this.studentPanel = studentPanel;

		setLocationRelativeTo(parent);
	}

	private void enrollCourse() {
		String studentName = studentNameField.getText();
		String studentId = studentIdField.getText();
		String email = emailField.getText();
		String phoneNumber = phoneNumberField.getText();
		String address = addressField.getText();
		int selectedLevel = (Integer) levelComboBox.getSelectedItem();

		// Get the selected row from the table
		int selectedRow = studentPanel.getCourseTable().getSelectedRow();
		if (selectedRow != -1) {
			String selectedCourse = (String) studentPanel.getCourseTable().getValueAt(selectedRow, 0);

			// Check if the session is available for the selected level
			if (isSessionAvailable(selectedCourse, selectedLevel)) {
				System.out.println("Session is available. Checking if it has started...");

				LocalDate sessionStartDate = getSessionStartDate(selectedCourse, selectedLevel);

				// Check if the session has started
				if (hasSessionStarted(sessionStartDate)) {
					System.out.println("Session not started. Enrolling...");

					try (Connection connection = ConnectionManager.getConnection()) {
						createEnrolledStudentsTable(connection);

						// Insert the enrolled student details into the enrolledstudents table
						String insertQuery = "INSERT INTO enrolledstudents (student_id, student_name, email, phone_number, address, course_name, level) VALUES (?, ?, ?, ?, ?, ?, ?)";

						try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
							preparedStatement.setString(2, studentName);
							preparedStatement.setString(1, studentId);
							preparedStatement.setString(3, email);
							preparedStatement.setString(4, phoneNumber);
							preparedStatement.setString(5, address);
							preparedStatement.setString(6, selectedCourse);
							preparedStatement.setInt(7, selectedLevel);

							int rowsAffected = preparedStatement.executeUpdate();

							if (rowsAffected > 0) {
								String enrollmentMessage = String.format(
										"Enrolling %s (%s) in Course %s\nEmail: %s\nPhone Number: %s\nAddress: %s\nLevel: %d",
										studentName, studentId, selectedCourse, email, phoneNumber, address,
										selectedLevel);
								JOptionPane.showMessageDialog(this, enrollmentMessage);
								dispose();
							} else {
								JOptionPane.showMessageDialog(this, "Enrollment failed. Please try again.");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(this, "Error during enrollment. Please try again.");
					}
				} else {
					JOptionPane.showMessageDialog(this,
							"The selected course session has already started. Cannot enroll.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "The session is not available for the selected level.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a course to enroll.");
		}
	}

	// Method to check if the session is available for the selected level
	private boolean isSessionAvailable(String courseName, int selectedLevel) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String checkSessionQuery = "SELECT * FROM session WHERE session_name = ? AND session_level = ?";
			try (PreparedStatement checkSessionStatement = connection.prepareStatement(checkSessionQuery)) {
				checkSessionStatement.setString(1, courseName);
				checkSessionStatement.setInt(2, selectedLevel);

				try (ResultSet resultSet = checkSessionStatement.executeQuery()) {
					return resultSet.next(); // Returns true if a matching session is found
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Return false in case of an exception or if no matching session is found
		}
	}

	private LocalDate getSessionStartDate(String courseName, int level) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String query = "SELECT session_start FROM session WHERE session_name = ? AND session_level = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, courseName);
				preparedStatement.setInt(2, level);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						String sessionStartString = resultSet.getString("session_start");
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

						try {
							return LocalDate.parse(sessionStartString, formatter);
						} catch (DateTimeParseException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(this,
									"Error parsing session start date. Please check date format.");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error retrieving session start date. Please try again.");
		}
		return null;
	}

	private boolean hasSessionStarted(LocalDate sessionStartDate) {
		if (sessionStartDate != null) {
			LocalDate currentDate = LocalDate.now();
			System.out.println("Current Date: " + currentDate);
			System.out.println("Session Start Date: " + sessionStartDate);

			// Compare dates using isAfter
			if (!(currentDate.isAfter(sessionStartDate))) {
				System.out.println("Session has started.");
				return true;
			} else {
				System.out.println("Session has not started.");
				return false;
			}
		}
		return false;
	}

	private void createEnrolledStudentsTable(Connection connection) throws Exception {
		String createTableQuery = "CREATE TABLE IF NOT EXISTS enrolledstudents (" + "id INT AUTO_INCREMENT,"
				+ "student_id VARCHAR(255)," + "student_name VARCHAR(255)," + "email VARCHAR(255),"
				+ "phone_number VARCHAR(255)," + "address VARCHAR(255)," + "course_name VARCHAR(255)," + "level INT,"
				+ "PRIMARY KEY (id, student_id)" + ")";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createTableQuery);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Student Course Panel");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			Enrollment studentPanel = new Enrollment();
			frame.getContentPane().add(studentPanel);

			EnrollCourse enrollCourseForm = new EnrollCourse(frame, studentPanel);
			enrollCourseForm.setVisible(true);

			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}