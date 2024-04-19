package result;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import connection.ConnectionManager;

public class StudentsResult extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
	private static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 12);

	public StudentsResult(String studentName, String email, int level) {
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500, 600)); // A4 size in points
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margins

		add(createHeaderPanel(studentName), BorderLayout.NORTH);
		add(createResultsPanel(email, level, studentName), BorderLayout.CENTER);
	}

	private JPanel createHeaderPanel(String studentName) {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(new Color(50, 205, 50));

		JLabel collegeLabel = new JLabel("Herald College Kathmandu");
		collegeLabel.setForeground(Color.WHITE);
		collegeLabel.setFont(TITLE_FONT);
		collegeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel studentLabel = new JLabel("Student: " + studentName);
		studentLabel.setForeground(Color.WHITE);
		studentLabel.setFont(TITLE_FONT);
		studentLabel.setHorizontalAlignment(SwingConstants.CENTER);

		headerPanel.add(collegeLabel, BorderLayout.NORTH);
		headerPanel.add(studentLabel, BorderLayout.CENTER);

		return headerPanel;
	}

	private JScrollPane createResultsPanel(String email, int level, String studentName) {
		Vector<String> columnNames = new Vector<>();
		columnNames.add("Module");
		columnNames.add("Marks");

		Vector<Vector<Object>> data = fetchResultsFromDatabase(email, level, studentName);

		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
		JTable resultTable = new JTable(tableModel);
		resultTable.setFont(CONTENT_FONT);

		JScrollPane scrollPane = new JScrollPane(resultTable);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		return scrollPane;
	}

	private Vector<Vector<Object>> fetchResultsFromDatabase(String email, int level, String studentName) {
		Vector<Vector<Object>> data = new Vector<>();

		// Fetch and display results from the database
		try (Connection connection = ConnectionManager.getConnection()) {
			if (connection != null) {
				String query = "SELECT module_name, marks FROM resultslip WHERE email = ? AND level = ? AND student_name = ?";
				try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
					preparedStatement.setString(1, email);
					preparedStatement.setInt(2, level);
					preparedStatement.setString(3, studentName);

					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						while (resultSet.next()) {
							Vector<Object> rowData = new Vector<>();
							rowData.add(resultSet.getString("module_name"));
							rowData.add(resultSet.getString("marks"));
							data.add(rowData);
						}
					}
				}
			} else {
				System.err.println("Failed to establish a database connection.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching results: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		return data;
	}
}
