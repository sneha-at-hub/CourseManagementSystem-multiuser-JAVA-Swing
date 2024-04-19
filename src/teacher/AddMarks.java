package teacher;

import javax.swing.*;

import connection.ConnectionManager;
import gui.RoundButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddMarks extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField marksField;
	private JButton saveButton;
	private Object[] selectedRowData;

	public AddMarks(JFrame parent, Object[] selectedRowData) {
		super(parent, "Add Marks", true);
		setUndecorated(true);
		getContentPane().setBackground(Color.WHITE);
		setSize(393, 392);
		this.selectedRowData = selectedRowData;

		initComponents();
		layoutComponents();
		addListeners();

		// Display student details in the dialog
		updateStudentDetails();
	}

	private void initComponents() {
		marksField = new JTextField(10);
		marksField.setBounds(94, 222, 134, 31);
		saveButton = new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
		saveButton.setBounds(80, 322, 100, 37);
	}

	private void layoutComponents() {
		getContentPane().setLayout(null);

		JLabel label = new JLabel("Student ID: " + selectedRowData[0]);
		label.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		label.setBounds(46, 64, 191, 57);
		getContentPane().add(label);
		JLabel label_1 = new JLabel("Student Name: " + selectedRowData[1]);
		label_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		label_1.setBounds(46, 105, 332, 57);
		getContentPane().add(label_1);
		JLabel label_2 = new JLabel("Module Name: " + selectedRowData[6]);
		label_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		label_2.setBounds(46, 153, 346, 57);
		getContentPane().add(label_2);
		JLabel lblMarks = new JLabel("Marks");
		lblMarks.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		lblMarks.setBounds(45, 222, 89, 31);
		getContentPane().add(lblMarks);
		getContentPane().add(marksField);
		getContentPane().add(saveButton);

		RoundButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(209, 322, 100, 37);
		getContentPane().add(cancelButton);

		JLabel lblAddMarks = new JLabel("Add Marks");
		lblAddMarks.setForeground(new Color(50, 144, 255));
		lblAddMarks.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblAddMarks.setBounds(37, 25, 200, 37);
		getContentPane().add(lblAddMarks);

		setLocationRelativeTo(null);
	}

	private void addListeners() {
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveMarksToDatabase();
				dispose();
			}
		});
	}

	private void saveMarksToDatabase() {
		try (Connection connection = ConnectionManager.getConnection()) {
			if (marksAlreadyAdded(connection)) {
				JOptionPane.showMessageDialog(this, "Marks are already added for the selected student and module.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String insertQuery = "INSERT INTO resultslip (id, email, marks, module_name, level, student_name) VALUES (?, ?, ?, ?, ?, ?)";

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
				preparedStatement.setInt(1, (int) selectedRowData[0]);
				preparedStatement.setString(2, selectedRowData[3].toString());
				int marksValue;
				try {
					marksValue = Integer.parseInt(marksField.getText());
					if (marksValue < 0 || marksValue > 100) {
						throw new NumberFormatException("Marks must be between 0 and 100.");
					}
				} catch (NumberFormatException e) {
					throw new SQLException("Invalid marks value. " + e.getMessage());
				}

				preparedStatement.setInt(3, marksValue);
				preparedStatement.setString(4, selectedRowData[6].toString());
				preparedStatement.setInt(5, Integer.parseInt(selectedRowData[5].toString()));
				preparedStatement.setString(6, selectedRowData[1].toString());

				// Execute the insert query
				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("Marks added successfully!");
				} else {
					System.out.println("No rows inserted. Check your conditions.");
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving marks to the database. " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean marksAlreadyAdded(Connection connection) throws SQLException {
		String checkQuery = "SELECT * FROM resultslip WHERE id = ? AND module_name = ?";
		try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
			checkStatement.setInt(1, (int) selectedRowData[0]);
			checkStatement.setString(2, selectedRowData[6].toString());

			try (ResultSet resultSet = checkStatement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	private void updateStudentDetails() {
		System.out.println("Selected Student ID: " + selectedRowData[0]);
		System.out.println("Selected Student Name: " + selectedRowData[5]);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Add Marks Dialog");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400, 200);
			frame.setLocationRelativeTo(null);

			Object[] selectedRowData = { 1, "john@example.com", 0, "Module1", 1, "John Doe" };

			AddMarks addMarksDialog = new AddMarks(frame, selectedRowData);
			addMarksDialog.setVisible(true);
		});
	}
}
