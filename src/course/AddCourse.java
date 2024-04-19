package course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connection.ConnectionManager;
import gui.RoundButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class AddCourse extends JDialog {

    private static final long serialVersionUID = 1L;
	private JTextField courseField;
    private JTextField yearField;
    private DefaultTableModel model;
    

    public AddCourse(JFrame parent, DefaultTableModel model) {
        super(parent, "Add Course", true);
        setUndecorated(true);

        getContentPane().setBackground(Color.WHITE);
        setSize(368, 460);

        courseField = new JTextField();
        courseField.setBounds(54, 137, 260, 35);

        yearField = new JTextField();
        yearField.setBounds(54, 226, 260, 35);

        this.model = model;

        getContentPane().setLayout(null);

        JLabel lblCourseName = new JLabel("Course Name");
        lblCourseName.setBounds(57, 116, 100, 20);
        getContentPane().add(lblCourseName);
        getContentPane().add(courseField);

        JLabel lblCourseYear = new JLabel("Course year");
        lblCourseYear.setBounds(54, 204, 80, 20);
        getContentPane().add(lblCourseYear);
        getContentPane().add(yearField);

        JButton okButton = new RoundButton("Add", Color.WHITE, new Color(50, 144, 255), 30);
        okButton.setBounds(57, 295, 110, 40);

        JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
        cancelButton.setBounds(200, 295, 110, 40);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        getContentPane().add(okButton);
        getContentPane().add(cancelButton);

        JLabel titleLabel = new JLabel("Add Course");
        titleLabel.setForeground(new Color(50, 144, 255));
        titleLabel.setFont(new Font("Oriya MN", Font.PLAIN, 20));
        titleLabel.setBounds(54, 60, 133, 20);
        getContentPane().add(titleLabel);

        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    private void onAdd() {
        String newCourse = getNewCourse();
        int newYear = getNewYear();

        if (newCourse.trim().isEmpty()) {
            showError("Please enter a course name.");
            return;
        }

        if (newYear < 0) {
            showError("Please enter a valid positive year.");
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            String existingCourse = (String) model.getValueAt(i, 0);
            int existingYear = (int) model.getValueAt(i, 1);

            if (existingCourse.equalsIgnoreCase(newCourse) && existingYear == newYear) {
                showError("Course already exists.");
                return;
            }
        }

        model.addRow(new Object[]{newCourse, newYear});

        addCourseToDatabase(newCourse, newYear);

        dispose();
    }

    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }



    private void onCancel() {
        dispose();
    }

    public String getNewCourse() {
        return courseField.getText();
    }

    public int getNewYear() {
        try {
            return Integer.parseInt(yearField.getText());
        } catch (NumberFormatException e) {
            return -1; 
        }
    }
    private void addCourseToDatabase(String courseName, int totalYear) {
        try (Connection connection = ConnectionManager.getConnection()) {

            createCourseTable(connection);

          
            String query = "INSERT INTO course (course_name, total_year) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, courseName);
                preparedStatement.setInt(2, totalYear);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Error adding course to the database: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Unexpected error: " + ex.getMessage());
        }
    }

    private void createCourseTable(Connection connection) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS course ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "course_name VARCHAR(255) NOT NULL,"
                + "total_year INT NOT NULL"
                + ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        } catch (Exception ex) {
            ex.printStackTrace();
           
        }
    }

}
