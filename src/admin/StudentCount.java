package admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.ConnectionManager;

public class StudentCount {
    public static int getTotalStudentCount() {
        int count = 0;

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT COUNT(id) AS student_count FROM student";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    count = resultSet.getInt("student_count");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching student count. Error: " + ex.getMessage());
        }

        return count;
    }
}
