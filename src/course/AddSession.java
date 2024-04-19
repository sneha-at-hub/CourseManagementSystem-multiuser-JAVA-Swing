package course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.ConnectionManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import gui.RoundButton;

public class AddSession extends JDialog {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> courseComboBox;
	private JFormattedTextField sessionStartField;
	private JFormattedTextField sessionEndField;
	private JComboBox<String> sessionLevelComboBox;

	private DefaultTableModel sessionTableModel;

	public AddSession(JFrame parentFrame, DefaultTableModel sessionTableModel) {
		super(parentFrame, "Add Session", true);
		getContentPane().setBackground(Color.WHITE);
		setSize(500,500);
		setUndecorated(true);
		this.sessionTableModel = sessionTableModel;

		JLabel courseLabel = new JLabel("Course:");
		courseLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		courseLabel.setBounds(70, 119, 91, 46);
		courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
		courseComboBox.setBounds(173, 120, 250, 46);

		JLabel sessionLevelLabel = new JLabel("Session Level:");
		sessionLevelLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		sessionLevelLabel.setBounds(70, 166, 110, 46);
		sessionLevelComboBox = new JComboBox<>(new String[] { "4", "5", "6" });
		sessionLevelComboBox.setBounds(173, 167, 250, 46);

		JLabel sessionStartLabel = new JLabel("Session Start:");
		sessionStartLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		sessionStartLabel.setBounds(70, 219, 104, 46);
		JLabel sessionEndLabel = new JLabel("Session End:");
		sessionEndLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		sessionEndLabel.setBounds(70, 277, 91, 46);

		MaskFormatter dateFormatter;
		try {
			dateFormatter = new MaskFormatter("####-##-##");
		} catch (ParseException e) {
			e.printStackTrace();
			dateFormatter = new MaskFormatter();
		}
		sessionStartField = new JFormattedTextField(dateFormatter);
		sessionStartField.setBounds(173, 224, 240, 37);
		sessionEndField = new JFormattedTextField(dateFormatter);
		sessionEndField.setBounds(173, 282, 240, 37);

		JButton addButton = new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
		addButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		addButton.setBounds(102, 361, 100, 37);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSession();
			}
		});
		getContentPane().setLayout(null);

		getContentPane().add(courseLabel);
		getContentPane().add(courseComboBox);
		getContentPane().add(sessionLevelLabel);
		getContentPane().add(sessionLevelComboBox);
		getContentPane().add(sessionStartLabel);
		getContentPane().add(sessionStartField);
		getContentPane().add(sessionEndLabel);
		getContentPane().add(sessionEndField);
		getContentPane().add(addButton);
		
		RoundButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(283, 361, 100, 37);
		getContentPane().add(cancelButton);
		
		JLabel lblAddSessions = new JLabel("Add Sessions");
		lblAddSessions.setForeground(new Color(50, 144, 255));
		lblAddSessions.setFont(new Font("Oriya MN", Font.PLAIN, 27));
		lblAddSessions.setBounds(70, 52, 200, 37);
		getContentPane().add(lblAddSessions);

		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void addSession() {
		String courseName = (String) courseComboBox.getSelectedItem();
		String sessionLevel = (String) sessionLevelComboBox.getSelectedItem();
		String sessionStart = sessionStartField.getText();
		String sessionEnd = sessionEndField.getText();

		if (courseName.isEmpty() || sessionLevel.isEmpty() || sessionStart.isEmpty() || sessionEnd.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		sessionTableModel.addRow(new Object[] { courseName, sessionLevel, sessionStart, sessionEnd });

		insertSessionIntoDatabase(courseName, sessionLevel, sessionStart, sessionEnd);

		dispose();
	}

	private void insertSessionIntoDatabase(String courseName, String sessionLevel, String sessionStart,
			String sessionEnd) {
		try (Connection connection = ConnectionManager.getConnection()) {
			createSessionTableIfNotExists(connection);

			String insertSQL = "INSERT INTO session (session_name, session_level, session_start, session_end) VALUES (?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
				preparedStatement.setString(1, courseName);
				preparedStatement.setString(2, sessionLevel);
				preparedStatement.setString(3, sessionStart);
				preparedStatement.setString(4, sessionEnd);

				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Session inserted into the database successfully.");
				} else {
					System.out.println("Failed to insert session into the database.");
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to insert session into the database. Error: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
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

		}

		return courseNames.toArray(new String[0]);
	}

	private void createSessionTableIfNotExists(Connection connection) throws SQLException {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS session (" + "id INT PRIMARY KEY AUTO_INCREMENT,"
				+ "session_name VARCHAR(255) NOT NULL," + "session_level VARCHAR(255) NOT NULL,"
				+ "session_start VARCHAR(10) NOT NULL," + "session_end VARCHAR(10) NOT NULL" + ")";

		try (PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL)) {
			createTableStatement.executeUpdate();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Test Frame");
				frame.setSize(400, 300);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);

				JButton openButton = new JButton("Open Add Session Form");
				openButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						AddSession addSessionForm = new AddSession(frame, new DefaultTableModel());
						addSessionForm.setVisible(true);
					}
				});

				frame.getContentPane().setLayout(new FlowLayout());
				frame.getContentPane().add(openButton);
				frame.setVisible(true);
			}
		});
	}
}
