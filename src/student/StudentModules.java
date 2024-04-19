package student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import connection.ConnectionManager;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentModules extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable moduleTable;
	private DefaultTableModel moduleTableModel;
	private String email;

	public StudentModules(String email) {
		this.email = email;
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		createModulesTable();
		this.email = email;
		moduleTableModel = new DefaultTableModel();
		initializeModuleTableModel();
		moduleTable = new JTable(moduleTableModel);

		setupTableAppearance();

		JScrollPane scrollPane = new JScrollPane(moduleTable);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void initializeModuleTableModel() {
		moduleTableModel.addColumn("Module ID");
		moduleTableModel.addColumn("Course Name");
		moduleTableModel.addColumn("Module Name");
		moduleTableModel.addColumn("Semester");
		moduleTableModel.addColumn("Credit");
		moduleTableModel.addColumn("Level");

		fetchModuleDataFromDatabase();
	}

	public void fetchModuleDataFromDatabase() {
		try (Connection connection = ConnectionManager.getConnection()) {
			String selectedCourse = ConnectionManager.getEnrolledCourseName(email);
			int selectedLevel = ConnectionManager.getEnrolledLevel(email);
			String query = "SELECT * FROM modules WHERE course_name = ? AND level = ?";

			System.out.println("Executing SQL Query: " + query);
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, selectedCourse);
				preparedStatement.setInt(2, selectedLevel);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						Object[] rowData = { resultSet.getInt("module_id"), resultSet.getString("course_name"),
								resultSet.getString("module_name"), resultSet.getInt("semester"),
								resultSet.getInt("credit"), resultSet.getInt("level") };
						moduleTableModel.addRow(rowData);
					}
				}
			}
		} catch (SQLException ex) {
			System.err.println("Error executing SQL query: " + ex.getMessage());
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setupTableAppearance() {
		moduleTable.getTableHeader().setBackground(Color.BLACK);
		moduleTable.getTableHeader().setForeground(Color.WHITE);
		moduleTable.setRowHeight(25);
		JTableHeader header = moduleTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		moduleTable.setGridColor(Color.LIGHT_GRAY);
		moduleTable.setBackground(new Color(240, 248, 255));
	}

	private void createModulesTable() {
		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {
			String createTableQuery = "CREATE TABLE IF NOT EXISTS modules (" + "module_id INT PRIMARY KEY,"
					+ "course_name VARCHAR(255) NOT NULL," + "module_name VARCHAR(255) NOT NULL,"
					+ "semester INT NOT NULL," + "credit INT NOT NULL," + "level INT NOT NULL)";

			statement.execute(createTableQuery);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public DefaultTableModel getModuleTableModel() {
		return moduleTableModel;
	}

}
