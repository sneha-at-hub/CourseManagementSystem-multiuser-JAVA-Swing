package teacher; // Assuming there's a 'teacher' package for your teacher-related classes

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.ConnectionManager;

public class TeacherCount {
    public static int getTotalTeacherCount() {
        int count = 0;

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT COUNT(id) AS teacher_count FROM teacher";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    count = resultSet.getInt("teacher_count");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching teacher count. Error: " + ex.getMessage());
        }

        return count;
    }
}
