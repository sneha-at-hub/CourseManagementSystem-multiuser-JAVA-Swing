package admin;

import javax.swing.*;

import gui.RoundButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditEnrollment extends JFrame {
    private static final long serialVersionUID = 1L;
	private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField courseNameField;
    private JTextField levelField;

    private JButton saveButton;
    private JButton cancelButton;

    private Enrollment enrolledStudentPanel;

    public EditEnrollment(Enrollment enrolledStudentPanel, String studentId, String studentName,
                           String email, String phoneNumber, String address, String courseName, int level) {
    	getContentPane().setBackground(Color.WHITE);
        this.enrolledStudentPanel = enrolledStudentPanel;

        setTitle("Edit Student");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 455);
        setLocationRelativeTo(null);
        setUndecorated(true);
        
        

        // Initialize components
        studentIdField = new JTextField(studentId);
        studentIdField.setBounds(186, 55, 200, 37);

        saveButton =new RoundButton("Save", Color.WHITE, new Color(50, 144, 255), 30);
        saveButton.setBounds(63, 360, 100, 37);
        cancelButton =  new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
        cancelButton.setBounds(200, 360, 100, 37);
                getContentPane().setLayout(null);
                
                JLabel titleLabel = new JLabel("Edit Enrollment");
                titleLabel.setBounds(22, 21, 200, 37);
                titleLabel.setForeground(new Color(50, 144, 255));
                titleLabel.setFont(new Font("Oriya MN", Font.PLAIN, 20));
                getContentPane().add(titleLabel);
        
                // Add components to the form
                JLabel label = new JLabel("Student ID:");
                label.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
                label.setBounds(22, 55, 152, 37);
                getContentPane().add(label);
        getContentPane().add(studentIdField);
        JLabel label_1 = new JLabel("Student Name:");
        label_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
        label_1.setBounds(22, 100, 127, 37);
        getContentPane().add(label_1);
        studentNameField = new JTextField(studentName);
        studentNameField.setBounds(186, 100, 200, 37);
        getContentPane().add(studentNameField);
        JLabel label_2 = new JLabel("Email:");
        label_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
        label_2.setBounds(23, 140, 84, 37);
        getContentPane().add(label_2);
        emailField = new JTextField(email);
        emailField.setBounds(186, 140, 200, 37);
        getContentPane().add(emailField);
        JLabel label_3 = new JLabel("Phone Number:");
        label_3.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
        label_3.setBounds(22, 179, 127, 37);
        getContentPane().add(label_3);
        phoneNumberField = new JTextField(phoneNumber);
        phoneNumberField.setBounds(186, 179, 200, 37);
        getContentPane().add(phoneNumberField);
        JLabel label_4 = new JLabel("Address:");
        label_4.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
        label_4.setBounds(22, 218, 100, 37);
        getContentPane().add(label_4);
        addressField = new JTextField(address);
        addressField.setBounds(186, 218, 200, 37);
        getContentPane().add(addressField);
        JLabel label_5 = new JLabel("Course Name:");
        label_5.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
        label_5.setBounds(22, 258, 117, 37);
        getContentPane().add(label_5);
        courseNameField = new JTextField(courseName);
        courseNameField.setBounds(186, 258, 200, 37);
        getContentPane().add(courseNameField);
        JLabel label_6 = new JLabel("Level:");
        label_6.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
        label_6.setBounds(22, 298, 122, 37);
        getContentPane().add(label_6);
        levelField = new JTextField(String.valueOf(level));
        levelField.setBounds(186, 298, 200, 37);
        getContentPane().add(levelField);
        
        JLabel label_9 = new JLabel("");
        label_9.setBounds(200, 298, 200, 37);
        getContentPane().add(label_9);
        getContentPane().add(saveButton);
        getContentPane().add(cancelButton);

        // Set up action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void saveChanges() {
        // Retrieve updated data from the form fields
        String studentId = studentIdField.getText();
        String studentName = studentNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        String courseName = courseNameField.getText();
        int level = Integer.parseInt(levelField.getText());

        // Update the data in the enrolledStudentPanel (you need to implement this method)
        enrolledStudentPanel.updateStudentData(studentId, studentName, email, phoneNumber, address, courseName, level);

        // Close the edit form
        dispose();
    }
}
