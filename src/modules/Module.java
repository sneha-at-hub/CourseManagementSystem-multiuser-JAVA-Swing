package modules;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import connection.ConnectionManager;
import course.CourseDeleteObserver;

public class Module extends JPanel implements CourseDeleteObserver {

    private static final long serialVersionUID = 1L;
    private JTable moduleTable;
   
    private JButton addModuleButton;
    private JButton viewModuleButton;
    private JButton deleteModuleButton;
    private JButton editModuleButton;
    private JComboBox<String> courseComboBox;

    public Module(DefaultTableModel courseTableModel) {
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        createModulesTable();

        DefaultTableModel moduleTableModel = fetchModuleDataFromDatabase();
        moduleTable = new JTable(moduleTableModel);
        
        moduleTable.getTableHeader().setBackground(Color.BLACK);
        moduleTable.getTableHeader().setForeground(Color.WHITE);
        

        moduleTable.setRowHeight(25); 
        JTableHeader header = moduleTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 25)); 
        header.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
        header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Set grid line color
        moduleTable.setGridColor(Color.LIGHT_GRAY);
        moduleTable.setBackground(new Color(240,248,255));



        // Add the JTable to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(moduleTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        // Course ComboBox
        courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
        buttonsPanel.add(courseComboBox);

        // Add Module button
        addModuleButton = new JButton("Add Module");
        
        addModuleButton.setBackground(new Color(30, 144, 255));
        addModuleButton.setFocusPainted(false);
        addModuleButton.setForeground(Color.WHITE);

        addModuleButton.setOpaque(true);
        addModuleButton.setBorderPainted(false);
		
		
        buttonsPanel.add(addModuleButton);

        // View Module button
        viewModuleButton = new JButton("View Module");
        
        viewModuleButton.setBackground(new Color(30, 144, 255));
        viewModuleButton.setFocusPainted(false);
        viewModuleButton.setForeground(Color.WHITE);

        viewModuleButton.setOpaque(true);
		viewModuleButton.setBorderPainted(false);
		
		
        buttonsPanel.add(viewModuleButton);

        // Delete Module button
        deleteModuleButton = new JButton("Delete Module");
        
        
        deleteModuleButton.setBackground(new Color(30, 144, 255));
        deleteModuleButton.setFocusPainted(false);
        deleteModuleButton.setForeground(Color.WHITE);

		deleteModuleButton.setOpaque(true);
		deleteModuleButton.setBorderPainted(false);
		
		
        buttonsPanel.add(deleteModuleButton);

        // Edit Module button
        editModuleButton = new JButton("Edit Module");
        
        
        editModuleButton.setBackground(new Color(30, 144, 255));
        editModuleButton.setFocusPainted(false);
        editModuleButton.setForeground(Color.WHITE);

        editModuleButton.setOpaque(true);
        editModuleButton.setBorderPainted(false);
		
		
        buttonsPanel.add(editModuleButton);

        // Show Modules for Course button
        JButton showModulesForCourseButton = new JButton("Show Modules for Course");
        
        showModulesForCourseButton.setBackground(new Color(30, 144, 255));
        showModulesForCourseButton.setFocusPainted(false);
        showModulesForCourseButton.setForeground(Color.WHITE);

        showModulesForCourseButton.setOpaque(true);
        showModulesForCourseButton.setBorderPainted(false);
		
		
        buttonsPanel.add(showModulesForCourseButton);

        // Add buttons panel to content pane
        add(buttonsPanel, BorderLayout.SOUTH);

        addModuleButton.addActionListener(e -> addModule());
        viewModuleButton.addActionListener(e -> viewModule());
        deleteModuleButton.addActionListener(e -> deleteModule());
        editModuleButton.addActionListener(e -> editModule());
        showModulesForCourseButton.addActionListener(e -> showModulesForSelectedCourse());
    }

    private void showModulesForSelectedCourse() {
        String selectedCourse = Objects.requireNonNull(courseComboBox.getSelectedItem()).toString();

        if ("All Modules".equals(selectedCourse)) {
            // Display all modules
            DefaultTableModel moduleTableModel = fetchModuleDataFromDatabase();
            moduleTable.setModel(moduleTableModel);
        } else {
            // Display modules for the selected course
            DefaultTableModel moduleTableModel = fetchModuleDataForCourseFromDatabase(selectedCourse);
            moduleTable.setModel(moduleTableModel);
        }
    }


    private DefaultTableModel fetchModuleDataForCourseFromDatabase(String courseName) {
        DefaultTableModel moduleTableModel = new DefaultTableModel();
        moduleTableModel.addColumn("Module ID");
        moduleTableModel.addColumn("Course Name");
        moduleTableModel.addColumn("Module Name");
        moduleTableModel.addColumn("Semester");
        moduleTableModel.addColumn("Credit");
        moduleTableModel.addColumn("Level"); // Added "Level" column

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM modules WHERE course_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, courseName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Object[] rowData = {
                                resultSet.getInt("module_id"),
                                resultSet.getString("course_name"),
                                resultSet.getString("module_name"),
                                resultSet.getInt("semester"),
                                resultSet.getInt("credit"),
                                resultSet.getInt("level") // Added "Level" column
                        };
                        moduleTableModel.addRow(rowData);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return moduleTableModel;
    }
    private String[] getCourseNamesFromDatabase() {
        List<String> courseNames = new ArrayList<>();

       
        courseNames.add("All Modules");

        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT course_name FROM course")) {

            while (resultSet.next()) {
                courseNames.add(resultSet.getString("course_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
     
        }

        return courseNames.toArray(new String[0]);
    }

    private DefaultTableModel fetchModuleDataFromDatabase() {
        DefaultTableModel moduleTableModel = new DefaultTableModel();
        moduleTableModel.addColumn("Module ID");
        moduleTableModel.addColumn("Course Name");
        moduleTableModel.addColumn("Module Name");
        moduleTableModel.addColumn("Semester");
        moduleTableModel.addColumn("Credit");
        moduleTableModel.addColumn("Level"); // Added "Level" column

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM modules";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getInt("module_id"),
                            resultSet.getString("course_name"),
                            resultSet.getString("module_name"),
                            resultSet.getInt("semester"),
                            resultSet.getInt("credit"),
                            resultSet.getInt("level") // Added "Level" column
                    };
                    moduleTableModel.addRow(rowData);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return moduleTableModel;
    }
    private void addModule() {
        try {
            AddModule addModule = new AddModule(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    (DefaultTableModel) moduleTable.getModel()
            );
            addModule.setVisible(true);
            refreshModuleTable();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void viewModule() {
    }

    private void deleteModule() {
        int selectedRow = moduleTable.getSelectedRow();
        if (selectedRow != -1) {
            int moduleId = (int) moduleTable.getValueAt(selectedRow, 0);

            String selectedCourse = Objects.requireNonNull(courseComboBox.getSelectedItem()).toString();

            try (Connection connection = ConnectionManager.getConnection()) {
                // Delete the module from the database
                String deleteQuery = "DELETE FROM modules WHERE module_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                    preparedStatement.setInt(1, moduleId);
                    preparedStatement.executeUpdate();
                }

                SwingUtilities.invokeLater(() -> {
                    if ("All Modules".equals(selectedCourse)) {
                        refreshModuleTable();
                    } else {
                        DefaultTableModel moduleTableModel = fetchModuleDataForCourseFromDatabase(selectedCourse);
                        moduleTable.setModel(moduleTableModel);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting module from the database.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a module to delete.");
        }
    }


    private void editModule() {
        int selectedRow = moduleTable.getSelectedRow();
        if (selectedRow != -1) {
            int moduleId = (int) moduleTable.getValueAt(selectedRow, 0);
            String selectedCourse = Objects.requireNonNull(courseComboBox.getSelectedItem()).toString();
            SwingUtilities.invokeLater(() -> {
                JFrame editFrame = new JFrame("Edit Module");
                EditModule editModuleForm = new EditModule(editFrame, (DefaultTableModel) moduleTable.getModel(), moduleId);
                editModuleForm.setSelectedCourse(selectedCourse);

                editModuleForm.setVisible(true);
                if ("All Modules".equals(selectedCourse)) {
                    refreshModuleTable();
                } else {
                    DefaultTableModel moduleTableModel = fetchModuleDataForCourseFromDatabase(selectedCourse);
                    moduleTable.setModel(moduleTableModel);
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please select a module to edit.");
        }
    }


    private void refreshModuleTable() {
        DefaultTableModel moduleTableModel = fetchModuleDataFromDatabase();
        moduleTable.setModel(moduleTableModel);
    }

    private void createModulesTable() {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS modules (" +
                    "module_id INT PRIMARY KEY," +
                    "course_name VARCHAR(255) NOT NULL," +
                    "module_name VARCHAR(255) NOT NULL," +
                    "semester INT NOT NULL," +
                    "credit INT NOT NULL," +
                    "level INT NOT NULL)";
            
            statement.execute(createTableQuery);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	@Override
	public void onCourseDeleted(String courseName) {
		 deleteModulesForDeletedCourse(courseName);
		
	}
	
	
	private void deleteModulesForDeletedCourse(String courseName) {
	    try (Connection connection = ConnectionManager.getConnection()) {
	        String deleteQuery = "DELETE FROM modules WHERE course_name = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
	            preparedStatement.setString(1, courseName);
	            preparedStatement.executeUpdate();
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error deleting modules for the deleted course.");
	    }

	    refreshModuleTable();
	}

}
