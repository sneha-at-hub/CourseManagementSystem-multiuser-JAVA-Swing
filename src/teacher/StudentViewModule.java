package teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import connection.ConnectionManager;
import panel.TeacherTableObserver;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentViewModule extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable teacherTable;
	private final List<TeacherTableObserver> observers = new ArrayList<>();
	private String email;

	public StudentViewModule(String email) {
		this.email = email;
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		Object[][] data = fetchAndDisplayStudents(email);
		String[] columnNames = { "ID", "Name", "Course", "Email", "Phone" };
		DefaultTableModel model = new DefaultTableModel(data, columnNames);

		teacherTable = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(teacherTable);

		setupTableAppearance();

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		add(contentPanel, BorderLayout.CENTER);
		setOpaque(true);
		setBackground(new Color(255, 255, 255));
	}

	private void setupTableAppearance() {

		teacherTable.getTableHeader().setBackground(Color.BLACK);
		teacherTable.getTableHeader().setForeground(Color.WHITE);
		teacherTable.setRowHeight(25);
		JTableHeader header = teacherTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		teacherTable.setGridColor(Color.LIGHT_GRAY);
		teacherTable.setBackground(new Color(240, 248, 255));
	}

	public Object[][] fetchAndDisplayStudents(String teacherEmail) {
		Object[][] data = null;
		String query = "SELECT e.id as student_id, e.student_name, e.course_name, e.email, e.phone_number, m.level, m.module_name "
				+ "FROM enrolledstudents e " + "JOIN modules m ON e.course_name = m.course_name AND e.level = m.level "
				+ "WHERE e.course_name = (SELECT course FROM teacher WHERE email = ?) AND m.module_name = (SELECT subject FROM teacher WHERE email = ?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, teacherEmail);
			preparedStatement.setString(2, teacherEmail);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				List<Object[]> dataList = new ArrayList<>();
				while (resultSet.next()) {
					int studentId = resultSet.getInt("student_id");
					String studentName = resultSet.getString("student_name");
					String course = resultSet.getString("course_name");
					String studentEmail = resultSet.getString("email");
					String phone = resultSet.getString("phone_number");
					int moduleLevel = resultSet.getInt("level");
					String moduleName = resultSet.getString("module_name");

					dataList.add(new Object[] { studentId, studentName, course, studentEmail, phone, moduleLevel,
							moduleName });
				}

				data = dataList.toArray(new Object[dataList.size()][]);

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

	private void handleSQLException(SQLException e, String errorMessage) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, errorMessage + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void handleException(Exception e, String errorMessage) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, errorMessage + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	public JTable getTeacherTable() {
		return teacherTable;
	}

	public void addObserver(TeacherTableObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(TeacherTableObserver observer) {
		observers.remove(observer);
	}
}
