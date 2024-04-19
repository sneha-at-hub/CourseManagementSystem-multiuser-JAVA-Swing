package modules;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import connection.ConnectionManager;
import gui.RoundButton;

public class EditModule extends JDialog {

    private static final long serialVersionUID = 1L;
	private JTextField moduleIdField;
    private JTextField moduleNameField;
    private JTextField semesterField;
    private JTextField creditField;
    private String selectedCourse;
    private JComboBox<String> courseComboBox;
    private DefaultTableModel moduleTableModel;
    private int selectedModuleId;
    private JComboBox<String> levelComboBox; 

    public EditModule(JFrame parentFrame, DefaultTableModel moduleTableModel, int selectedModuleId) {
        super(parentFrame, "Edit Module", true);
        this.moduleTableModel = moduleTableModel;
        this.selectedModuleId = selectedModuleId;
        setSize(450, 550);
      	setUndecorated(true);

        getContentPane().setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(); // Adjusted for the new combo box
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
        courseComboBox.setBounds(149, 71, 220, 61);
        formPanel.setLayout(null);
        JLabel courseLabel = new JLabel("Course Name:");
        courseLabel.setBounds(32, 87, 108, 26);
        formPanel.add(courseLabel);
        formPanel.add(courseComboBox);

        moduleIdField = new JTextField();
        moduleIdField.setBounds(149, 136, 220, 33);
        moduleIdField.setEditable(false);  // User cannot edit the module_id
        JLabel moduleIdLabel = new JLabel("Module ID:");
        moduleIdLabel.setBounds(32, 131, 108, 43);
        formPanel.add(moduleIdLabel);
        formPanel.add(moduleIdField);

        moduleNameField = new JTextField();
        moduleNameField.setBounds(149, 187, 221, 33);
        JLabel moduleNameLabel = new JLabel("Module Name:");
        moduleNameLabel.setBounds(32, 173, 180, 61);
        formPanel.add(moduleNameLabel);
        formPanel.add(moduleNameField);

        semesterField = new JTextField();
        semesterField.setBounds(149, 241, 220, 33);
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setBounds(32, 241, 93, 33);
        formPanel.add(semesterLabel);
        formPanel.add(semesterField);

        creditField = new JTextField();
        creditField.setBounds(149, 295, 220, 33);
        JLabel creditLabel = new JLabel("Credit:");
        creditLabel.setBounds(32, 286, 83, 51);
        formPanel.add(creditLabel);
        formPanel.add(creditField);

        // Added level combo box
        levelComboBox = new JComboBox<>(new String[]{"4", "5", "6"});
        levelComboBox.setBounds(149, 342, 220, 61);
        JLabel levelLabel = new JLabel("Level:");
        levelLabel.setBounds(32, 355, 67, 33);
        formPanel.add(levelLabel);
        formPanel.add(levelComboBox);

        getContentPane().add(formPanel, BorderLayout.CENTER);

        JButton updateModuleButton = new RoundButton("Update", Color.WHITE, new Color(50, 144, 255), 30);
        updateModuleButton.setBounds(81, 5, 100, 37);
        updateModuleButton.addActionListener(e -> updateModule());
        JButton cancelButton =new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
        cancelButton.setBounds(224, 5, 100, 37);
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBounds(6, 429, 391, 61);
        formPanel.add(buttonPanel);
        buttonPanel.setLayout(null);
        buttonPanel.add(updateModuleButton);
        buttonPanel.add(cancelButton);
        
        JLabel lblEditModule = new JLabel("Edit Module");
        lblEditModule.setForeground(new Color(50, 144, 255));
        lblEditModule.setFont(new Font("Oriya MN", Font.PLAIN, 20));
        lblEditModule.setBounds(26, 22, 200, 37);
        formPanel.add(lblEditModule);

        populateFields();
        
       
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }

    private String[] getCourseNamesFromDatabase() {
        List<String> courseNames = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT course_name FROM course")) {

            while (resultSet.next()) {
                courseNames.add(resultSet.getString("course_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or log it
        }

        return courseNames.toArray(new String[0]);
    }

    private void populateFields() {
        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM modules WHERE module_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, selectedModuleId);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        courseComboBox.setSelectedItem(resultSet.getString("course_name"));
                        moduleIdField.setText(String.valueOf(resultSet.getInt("module_id")));
                        moduleNameField.setText(resultSet.getString("module_name"));
                        semesterField.setText(String.valueOf(resultSet.getInt("semester")));
                        creditField.setText(String.valueOf(resultSet.getInt("credit")));

                        // Set the selected level
                        String level = String.valueOf(resultSet.getInt("level"));
                        levelComboBox.setSelectedItem(level);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateModule() {
        try (Connection connection = ConnectionManager.getConnection()) {
            String courseName = Objects.requireNonNull(courseComboBox.getSelectedItem()).toString();
            String moduleName = moduleNameField.getText();
            int semester = Integer.parseInt(semesterField.getText());
            int credit = Integer.parseInt(creditField.getText());
            int level = Integer.parseInt(Objects.requireNonNull(levelComboBox.getSelectedItem()).toString()); // Added level

            String query = "UPDATE modules SET course_name=?, module_name=?, semester=?, credit=?, level=? WHERE module_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, courseName);
                preparedStatement.setString(2, moduleName);
                preparedStatement.setInt(3, semester);
                preparedStatement.setInt(4, credit);
                preparedStatement.setInt(5, level); // Added level
                preparedStatement.setInt(6, selectedModuleId);

                preparedStatement.executeUpdate();
            }

            // Update the table model with the edited module
            for (int row = 0; row < moduleTableModel.getRowCount(); row++) {
                if ((int) moduleTableModel.getValueAt(row, 0) == selectedModuleId) {
                    moduleTableModel.setValueAt(courseName, row, 1);
                    moduleTableModel.setValueAt(moduleName, row, 2);
                    moduleTableModel.setValueAt(semester, row, 3);
                    moduleTableModel.setValueAt(credit, row, 4);
                    moduleTableModel.setValueAt(level, row, 5); // Adjusted for level
                    break;
                }
            }

            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating module. Please check the input values.");
        }
    }
    // Add this method to set the selected course
    public void setSelectedCourse(String selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Edit Module Test");
            DefaultTableModel defaultTableModel = new DefaultTableModel();
            EditModule editModuleForm = new EditModule(frame, defaultTableModel, 1);
            editModuleForm.setVisible(true);
        });
    }
}
