package result;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connection.ConnectionManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewMarks extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTable marksTable;

	public ViewMarks(JFrame parent, String studentName, String email, int level) {
		super(parent, "View Marks", true);
		setLayout(new BorderLayout());
		setUndecorated(true);

		DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Module Name", "Marks" }, 0);
		marksTable = new JTable(tableModel);
		marksTable.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane(marksTable);

		customizeTableAppearance();

		add(scrollPane, BorderLayout.CENTER);

		fetchAndDisplayMarks(email, level, studentName);

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(e -> dispose());

		JButton viewResultButton = new JButton("View Result Slip");
		viewResultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResultSlip(studentName, email, level);
			}
		});
		add(viewResultButton, BorderLayout.NORTH);

		add(closeButton, BorderLayout.SOUTH);

		setSize(400, 300);
		setLocationRelativeTo(parent);
	}

	private void showResultSlip(String studentName, String email, int level) {
		StudentsResult resultSlipPanel = new StudentsResult(studentName, email, level);
		JOptionPane.showMessageDialog(this, resultSlipPanel, "Result Slip", JOptionPane.PLAIN_MESSAGE);
	}

	private void customizeTableAppearance() {
		// Customize the appearance of the marksTable
		marksTable.getTableHeader().setBackground(Color.BLACK);
		marksTable.getTableHeader().setForeground(Color.WHITE);
		marksTable.setRowHeight(25);
		JTableHeader header = marksTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		marksTable.setGridColor(Color.LIGHT_GRAY);
		marksTable.setBackground(new Color(240, 248, 255));
	}

	private void fetchAndDisplayMarks(String email, int level, String studentName) {
		// Fetch and display marks from the database
		try (Connection connection = ConnectionManager.getConnection()) {
			if (connection != null) {
				String query = "SELECT module_name, marks FROM resultslip WHERE email = ? AND level = ? AND student_name = ?";
				try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
					preparedStatement.setString(1, email);
					preparedStatement.setInt(2, level);
					preparedStatement.setString(3, studentName);

					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						DefaultTableModel tableModel = (DefaultTableModel) marksTable.getModel();
						tableModel.setRowCount(0); // Clear existing rows

						while (resultSet.next()) {
							String moduleName = resultSet.getString("module_name");
							String marks = resultSet.getString("marks");
							tableModel.addRow(new Object[] { moduleName, marks });
						}
					}
				}
			} else {
				System.err.println("Failed to establish a database connection.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching marks: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
