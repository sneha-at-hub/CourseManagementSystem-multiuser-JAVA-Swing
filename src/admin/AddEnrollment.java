package admin;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import connection.ConnectionManager;
import gui.RoundButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddEnrollment extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField studentIdField;
	private JTextField studentNameField;
	private JTextField emailField;
	private JTextField phoneNumberField;
	private JTextField addressField;
	private Enrollment enrolledStudentPanel;
	private JComboBox<String> courseNameComboBox;
	private JComboBox<Integer> levelComboBox;

	public AddEnrollment(Enrollment enrolledStudentPanel) {
		getContentPane().setBackground(Color.WHITE);
		this.enrolledStudentPanel = enrolledStudentPanel;
		initialize();
	}

	private void initialize() {
		setTitle("Enroll Student");
		setSize(417, 571);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel label = new JLabel("Student ID:");
		label.setBounds(39, 67, 149, 45);
		getContentPane().add(label);
		studentIdField = new JTextField();
		studentIdField.setBounds(175, 67, 217, 37);
		getContentPane().add(studentIdField);
		JLabel label_1 = new JLabel("Student Name:");
		label_1.setBounds(39, 124, 133, 45);
		getContentPane().add(label_1);
		studentNameField = new JTextField();
		studentNameField.setBounds(175, 128, 217, 37);
		getContentPane().add(studentNameField);

		JLabel label_2 = new JLabel("Email:");
		label_2.setBounds(39, 181, 108, 45);
		getContentPane().add(label_2);
		emailField = new JTextField();
		emailField.setBounds(175, 185, 217, 37);
		getContentPane().add(emailField);

		JLabel label_3 = new JLabel("Phone Number:");
		label_3.setBounds(39, 238, 125, 45);
		getContentPane().add(label_3);
		phoneNumberField = new JTextField();
		phoneNumberField.setBounds(175, 242, 217, 37);
		getContentPane().add(phoneNumberField);

		JLabel label_4 = new JLabel("Address:");
		label_4.setBounds(39, 295, 125, 45);
		getContentPane().add(label_4);
		addressField = new JTextField();
		addressField.setBounds(175, 299, 217, 37);
		getContentPane().add(addressField);

		JLabel label_5 = new JLabel("Course Name:");
		label_5.setBounds(39, 350, 108, 45);
		getContentPane().add(label_5);
		String[] courseNames = getCourseNamesFromDatabase();
		courseNameComboBox = new JComboBox<>(courseNames);
		courseNameComboBox.setBounds(175, 351, 217, 45);
		getContentPane().add(courseNameComboBox);

		JLabel label_6 = new JLabel("Level:");
		label_6.setBounds(39, 396, 140, 45);
		getContentPane().add(label_6);
		Integer[] levels = { 4, 5, 6 };
		levelComboBox = new JComboBox<>(levels);
		levelComboBox.setBounds(175, 397, 217, 45);
		getContentPane().add(levelComboBox);

		JButton enrollButton = new RoundButton("Enroll", Color.WHITE, new Color(50, 144, 255), 30);
		enrollButton.setBounds(71, 463, 101, 37);
		enrollButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enrollStudent();
			}
		});
		getContentPane().add(enrollButton);
		JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.setBounds(225, 463, 108, 37);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // Close the form when "Cancel" is clicked
			}
		});
		getContentPane().add(cancelButton);

		JLabel lblEnrollStudent = new JLabel("Enroll Student");
		lblEnrollStudent.setForeground(new Color(50, 144, 255));
		lblEnrollStudent.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblEnrollStudent.setBounds(40, 35, 178, 20);
		getContentPane().add(lblEnrollStudent);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void enrollStudent() {
		if (validateInput()) {
			String studentId = studentIdField.getText();
			String studentName = studentNameField.getText();
			String email = emailField.getText();
			String phoneNumber = phoneNumberField.getText();
			String address = addressField.getText();
			String courseName = (String) courseNameComboBox.getSelectedItem();
			int level = (int) levelComboBox.getSelectedItem();

			saveToDatabase(studentId, studentName, email, phoneNumber, address, level, courseName);
			fetchDataAndDisplay();
			updateEnrolledStudentTable();
			dispose();
		}
	}

	private boolean validateInput() {
		return true;
	}

	private void saveToDatabase(String studentId, String studentName, String email, String phoneNumber, String address,
			int level, String courseName) {
		try (Connection connection = ConnectionManager.getConnection()) {
			if (connection != null) {
				String query = "INSERT INTO enrolledstudents (student_id, student_name, email, phone_number, address, level, course_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
					preparedStatement.setString(1, studentId);
					preparedStatement.setString(2, studentName);
					preparedStatement.setString(3, email);
					preparedStatement.setString(4, phoneNumber);
					preparedStatement.setString(5, address);
					preparedStatement.setInt(6, level);
					preparedStatement.setString(7, courseName);

					preparedStatement.executeUpdate();
				}
			} else {
				System.err.println("Failed to establish a database connection.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error saving data to the database: " + ex.getMessage());
		}
	}

	private void updateEnrolledStudentTable() {
		fetchDataAndDisplay();
	}

	private String[] getCourseNamesFromDatabase() {
		List<String> courseNames = new ArrayList<>();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT course_name FROM course")) {

			while (resultSet.next()) {
				courseNames.add(resultSet.getString("course_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return courseNames.toArray(new String[0]);
	}

	private void fetchDataAndDisplay() {
		DefaultTableModel model = fetchDataFromDatabase();
		enrolledStudentPanel.setTableModel(model);
	}

	private DefaultTableModel fetchDataFromDatabase() {
		DefaultTableModel model = new DefaultTableModel();

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM enrolledstudents")) {

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				model.addColumn(metaData.getColumnName(i));
			}

			while (resultSet.next()) {
				Object[] rowData = new Object[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					rowData[i - 1] = resultSet.getObject(i);
				}
				model.addRow(rowData);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return model;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Enrollment enrolledStudentPanel = new Enrollment();
			new AddEnrollment(enrolledStudentPanel);
		});
	}
}