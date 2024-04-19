package teacher;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gui.InvisibleBorderButton;
import gui.RoundedPanel;
import panel.StudentMarks;
import user.Login;

public class Teacher extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String email;
	private String course;

	public Teacher(String email, String course) {
		this.email = email;
		this.course = course;
	}

	public Teacher(String email) {
		this.email = email;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1271, 700);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		 setResizable(false);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(6, 18, 1259, 648);
		panel.setBackground(Color.WHITE);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new RoundedPanel(70, new Color(0, 102, 255), new Color(0, 102, 255));
		panel_1.setBounds(6, 0, 215, 628);
		panel.add(panel_1);
		panel_1.setBackground(UIManager.getColor("CheckBoxMenuItem.background"));
		panel_1.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setForeground(Color.BLUE);
		panel_4.setBounds(15, 91, 186, 327);
		panel_1.add(panel_4);
		panel_4.setBackground(new Color(0, 102, 255));
		panel_4.setLayout(null);
		System.out.println("Welcome " + email);

		JButton btnNewButton_1_2_1 = new InvisibleBorderButton("Teachers",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-teacher-17.png");
		btnNewButton_1_2_1.setText("My Students");

		btnNewButton_1_2_1.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_1.setBounds(10, 53, 160, 35);
		panel_4.add(btnNewButton_1_2_1);
		btnNewButton_1_2_1.setForeground(Color.white);
		btnNewButton_1_2_1.setFont(new Font("Arial", Font.PLAIN, 12));

		JButton btnNewButton_1_2_3 = new InvisibleBorderButton("Result",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-stats-17.png");
		btnNewButton_1_2_3.setText("Add Result");

		btnNewButton_1_2_3.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_3.setBounds(10, 100, 160, 35);
		panel_4.add(btnNewButton_1_2_3);
		btnNewButton_1_2_3.setForeground(Color.white);
		btnNewButton_1_2_3.setFont(new Font("Arial", Font.PLAIN, 12));

		JButton btnNewButton_1_2_5 = new InvisibleBorderButton("Activity",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-history-17.png");
		btnNewButton_1_2_5.setText("My Modules");

		btnNewButton_1_2_5.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_5.setBounds(10, 6, 160, 35);
		panel_4.add(btnNewButton_1_2_5);
		btnNewButton_1_2_5.setForeground(Color.white);
		btnNewButton_1_2_5.setFont(new Font("Arial", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 102, 255));
		panel_2.setBounds(15, 420, 186, 187);
		panel_1.add(panel_2);
		panel_2.setLayout(null);

		InvisibleBorderButton btnNewButton_1_2_5_2 = new InvisibleBorderButton("Logout",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-logout-17.png");

		btnNewButton_1_2_5_2.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_5_2.setText("Logout");
		btnNewButton_1_2_5_2.setForeground(Color.white);
		btnNewButton_1_2_5_2.setFont(new Font("Arial", Font.PLAIN, 12));
		btnNewButton_1_2_5_2.setBounds(10, 119, 160, 35);
		panel_2.add(btnNewButton_1_2_5_2);
		
		
		btnNewButton_1_2_5_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login sg = new Login();
				sg.setVisible(true);
				dispose();
			
				
			}
		});

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setBounds(233, 0, 1020, 671);

		panel.add(tabbedPane);

		JPanel panel_3 = new JPanel();
		StudentViewModule teacherTablePanel = new StudentViewModule(email);

		panel_3.setLayout(new BorderLayout());

		panel_3.add(teacherTablePanel, BorderLayout.CENTER);

		tabbedPane.addTab("Student Table", null, panel_3, null);

		TeacherDetail subjectsPanel = new TeacherDetail(email);

	
		tabbedPane.addTab("Details", null, subjectsPanel, null);

		StudentMarks teacherPanel = new StudentMarks(email);
		teacherPanel.setBackground(Color.WHITE);

		tabbedPane.addTab("Result", null, teacherPanel, null);
		
		
		btnNewButton_1_2_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(subjectsPanel);
				
			}
		});
		
		btnNewButton_1_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(panel_3);
			}
		});
		
		btnNewButton_1_2_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(teacherPanel);
			}
		});
	}
}