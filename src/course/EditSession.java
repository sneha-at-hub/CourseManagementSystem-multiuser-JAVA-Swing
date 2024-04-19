package course;


import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import connection.ConnectionManager;
import gui.RoundButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

public class EditSession extends JDialog {

    private static final long serialVersionUID = 1L;
	private JComboBox<String> sessionNameComboBox;
    private JComboBox<String> sessionLevelComboBox;
    private JTextField sessionStartField;
    private JTextField sessionEndField;

    private DefaultTableModel sessionTableModel;
    private int rowIndex;

    public EditSession(JFrame parentFrame, String sessionName, String sessionLevel, String sessionStart, String sessionEnd, DefaultTableModel sessionTableModel, int rowIndex) {
        super(parentFrame, "Edit Session", true);
        getContentPane().setBackground(Color.WHITE);
        this.sessionTableModel = sessionTableModel;
        this.rowIndex = rowIndex;
        setSize(500,500);
        setUndecorated(true);

        JLabel sessionNameLabel = new JLabel("Session Name:");
        sessionNameLabel.setBounds(57, 110, 108, 46);
        sessionNameComboBox = new JComboBox<>(getCourseNamesFromDatabase());
        sessionNameComboBox.setBounds(160, 111, 290, 46);
        sessionNameComboBox.setSelectedItem(sessionName);

        JLabel sessionLevelLabel = new JLabel("Session Level:");
        sessionLevelLabel.setBounds(57, 168, 108, 46);
        sessionLevelComboBox = new JComboBox<>(new String[]{"4", "5", "6"}); 
        sessionLevelComboBox.setBounds(160, 169, 290, 46);
        sessionLevelComboBox.setSelectedItem(sessionLevel);

        
        JLabel sessionStartLabel = new JLabel("Session Start:");
        sessionStartLabel.setBounds(57, 222, 93, 46);
        JLabel sessionEndLabel = new JLabel("Session End:");
        sessionEndLabel.setBounds(57, 283, 101, 46);

        MaskFormatter dateFormatter;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            e.printStackTrace();
            dateFormatter = new MaskFormatter(); 
        }

        sessionStartField = new JFormattedTextField(dateFormatter);
        sessionStartField.setBounds(160, 227, 290, 37);
        sessionEndField = new JFormattedTextField(dateFormatter);
        sessionEndField.setBounds(160, 288, 290, 37);


        JButton editButton = new RoundButton("Edit Session", Color.WHITE, new Color(50, 144, 255), 30);
        editButton.setBounds(125, 372, 100, 37);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSession();
            }
        });
        getContentPane().setLayout(null);
        getContentPane().add(sessionNameLabel);
        getContentPane().add(sessionNameComboBox);
        getContentPane().add(sessionLevelLabel);
        getContentPane().add(sessionLevelComboBox);
        getContentPane().add(sessionStartLabel);
        getContentPane().add(sessionStartField);
        getContentPane().add(sessionEndLabel);
        getContentPane().add(sessionEndField);
        getContentPane().add(editButton);
        
        RoundButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
        cancelButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        	}
        });
        cancelButton.setBounds(282, 372, 100, 37);
        getContentPane().add(cancelButton);
        
        JLabel lblEditSession = new JLabel("Edit Session");
        lblEditSession.setForeground(new Color(50, 144, 255));
        lblEditSession.setFont(new Font("Oriya MN", Font.PLAIN, 27));
        lblEditSession.setBounds(57, 44, 200, 37);
        getContentPane().add(lblEditSession);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void editSession() {
        String editedSessionName = (String) sessionNameComboBox.getSelectedItem();
        String editedSessionLevel = (String) sessionLevelComboBox.getSelectedItem();
        String editedSessionStart = sessionStartField.getText();
        String editedSessionEnd = sessionEndField.getText();

        if (editedSessionName.isEmpty() || editedSessionLevel.isEmpty() || editedSessionStart.isEmpty() || editedSessionEnd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        sessionTableModel.setValueAt(editedSessionName, rowIndex, 0);
        sessionTableModel.setValueAt(editedSessionLevel, rowIndex, 1);
        sessionTableModel.setValueAt(editedSessionStart, rowIndex, 2);
        sessionTableModel.setValueAt(editedSessionEnd, rowIndex, 3);
        updateSessionInDatabase(editedSessionName, editedSessionLevel, editedSessionStart, editedSessionEnd);

        // Close the dialog
        dispose();
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
        }

        return courseNames.toArray(new String[0]);
    }

  
    private void updateSessionInDatabase(String editedSessionName, String editedSessionLevel, String editedSessionStart, String editedSessionEnd) {
        try (Connection connection = ConnectionManager.getConnection()) {
            String updateSessionQuery = "UPDATE session SET session_level=?, session_start=?, session_end=? WHERE session_name=?";
            try (PreparedStatement updateSessionStatement = connection.prepareStatement(updateSessionQuery)) {
                updateSessionStatement.setString(1, editedSessionLevel);
                updateSessionStatement.setString(2, editedSessionStart);
                updateSessionStatement.setString(3, editedSessionEnd);
                updateSessionStatement.setString(4, editedSessionName);

                int rowsAffected = updateSessionStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Session updated in the database successfully.");
                } else {
                    System.out.println("Failed to update session in the database.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update session in the database. Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Test Frame");
                frame.setSize(400, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);

                JButton openButton = new JButton("Open Edit Session Form");
                openButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DefaultTableModel sessionTableModel = new DefaultTableModel();
                        sessionTableModel.addColumn("Session");
                        sessionTableModel.addColumn("Level");
                        sessionTableModel.addColumn("Start");
                        sessionTableModel.addColumn("End");

                        sessionTableModel.addRow(new Object[]{"Session 1", "Beginner", "9:00 AM", "10:30 AM"});
                        sessionTableModel.addRow(new Object[]{"Session 2", "Intermediate", "11:00 AM", "12:30 PM"});

                        EditSession editSessionForm = new EditSession(frame,
                                "Session 1", "Beginner", "9:00 AM", "10:30 AM",
                                sessionTableModel, 0);
                        editSessionForm.setVisible(true);
                    }
                });

                frame.getContentPane().setLayout(new FlowLayout());
                frame.getContentPane().add(openButton);
                frame.setVisible(true);
            }
        });
    }
}
