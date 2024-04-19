package teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connection.ConnectionManager;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDetail extends JPanel {

	private static final long serialVersionUID = 1L;
	private final String teacherEmail;
	private JTable table;

	public TeacherDetail(String teacherEmail) {
		this.teacherEmail = teacherEmail;

		setLayout(new GridLayout(1, 1)); // Set GridLayout with 1 row and 1 column

		List<Module> modules = getTeacherModules(teacherEmail);

		// Create a table model with column names and data
		String[] columnNames = { "Module ID", "Module Name", "Level", "Course Name" };
		Object[][] data = new Object[modules.size()][4];

		for (int i = 0; i < modules.size(); i++) {
			Module module = modules.get(i);
			data[i][0] = module.getModuleId();
			data[i][1] = module.getModuleName();
			data[i][2] = module.getLevel();
			data[i][3] = module.getCourseName();
		}

		// Create a custom table model
		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

		// Create a JTable with the custom model
		JTable table = new JTable(tableModel);

		// Set up table appearance (similar to StudentTablePanelWithoutButtons)
		setupTableAppearance(table);

		// Create a JScrollPane to hold the table
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to the main panel
		add(scrollPane);

		// Set the preferred size of the table to fit the panel
		table.setPreferredScrollableViewportSize(getSize());
	}

	public List<Module> getTeacherModules(String teacherEmail) {
		List<Module> modules = new ArrayList<>();
		String query = "SELECT m.module_id, m.module_name, m.level, m.course_name " + "FROM modules m "
				+ "WHERE m.course_name = (SELECT course FROM teacher WHERE email = ?) "
				+ "AND m.module_name IN (SELECT subject FROM teacher WHERE email = ?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, teacherEmail);
			preparedStatement.setString(2, teacherEmail);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int moduleId = resultSet.getInt("module_id");
					String moduleName = resultSet.getString("module_name");
					int level = resultSet.getInt("level");
					String courseName = resultSet.getString("course_name");

					Module module = new Module(moduleId, moduleName, level, courseName);
					modules.add(module);
				}
			}
		} catch (SQLException e) {
			handleSQLException(e, "Error fetching teacher modules");
			e.printStackTrace();
		} catch (Exception e) {
			handleException(e, "Unexpected error");
			e.printStackTrace();
		}

		return modules;
	}

	private void handleSQLException(SQLException e, String message) {
		JOptionPane.showMessageDialog(this, message + "\nError details: " + e.getMessage(), "SQL Exception",
				JOptionPane.ERROR_MESSAGE);
	}

	private void handleException(Exception e, String message) {
		JOptionPane.showMessageDialog(this, message + "\nError details: " + e.getMessage(), "Exception",
				JOptionPane.ERROR_MESSAGE);
	}

	private void setupTableAppearance(JTable table) {
		JTableHeader header = table.getTableHeader();
		UIManager.put("Table.background", new Color(240, 248, 255));
		UIManager.put("Table.gridColor", Color.LIGHT_GRAY);
		UIManager.put("TableHeader.background", Color.BLACK);
		UIManager.put("TableHeader.foreground", Color.WHITE);

		header.setBackground(Color.BLACK);
		header.setForeground(Color.WHITE);
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		table.setGridColor(Color.LIGHT_GRAY);
		table.setRowHeight(25);
		table.setBackground(new Color(240, 248, 255));

	}

	class Module {
		private int moduleId;
		private String moduleName;
		private int level;
		private String courseName;

		public Module(int moduleId, String moduleName, int level, String courseName) {
			this.moduleId = moduleId;
			this.moduleName = moduleName;
			this.level = level;
			this.courseName = courseName;
		}

		public int getModuleId() {
			return moduleId;
		}

		public String getModuleName() {
			return moduleName;
		}

		public int getLevel() {
			return level;
		}

		public String getCourseName() {
			return courseName;
		}
	}
}
