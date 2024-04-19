package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connection.ConnectionManager;
import panel.TeacherTableObserver;
import student.StudentsObserver;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ActivityPanel extends JPanel implements StudentsObserver, TeacherTableObserver {

	private static final long serialVersionUID = 1L;
	private JTable activityTable;

	public ActivityPanel() {
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		DefaultTableModel model = new DefaultTableModel();
		activityTable = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(activityTable);

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		add(contentPanel, BorderLayout.CENTER);
		activityTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

		fetchDataAndDisplay();

		activityTable.getTableHeader().setBackground(Color.BLACK);
		activityTable.getTableHeader().setForeground(Color.WHITE);

		activityTable.setRowHeight(25);
		JTableHeader header = activityTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		activityTable.setGridColor(Color.LIGHT_GRAY);
		activityTable.setBackground(new Color(240, 248, 255));

	}

	private void fetchDataAndDisplay() {
		DefaultTableModel model = fetchDataFromDatabase();
		activityTable.setModel(model);
	}

	private DefaultTableModel fetchDataFromDatabase() {

		try (Connection connection = ConnectionManager.getConnection()) {
			createActivityLogTable(connection);

			DefaultTableModel model = new DefaultTableModel();
			model.setColumnIdentifiers(new Object[] { "Date", "Time", "Activity" });

			String query = "SELECT * FROM activity_log";
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(query)) {

				while (resultSet.next()) {

					String dateTime = resultSet.getString("date");
					String date = dateTime.split(" ")[0];
					String time = dateTime.split(" ")[1];

					String activity = resultSet.getString("activity");

					model.addRow(new Object[] { date, time, activity });
				}
			}

			return model;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error fetching data from the activity log: " + ex.getMessage());
			return new DefaultTableModel();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Activity Panel");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ActivityPanel activityPanel = new ActivityPanel();
			frame.getContentPane().add(activityPanel);

			frame.setSize(600, 400);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

	@Override
	public void onStudentViewed(String studentName) {
		logActivity("Student viewed: " + studentName);
		fetchDataAndDisplay();
	}

	@Override
	public void onStudentEdited(String studentName) {
		logActivity("Student edited: " + studentName);
		fetchDataAndDisplay();
	}

	@Override
	public void onStudentAdded(String studentName) {
		logActivity("Student added: " + studentName);
		fetchDataAndDisplay();
	}

	@Override
	public void onStudentDeleted(String studentName) {
		logActivity("Student deleted: " + studentName);
		fetchDataAndDisplay();
	}

	private void logActivity(String activity) {
		try (Connection connection = ConnectionManager.getConnection()) {
			if (connection != null) {
				// Create activity_log table if not exists
				createActivityLogTable(connection);

				// Insert activity log
				String insertQuery = "INSERT INTO activity_log (activity) VALUES (?)";
				try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
					insertStatement.setString(1, activity);
					insertStatement.executeUpdate();
				}
			} else {
				System.err.println("Failed to establish a database connection.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error inserting activity log: " + ex.getMessage());
		}
	}

	private void createActivityLogTable(Connection connection) {
		try {
			String createTableQuery = "CREATE TABLE IF NOT EXISTS activity_log ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY, " + "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
					+ "activity VARCHAR(255) NOT NULL)";

			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(createTableQuery);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Error creating activity_log table: " + ex.getMessage());
		}
	}

	private class CustomTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value != null && value.toString().contains("Student")) {
				component.setBackground(new Color(135, 206, 250));
				component.setForeground(Color.BLACK);
			} else if (value != null && value.toString().contains("Teacher")) {
				component.setBackground(new Color(144, 238, 144));
				component.setForeground(Color.BLACK);
			} else {

				component.setBackground(table.getBackground());
				component.setForeground(table.getForeground());
			}

			return component;
		}
	}

	@Override
	public void onTeacherViewed(String teacherName) {
		logActivity("Teacher viewed: " + teacherName);
		fetchDataAndDisplay();
	}

	@Override
	public void onTeacherEdited(String teacherName) {
		logActivity("Teacher edited: " + teacherName);
		fetchDataAndDisplay();
	}

	@Override
	public void onTeacherAdded(String teacherName) {
		logActivity("Teacher added: " + teacherName);
		fetchDataAndDisplay();
	}

	@Override
	public void onTeacherDeleted(String teacherName) {
		logActivity("Teacher deleted: " + teacherName);
		fetchDataAndDisplay();
	}
}
