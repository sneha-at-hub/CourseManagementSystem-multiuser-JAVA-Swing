package course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.ConnectionManager;

public class CourseCount {
    public static int getTotalCourseCount() {
        int count = 0;

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT COUNT(id) AS course_count FROM course";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    count = resultSet.getInt("course_count");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching course count. Error: " + ex.getMessage());
        }

        return count;
    }
}
