package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import connection.ConnectionManager;
import gui.RoundButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddStudentsDetail extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField idField;
	private JTextField phoneField;
	private JTextField emailField;
	private JComboBox<String> courseComboBox;
	private JTextField semesterField;
	private JTextField levelField;
	private DefaultTableModel model;

	public AddStudentsDetail(JFrame parent, DefaultTableModel model) {
		super(parent, "Add Student", true);
		setUndecorated(true);

		getContentPane().setBackground(Color.WHITE);
		setSize(368, 560);

		nameField = new JTextField();
		nameField.setBounds(54, 108, 260, 35);

		idField = new JTextField();
		idField.setBounds(54, 166, 260, 35);

		phoneField = new JTextField();
		phoneField.setBounds(54, 226, 260, 35);

		emailField = new JTextField();
		emailField.setBounds(54, 283, 260, 35);

		courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
		courseComboBox.setBounds(54, 346, 260, 35);
		getContentPane().add(courseComboBox);

		semesterField = new JTextField();
		semesterField.setBounds(54, 419, 260, 35);

		levelField = new JTextField();
		levelField.setBounds(54, 478, 260, 35);

		this.model = model;

		getContentPane().setLayout(null);

		JLabel lblStudentName = new JLabel("Student Name");
		lblStudentName.setBounds(54, 87, 100, 20);
		getContentPane().add(lblStudentName);
		getContentPane().add(nameField);

		JLabel lblStudentId = new JLabel("Student ID");
		lblStudentId.setBounds(54, 145, 100, 20);
		getContentPane().add(lblStudentId);
		getContentPane().add(idField);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(54, 204, 100, 20);
		getContentPane().add(lblPhone);
		getContentPane().add(phoneField);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(54, 262, 100, 20);
		getContentPane().add(lblEmail);
		getContentPane().add(emailField);

		JLabel lblCourse = new JLabel("Course");
		lblCourse.setBounds(54, 324, 80, 20);
		getContentPane().add(lblCourse);
		getContentPane().add(courseComboBox);

		JLabel lblSemester = new JLabel("Semester");
		lblSemester.setBounds(54, 393, 80, 20);
		getContentPane().add(lblSemester);
		getContentPane().add(semesterField);

		JLabel lblLevel = new JLabel("Level"); // New label for level
		lblLevel.setBounds(54, 456, 80, 20);
		getContentPane().add(lblLevel);
		getContentPane().add(levelField);

		JButton okButton = new RoundButton("Add", Color.WHITE, new Color(50, 144, 255), 30);
		okButton.setBounds(57, 518, 110, 40);

		JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.setBounds(197, 518, 110, 40);

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

		JLabel titleLabel = new JLabel("Add Student");
		titleLabel.setForeground(new Color(50, 144, 255));
		titleLabel.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		titleLabel.setBounds(41, 46, 133, 20);
		getContentPane().add(titleLabel);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	// Fetch course names from the database
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
			// Handle the exception or log it
		}

		return courseNames.toArray(new String[0]);
	}

	private void addStudentToDatabase(String name, int id, String phone, String email, int level, int semester,
			String course) {
		try (Connection connection = ConnectionManager.getConnection()) {
			// Create student table if not exists
			createStudentTableIfNotExists(connection);

			// Insert the new student record
			String insertQuery = "INSERT INTO student (name, student_id, phone, email, course, semester, level) VALUES (?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
				insertStatement.setString(1, name);
				insertStatement.setInt(2, id);
				insertStatement.setString(3, phone);
				insertStatement.setString(4, email);
				insertStatement.setString(5, course);
				insertStatement.setInt(6, semester);
				insertStatement.setInt(7, level);

				int affectedRows = insertStatement.executeUpdate();

				if (affectedRows > 0) {
					System.out.println("Student added successfully to the database.");
				} else {
					System.out.println("No rows were added to the database.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error adding student to the database: " + ex.getMessage());
			JOptionPane.showMessageDialog(this, "Error adding student to the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onAdd() {
		String newName = getNewName();
		int newId = getNewId();
		String newPhone = getNewPhone();
		String newEmail = getNewEmail();
		String newCourse = getSelectedCourse();
		int newSemester = getNewSemester();
		int newLevel = getNewLevel();

		// Validation logic

		// Check if any of the required fields are empty
		if (newName.trim().isEmpty() || newPhone.trim().isEmpty() || newCourse.trim().isEmpty() || newId <= 0
				|| newSemester <= 0) {
			JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Validate email format
		if (!isValidEmail(newEmail)) {
			JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		for (int i = 0; i < model.getRowCount(); i++) {
			int existingId = (int) model.getValueAt(i, 1);

			if (existingId == newId) {
				JOptionPane.showMessageDialog(this, "Student with the same ID already exists.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		model.addRow(new Object[] { newName, newId, newPhone, newEmail, newLevel, newSemester, newCourse });

		addStudentToDatabase(newName, newId, newPhone, newEmail, newLevel, newSemester, newCourse);

		dispose();

	}

	private String getSelectedCourse() {
		return Objects.requireNonNull(courseComboBox.getSelectedItem()).toString();
	}

	// Validate email format
	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email.matches(emailRegex);
	}

	private void createStudentTableIfNotExists(Connection connection) {
		try (Statement statement = connection.createStatement()) {
			String createTableQuery = "CREATE TABLE IF NOT EXISTS student (" + "id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "name VARCHAR(255) NOT NULL," + "student_id INT UNIQUE NOT NULL," + "phone VARCHAR(20),"
					+ "email VARCHAR(255)," + "course VARCHAR(255)," + "semester INT," + "level INT" + ")";
			statement.executeUpdate(createTableQuery);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error creating student table: " + ex.getMessage());
			JOptionPane.showMessageDialog(this, "Error creating student table.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public int getNewId() {
		try {
			return Integer.parseInt(idField.getText());
		} catch (NumberFormatException e) {
			return -1; // or handle the error accordingly
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

	public int getNewLevel() {
		try {
			return Integer.parseInt(levelField.getText());
		} catch (NumberFormatException e) {
			return -1; // or handle the error accordingly
		}
	}

	public int getNewSemester() {
		try {
			return Integer.parseInt(semesterField.getText());
		} catch (NumberFormatException e) {
			return -1; // or handle the error accordingly
		}
	}
}
