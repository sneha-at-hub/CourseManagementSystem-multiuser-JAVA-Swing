package result;

import javax.swing.*;

import connection.ConnectionManager;
import gui.RoundButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class EditResults extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField newMarksField;
	private JComboBox<String> moduleNameComboBox;

	public EditResults(JFrame parent, String studentName, String email, int level) {
		super(parent, "Edit Marks", true);
		getContentPane().setBackground(Color.WHITE);

		newMarksField = new JTextField();
		newMarksField.setBounds(139, 160, 188, 35);
		moduleNameComboBox = new JComboBox<>(fetchModules(email, level));
		moduleNameComboBox.setBounds(139, 108, 196, 40);
		getContentPane().setLayout(null);
		setUndecorated(true);

		JLabel label = new JLabel("Student Name:");
		label.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		label.setBounds(27, 56, 100, 40);
		getContentPane().add(label);
		JLabel label_1 = new JLabel(studentName);
		label_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		label_1.setBounds(139, 56, 196, 40);
		getContentPane().add(label_1);
		JLabel label_2 = new JLabel("Module Name:");
		label_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		label_2.setBounds(27, 107, 100, 40);
		getContentPane().add(label_2);
		getContentPane().add(moduleNameComboBox);
		JLabel label_3 = new JLabel("New Marks:");
		label_3.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		label_3.setBounds(27, 157, 100, 40);
		getContentPane().add(label_3);
		getContentPane().add(newMarksField);

		JButton saveButton = new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
		saveButton.setBounds(45, 238, 100, 37);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateMarks(email, newMarksField.getText(), (String) moduleNameComboBox.getSelectedItem(), level,
						studentName);
				dispose();
			}
		});

		getContentPane().add(saveButton);
		
		JLabel lblEditMarks = new JLabel("Edit Result");
		lblEditMarks.setForeground(new Color(50, 144, 255));
		lblEditMarks.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblEditMarks.setBounds(27, 17, 200, 37);
		getContentPane().add(lblEditMarks);
		
		RoundButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(185, 238, 100, 37);
		getContentPane().add(cancelButton);

		setSize(341, 310);
		setLocationRelativeTo(parent);
	}

	private String[] fetchModules(String email, int level) {
		try (Connection connection = ConnectionManager.getConnection()) {
			if (connection != null) {
				String query = "SELECT DISTINCT module_name FROM modules WHERE course_name = ? AND level = ?";
				try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
					preparedStatement.setString(1, ConnectionManager.getEnrolledCourseName(email));
					preparedStatement.setInt(2, ConnectionManager.getEnrolledLevel(email));

					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						Vector<String> modules = new Vector<>();
						while (resultSet.next()) {
							modules.add(resultSet.getString("module_name"));
						}
						return modules.toArray(new String[0]);
					}
				}
			} else {
				System.err.println("Failed to establish a database connection.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching modules: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return new String[0];
	}

	private void updateMarks(String email, String newMarks, String moduleName, int level, String studentName) {
		// Update the marks in the database
		try (Connection connection = ConnectionManager.getConnection()) {
			if (connection != null) {
				String updateQuery = "UPDATE resultslip SET marks = ? WHERE email = ? AND module_name = ? AND level = ? AND student_name = ?";
				try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
					preparedStatement.setString(1, newMarks);
					preparedStatement.setString(2, email);
					preparedStatement.setString(3, moduleName);
					preparedStatement.setInt(4, level);
					preparedStatement.setString(5, studentName);

					int rowsAffected = preparedStatement.executeUpdate();

					if (rowsAffected > 0) {
						JOptionPane.showMessageDialog(this, "Marks updated successfully.", "Success",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Failed to update marks.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				System.err.println("Failed to establish a database connection.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error updating marks: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}
}
