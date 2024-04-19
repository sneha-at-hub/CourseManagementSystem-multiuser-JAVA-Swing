package admin;

import javax.swing.*;

import connection.ConnectionManager;
import gui.RoundButton;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EditTeacher extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField nameTextField;
	private JTextField subjectTextField;
	private JTextField emailTextField;
	private JTextField phoneTextField;
	private JComboBox<String> courseComboBox;
	private JButton updateButton;
	private boolean updateSuccessful = false;

	public EditTeacher(JFrame parentFrame, int currentId, String currentName, String currentSubject,
			String currentCourse, String currentEmail, String currentPhone) {
		super(parentFrame, "Edit Teacher", true);
		initialize(currentId, currentName, currentSubject, currentCourse, currentEmail, currentPhone);
	}

	private void initialize(int currentId, String currentName, String currentSubject, String currentCourse,
			String currentEmail, String currentPhone) {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setLayout(null);

		JLabel nameLabel = new JLabel("Name:");
		setUndecorated(true);
		nameLabel.setBounds(44, 114, 51, 37);
		panel.add(nameLabel);
		nameTextField = new JTextField(currentName);
		nameTextField.setBounds(117, 114, 255, 37);
		panel.add(nameTextField);

		JLabel subjectLabel = new JLabel("Subject:");
		subjectLabel.setBounds(44, 175, 61, 32);
		panel.add(subjectLabel);
		subjectTextField = new JTextField(currentSubject);
		subjectTextField.setBounds(117, 173, 255, 37);
		panel.add(subjectTextField);

		JLabel courseLabel = new JLabel("Course:");
		courseLabel.setBounds(44, 231, 61, 32);
		panel.add(courseLabel);
		courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
		courseComboBox.setBounds(117, 232, 255, 32);
		courseComboBox.setSelectedItem(currentCourse);
		panel.add(courseComboBox);

		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setBounds(44, 280, 69, 32);
		panel.add(emailLabel);
		emailTextField = new JTextField(currentEmail);
		emailTextField.setBounds(117, 278, 255, 37);
		panel.add(emailTextField);

		JLabel phoneLabel = new JLabel("Phone:");
		phoneLabel.setBounds(44, 344, 69, 32);
		panel.add(phoneLabel);
		phoneTextField = new JTextField(currentPhone);
		phoneTextField.setBounds(118, 342, 255, 37);
		panel.add(phoneTextField);

		updateButton = new RoundButton("Update", Color.WHITE, new Color(50, 144, 255), 30);
		updateButton.setBounds(67, 448, 100, 37);
		updateButton.addActionListener(e -> updateTeacher(currentId));
		panel.add(updateButton);

		JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.setBounds(219, 448, 100, 37);
		cancelButton.addActionListener(e -> dispose());
		panel.add(cancelButton);

		getContentPane().add(panel);
		
		JLabel lblAddMarks = new JLabel("Add Marks");
		lblAddMarks.setForeground(new Color(50, 144, 255));
		lblAddMarks.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblAddMarks.setBounds(41, 38, 200, 37);
		panel.add(lblAddMarks);
		setSize(400,557);
		setLocationRelativeTo(null);
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

	private void updateTeacher(int currentId) {
		try {
			String updatedName = nameTextField.getText();
			String updatedSubject = subjectTextField.getText();
			String updatedCourse = (String) courseComboBox.getSelectedItem();
			String updatedEmail = emailTextField.getText();
			String updatedPhone = phoneTextField.getText();

			try (Connection connection = ConnectionManager.getConnection()) {
				String updateQuery = "UPDATE teacher SET teacher_name = ?, subject = ?, course = ?, email = ?, phone = ? WHERE id = ?";
				try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
					preparedStatement.setString(1, updatedName);
					preparedStatement.setString(2, updatedSubject);
					preparedStatement.setString(3, updatedCourse);
					preparedStatement.setString(4, updatedEmail);
					preparedStatement.setString(5, updatedPhone);
					preparedStatement.setInt(6, currentId);

					int rowsUpdated = preparedStatement.executeUpdate();
					if (rowsUpdated > 0) {
						updateSuccessful = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		dispose();
	}

	public boolean isUpdateSuccessful() {
		return updateSuccessful;
	}

	public String getUpdatedName() {
		return nameTextField.getText();
	}

	public String getUpdatedSubject() {
		return subjectTextField.getText();
	}

	public String getUpdatedCourse() {
		return (String) courseComboBox.getSelectedItem();
	}

	public String getUpdatedEmail() {
		return emailTextField.getText();
	}

	public String getUpdatedPhone() {
		return phoneTextField.getText();
	}
}