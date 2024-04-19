package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connection.ConnectionManager;
import course.CourseDeleteObserver;
import student.StudentsObserver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentsTable extends JPanel implements CourseDeleteObserver {

    private static final long serialVersionUID = 1L;
    private JTable studentTable;
    private JButton viewDetailsButton;
    private JButton editStudentButton;
    private JButton addStudentButton;
    private List<StudentsObserver> observers = new ArrayList<>();

    private JButton deleteStudentButton;
    

    public StudentsTable() {
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        DefaultTableModel model = new DefaultTableModel();
        studentTable = new JTable(model);

        studentTable.getTableHeader().setBackground(Color.BLACK);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        
        studentTable.setRowHeight(25);
        JTableHeader header = studentTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 25));
        header.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
        header.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
        studentTable.setGridColor(Color.LIGHT_GRAY);
        studentTable.setBackground(new Color(240,248,255));


        fetchDataFromDatabase();
        JScrollPane scrollPane = new JScrollPane(studentTable);

        JPanel buttonsPanel =new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(new Color(240, 248, 255));

        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
        viewDetailsButton.setBackground(new Color(30, 144, 255));
        viewDetailsButton.setFocusPainted(false); 
        viewDetailsButton.setForeground(Color.WHITE);

 
        viewDetailsButton.setOpaque(true);  
        viewDetailsButton.setBorderPainted(false);  
        
        buttonsPanel.add(viewDetailsButton);

        editStudentButton = new JButton("Edit Student");
        editStudentButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
        editStudentButton.setBackground(new Color(30, 144, 255));
        editStudentButton.setOpaque(true);
        editStudentButton.setFocusPainted(false); 
        editStudentButton.setBorderPainted(false);
        editStudentButton.setForeground(Color.WHITE);
        buttonsPanel.add(editStudentButton);


        addStudentButton = new JButton("Add Student");
        addStudentButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
        addStudentButton.setBackground(new Color(30, 144, 255));
        addStudentButton.setOpaque(true);
        addStudentButton.setFocusPainted(false); 
        addStudentButton.setBorderPainted(false);
        addStudentButton.setForeground(Color.WHITE);
        buttonsPanel.add(addStudentButton);


        deleteStudentButton = new JButton("Delete Student");
        deleteStudentButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
        deleteStudentButton.setBackground(new Color(176, 196, 222));
        deleteStudentButton.setOpaque(true);
        deleteStudentButton.setFocusPainted(false); 
        deleteStudentButton.setBorderPainted(false);
        buttonsPanel.add(deleteStudentButton);

     
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

  
        add(contentPanel, BorderLayout.CENTER);

 
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDetails();
            }
        });

        editStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        deleteStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });


        setOpaque(true);
        setBackground(new Color(240, 255, 255)); 
        fetchDataAndDisplay();
    }
    private void fetchDataAndDisplay() {
        DefaultTableModel model = fetchDataFromDatabase();
        studentTable.setModel(model);
    }

    private void viewDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            String selectedName = (String) studentTable.getValueAt(selectedRow, 0);
            int selectedId = (int) studentTable.getValueAt(selectedRow, 1);
            String selectedPhone = (String) studentTable.getValueAt(selectedRow, 2);
            String selectedEmail = (String) studentTable.getValueAt(selectedRow, 3);
            int selectedLevel = (int) studentTable.getValueAt(selectedRow, 4);
            int selectedSemester = (int) studentTable.getValueAt(selectedRow, 5);
            String selectedCourse = (String) studentTable.getValueAt(selectedRow, 6);

            // Display student details
            String details = "Name: " + selectedName + "\nID: " + selectedId +
                    "\nPhone: " + selectedPhone + "\nEmail: " + selectedEmail +
                    "\nLevel: " + selectedLevel + "\nSemester: " + selectedSemester +
                    "\nCourse: " + selectedCourse;
            JOptionPane.showMessageDialog(this, details, "Student Details", JOptionPane.INFORMATION_MESSAGE);
            observers.forEach(observer -> observer.onStudentViewed(selectedName));
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student.");
        }
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            String currentName = (String) studentTable.getValueAt(selectedRow, 0);
            int currentId = (int) studentTable.getValueAt(selectedRow, 1);
            String currentPhone = (String) studentTable.getValueAt(selectedRow, 2);
            String currentEmail = (String) studentTable.getValueAt(selectedRow, 3);
            int currentLevel = (int) studentTable.getValueAt(selectedRow, 4);
            int currentSemester = (int) studentTable.getValueAt(selectedRow, 5);
            String currentCourse = (String) studentTable.getValueAt(selectedRow, 6);

            EditStudentDetails editForm = new EditStudentDetails(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    currentName,
                    currentId,
                    currentPhone,
                    currentEmail,
                    currentLevel,
                    currentSemester,
                    currentCourse,
                    (DefaultTableModel) studentTable.getModel(),
                    selectedRow
            );

            editForm.setVisible(true);
            observers.forEach(observer -> observer.onStudentEdited(currentName));
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student.");
        }
    }
    private DefaultTableModel fetchDataFromDatabase() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Name", "ID", "Phone", "Email", "Level", "Semester", "Course"});

        try (Connection connection = ConnectionManager.getConnection()) {
            if (connection != null) {
                String query = "SELECT * FROM student";
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {

                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int id = resultSet.getInt("student_id");
                        String phone = resultSet.getString("phone");
                        String email = resultSet.getString("email");
                        int level = resultSet.getInt("level");
                        int semester = resultSet.getInt("semester");
                        String course = resultSet.getString("course");

                        model.addRow(new Object[]{name, id, phone, email, level, semester, course});
                    }
                }
            } else {
                System.err.println("Failed to establish a database connection.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error fetching data from the database: " + ex.getMessage());
        }

        return model;}


    private void addStudent() {
      
        AddStudentsDetail addForm = new AddStudentsDetail(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                (DefaultTableModel) studentTable.getModel()
        );
        addForm.setVisible(true);
        String newStudentName = addForm.getNewName();

        observers.forEach(observer -> observer.onStudentAdded(newStudentName));
    }
    public JTable getStudentTable() {
        return studentTable;
    }

    public JButton getViewDetailsButton() {
        return viewDetailsButton;
    }

    public JButton getEditStudentButton() {
        return editStudentButton;
    }
    
    public int getSelectedStudentId() {
        int selectedRow = studentTable.getSelectedRow();
        return selectedRow != -1 ? (int) studentTable.getValueAt(selectedRow, 1) : -1;
    }

    public String getSelectedStudentPhone() {
        int selectedRow = studentTable.getSelectedRow();
        return selectedRow != -1 ? (String) studentTable.getValueAt(selectedRow, 2) : null;
    }

    public String getSelectedStudentEmail() {
        int selectedRow = studentTable.getSelectedRow();
        return selectedRow != -1 ? (String) studentTable.getValueAt(selectedRow, 3) : null;
    }


    public JButton getAddStudentButton() {
        return addStudentButton;
    }

    public JButton getDeleteStudentButton() {
        return deleteStudentButton;
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            int selectedId = (int) studentTable.getValueAt(selectedRow, 1);
            String selectedName = (String) studentTable.getValueAt(selectedRow, 0);
            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.removeRow(selectedRow);

            deleteStudentFromDatabase(selectedId);
            observers.forEach(observer -> observer.onStudentDeleted(selectedName));
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student.");
        }
    }

    private void deleteStudentFromDatabase(int studentId) {
        try (Connection connection = ConnectionManager.getConnection()) {
            if (connection != null) {
                String deleteQuery = "DELETE FROM student WHERE student_id = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                    deleteStatement.setInt(1, studentId);
                    int affectedRows = deleteStatement.executeUpdate();

                    if (affectedRows > 0) {
                        System.out.println("Student deleted successfully from the database.");
                    } else {
                        System.out.println("No rows were deleted from the database.");
                    }
                }
            } else {
                System.err.println("Failed to establish a database connection.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error deleting student from the database: " + ex.getMessage());
        }
    }
    @Override
    public void onCourseDeleted(String courseName) {
       
        if (deleteStudentRecords(courseName)) {
         
            fetchDataAndDisplay();
        } else {
          
            JOptionPane.showMessageDialog(this, "Failed to delete student records from the database.");
        }
    }
    public void addObserver(StudentsObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StudentsObserver observer) {
        observers.remove(observer);
    }

    private boolean deleteStudentRecords(String courseName) {
        try (Connection connection = ConnectionManager.getConnection()) {
    
            String deleteStudentQuery = "DELETE FROM student WHERE course = ?";
            try (PreparedStatement deleteStudentStatement = connection.prepareStatement(deleteStudentQuery)) {
                deleteStudentStatement.setString(1, courseName);
                int rowsAffected = deleteStudentStatement.executeUpdate();
                
                return rowsAffected > 0; 
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false; 
        }
    }



}
