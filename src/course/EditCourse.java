package course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connection.ConnectionManager;
import gui.RoundButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditCourse extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField courseField;
	private JTextField yearField;
	private DefaultTableModel model;
	private int selectedRow;

	public EditCourse(JFrame parent, String currentCourse, int currentYear, DefaultTableModel model, int selectedRow) {
		super(parent, "Edit Course", true);
		setUndecorated(true);

		getContentPane().setBackground(Color.WHITE);
		setSize(368, 460);

		courseField = new JTextField(currentCourse);
		courseField.setBounds(54, 137, 260, 35);
		yearField = new JTextField(String.valueOf(currentYear));
		yearField.setBounds(54, 226, 260, 35);
		this.model = model;
		this.selectedRow = selectedRow;
		getContentPane().setLayout(null);
		JLabel lblCourseName = new JLabel("Course Name");
		lblCourseName.setBounds(57, 116, 100, 20);
		getContentPane().add(lblCourseName);
		getContentPane().add(courseField);
		JLabel lblCourseYear = new JLabel("Course year");
		lblCourseYear.setBounds(54, 204, 80, 20);
		getContentPane().add(lblCourseYear);
		getContentPane().add(yearField);

		JButton okButton = new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
		okButton.setBounds(57, 295, 110, 40);
		JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.setBounds(200, 295, 110, 40);

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

		JLabel titleLabel = new JLabel("Edit Course");
		titleLabel.setForeground(new Color(50, 144, 255));
		titleLabel.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		titleLabel.setBounds(54, 60, 133, 20);
		getContentPane().add(titleLabel);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void onOK() {

		String newCourse = getNewCourse();
		int newYear = getNewYear();

		if (newCourse.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a course name.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (newYear < 0) {
			JOptionPane.showMessageDialog(this, "Please enter a valid positive year.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String currentCourse = (String) model.getValueAt(selectedRow, 0);

		model.setValueAt(newCourse, selectedRow, 0);
		model.setValueAt(newYear, selectedRow, 1);

		updateCourseInDatabase(newCourse, newYear, currentCourse);

		System.out.println("newCourse: " + newCourse);
		System.out.println("newYear: " + newYear);
		System.out.println("currentCourse: " + currentCourse);

		dispose();
	}

	private void updateCourseInDatabase(String newCourse, int newYear, String currentCourse) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String query = "UPDATE course SET course_name = ?, total_year = ? WHERE course_name = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, newCourse);
				preparedStatement.setInt(2, newYear);
				preparedStatement.setString(3, currentCourse);
				int affectedRows = preparedStatement.executeUpdate();

				if (affectedRows > 0) {
					System.out.println("Course updated successfully in the database.");

				} else {
					System.out.println("No rows were updated in the database.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

			System.err.println("Error updating course in the database: " + ex.getMessage());
			JOptionPane.showMessageDialog(this, "Error updating course in the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onCancel() {
		dispose();
	}

	public String getNewCourse() {
		return courseField.getText();
	}

	public int getNewYear() {
		try {
			return Integer.parseInt(yearField.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
