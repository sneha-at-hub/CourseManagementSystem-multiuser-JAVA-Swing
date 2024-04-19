package student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import connection.ConnectionManager;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfilePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel courseLabel;
    private JLabel levelLabel;
    private String email;

    public ProfilePanel(String email) {
    	setBackground(new Color(255, 255, 255));
        this.email = email;
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(null);

        nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
        nameLabel.setBounds(10, 102, 317, 69);
        add(nameLabel);

        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 160, 317, 69);
        add(emailLabel);

        courseLabel = new JLabel("Course:");
        courseLabel.setBounds(10, 219, 317, 69);
        add(courseLabel);

        levelLabel = new JLabel("Level:");
        levelLabel.setBounds(10, 287, 317, 69);
        add(levelLabel);
        
        JLabel lblProfile = new JLabel("Profile");
        lblProfile.setForeground(new Color(50, 144, 255));
        lblProfile.setFont(new Font("Oriya MN", Font.PLAIN, 46));
        lblProfile.setBounds(10, 32, 221, 50);
        add(lblProfile);



        fetchStudentProfileFromDatabase();
    }

    private void fetchStudentProfileFromDatabase() {
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT student_name, email, course_name, level FROM enrolledstudents WHERE email = ?";
            System.out.println("Executing SQL Query: " + query);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("student_name");
                        String email = resultSet.getString("email");
                        String course = resultSet.getString("course_name");
                        int level = resultSet.getInt("level");

                        nameLabel.setText("Name: " + name);
                        emailLabel.setText("Email: " + email);
                        courseLabel.setText("Course: " + course);
                        levelLabel.setText("Level: " + level);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error executing SQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
