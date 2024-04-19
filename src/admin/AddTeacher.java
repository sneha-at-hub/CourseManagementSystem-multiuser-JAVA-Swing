package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionManager; // Assuming you have a connection package
import gui.RoundButton;

public class AddTeacher extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField subjectField;
	private JTextField emailField;
	private JTextField phoneField;
	private JComboBox<String> courseComboBox;
	private DefaultTableModel model;

	public AddTeacher(JFrame parent, DefaultTableModel model) {
		super(parent, "Add Teacher", true);
		setUndecorated(true);

		getContentPane().setBackground(Color.WHITE);
		setSize(368, 560);

		nameField = new JTextField();
		nameField.setBounds(54, 108, 260, 35);

		subjectField = new JTextField();
		subjectField.setBounds(54, 166, 260, 35);

		phoneField = new JTextField();
		phoneField.setBounds(54, 226, 260, 35);

		emailField = new JTextField();
		emailField.setBounds(54, 283, 260, 35);

		courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
		courseComboBox.setBounds(54, 341, 260, 35);

		this.model = model;

		getContentPane().setLayout(null);

		JLabel lblTeacherName = new JLabel("Teacher Name");
		lblTeacherName.setBounds(54, 87, 100, 20);
		getContentPane().add(lblTeacherName);
		getContentPane().add(nameField);

		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(54, 145, 100, 20);
		getContentPane().add(lblSubject);
		getContentPane().add(subjectField);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(54, 204, 100, 20);
		getContentPane().add(lblPhone);
		getContentPane().add(phoneField);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(54, 262, 100, 20);
		getContentPane().add(lblEmail);
		getContentPane().add(emailField);

		JLabel lblCourse = new JLabel("Course");
		lblCourse.setBounds(54, 319, 100, 20);
		getContentPane().add(lblCourse);
		getContentPane().add(courseComboBox);

		JButton okButton = new RoundButton("Add", Color.WHITE, new Color(50, 144, 255), 30);
		okButton.setBounds(57, 478, 110, 40);

		JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.setBounds(197, 478, 110, 40);

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onAdd();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		getContentPane().add(okButton);
		getContentPane().add(cancelButton);

		JLabel titleLabel = new JLabel("Add Teacher");
		titleLabel.setForeground(new Color(50, 144, 255));
		titleLabel.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		titleLabel.setBounds(41, 46, 133, 20);
		getContentPane().add(titleLabel);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

	private void onAdd() {

		String newName = getNewName();
		String newSubject = getNewSubject();
		String newPhone = getNewPhone();
		String newEmail = getNewEmail();
		String selectedCourse = (String) courseComboBox.getSelectedItem();

		if (newName.isEmpty() || newSubject.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty()) {
			showError("Please fill in all required fields.");
			return;
		}

		model.addRow(new Object[] { null, newName, newSubject, selectedCourse, newEmail, newPhone });

		insertTeacherIntoDatabase(newName, newSubject, selectedCourse, newEmail, newPhone);

		dispose();
	}

	private void insertTeacherIntoDatabase(String name, String subject, String course, String email, String phone) {
		try (Connection connection = ConnectionManager.getConnection()) {
			createTeacherTable(connection);

			String insertQuery = "INSERT INTO teacher (teacher_name, subject, course, email, phone) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
					Statement.RETURN_GENERATED_KEYS)) {
				preparedStatement.setString(1, name);
				preparedStatement.setString(2, subject);
				preparedStatement.setString(3, course);
				preparedStatement.setString(4, email);
				preparedStatement.setString(5, phone);

				int rowsAffected = preparedStatement.executeUpdate();

				if (rowsAffected > 0) {
					ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
					if (generatedKeys.next()) {
						int generatedID = generatedKeys.getInt(1);

						updateTableID(generatedID);
					}
					System.out.println("Teacher added to the database successfully!");
				} else {
					showError("Failed to add teacher to the database.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showError("Error adding teacher to the database: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			showError("Unexpected error: " + e.getMessage());
		}
	}

	private void updateTableID(int generatedID) {
		int rowCount = model.getRowCount();
		model.setValueAt(generatedID, rowCount - 1, 0);
	}

	private void showError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void createTeacherTable(Connection connection) {
		String createTableQuery = "CREATE TABLE IF NOT EXISTS teacher (" + "id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "teacher_name VARCHAR(255) NOT NULL," + "subject VARCHAR(255) NOT NULL,"
				+ "course VARCHAR(255) NOT NULL," + "email VARCHAR(255) NOT NULL," + "phone VARCHAR(20) NOT NULL" + ")";
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(createTableQuery);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void onCancel() {
		dispose();
	}

	public String getNewName() {
		return nameField.getText();
	}

	private String getNewSubject() {
		return subjectField.getText();
	}

	private String getNewPhone() {
		return phoneField.getText();
	}

	private String getNewEmail() {
		return emailField.getText();
	}
}
