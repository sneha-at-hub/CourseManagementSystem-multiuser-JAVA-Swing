package user;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import gui.RoundButton;
import gui.RoundedPanel;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

public class Select extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String selectedUserType;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Select frame = new Select();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Select() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(50, 144, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(50, 144, 255));
		panel.setBounds(6, 6, 675, 460);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel userTypePanel = new RoundedPanel(100, new Color(255, 255, 255), new Color(255, 255, 255));
		userTypePanel.setBounds(85, 73, 509, 316);
		panel.add(userTypePanel);
		userTypePanel.setLayout(null);

		ImageIcon adminIcon = new ImageIcon(
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-admin-100.png");
		ImageIcon teacherIcon = new ImageIcon(
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-teacher-100.png");
		ImageIcon studentIcon = new ImageIcon(
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-student-100.png");

		JRadioButton adminRadioButton = new JRadioButton(adminIcon);
		adminRadioButton.setBackground(new Color(0, 255, 255));
		adminRadioButton.setBounds(22, 89, 110, 100);
		userTypePanel.add(adminRadioButton);

		JRadioButton teacherRadioButton = new JRadioButton(teacherIcon);
		teacherRadioButton.setBounds(202, 89, 110, 100);
		userTypePanel.add(teacherRadioButton);

		JRadioButton studentRadioButton = new JRadioButton(studentIcon);
		studentRadioButton.setBounds(379, 89, 110, 100);
		userTypePanel.add(studentRadioButton);

		ButtonGroup userTypeGroup = new ButtonGroup();
		userTypeGroup.add(adminRadioButton);
		userTypeGroup.add(teacherRadioButton);
		userTypeGroup.add(studentRadioButton);

		// Continue button
		JButton continueButton = new RoundButton("Continue", Color.white, new Color(50, 144, 255), 17);
		continueButton.setBounds(202, 253, 120, 30);
		userTypePanel.add(continueButton);

		JLabel lblNewLabel = new JLabel("Select User");
		lblNewLabel.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblNewLabel.setBounds(202, 21, 120, 30);
		userTypePanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Admin");
		lblNewLabel_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(59, 195, 61, 16);
		userTypePanel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Teacher");
		lblNewLabel_2.setBounds(230, 195, 61, 16);
		userTypePanel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Student");
		lblNewLabel_3.setBounds(410, 195, 61, 16);
		userTypePanel.add(lblNewLabel_3);

		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Check which user type is selected
				if (adminRadioButton.isSelected()) {
					selectedUserType = "Admin";
				} else if (teacherRadioButton.isSelected()) {
					selectedUserType = "Teacher";
				} else if (studentRadioButton.isSelected()) {
					selectedUserType = "Student";
				}

				System.out.println("Selected User Type: " + selectedUserType);
				openSignupPage(selectedUserType);

			}
		});
	}

	private void openSignupPage(String userType) {
		// Create an instance of the SignupPage class
		SignupPage signupPage = new SignupPage();

		// Set the selected user type using the setter method
		signupPage.setSelectedUserType(userType);

		// Set the visibility of the signup page
		signupPage.setVisible(true);

		// Close the current Select frame (if needed)
		this.dispose();
	}

}
