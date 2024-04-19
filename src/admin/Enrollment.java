package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import connection.ConnectionManager;
import course.CourseDeleteObserver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Enrollment extends JPanel implements CourseDeleteObserver {

	private static final long serialVersionUID = 1L;
	private JTable enrolledStudentTable;
	private JButton editEnrolledStudentButton;
	private JButton addEnrolledStudentButton;
	private JButton deleteEnrolledStudentButton;
	private JLabel searchLabel;
	private JTextField searchField;


	public Enrollment() {
		setBackground(new Color(240, 248, 255));
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		DefaultTableModel model = new DefaultTableModel();
		enrolledStudentTable = new JTable(model);

		customizeTableAppearance();

		fetchDataAndDisplay();

		JScrollPane scrollPane = new JScrollPane(enrolledStudentTable);

		JPanel buttonsPanel = createButtonsPanel();

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

		add(contentPanel, BorderLayout.CENTER);
        // Add search bar components
        searchLabel = new JLabel("Search");
        searchLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterData();
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 248, 255));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        add(searchPanel, BorderLayout.NORTH);
    }

    private void filterData() {
        DefaultTableModel model = (DefaultTableModel) enrolledStudentTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        enrolledStudentTable.setRowSorter(sorter);

        String searchText = searchField.getText().trim();
        if (searchText.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 1)); // 1 corresponds to the Student Name column
        }
    }

	private void customizeTableAppearance() {
		enrolledStudentTable.getTableHeader().setBackground(Color.BLACK);
		enrolledStudentTable.getTableHeader().setForeground(Color.WHITE);
		enrolledStudentTable.setRowHeight(25);
		JTableHeader header = enrolledStudentTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		enrolledStudentTable.setGridColor(Color.LIGHT_GRAY);
		enrolledStudentTable.setBackground(new Color(240, 248, 255));

	}

	private JPanel createButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setBackground(new Color(240, 248, 255));

		editEnrolledStudentButton = new JButton("Edit Enrolled Student");
		customizeButton(editEnrolledStudentButton);
		buttonsPanel.add(editEnrolledStudentButton);

		addEnrolledStudentButton = new JButton("Add Enrolled Student");
		addEnrolledStudentButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		customizeButton(addEnrolledStudentButton);
		buttonsPanel.add(addEnrolledStudentButton);

		deleteEnrolledStudentButton = new JButton("Delete Enrolled Student");
		customizeButton(deleteEnrolledStudentButton);
		buttonsPanel.add(deleteEnrolledStudentButton);

		return buttonsPanel;
	}

	private void customizeButton(JButton button) {
		button.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		button.setBackground(new Color(30, 144, 255));
		button.setOpaque(true);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setForeground(Color.WHITE);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleButtonAction(e.getSource());
			}
		});
	}

	private void handleButtonAction(Object source) {
		if (source == editEnrolledStudentButton) {
			editEnrolledStudent();
		} else if (source == addEnrolledStudentButton) {
			addEnrolledStudent();
		} else if (source == deleteEnrolledStudentButton) {
			deleteEnrolledStudent();
		}
	}

	private void editEnrolledStudent() {
		int selectedRow = enrolledStudentTable.getSelectedRow();

		if (selectedRow != -1) {
			DefaultTableModel model = (DefaultTableModel) enrolledStudentTable.getModel();
			String studentId = model.getValueAt(selectedRow, 0).toString();
			String studentName = model.getValueAt(selectedRow, 1).toString();
			String email = model.getValueAt(selectedRow, 2).toString();
			String phoneNumber = model.getValueAt(selectedRow, 3).toString();
			String address = model.getValueAt(selectedRow, 4).toString();
			String courseName = model.getValueAt(selectedRow, 5).toString();
			int level = Integer.parseInt(model.getValueAt(selectedRow, 6).toString());

			EditEnrollment editEnrollment = new EditEnrollment(this, studentId, studentName, email, phoneNumber,
					address, courseName, level);

			editEnrollment.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Row Selected",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void updateStudentInDatabase(String studentId, String studentName, String email, String phoneNumber,
			String address, String courseName, int level) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String updateQuery = "UPDATE enrolledstudents SET student_name=?, email=?, phone_number=?, address=?, course_name=?, level=? WHERE student_id=?";
			try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
				updateStatement.setString(1, studentName);
				updateStatement.setString(2, email);
				updateStatement.setString(3, phoneNumber);
				updateStatement.setString(4, address);
				updateStatement.setString(5, courseName);
				updateStatement.setInt(6, level);
				updateStatement.setString(7, studentId);

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

	public void updateStudentData(String studentId, String studentName, String email, String phoneNumber,
			String address, String courseName, int level) {
		DefaultTableModel model = (DefaultTableModel) enrolledStudentTable.getModel();

		// Find the row to update based on the student ID
		for (int i = 0; i < model.getRowCount(); i++) {
			if (model.getValueAt(i, 0).toString().equals(studentId)) {
				// Update the table
				model.setValueAt(studentId, i, 0);
				model.setValueAt(studentName, i, 1);
				model.setValueAt(email, i, 2);
				model.setValueAt(phoneNumber, i, 3);
				model.setValueAt(address, i, 4);
				model.setValueAt(courseName, i, 5);
				model.setValueAt(level, i, 6);

				// Update the database
				updateStudentInDatabase(studentId, studentName, email, phoneNumber, address, courseName, level);
				break;
			}
		}
	}

	private void addEnrolledStudent() {
		SwingUtilities.invokeLater(() -> {
			AddEnrollment enrollStudentFormAdmin = new AddEnrollment(this);
			enrollStudentFormAdmin.setVisible(true);
		});
	}

	public void setTableModel(DefaultTableModel model) {
		enrolledStudentTable.setModel(model);
	}
	

	private void deleteEnrolledStudent() {
		int selectedRow = enrolledStudentTable.getSelectedRow();

		if (selectedRow != -1) {
			DefaultTableModel model = (DefaultTableModel) enrolledStudentTable.getModel();
			String studentId = model.getValueAt(selectedRow, 0).toString();
			int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?",
					"Confirm Deletion", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				// Delete the student from the database and the table
				deleteStudentFromDatabase(studentId);
				model.removeRow(selectedRow);
				System.out.println("Student deleted successfully.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Row Selected",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void deleteStudentFromDatabase(String studentId) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String deleteQuery = "DELETE FROM enrolledstudents WHERE student_id=?";
			try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
				deleteStatement.setString(1, studentId);

				int affectedRows = deleteStatement.executeUpdate();

				if (affectedRows > 0) {
					System.out.println("Student deleted successfully from the database.");
				} else {
					System.out.println("No rows were deleted from the database.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error deleting student from the database: " + ex.getMessage());
			JOptionPane.showMessageDialog(this, "Error deleting student from the database.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void fetchDataAndDisplay() {
		DefaultTableModel model = fetchDataFromDatabase();
		enrolledStudentTable.setModel(model);
	}

	private DefaultTableModel fetchDataFromDatabase() {
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(new Object[] { "Student ID", "Student Name", "Email", "Phone Number", "Address",
				"Course Name", "Level" });

		try (Connection connection = ConnectionManager.getConnection()) {
			if (connection != null) {
				String query = "SELECT * FROM enrolledstudents";
				try (Statement statement = connection.createStatement();
						ResultSet resultSet = statement.executeQuery(query)) {

					while (resultSet.next()) {
						String studentId = resultSet.getString("student_id");
						String studentName = resultSet.getString("student_name");
						String email = resultSet.getString("email");
						String phoneNumber = resultSet.getString("phone_number");
						String address = resultSet.getString("address");
						String courseName = resultSet.getString("course_name");
						int level = resultSet.getInt("level");

						model.addRow(new Object[] { studentId, studentName, email, phoneNumber, address, courseName,
								level });
					}
				}
			} else {
				System.err.println("Failed to establish a database connection.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error fetching data from the database: " + ex.getMessage());
		}

		return model;
	}
	
	private void deleteModulesForDeletedCourse(String courseName) {
	    try (Connection connection = ConnectionManager.getConnection()) {
	        String deleteQuery = "DELETE FROM enrolledstudents WHERE course_name = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
	            preparedStatement.setString(1, courseName);
	            preparedStatement.executeUpdate();
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error deleting modules for the deleted course.");
	    }

	    // Refresh the module table outside the try-catch block
	    fetchDataAndDisplay();
	}

	@Override
	public void onCourseDeleted(String courseName) {
		 deleteModulesForDeletedCourse(courseName);
		
	}

}
