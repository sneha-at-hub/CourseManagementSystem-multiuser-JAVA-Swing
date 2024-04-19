package panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import connection.ConnectionManager;
import teacher.AddMarks;
import teacher.EditMarks;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentMarks extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable studentTable;
	private JButton addResultSlipButton;
	private JButton editMarksButton;
	private JButton viewMarksButton;

	public StudentMarks(String teacherEmail) {
		setLayout(new BorderLayout());

	
		addResultSlipButton = createStyledButton("Add Result Slip");
		editMarksButton = createStyledButton("Edit Marks");
		viewMarksButton = createStyledButton("View Marks");

		// Add buttons to a button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addResultSlipButton);
		buttonPanel.add(editMarksButton);
		buttonPanel.add(viewMarksButton);

		// Initialize table model
		DefaultTableModel tableModel = new DefaultTableModel();
		studentTable = new JTable(tableModel);
		studentTable.setRowHeight(25);
		studentTable.getTableHeader()
				.setPreferredSize(new Dimension(studentTable.getColumnModel().getTotalColumnWidth(), 25));

		// Add table to a scroll pane
		JScrollPane scrollPane = new JScrollPane(studentTable);

		// Add components to the main panel
		add(buttonPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		addResultSlipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = studentTable.getSelectedRow();
				if (selectedRowIndex != -1) {
					Object[] selectedRowData = ((DefaultTableModel) studentTable.getModel()).getDataVector()
							.get(selectedRowIndex).toArray();

					// Open the "Add Marks" dialog
					AddMarks addMarksDialog = new AddMarks(
							(JFrame) SwingUtilities.getWindowAncestor(StudentMarks.this), selectedRowData);
					addMarksDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					addMarksDialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(StudentMarks.this, "Please select a student to add marks.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		editMarksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = studentTable.getSelectedRow();
				if (selectedRowIndex != -1) {
					Object[] selectedRowData = ((DefaultTableModel) studentTable.getModel()).getDataVector()
							.get(selectedRowIndex).toArray();

					EditMarks editMarksDialog = new EditMarks(
							(JFrame) SwingUtilities.getWindowAncestor(StudentMarks.this), selectedRowData);
					editMarksDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					editMarksDialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(StudentMarks.this, "Please select a student to edit marks.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		viewMarksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = studentTable.getSelectedRow();

				if (selectedRowIndex != -1) {

					int studentId = (int) studentTable.getValueAt(selectedRowIndex, 0);

					fetchAndDisplayResultSlip(studentId);
				} else {
					JOptionPane.showMessageDialog(StudentMarks.this, "Please select a student to view marks.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Fetch and display students
		fetchAndDisplayStudents(teacherEmail);
	}

	// Helper method to create styled buttons
	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(0, 102, 255));
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setOpaque(true);
		button.setFont(new Font("Arial", Font.BOLD, 12));

		return button;
	}

	private void fetchAndDisplayResultSlip(int studentId) {
		// Your logic to fetch data from the result slip based on the selected student
		// ID
		// Update the query and data retrieval process according to your database
		// structure

		String query = "SELECT * FROM resultslip WHERE id = ?";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setInt(1, studentId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// Process the result set and display the result slip data
				if (resultSet.next()) {
					// Data is available
					String subject = resultSet.getString("module_name");
					int marks = resultSet.getInt("marks");

					// Display the data, you can use a JOptionPane or another UI component
					String message = "Subject: " + subject + "\nMarks: " + marks;
					JOptionPane.showMessageDialog(StudentMarks.this, message, "Result Slip",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					// No data available (marks not entered yet)
					JOptionPane.showMessageDialog(StudentMarks.this, "Marks not entered yet for this student.",
							"Result Slip", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (SQLException ex) {
			handleSQLException(ex, "Error fetching and displaying result slip data");
		}
	}

	protected void fetchAndDisplayStudents(String teacherEmail) {
		Object[][] data = fetchDataBasedOnTeacherEmail(teacherEmail);
		DefaultTableModel tableModel = (DefaultTableModel) studentTable.getModel();
		tableModel.setDataVector(data, new Object[] { "Student ID", "Student Name", "Course", "Email", "Phone Number",
				"Module Level", "Module Name" });
	}

	private Object[][] fetchDataBasedOnTeacherEmail(String teacherEmail) {
		Object[][] data = null;
		String query = "SELECT e.id as student_id, e.student_name, e.course_name, e.email, e.phone_number, m.level, m.module_name "
				+ "FROM enrolledstudents e " + "JOIN modules m ON e.course_name = m.course_name AND e.level = m.level "
				+ "WHERE e.course_name = (SELECT course FROM teacher WHERE email = ?) AND m.module_name = (SELECT subject FROM teacher WHERE email = ?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, teacherEmail);
			preparedStatement.setString(2, teacherEmail);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				// Process the result set and populate the student data in the table
				List<Object[]> dataList = new ArrayList<>();
				while (resultSet.next()) {
					int studentId = resultSet.getInt("student_id");
					String studentName = resultSet.getString("student_name");
					String course = resultSet.getString("course_name");
					String studentEmail = resultSet.getString("email");
					String phone = resultSet.getString("phone_number");
					int moduleLevel = resultSet.getInt("level");
					String moduleName = resultSet.getString("module_name");

					// Add a new row to the data list
					dataList.add(new Object[] { studentId, studentName, course, studentEmail, phone, moduleLevel,
							moduleName });
				}

				// Convert the list to a 2D array
				data = dataList.toArray(new Object[dataList.size()][]);

				// Print the retrieved data for debugging
				for (Object[] row : data) {
					System.out.println(Arrays.toString(row));
				}
			}
		} catch (SQLException e) {
			handleSQLException(e, "Error fetching and displaying student data");
			e.printStackTrace();
		} catch (Exception e) {
			handleException(e, "Unexpected error");
			e.printStackTrace();
		}

		return data;
	}

	private void handleException(Exception e, String string) {
		// TODO Auto-generated method stub

	}

	private void handleSQLException(SQLException e, String string) {
		// TODO Auto-generated method stub

	}
}
