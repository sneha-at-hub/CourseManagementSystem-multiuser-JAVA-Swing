package user;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.DefaultCaret;

import connection.ConnectionManager;
import gui.CustomDialog;
import gui.RoundBorder;
import gui.RoundButton;
import gui.RoundedPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
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
import javax.swing.JTextArea;

public class SignupPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField emailField;
	private JTextField confirmpassField;
	private JTextField username_Field;
	private JTextField passwordField;
	private String selectedUserType;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignupPage frame = new SignupPage();
					frame.setVisible(true);
				} catch (Exception e) {

					e.printStackTrace();

				}
			}
		});
	}

	public SignupPage() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 248, 255));
		setResizable(false);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("ComboBox.selectionBackground", Color.white);
		UIManager.put("ComboBox.selectionForeground", Color.black);
		UIDefaults defaults = new UIDefaults();

		ImageIcon logoIcon = new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/qu.png");

		JPanel panel_1 = new RoundedPanel(100, new Color(0, 102, 255), new Color(0, 102, 255));
		panel_1.setBounds(45, 47, 821, 580);
		panel_1.setBackground(new Color(0, 0, 0));
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_3 = new JLabel("Start your");
		lblNewLabel_3.setBackground(new Color(255, 255, 255));
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setBounds(30, 170, 200, 50);
		lblNewLabel_3.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 28));
		panel_1.add(lblNewLabel_3);

		JLabel lblNewLabel_3_1 = new JLabel("journey with us.");
		lblNewLabel_3_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_3_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 28));
		lblNewLabel_3_1.setBounds(30, 205, 250, 50);
		panel_1.add(lblNewLabel_3_1);

		JTextArea txtrDiscoverTheSimplicity = new JTextArea();
		txtrDiscoverTheSimplicity.setBackground(new Color(0, 102, 255));
		txtrDiscoverTheSimplicity.setForeground(new Color(255, 255, 255));
		txtrDiscoverTheSimplicity.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		txtrDiscoverTheSimplicity.setText(
				"Discover the simplicity of academic navigation\nusing our College Management System. \nDive into a world where learning meets efficiency!\n");
		txtrDiscoverTheSimplicity.setBounds(30, 258, 315, 120);
		panel_1.add(txtrDiscoverTheSimplicity);

		ImageIcon logoIcon1 = new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/81.png");

		txtrDiscoverTheSimplicity.setCaret(new HiddenCaret());

		JPanel panel_2_1 = new JPanel();
		panel_2_1.setForeground(new Color(255, 255, 255));
		panel_2_1.setLayout(null);
		panel_2_1.setBackground(new Color(0, 102, 255));
		panel_2_1.setBounds(21, 18, 324, 69);
		panel_1.add(panel_2_1);

		JLabel lblNewLabel_1_1 = new JLabel("COMPASS");
		lblNewLabel_1_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_1.setFont(new Font("Ayuthaya", Font.BOLD, 11));
		lblNewLabel_1_1.setBounds(6, 6, 80, 52);
		panel_2_1.add(lblNewLabel_1_1);
		JPanel panel = new RoundedPanel(100, new Color(255, 255, 255), new Color(255, 255, 255));
		panel.setBounds(428, 0, 393, 580);
		panel_1.add(panel);
		panel.setBackground(Color.WHITE);

		JLabel lblNewLabel_2 = new JLabel("Email");
		lblNewLabel_2.setBounds(55, 186, 61, 16);
		lblNewLabel_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		panel.add(lblNewLabel_2);

		JPanel emailPanel = new JPanel();
		emailPanel.setBounds(55, 206, 300, 38);
		emailPanel.setBackground(Color.WHITE);
		panel.add(emailPanel);
		emailPanel.setLayout(null);

		JLabel emailIconLabel = new JLabel(
				new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-mail-15.png"));
		emailIconLabel.setBounds(6, 4, 32, 27);
		emailPanel.add(emailIconLabel);

		emailPanel.setBorder(new RoundBorder(SystemColor.window, 30));

		emailField = new JTextField();
		emailField.setForeground(Color.DARK_GRAY);
		emailField.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		emailField.setBackground(Color.DARK_GRAY);
		emailField.setBounds(40, 10, 251, 14);
		emailField.setColumns(10);
		emailField.setOpaque(false);
		emailField.setBorder(null);
		emailPanel.add(emailField);

		JPanel passwordPanel = new JPanel();
		passwordPanel.setBounds(55, 284, 300, 38);
		passwordPanel.setBackground(Color.WHITE);
		panel.add(passwordPanel);
		passwordPanel.setLayout(null);

		JLabel passwordIconLabel = new JLabel(
				new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-password-16.png"));
		passwordIconLabel.setBounds(7, 4, 32, 27);
		passwordPanel.add(passwordIconLabel);

		passwordPanel.setBorder(new RoundBorder(SystemColor.window, 30));

		JPasswordField passwordField_1 = new JPasswordField();
		passwordField_1.setForeground(Color.DARK_GRAY);
		passwordField_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		passwordField_1.setBounds(40, 11, 240, 14);
		passwordField_1.setColumns(10);
		passwordField_1.setOpaque(false);
		passwordField_1.setBorder(null);
		passwordPanel.add(passwordField_1);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(55, 265, 105, 16);
		lblPassword.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 10));

		panel.add(lblPassword);

		panel.setLayout(null);

		JPanel ConfimP_panel = new JPanel();
		ConfimP_panel.setBounds(55, 362, 300, 38);
		ConfimP_panel.setLayout(null);
		ConfimP_panel.setBorder(new RoundBorder(SystemColor.window, 30));
		ConfimP_panel.setBackground(Color.WHITE);
		panel.add(ConfimP_panel);

		JLabel passwordIconLabel_1 = new JLabel(
				new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-confirm-15.png"));
		passwordIconLabel_1.setBounds(7, 4, 32, 27);

		ConfimP_panel.add(passwordIconLabel_1);

		JPasswordField confirmpassField_1 = new JPasswordField();
		confirmpassField_1.setOpaque(false);
		confirmpassField_1.setForeground(Color.DARK_GRAY);
		confirmpassField_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		confirmpassField_1.setColumns(10);
		confirmpassField_1.setBorder(null);
		confirmpassField_1.setBounds(40, 11, 240, 14);
		ConfimP_panel.add(confirmpassField_1);

		JLabel lblConfirmPassword = new JLabel("Confirm Password");
		lblConfirmPassword.setBounds(55, 343, 105, 16);
		lblConfirmPassword.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 10));
		panel.add(lblConfirmPassword);

		JPanel Fullname_panel = new JPanel();
		Fullname_panel.setBounds(55, 136, 300, 38);
		Fullname_panel.setLayout(null);
		Fullname_panel.setBorder(new RoundBorder(SystemColor.window, 30));
		Fullname_panel.setBackground(Color.WHITE);
		panel.add(Fullname_panel);

		JLabel fullnameicon = new JLabel(
				new ImageIcon("/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/user16.png"));
		fullnameicon.setBounds(6, 4, 32, 27);
		Fullname_panel.add(fullnameicon);

		username_Field = new JTextField();
		username_Field.setOpaque(false);
		username_Field.setForeground(Color.DARK_GRAY);
		username_Field.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		username_Field.setColumns(10);
		username_Field.setBorder(null);
		username_Field.setBackground(Color.DARK_GRAY);
		username_Field.setBounds(40, 10, 251, 14);
		Fullname_panel.add(username_Field);

		JLabel lblFullname = new JLabel("Fullname");
		lblFullname.setBounds(55, 116, 61, 16);
		lblFullname.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		panel.add(lblFullname);
		panel.setLayout(null);

		RoundButton createacc_btn = new RoundButton("Login", Color.white, new Color(50, 144, 255), 30);
		createacc_btn.setBounds(55, 426, 300, 35);
		createacc_btn.setText("Signup");
		createacc_btn.setBackground(Color.BLUE);

		createacc_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String username = username_Field.getText();
				String email = emailField.getText();
				String password = new String(passwordField_1.getPassword());
				String confirmpass = new String(confirmpassField_1.getPassword());

				if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmpass.isEmpty()) {
					
					ImageIcon errorIcon = new ImageIcon(
							"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-error-64.png");
					CustomDialog errorDialog = new CustomDialog();
					errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					errorDialog.setDialogDetails(errorIcon, "Please fill in all fields!");
					errorDialog.setVisible(true);
					return;
				}

				if (!isValidEmail(email)) {
					ImageIcon errorIcon = new ImageIcon(
							"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-error-64.png");
					CustomDialog errorDialog = new CustomDialog();
					errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					errorDialog.setDialogDetails(errorIcon, "Invalid email format!");
					errorDialog.setVisible(true);
					return; // Exit the method, do not proceed with signup
				}

				if (isEmailExists(email)) {
					// Email already exists, show an error message or take appropriate action
					ImageIcon errorIcon = new ImageIcon(
							"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-error-64.png");
					CustomDialog errorDialog = new CustomDialog();
					errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					errorDialog.setDialogDetails(errorIcon, "Email already exists!");
					errorDialog.setVisible(true);
					return; // Exit the method, do not proceed with signup
				}

				if (password.equals(confirmpass)) {
					// Password and confirm password match
					System.out.println("Username: " + username);
					System.out.println("Password: " + password);
					System.out.println("Email: " + email);
					// Check if selectedUserType is not null before printing it
					if (selectedUserType != null) {
						System.out.println("User Type: " + selectedUserType);
					}

					// Insert data into the database
					try {
						Connection connection = ConnectionManager.getConnection();
						System.out.println("Connection to the database successful!");

						// Check if the Users table exists, create it if not
						String createTableQuery = "CREATE TABLE IF NOT EXISTS Users (username VARCHAR(255), email VARCHAR(255), password VARCHAR(255), userType VARCHAR(255))";
						try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
							createTableStatement.executeUpdate();
							System.out.println("Users table is created if not exists");
						}

						// Insert data into the Users table
						String insertQuery = "INSERT INTO Users (username, email, password, userType) VALUES (?, ?, ?, ?)";
						try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
							insertStatement.setString(1, username);
							insertStatement.setString(2, email);
							insertStatement.setString(3, password);
							insertStatement.setString(4, selectedUserType);

							insertStatement.executeUpdate();
							System.out.println("Data inserted into Users table successfully");
						}

						connection.close(); // Close the connection when done.

						ImageIcon successIcon = new ImageIcon(
								"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-success-64.png");
						CustomDialog successDialog = new CustomDialog();
						successDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						successDialog.setDialogDetails(successIcon, "Signed up successfully!");
						successDialog.setVisible(true);

						username_Field.setText("");
						emailField.setText("");
						passwordField_1.setText("");
						confirmpassField_1.setText("");
					} catch (SQLException ex) {
						System.err.println("Error: " + ex.getMessage());
						ex.printStackTrace();
					}

					// Add your additional signup logic here

				} else {
					// Password and confirm password do not match
					ImageIcon errorIcon = new ImageIcon(
							"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-error-64.png");
					CustomDialog errorDialog = new CustomDialog();
					errorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					errorDialog.setDialogDetails(errorIcon, "Password and Confirm Password do not match!");
					errorDialog.setVisible(true);
					passwordField_1.setText("");
					confirmpassField_1.setText("");
				}
			}
		});

		panel.setLayout(null);
		panel.add(createacc_btn);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		panel_5.setBounds(96, 488, 240, 30);
		panel.add(panel_5);

		JButton signupbutton = new JButton("Login");
		signupbutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Login sg = new Login();
				sg.setVisible(true);
				dispose();
			}
		});
		signupbutton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		signupbutton.setBounds(145, 4, 80, 24);

		// Set link-like appearance
		signupbutton.setForeground(Color.black);
		signupbutton.setBorderPainted(false);
		signupbutton.setContentAreaFilled(false);
		signupbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		signupbutton.setUI(new BasicButtonUI());

		signupbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				signupbutton.setForeground(new Color(0, 102, 255));

			}
		});

		panel_5.setLayout(null);
		panel_5.add(signupbutton);

		JLabel lblNewLabel_1 = new JLabel("Already have an account?");
		lblNewLabel_1.setForeground(Color.GRAY);
		lblNewLabel_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(16, 7, 200, 16);
		panel_5.add(lblNewLabel_1);

		JLabel lblNewLabel_2_1 = new JLabel("Create a new account!");
		lblNewLabel_2_1.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblNewLabel_2_1.setBounds(78, 45, 234, 30);
		panel.add(lblNewLabel_2_1);

	}

	private boolean isEmailExists(String email) {
		try {
			Connection connection = ConnectionManager.getConnection();
			String query = "SELECT COUNT(*) FROM Users WHERE email=?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, email);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						int count = resultSet.getInt(1);
						return count > 0;
					}
				}
			}
		} catch (SQLException ex) {
			System.err.println("Error checking email existence. Error: " + ex.getMessage());
		}
		return false;
	}

	public void setSelectedUserType(String userType) {
		this.selectedUserType = userType;
		System.out.println("Received User Type in SignupPage: " + selectedUserType);
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	static class HiddenCaret extends DefaultCaret {
		@Override
		protected synchronized void damage(Rectangle r) {

		}

		@Override
		public void paint(Graphics g) {

		}
	}

	private void openSignupPage(String userType) {

		SignupPage signupPage = new SignupPage();

		signupPage.setSelectedUserType(userType);

		signupPage.setVisible(true);

		this.dispose();
	}

}