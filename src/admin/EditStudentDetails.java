package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import gui.RoundButton;
import connection.ConnectionManager;

public class EditStudentDetails extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JComboBox<String> courseComboBox;
	private JTextField semesterField;
	private DefaultTableModel model;
	private int selectedRow;
	private JTextField idField;
	private JTextField phoneField;
	private JTextField levelField;
	private JTextField emailField;

	public EditStudentDetails(JFrame parent, String currentName, int currentId, String currentPhone, String currentEmail,
			int currentLevel, int currentSemester, String currentCourse, DefaultTableModel model, int selectedRow) {
		setUndecorated(true);

		getContentPane().setBackground(Color.WHITE);
		setSize(368, 580);

		nameField = new JTextField(currentName);
		nameField.setBounds(54, 174, 260, 35);

		// Replace JTextField with JComboBox for the course
		courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
		courseComboBox.setSelectedItem(currentCourse);
		courseComboBox.setBounds(54, 366, 260, 35);
		getContentPane().add(courseComboBox);

		semesterField = new JTextField(String.valueOf(currentSemester));
		semesterField.setBounds(54, 434, 260, 35);
		this.model = model; // Use this.model instead of model
		this.selectedRow = selectedRow;

		JLabel lblId = new JLabel("Student ID");
		lblId.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		lblId.setBounds(54, 86, 100, 20);
		getContentPane().add(lblId);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		lblPhone.setBounds(54, 223, 80, 20);
		getContentPane().add(lblPhone);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(54, 285, 80, 20);
		getContentPane().add(lblEmail);

		// Inside the constructor
		idField = new JTextField(String.valueOf(currentId));
		idField.setBounds(54, 106, 260, 35);
		phoneField = new JTextField(currentPhone);
		phoneField.setBounds(54, 243, 260, 35);
		emailField = new JTextField(currentEmail);
		emailField.setBounds(54, 303, 260, 35);

		getContentPane().add(idField);
		getContentPane().add(phoneField);
		getContentPane().add(emailField);

		getContentPane().setLayout(null);

		JLabel lblStudentName = new JLabel("Student Name");
		lblStudentName.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		lblStudentName.setBounds(54, 154, 100, 20);
		getContentPane().add(lblStudentName);
		getContentPane().add(nameField);

		JLabel lblCourse = new JLabel("Course");
		lblCourse.setBounds(54, 344, 80, 20);
		getContentPane().add(lblCourse);

		levelField = new JTextField(String.valueOf(currentLevel));
		levelField.setBounds(54, 494, 260, 35); // New field for level
		getContentPane().add(levelField);

		JLabel lblSemester = new JLabel("Semester");
		lblSemester.setBounds(54, 413, 80, 20);
		getContentPane().add(lblSemester);
		getContentPane().add(semesterField);

		JButton okButton = new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
		okButton.setBounds(54, 534, 110, 40);
		JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.setBounds(204, 534, 110, 40);

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onOK();
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

		JLabel titleLabel = new JLabel("Edit Student");
		titleLabel.setForeground(new Color(50, 144, 255));
		titleLabel.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		titleLabel.setBounds(27, 41, 133, 20);
		getContentPane().add(titleLabel);

		JLabel lblNewLabel = new JLabel("LEVEL");
		lblNewLabel.setBounds(54, 481, 61, 16);
		getContentPane().add(lblNewLabel);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void onOK() {
		String newName = getNewName();
		int newId = getNewId();
		String newPhone = getNewPhone();
		String newEmail = getNewEmail();
		String newCourse = getNewCourse();
		int newSemester = getNewSemester();
		int newLevel = getNewLevel();

		if (newName.trim().isEmpty()) {
			showError("Please enter a student name.");
			return;
		}

		if (isIdDuplicate(newId, selectedRow)) {
			showError("Student with the same ID already exists.");
			return;
		}

		if (!isValidSemester(newSemester)) {
			showError("Please enter a valid positive semester.");
			return;
		}

		if (newId != getOriginalId()) {
			showError("Cannot change the Student ID.");
			return;
		}

		model.setValueAt(newName, selectedRow, 0);
		model.setValueAt(newId, selectedRow, 1);
		model.setValueAt(newPhone, selectedRow, 2);
		model.setValueAt(newEmail, selectedRow, 3);
		model.setValueAt(newLevel, selectedRow, 4);
		model.setValueAt(newCourse, selectedRow, 6);
		model.setValueAt(newSemester, selectedRow, 5);

		// Update the database
		updateStudentInDatabase(newId, newName, newPhone, newEmail, newCourse, newSemester, newLevel);

		dispose();
	}

	private int getOriginalId() {
		return (int) model.getValueAt(selectedRow, 1);
	}

	private void updateStudentInDatabase(int id, String name, String phone, String email, String course, int semester,
			int level) {
		try (Connection connection = ConnectionManager.getConnection()) {

			String updateQuery = "UPDATE student SET name=?, phone=?, email=?, course=?, semester=?, level=? WHERE student_id=?";
			try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
				updateStatement.setString(1, name);
				updateStatement.setString(2, phone);
				updateStatement.setString(3, email);
				updateStatement.setString(4, course);
				updateStatement.setInt(5, semester);
				updateStatement.setInt(6, level);
				updateStatement.setInt(7, id);

				int affectedRows = updateStatement.executeUpdate();

				if (affectedRows > 0) {
					System.out.println("Student updated successfully in the database.");
				} else {
					System.out.println("No rows were updated in the database.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error updating student in the database: " + ex.getMessage());
			JOptionPane.showMessageDialog(this, "Error updating student in the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
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

	public int getNewLevel() {
		try {
			return Integer.parseInt(levelField.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private boolean isIdDuplicate(int newId, int selectedRow) {
		for (int i = 0; i < model.getRowCount(); i++) {
			if (i != selectedRow && (int) model.getValueAt(i, 1) == newId) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidSemester(int semester) {
		return semester > 0;
	}

	public int getNewId() {
		try {
			return Integer.parseInt(idField.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public String getNewPhone() {
		return phoneField.getText();
	}

	public String getNewEmail() {
		return emailField.getText();
	}

	private void onCancel() {
		dispose();
	}

	public String getNewName() {
		return nameField.getText();
	}

	public String getNewCourse() {
		return (String) courseComboBox.getSelectedItem();
	}

	public int getNewSemester() {
		try {
			return Integer.parseInt(semesterField.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
