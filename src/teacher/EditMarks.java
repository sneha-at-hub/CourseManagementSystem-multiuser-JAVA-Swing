package teacher;

import javax.swing.*;

import connection.ConnectionManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import gui.RoundButton;

public class EditMarks extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField marksField;
	private JButton saveButton;
	private Object[] selectedRowData;

	private JLabel studentIdLabel;
	private JLabel studentNameLabel;
	private JLabel courseLabel;
	private JLabel emailLabel;
	private JLabel phoneLabel;
	private JLabel levelLabel;
	private JLabel moduleNameLabel;

	public EditMarks(JFrame parent, Object[] selectedRowData) {
		super(parent, "Edit Marks", true);
		getContentPane().setBackground(Color.WHITE);
		this.selectedRowData = selectedRowData;
		setSize(400, 400);

		initComponents();
		layoutComponents();
		setUndecorated(true);
		addListeners();

		// Display student details in JLabels
		updateStudentDetails();
	}

	private void initComponents() {
		marksField = new JTextField(10);
		marksField.setBounds(158, 5, 130, 26);
		saveButton = new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
		saveButton.setBounds(75, 329, 100, 37);

		// Initialize JLabels
		studentIdLabel = new JLabel();
		studentIdLabel.setBounds(171, 35, 223, 42);
		studentNameLabel = new JLabel();
		studentNameLabel.setBounds(171, 75, 223, 42);
		courseLabel = new JLabel();
		courseLabel.setBounds(171, 129, 223, 42);
		emailLabel = new JLabel();
		emailLabel.setBounds(171, 167, 223, 42);
		phoneLabel = new JLabel();
		levelLabel = new JLabel();
		moduleNameLabel = new JLabel();
		moduleNameLabel.setBounds(171, 221, 223, 42);
	}

	private void layoutComponents() {

		JPanel marksPanel = new JPanel();
		marksPanel.setBackground(Color.WHITE);
		marksPanel.setBounds(6, 275, 363, 42);
		marksPanel.setLayout(null);
		JLabel label_5 = new JLabel("Enter Marks:");
		label_5.setBounds(27, 10, 78, 16);
		marksPanel.add(label_5);
		marksPanel.add(marksField);
		getContentPane().setLayout(null);

		// Add JLabels for student details
		JLabel label = new JLabel("Student ID:");
		label.setBounds(24, 35, 123, 42);
		getContentPane().add(label);
		getContentPane().add(studentIdLabel);
		JLabel label_1 = new JLabel("Student Name:");
		label_1.setBounds(24, 75, 123, 42);
		getContentPane().add(label_1);
		getContentPane().add(studentNameLabel);
		JLabel label_2 = new JLabel("Course:");
		label_2.setBounds(24, 129, 94, 42);
		getContentPane().add(label_2);
		getContentPane().add(courseLabel);
		JLabel label_3 = new JLabel("Email:");
		label_3.setBounds(24, 167, 94, 42);
		getContentPane().add(label_3);
		getContentPane().add(emailLabel);
		JLabel label_4 = new JLabel("ModuleName:");
		label_4.setBounds(24, 221, 94, 42);
		getContentPane().add(label_4);
		getContentPane().add(moduleNameLabel);

		getContentPane().add(marksPanel);
		getContentPane().add(saveButton);

		RoundButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(223, 329, 100, 37);
		getContentPane().add(cancelButton);

		setLocationRelativeTo(null);
	}

	private void addListeners() {
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveMarksToDatabase();
					dispose();
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(EditMarks.this,
							"Error saving marks to the database. Please check your database connection and try again.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(EditMarks.this,
							"Invalid marks entered. Please enter a valid numeric value between 0 and 100.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(EditMarks.this,
							"An unexpected error occurred. Please contact support.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private void saveMarksToDatabase() throws SQLException {
		int studentId = (int) selectedRowData[0];
		int newMarks;

		try {
			newMarks = Integer.parseInt(marksField.getText());

			// Check if marks are within the valid range (0-100)
			if (newMarks < 0 || newMarks > 100) {
				throw new NumberFormatException("Invalid marks entered. Marks should be in the range 0-100.");
			}

			updateMarksInDatabase(studentId, newMarks);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Invalid marks entered. Please enter a valid numeric value.");
		}
	}

	private void updateMarksInDatabase(int studentId, int newMarks) throws SQLException {
		try (Connection connection = ConnectionManager.getConnection()) {
			String updateQuery = "UPDATE resultslip SET marks = ? WHERE email = ? AND module_name = ? AND level = ? AND student_name = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
				preparedStatement.setInt(1, newMarks);
				preparedStatement.setString(2, selectedRowData[3].toString());
				preparedStatement.setString(3, selectedRowData[6].toString());
				preparedStatement.setString(4, selectedRowData[5].toString());
				preparedStatement.setString(5, selectedRowData[1].toString());

				int rowsUpdated = preparedStatement.executeUpdate();

				if (rowsUpdated > 0) {
					System.out.println("Marks updated successfully!");
				} else {
					System.out.println("No rows updated. Check your conditions.");
					JOptionPane.showMessageDialog(EditMarks.this, "No rows updated. Check your conditions.", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(EditMarks.this,
					"Error updating marks. Please check your database connection and try again.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateStudentDetails() {
		studentIdLabel.setText(selectedRowData[0].toString());
		studentNameLabel.setText(selectedRowData[1].toString());
		courseLabel.setText(selectedRowData[2].toString());
		emailLabel.setText(selectedRowData[3].toString());
		moduleNameLabel.setText(selectedRowData[6].toString());
	}
}
