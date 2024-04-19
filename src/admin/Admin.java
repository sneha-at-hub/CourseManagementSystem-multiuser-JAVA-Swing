package admin;

import course.Course;
import course.Sessions;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import course.CourseCount;
import gui.InvisibleBorderButton;
import gui.RoundedPanel;
import modules.Module;
import result.ResultSlip;
import teacher.TeacherCount;
import user.Login;

import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;

public class Admin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Admin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1305, 700);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(6, 0, 1278, 666);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new RoundedPanel(70, new Color(0, 102, 255), new Color(0, 102, 255));
		panel_1.setBounds(7, 17, 215, 628);
		panel.add(panel_1);
		panel_1.setBackground(new Color(65, 105, 225));

		panel_1.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(15, 91, 186, 327);
		panel_1.add(panel_4);
		panel_4.setBackground(new Color(0, 102, 255));

		panel_4.setLayout(null);

		JButton btnNewButton_1_1 = new InvisibleBorderButton("Courses",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-books-16.png");

		btnNewButton_1_1.setBackground(new Color(0, 102, 255));
		btnNewButton_1_1.setBounds(10, 44, 160, 35);
		panel_4.add(btnNewButton_1_1);
		btnNewButton_1_1.setForeground(Color.white);
		btnNewButton_1_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		JButton btnNewButton_1_2 = new InvisibleBorderButton("Students",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-student-17.png");

		btnNewButton_1_2.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2.setBounds(10, 82, 160, 35);
		panel_4.add(btnNewButton_1_2);
		btnNewButton_1_2.setForeground(Color.white);
		btnNewButton_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		JButton btnNewButton_1_2_1 = new InvisibleBorderButton("Teachers",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-teacher-17.png");

		btnNewButton_1_2_1.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_1.setBounds(10, 120, 160, 35);
		panel_4.add(btnNewButton_1_2_1);
		btnNewButton_1_2_1.setForeground(Color.white);
		btnNewButton_1_2_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		JButton btnNewButton_1_2_2 = new InvisibleBorderButton("Modules",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-exam-17.png");

		btnNewButton_1_2_2.setBounds(10, 158, 160, 35);
		panel_4.add(btnNewButton_1_2_2);
		btnNewButton_1_2_2.setForeground(Color.white);
		btnNewButton_1_2_2.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1_2_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		JButton btnNewButton_1_2_3 = new InvisibleBorderButton("Result",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-stats-17.png");

		btnNewButton_1_2_3.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1_2_3.setBounds(10, 196, 160, 35);
		panel_4.add(btnNewButton_1_2_3);
		btnNewButton_1_2_3.setForeground(Color.white);
		btnNewButton_1_2_3.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		JButton btnNewButton_1_2_5 = new InvisibleBorderButton("Activity",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-history-17.png");

		btnNewButton_1_2_5.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_5.setBounds(10, 232, 160, 35);
		panel_4.add(btnNewButton_1_2_5);
		btnNewButton_1_2_5.setForeground(Color.white);
		btnNewButton_1_2_5.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		JButton btnNewButton_1 = new InvisibleBorderButton("Dashboard",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-dashboards-15.png");

		btnNewButton_1.setBounds(10, 6, 160, 35);
		panel_4.add(btnNewButton_1);
		btnNewButton_1.setBackground(new Color(0, 102, 255));
		btnNewButton_1.setForeground(new Color(255, 255, 255));

		btnNewButton_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		InvisibleBorderButton btnNewButton_1_2_5_3 = new InvisibleBorderButton("Enrollment",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-notice-17.png");
		btnNewButton_1_2_5_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		btnNewButton_1_2_5_3.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_5_3.setBounds(10, 270, 160, 35);
		panel_4.add(btnNewButton_1_2_5_3);
		btnNewButton_1_2_5_3.setText("Enrollement");
		btnNewButton_1_2_5_3.setForeground(Color.white);
		btnNewButton_1_2_5_3.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 102, 255));
		panel_2.setBounds(15, 420, 186, 187);
		panel_1.add(panel_2);
		panel_2.setLayout(null);

		InvisibleBorderButton btnNewButton_1_2_5_1 = new InvisibleBorderButton("Sessions",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-share-17.png");

		btnNewButton_1_2_5_1.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_5_1.setText("Sessions");
		btnNewButton_1_2_5_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1_2_5_1.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		btnNewButton_1_2_5_1.setBounds(10, 80, 160, 35);
		panel_2.add(btnNewButton_1_2_5_1);

		InvisibleBorderButton btnNewButton_1_2_5_2 = new InvisibleBorderButton("Logout",
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-logout-17.png");

		btnNewButton_1_2_5_2.setBackground(new Color(0, 102, 255));
		btnNewButton_1_2_5_2.setText("Logout");
		btnNewButton_1_2_5_2.setForeground(new Color(255, 255, 255));
		btnNewButton_1_2_5_2.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 12));
		btnNewButton_1_2_5_2.setBounds(10, 119, 160, 35);
		
		btnNewButton_1_2_5_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login sg = new Login();
				sg.setVisible(true);
				dispose();
			
				
			}
		});

		panel_2.add(btnNewButton_1_2_5_2);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(255, 255, 255));
		panel_3.setBounds(234, 0, 1100, 743);
		panel.add(panel_3);
		panel_3.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setBounds(0, 0, 1050, 700);
		tabbedPane.setBackground(new Color(255, 255, 255));
		panel_3.add(tabbedPane);

		JPanel dashboardpanel = new JPanel();
		dashboardpanel.setBackground(new Color(255, 255, 255));
		tabbedPane.addTab("Dashboard", null, dashboardpanel, null);

		JPanel coursesPanel = new JPanel();
		tabbedPane.addTab("Courses", null, coursesPanel, null);
		coursesPanel.setLayout(new BorderLayout());

		Course courseTablePanel = new Course();
		coursesPanel.add(courseTablePanel, BorderLayout.CENTER);

		btnNewButton_1_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(coursesPanel);

			}

		});
		tabbedPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		// StudentTable
		JPanel studentPanel = new JPanel(new BorderLayout());
		StudentsTable studentTable = new StudentsTable();
		studentPanel.add(studentTable, BorderLayout.CENTER);
		courseTablePanel.addObserver(studentTable);
		tabbedPane.addTab("Students", null, studentPanel, null);

		studentPanel.setVisible(true);
		btnNewButton_1_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(studentPanel);

			}
		});

		// TeacherTable
		JPanel teacherPanel = new JPanel(new BorderLayout());
		TeachersView teacherTable = new TeachersView();
		teacherPanel.add(teacherTable, BorderLayout.CENTER);
		courseTablePanel.addObserver(teacherTable);
		tabbedPane.addTab("Teachers", null, teacherPanel, null);
		btnNewButton_1_2_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(teacherPanel);
			}
		});

		// Resultslip
		JPanel resultpanel = new JPanel(new BorderLayout());
		ResultSlip resultSlipPanel = new ResultSlip();
		resultpanel.add(resultSlipPanel, BorderLayout.CENTER);
		tabbedPane.addTab("Result Slip", null, resultpanel, null);

		DefaultTableModel courseTableModel = courseTablePanel.getTableDataModel();

		// Modules
		JPanel modulesPanel = new JPanel(new BorderLayout());
		Module modulePanel = new Module(courseTableModel);
		modulesPanel.add(modulePanel, BorderLayout.CENTER);
		tabbedPane.addTab("Modules", null, modulesPanel, null);
		btnNewButton_1_2_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(modulesPanel);
			}
		});
		courseTablePanel.addObserver(modulePanel);
		JPanel activitypanel = new JPanel();
		tabbedPane.addTab("Activity Log", null, activitypanel, null);

		ActivityPanel activityPanel = new ActivityPanel();
		activitypanel.setLayout(new BorderLayout());

		studentTable.addObserver(activityPanel);
		teacherTable.addObserver(activityPanel);

		btnNewButton_1_2_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(activitypanel);
			}
		});

		activitypanel.add(activityPanel, BorderLayout.CENTER);

		Sessions sessionTablePanel = new Sessions();
		JPanel sessionTabPanel = new JPanel(new BorderLayout());
		sessionTabPanel.add(sessionTablePanel, BorderLayout.CENTER);
		tabbedPane.addTab("Session Tab", null, sessionTabPanel, null);
		Enrollment enroll = new Enrollment();

		tabbedPane.addTab("enrolled students", null, enroll, null);

		courseTablePanel.addObserver(sessionTablePanel);

		courseTablePanel.addObserver(enroll);

		btnNewButton_1_2_5_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(sessionTabPanel);

			}
		});

		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedComponent(dashboardpanel);
			}
		});

		int totalStudentCount = StudentCount.getTotalStudentCount();

		// Create an ImageIcon and set it as the button's icon
		ImageIcon icon = new ImageIcon(
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-student-100.png");

		int totalCourseCount = CourseCount.getTotalCourseCount();

		// Create an ImageIcon and set it as the button's icon
		ImageIcon courseicon = new ImageIcon(
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-books-100.png");

		// Assuming getTotalTeacherCount is a static method in the TeacherCount class
		int totalTeacherCount = TeacherCount.getTotalTeacherCount();

		// Create an ImageIcon and set it as the button's icon
		ImageIcon teachericon = new ImageIcon(
				"/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-teacher-100.png");
		dashboardpanel.setLayout(null);

		JPanel panel_5 = new RoundedPanel(80, new Color(255, 255, 255), new Color(255, 255, 255));
		panel_5.setBounds(16, 30, 1007, 228);
		panel_5.setBackground(new Color(255, 255, 255));
		dashboardpanel.add(panel_5);
		panel_5.setLayout(null);

		JPanel totalstudentpan = new RoundedPanel(20, new Color(0, 0, 0), new Color(0, 0, 0));
		totalstudentpan.setBounds(19, 25, 280, 176);
		panel_5.add(totalstudentpan);
		totalstudentpan.setBackground(new Color(128, 128, 128));
		totalstudentpan.setLayout(null);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(dashboardpanel);

			}
		});

		// Create the JLabel
		JLabel studentcount = new JLabel("New label");
		studentcount.setForeground(new Color(255, 255, 255));
		studentcount.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 20));
		studentcount.setBounds(224, 135, 34, 35);
		totalstudentpan.add(studentcount);
		studentcount.setText(Integer.toString(totalStudentCount));

		JLabel totalstudent = new JLabel("Total Students");
		totalstudent.setForeground(new Color(255, 255, 255));
		totalstudent.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		totalstudent.setBounds(16, 17, 113, 16);
		totalstudentpan.add(totalstudent);

		JButton btnNewButton_2_1 = new JButton("");
		btnNewButton_2_1.setBounds(52, 34, 171, 112);
		totalstudentpan.add(btnNewButton_2_1);
		btnNewButton_2_1.setIcon(icon);
		btnNewButton_2_1.setFocusPainted(false);

		totalstudentpan.add(btnNewButton_2_1);

		JPanel totalcoursepnl = new RoundedPanel(20, new Color(0, 0, 0), new Color(0, 0, 0));
		totalcoursepnl.setBounds(367, 25, 280, 176);
		panel_5.add(totalcoursepnl);
		totalcoursepnl.setBackground(new Color(255, 255, 255));
		totalcoursepnl.setLayout(null);

		JLabel coursecount = new JLabel("New label");
		coursecount.setForeground(new Color(255, 255, 255));
		coursecount.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 20));
		coursecount.setBounds(225, 140, 37, 16);
		totalcoursepnl.add(coursecount);

		JLabel totalcourses = new JLabel("Total Courses");
		totalcourses.setForeground(new Color(255, 255, 255));
		totalcourses.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		totalcourses.setBounds(16, 17, 113, 16);
		totalcoursepnl.add(totalcourses);
		coursecount.setText(Integer.toString(totalCourseCount));

		JButton btnNewButton_2_1_1 = new JButton();
		btnNewButton_2_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_2_1_1.setBounds(47, 34, 171, 112);
		btnNewButton_2_1_1.setIcon(courseicon);
		btnNewButton_2_1_1.setFocusPainted(false);

		totalcoursepnl.add(btnNewButton_2_1_1);

		JPanel totalteacherpnl = new RoundedPanel(20, new Color(0, 0, 0), new Color(0, 0, 0));
		totalteacherpnl.setBounds(708, 25, 280, 176);
		panel_5.add(totalteacherpnl);
		totalteacherpnl.setBackground(new Color(255, 255, 255));
		totalteacherpnl.setLayout(null);

		JLabel teachercount = new JLabel("New label");
		teachercount.setForeground(new Color(255, 255, 255));
		teachercount.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 20));
		teachercount.setBounds(237, 133, 37, 37);
		totalteacherpnl.add(teachercount);

		JLabel totalteachers = new JLabel("Total Teachers");
		totalteachers.setForeground(new Color(255, 255, 255));
		totalteachers.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		totalteachers.setBounds(16, 17, 113, 16);
		totalteacherpnl.add(totalteachers);
		teachercount.setText(Integer.toString(totalTeacherCount));

		JButton btnNewButton_2_1_2 = new JButton();
		btnNewButton_2_1_2.setBounds(47, 34, 171, 112);
		btnNewButton_2_1_2.setIcon(teachericon);
		btnNewButton_2_1_2.setFocusPainted(false);

		totalteacherpnl.add(btnNewButton_2_1_2);

		// Create an instance of ActivityPanel
		ActivityPanel activityPanel1 = new ActivityPanel();
		activityPanel1.setBounds(6, 276, 1017, 372);
		activityPanel1.setBackground(Color.WHITE);
		dashboardpanel.add(activityPanel1);
		
		

		btnNewButton_1_2_5_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(enroll);
			}
		});

		btnNewButton_1_2_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(resultpanel);

			}
		});

		btnNewButton_1_2_5_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedComponent(sessionTabPanel);

			}
		});
		
		

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}

	public void onCourseEditedFromForm(String editedCourse, int editedYear) {
		System.out.println("Course edited in Admin: " + editedCourse + ", Year: " + editedYear);

	}

	public void loadUserData(String userEmail) {
		// TODO Auto-generated method stub

	}
}