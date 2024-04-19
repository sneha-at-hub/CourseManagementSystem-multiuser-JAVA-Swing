package user;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import admin.Admin;
import connection.ConnectionManager;
import gui.CustomDialog;
import gui.RoundBorder;
import gui.RoundButton;
import student.Students;
import teacher.Teacher;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private String teacherEmail;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        contentPane = new JPanel();
        setResizable(false);
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(329, 140, 371, 300);
        contentPane.add(panel);
        
        RoundButton btnNewButton = new RoundButton("Login", Color.white, new Color(50,144,255), 30);


        btnNewButton.setBackground(new Color(50, 144, 255));


        btnNewButton.setBounds(38, 191, 300, 38);
        
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your login logic here
            }
        });
        panel.setLayout(null);
        panel.add(btnNewButton);;
        
        JLabel lblNewLabel = new JLabel("Email");
        lblNewLabel.setBounds(38, 20, 61, 16);
        lblNewLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
        panel.add(lblNewLabel);
     

        // Create a JPanel for the email field
        JPanel emailPanel = new JPanel();
        emailPanel.setBackground(Color.WHITE);
        emailPanel.setBounds(35, 47, 300, 38);
        panel.add(emailPanel);
        emailPanel.setLayout(null);

        // Add an icon to the email panel (assuming you have an icon for the email field)
        JLabel emailIconLabel = new JLabel(new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-mail-15.png"));
        emailIconLabel.setBounds(6, 4, 32, 27);
        emailPanel.add(emailIconLabel);

        // Set rounded borders for emailPanel
        emailPanel.setBorder(new RoundBorder(SystemColor.window, 30));

        // Create the email text field
        textField = new JTextField();
        textField.setForeground(Color.DARK_GRAY);
        textField.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
        textField.setBackground(Color.DARK_GRAY);
        textField.setBounds(40, 10, 251, 14);
        textField.setColumns(10);
        textField.setOpaque(false); // Make the text field background transparent
        textField.setBorder(null);  // Remove border from the text field
        emailPanel.add(textField);

        // Create a JPanel for the password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBounds(35, 125, 300, 38);
        panel.add(passwordPanel);
        passwordPanel.setLayout(null);

        // Add an icon to the password panel (assuming you have an icon for the password field)
        JLabel passwordIconLabel = new JLabel(new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-password-16.png"));
        passwordIconLabel.setBounds(7, 4, 32, 27);
        passwordPanel.add(passwordIconLabel);

        // Set rounded borders for passwordPanel
        passwordPanel.setBorder(new RoundBorder(SystemColor.window, 30));

        // Create the password text field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setForeground(Color.DARK_GRAY);
        passwordField.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
        passwordField.setBounds(40, 11, 240, 14);
        passwordField.setColumns(10);
        passwordField.setOpaque(false); 
        passwordField.setBorder(null);  
        passwordPanel.add(passwordField);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 10));
        lblPassword.setBounds(35, 97, 105, 16);
        panel.add(lblPassword);
        
        JPanel panel_5 = new JPanel();
        panel_5.setBackground(Color.WHITE);
        panel_5.setBounds(0, 266, 359, 53);
        panel.add(panel_5);
        
        JButton signupbutton = new JButton("Sign Up");
        signupbutton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                Select sg = new Select();
                sg.setVisible(true);
                dispose();
        	}
        });
        signupbutton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
        signupbutton.setBounds(197, 8, 88, 24);

        // Set link-like appearance
        signupbutton.setForeground(Color.black);
        signupbutton.setBorderPainted(false);
        signupbutton.setContentAreaFilled(false);
        signupbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupbutton.setUI(new BasicButtonUI());

        signupbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add your action here
                // For example, change text color on click
                signupbutton.setForeground(new Color(0, 102, 255));

            }
        });

        panel_5.setLayout(null);
        panel_5.add(signupbutton);
        
        JLabel lblNewLabel_2 = new JLabel("Dont have an account?");
        lblNewLabel_2.setForeground(new Color(128, 128, 128));
        lblNewLabel_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
        lblNewLabel_2.setBounds(83, 11, 142, 16);
        panel_5.add(lblNewLabel_2);

        // Add image to panel_2
        ImageIcon logoIcon = new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/nilo.png");
        
        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(255, 255, 255));
        panel_3.setBounds(6, 6, 324, 466);
        contentPane.add(panel_3);
        
        // Add image to panel_2
        ImageIcon logoIcon2 = new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/sneha.png");
        panel_3.setLayout(null);
        JLabel logoLabel2 = new JLabel(logoIcon2);
        logoLabel2.setBackground(new Color(255, 255, 255));
        logoLabel2.setBounds(6, 6, 312, 454);
        panel_3.add(logoLabel2);
        
        JPanel panel_2 = new JPanel();
        panel_2.setBounds(0, 6, 324, 69);
        panel_3.add(panel_2);
        panel_2.setBackground(new Color(255, 255, 255));
        panel_2.setLayout(null);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(6, 6, 76, 66);
        panel_2.add(logoLabel);
        
        JLabel lblNewLabel_1 = new JLabel("COMPASS");
        lblNewLabel_1.setFont(new Font("Ayuthaya", Font.BOLD, 11));
        lblNewLabel_1.setBounds(47, 25, 80, 52);
        panel_2.add(lblNewLabel_1);
        
        JPanel panel_4 = new JPanel();
        panel_4.setBackground(Color.WHITE);
        panel_4.setBounds(0, 6, 700, 466);
        contentPane.add(panel_4);
        panel_4.setLayout(null);
        
          
          JPanel panel_1 = new JPanel();
          panel_1.setBounds(327, 95, 375, 40);
          panel_4.add(panel_1);
          panel_1.setBackground(Color.WHITE);
          panel_1.setLayout(null);
          
          JLabel lblLogin = new JLabel("Login");
          lblLogin.setBounds(35, 6, 102, 27);
          panel_1.add(lblLogin);
          lblLogin.setFont(new Font(".AppleSystemUIFont", Font.BOLD, 18));
          
          JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
          separator.setPreferredSize(new Dimension(20, 40));
          separator.setBounds(45, 28, 78, 16);
          panel_1.add(separator);
          separator.setBackground(new Color(0, 144, 255));
          
          JLabel lblNewLabel_3 = new JLabel("Welcome to");
          lblNewLabel_3.setForeground(SystemColor.windowText);
          lblNewLabel_3.setFont(new Font("Oriya MN", Font.BOLD, 25));
          lblNewLabel_3.setBounds(355, 32, 189, 40);
          panel_4.add(lblNewLabel_3);
          
          JLabel lblNewLabel_3_1 = new JLabel("Compass");
          lblNewLabel_3_1.setForeground(new Color(3, 75, 234));
          lblNewLabel_3_1.setFont(new Font("Oriya MN", Font.PLAIN, 26));
          lblNewLabel_3_1.setBounds(512, 32, 161, 40);
          panel_4.add(lblNewLabel_3_1);
          

    
          btnNewButton.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                  // Add your login logic here
                  String email = textField.getText();
                  char[] password = passwordField.getPassword();

                  if (email.isEmpty() || password.length == 0) {
                      showErrorMessage("Please enter both email and password.");
                      return;
                  }

                  String userType = validateUser(email, password);

                  if (userType != null) {
                      switch (userType) {
                          case "Teacher":
                              Teacher teacherPage = new Teacher(email);
                              teacherPage.setVisible(true);
                              break;
                          case "Student":
                              Students studentPage = new Students(email);
                              studentPage.setVisible(true);
                              break;
                          case "Admin":
                              Admin adminPage = new Admin();
                              adminPage.setVisible(true);
                              break;
                          default:
                              break;
                      }
                      dispose();
                  } else {
                      showErrorMessage("Invalid credentials. Please try again.");
                  }
              }
          });
}
    private void showErrorMessage(String message) {
        ImageIcon errorIcon = new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-error-64.png");
        CustomDialog errorDialog = new CustomDialog();
        errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        errorDialog.setDialogDetails(errorIcon, message);
        errorDialog.setLocationRelativeTo(null);
        errorDialog.setVisible(true);
    }

    private String validateUser(String email, char[] password) {
        try {
            Connection connection = ConnectionManager.getConnection();
            String query = "SELECT userType FROM Users WHERE email=? AND password=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, new String(password));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("userType");
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error validating user. Error: " + ex.getMessage());
        }
        return null;
    }


  
}
