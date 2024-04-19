package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "CourseManagementSystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + DATABASE_NAME, USERNAME, PASSWORD);
    }

    public static void createDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();

            // Create the database if not exists
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
            System.out.println("Database created successfully!");

            // Switch to the created database
            statement.executeUpdate("USE " + DATABASE_NAME);

        } catch (SQLException ex) {
            System.err.println("Error creating database. Error: " + ex.getMessage());
        }
    }
    
    public static String getEnrolledCourseName(String email) {
        try (Connection connection = getConnection()) {
            String query = "SELECT course_name FROM enrolledstudents WHERE email = '" + email + "'";
            
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                if (resultSet.next()) {
                    return resultSet.getString("course_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no course is found
    }
    
    public static int getEnrolledLevel(String email) {
        int enrolledLevel = -1; // Default value or error indicator

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT level FROM enrolledstudents WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        enrolledLevel = resultSet.getInt("level");
                    }
                }
            }
        } catch (SQLException ex) {
            // Log SQL exceptions
            System.err.println("Error executing SQL query: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            // Log other exceptions
            ex.printStackTrace();
        }

        return enrolledLevel;
    }




}
