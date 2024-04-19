package result;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connection.ConnectionManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class ResultSlip extends JPanel {
	private static final long serialVersionUID = 1L;
	protected DefaultTableModel enrolledStudentsTableModel;
	private JTable enrolledStudentsTable;
	private JButton addResultSlipButton;
	private JButton editMarksButton;
	private JButton viewMarksButton;

	public ResultSlip() {
		setBackground(new Color(240, 248, 255));
		setLayout(new BorderLayout(5, 5));

		enrolledStudentsTableModel = new DefaultTableModel();
		enrolledStudentsTableModel.setColumnIdentifiers(new Object[] { "Student ID", "Student Name", "Email",
				"Phone Number", "Address", "Course Name", "Level" });
		enrolledStudentsTable = new JTable(enrolledStudentsTableModel);

		fetchEnrolledStudents();
		customizeTableAppearance();
		JScrollPane enrolledStudentsScrollPane = new JScrollPane(enrolledStudentsTable);
		add(enrolledStudentsScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = getButtonPanel();
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void customizeTableAppearance() {
		JTableHeader header = enrolledStudentsTable.getTableHeader();
		header.setBackground(Color.BLACK);
		header.setForeground(Color.WHITE);
		enrolledStudentsTable.setRowHeight(25);
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		enrolledStudentsTable.setGridColor(Color.LIGHT_GRAY);
		enrolledStudentsTable.setBackground(new Color(240, 248, 255));
	}

	protected JPanel getButtonPanel() {

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(new Color(240, 248, 255));

		JButton addResultSlipButton = new JButton("Add Result Slip");
		addResultSlipButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		addResultSlipButton.setBackground(new Color(30, 144, 255));
		addResultSlipButton.setFocusPainted(false);
		addResultSlipButton.setForeground(Color.WHITE);
		addResultSlipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResultSlipDialog();
			}
		});

		JButton editMarksButton = new JButton("Edit Marks");
		editMarksButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		editMarksButton.setBackground(new Color(30, 144, 255));
		editMarksButton.setFocusPainted(false);
		editMarksButton.setForeground(Color.WHITE);
		editMarksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editMarks();
			}
		});

		JButton viewMarksButton = new JButton("View Marks");
		viewMarksButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		viewMarksButton.setBackground(new Color(30, 144, 255));
		viewMarksButton.setFocusPainted(false);
		viewMarksButton.setForeground(Color.WHITE);

		viewMarksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewMarks();
			}
		});

		buttonPanel.add(addResultSlipButton);
		buttonPanel.add(editMarksButton);
		buttonPanel.add(viewMarksButton);
		viewMarksButton.setOpaque(true);
		viewMarksButton.setBorderPainted(false);
		editMarksButton.setOpaque(true);
		editMarksButton.setBorderPainted(false);
		addResultSlipButton.setOpaque(true);
		addResultSlipButton.setBorderPainted(false);

		return buttonPanel;
	}

	private void showResultSlipDialog() {
		int selectedRow = enrolledStudentsTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a student to add a result slip.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String studentName = (String) enrolledStudentsTableModel.getValueAt(selectedRow, 1);
		String email = (String) enrolledStudentsTableModel.getValueAt(selectedRow, 2);
		int level = (int) enrolledStudentsTableModel.getValueAt(selectedRow, 6);

		AddResults resultSlipDialog = new AddResults((JFrame) SwingUtilities.getWindowAncestor(this), studentName,
				email, level);
		resultSlipDialog.setVisible(true);
	}

	private void fetchEnrolledStudents() {
		DefaultTableModel model = fetchDataFromDatabaseForEnrolledStudents();
		updateEnrolledStudentsTableModel(model);
	}

	private DefaultTableModel fetchDataFromDatabaseForEnrolledStudents() {
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
			System.err.println("Error fetching enrolled students data from the database: " + ex.getMessage());
		}

		return model;
	}

	private void editMarks() {
		int selectedRow = enrolledStudentsTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a student to edit marks.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String studentName = (String) enrolledStudentsTableModel.getValueAt(selectedRow, 1);
		String email = (String) enrolledStudentsTableModel.getValueAt(selectedRow, 2);
		int level = (int) enrolledStudentsTableModel.getValueAt(selectedRow, 6);

		EditResults editMarksDialog = new EditResults((JFrame) SwingUtilities.getWindowAncestor(this), studentName,
				email, level);
		editMarksDialog.setVisible(true);
	}

	private void viewMarks() {
		int selectedRow = enrolledStudentsTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a student to view marks.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String studentName = (String) enrolledStudentsTableModel.getValueAt(selectedRow, 1);
		String email = (String) enrolledStudentsTableModel.getValueAt(selectedRow, 2);
		int level = (int) enrolledStudentsTableModel.getValueAt(selectedRow, 6);

		// Assuming you have a ViewMarksDialog class, create an instance and show it
		ViewMarks viewMarksDialog = new ViewMarks((JFrame) SwingUtilities.getWindowAncestor(this), studentName, email,
				level);
		viewMarksDialog.setVisible(true);
	}

	private void updateEnrolledStudentsTableModel(DefaultTableModel model) {
		SwingUtilities.invokeLater(() -> {
			enrolledStudentsTableModel.setRowCount(0);
			for (int i = 0; i < model.getRowCount(); i++) {
				Vector<Object> row = new Vector<>();
				for (int j = 0; j < model.getColumnCount(); j++) {
					row.add(model.getValueAt(i, j));
				}
				enrolledStudentsTableModel.addRow(row);
			}
		});
	}

	public JButton getAddResultSlipButton() {
		if (addResultSlipButton == null) {
			addResultSlipButton = new JButton("Add Result Slip");
			addResultSlipButton.addActionListener(e -> {
				showResultSlipDialog();
				System.out.println("Add Result Slip Button Clicked");
			});
		}
		return addResultSlipButton;
	}

	public JButton getEditMarksButton() {
		if (editMarksButton == null) {
			editMarksButton = new JButton("Edit Marks");
			editMarksButton.addActionListener(e -> {
				editMarks();
				System.out.println("Edit Marks Button Clicked");
			});
		}
		return editMarksButton;
	}

	public JButton getViewMarksButton() {
		if (viewMarksButton == null) {
			viewMarksButton = new JButton("View Marks");
			viewMarksButton.addActionListener(e -> {
				viewMarks();
				System.out.println("View Marks Button Clicked");
			});
		}
		return viewMarksButton;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Enrolled Students with Result Slip");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800, 400);
			frame.setLocationRelativeTo(null);

			ResultSlip panel = new ResultSlip();
			JButton addResultSlipButton = panel.getAddResultSlipButton();
			JButton editMarksButton = panel.getEditMarksButton();
			JButton viewMarksButton = panel.getViewMarksButton();

			frame.add(panel);

			frame.setVisible(true);
		});
	}

	protected void fetchAndDisplayStudents(String teacherEmail) {

		// TODO Auto-generated method stub

	}

}
