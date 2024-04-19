package result;

import javax.swing.JButton;
import javax.swing.JDialog;

import connection.ConnectionManager;
import gui.RoundButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class AddResults extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextField marksField;

	private JTextField studentNameField;
	private JTextField emailField;
	private JTextField moduleNameField;
	private JTextField levelField;
	private JComboBox<String> moduleNameComboBox;
	private JLabel lblResultslip;

	public AddResults(JFrame parent, String studentName, String email, int level) {
		super(parent, "Add Result Slip", true);
		getContentPane().setBackground(Color.WHITE);
		setUndecorated(true);

		marksField = new JTextField();
		marksField.setBounds(173, 252, 201, 34);
		moduleNameField = new JTextField();
		studentNameField = new JTextField(studentName);
		studentNameField.setBounds(173, 114, 201, 34);
		emailField = new JTextField(email);
		emailField.setBounds(173, 160, 201, 34);
		levelField = new JTextField(String.valueOf(level));
		levelField.setBounds(173, 206, 201, 34);

		emailField.setEditable(false);
		levelField.setEditable(false);
		getContentPane().setLayout(null);

		JLabel label = new JLabel("Student Name:");
		label.setBounds(41, 114, 120, 34);
		getContentPane().add(label);
		getContentPane().add(studentNameField);
		JLabel label_1 = new JLabel("Email:");
		label_1.setBounds(41, 160, 120, 34);
		getContentPane().add(label_1);
		getContentPane().add(emailField);
		JLabel label_2 = new JLabel("Level:");
		label_2.setBounds(41, 206, 120, 34);
		getContentPane().add(label_2);
		getContentPane().add(levelField);
		JLabel label_3 = new JLabel("Marks:");
		label_3.setBounds(41, 252, 120, 34);
		getContentPane().add(label_3);
		getContentPane().add(marksField);
		// Add a JComboBox for module names
		moduleNameComboBox = new JComboBox<>(fetchModules(email, level));
		moduleNameComboBox.setBounds(173, 298, 201, 34);
		JLabel label_4 = new JLabel("Module Name:");
		label_4.setBounds(41, 297, 120, 34);
		getContentPane().add(label_4);
		getContentPane().add(moduleNameComboBox);
		JButton saveButton = new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
		saveButton.setBounds(58, 394, 100, 37);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Update moduleNameField with the selected item from moduleNameComboBox
				moduleNameField.setText((String) moduleNameComboBox.getSelectedItem());

				// Call saveResultSlip with the updated moduleNameField
				saveResultSlip(email, marksField.getText(), moduleNameField.getText(), level, studentName);
				dispose();
			}
		});

		getContentPane().add(saveButton);

		lblResultslip = new JLabel("ResultSlip");
		lblResultslip.setForeground(new Color(50, 144, 255));
		lblResultslip.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblResultslip.setBounds(41, 35, 200, 37);
		getContentPane().add(lblResultslip);

		RoundButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(226, 394, 100, 37);
		getContentPane().add(cancelButton);

		setSize(407, 469);
		setLocationRelativeTo(parent);
	}

	private String[] fetchModules(String email, int level) {
		// Fetch the modules based on the selected email and level
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

	private boolean hasPassedPreviousLevel(String email, int previousLevel) {
        try (Connection connection = ConnectionManager.getConnection()) {
            if (connection != null) {
                String moduleQuery = "SELECT DISTINCT module_name FROM modules WHERE course_name = ? AND level = ?";
                try (PreparedStatement moduleStatement = connection.prepareStatement(moduleQuery)) {
                    moduleStatement.setString(1, ConnectionManager.getEnrolledCourseName(email));
                    moduleStatement.setInt(2, previousLevel);

                    try (ResultSet moduleResultSet = moduleStatement.executeQuery()) {
                        while (moduleResultSet.next()) {
                            String moduleName = moduleResultSet.getString("module_name");

                            if (!hasPassedModule(email, moduleName, previousLevel)) {
                                return false;
                            }
                        }
                    }
                }
                return true; // All modules for the previous level are passed
            } else {
                System.err.println("Failed to establish a database connection.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false; // Return false in case of any exception or failure
    }

    private boolean hasPassedModule(String email, String moduleName, int level) {
        try (Connection connection = ConnectionManager.getConnection()) {
            if (connection != null) {
                String query = "SELECT marks FROM resultslip WHERE email = ? AND module_name = ? AND level = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, moduleName);
                    preparedStatement.setInt(3, level);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next() && resultSet.getInt("marks") >= 40;
                    }
                }
            } else {
                System.err.println("Failed to establish a database connection.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false; // Return false in case of any exception or failure
    }

    private void saveResultSlip(String email, String marks, String moduleName, int level, String studentName) {
        marks = marks.trim();
        moduleName = moduleName.trim();

        if (marks.isEmpty() || moduleName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter marks and module name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            if (connection != null) {
                if (level > 1 && !hasPassedPreviousLevel(email, level - 1)) {
                    JOptionPane.showMessageDialog(this, "Cannot add result. The student must pass the previous level first.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String createTableQuery = "CREATE TABLE IF NOT EXISTS resultslip ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT," + "email VARCHAR(255)," + "marks VARCHAR(255),"
                        + "module_name VARCHAR(255)," + "level INT," + "student_name VARCHAR(255))";

                try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
                    createTableStatement.executeUpdate();
                }

                String insertQuery = "INSERT INTO resultslip (email, marks, module_name, level, student_name) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, marks);
                    preparedStatement.setString(3, moduleName);
                    preparedStatement.setInt(4, level);
                    preparedStatement.setString(5, studentName);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Result slip added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add result slip.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                System.err.println("Failed to establish a database connection.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding result slip: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }}