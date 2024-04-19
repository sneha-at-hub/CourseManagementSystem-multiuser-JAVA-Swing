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

public class TeacherView extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable teacherTable;
    private DefaultTableModel teacherTableModel;
    private String email;

    public TeacherView(String email) {
        this.email = email;
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        createTeacherTable();
        teacherTableModel = new DefaultTableModel();
        initializeTeacherTableModel();
        teacherTable = new JTable(teacherTableModel);

        setupTableAppearance();

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeTeacherTableModel() {
        teacherTableModel.addColumn("Teacher ID");
        teacherTableModel.addColumn("Teacher Name");
        teacherTableModel.addColumn("Subject");
        teacherTableModel.addColumn("Course");
        teacherTableModel.addColumn("Email");
        teacherTableModel.addColumn("Phone");

        fetchTeacherDataFromDatabase();
    }

    public void fetchTeacherDataFromDatabase() {
        try (Connection connection = ConnectionManager.getConnection()) {
            String selectedCourse = ConnectionManager.getEnrolledCourseName(email);
            int selectedLevel = ConnectionManager.getEnrolledLevel(email);
            String query = "SELECT * FROM teacher WHERE subject IN (SELECT module_name FROM modules WHERE course_name = ? AND level = ?)";

            System.out.println("Executing SQL Query: " + query);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, selectedCourse);
                preparedStatement.setInt(2, selectedLevel);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Object[] rowData = {resultSet.getInt("id"), resultSet.getString("teacher_name"),
                                resultSet.getString("subject"), resultSet.getString("course"),
                                resultSet.getString("email"), resultSet.getString("phone")};
                        teacherTableModel.addRow(rowData);
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

    private void createTeacherTable() {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS teacher (" +
                    "id INT PRIMARY KEY," +
                    "teacher_name VARCHAR(255) NOT NULL," +
                    "subject VARCHAR(255) NOT NULL," +
                    "course VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "phone VARCHAR(255) NOT NULL)";

            statement.execute(createTableQuery);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DefaultTableModel getTeacherTableModel() {
        return teacherTableModel;
    }
}
