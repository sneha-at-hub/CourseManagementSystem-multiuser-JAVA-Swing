package student;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import course.Course;
import gui.InvisibleBorderButton;
import gui.RoundedPanel;
import user.Login;

public class Students extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Enrollment courseTablePanel;
    private String email;
    private Timer refreshTimer;

    public Students(String email) {
        this.email = email;
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1338, 700);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(6, 18, 1326, 634);
        contentPane.add(panel);
        panel.setLayout(null);

        JPanel panel_1 = new RoundedPanel(70, new Color(0, 102, 255), new Color(0, 102, 255));
        panel_1.setBounds(6, 0, 215, 628);
        panel.add(panel_1);
        panel_1.setBackground(UIManager.getColor("CheckBoxMenuItem.background"));
        panel_1.setLayout(null);

        JPanel panel_4 = new JPanel();
        panel_4.setForeground(Color.BLUE);
        panel_4.setBounds(15, 33, 186, 327);
        panel_1.add(panel_4);
        panel_4.setBackground(new Color(0, 102, 255));
        panel_4.setLayout(null);
        System.out.println("Welcome " + email);

        JButton btnNewButton_1_1 = new InvisibleBorderButton("Courses",
                "/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-books-15.png");

        btnNewButton_1_1.setBackground(new Color(0, 102, 255));
        btnNewButton_1_1.setBounds(10, 44, 160, 35);
        panel_4.add(btnNewButton_1_1);
        btnNewButton_1_1.setForeground(Color.white);
        btnNewButton_1_1.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton btnNewButton_1_2_1 = new InvisibleBorderButton("Teachers",
                "/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-teacher-17.png");
        btnNewButton_1_2_1.setText("My Teachers");

        btnNewButton_1_2_1.setBackground(new Color(0, 102, 255));
        btnNewButton_1_2_1.setBounds(10, 79, 160, 35);
        panel_4.add(btnNewButton_1_2_1);
        btnNewButton_1_2_1.setForeground(Color.white);
        btnNewButton_1_2_1.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton btnNewButton_1_2_2 = new InvisibleBorderButton("Examination",
                "/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-exam-17.png");
        btnNewButton_1_2_2.setText("Enroll");

        btnNewButton_1_2_2.setBounds(10, 116, 160, 35);
        panel_4.add(btnNewButton_1_2_2);
        btnNewButton_1_2_2.setForeground(Color.white);
        btnNewButton_1_2_2.setBackground(new Color(0, 102, 255));
        btnNewButton_1_2_2.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton btnNewButton_1_2_3 = new InvisibleBorderButton("Result",
                "/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-stats-17.png");

        btnNewButton_1_2_3.setBackground(new Color(0, 102, 255));
        btnNewButton_1_2_3.setBounds(10, 157, 160, 35);
        panel_4.add(btnNewButton_1_2_3);
        btnNewButton_1_2_3.setForeground(Color.white);
        btnNewButton_1_2_3.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton btnNewButton_1_2_5 = new InvisibleBorderButton("Activity",
                "/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-history-17.png");

        btnNewButton_1_2_5.setText("My Modules");

        btnNewButton_1_2_5.setBackground(new Color(0, 102, 255));
        btnNewButton_1_2_5.setBounds(10, 193, 160, 35);
        panel_4.add(btnNewButton_1_2_5);
        btnNewButton_1_2_5.setForeground(Color.white);
        btnNewButton_1_2_5.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(0, 102, 255));
        panel_2.setBounds(15, 420, 186, 187);
        panel_1.add(panel_2);
        panel_2.setLayout(null);

        InvisibleBorderButton btnNewButton_1_2_5_1 = new InvisibleBorderButton("Account",
                "/Users/sneha/eclipse-workspace/coursemanagementsystem/src/gui1/icons8-share-17.png");


        btnNewButton_1_2_5_1.setBackground(new Color(0, 102, 255));
        btnNewButton_1_2_5_1.setText("Account");
        btnNewButton_1_2_5_1.setForeground(Color.white);
        btnNewButton_1_2_5_1.setFont(new Font("Arial", Font.PLAIN, 12));
        btnNewButton_1_2_5_1.setBounds(10, 80, 160, 35);
        panel_2.add(btnNewButton_1_2_5_1);
        

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
        tabbedPane.setBounds(233, 0, 1087, 661);
        panel.add(tabbedPane);

        Enrollment studentCourseTablePanel = new Enrollment();
        tabbedPane.addTab("Courses", null, studentCourseTablePanel, null);

        StudentModules modulePanel = new StudentModules(email);
        DefaultTableModel moduleTableModel = modulePanel.getModuleTableModel();
        tabbedPane.addTab("Modules", null, modulePanel, null);

        TeacherView teacherTableViewPanel = new TeacherView(email);
        tabbedPane.addTab("Teachers", null, teacherTableViewPanel, null);

        
        btnNewButton_1_2_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		tabbedPane.setSelectedComponent(teacherTableViewPanel);

        	}
        });
        
        btnNewButton_1_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
           		tabbedPane.setSelectedComponent(studentCourseTablePanel);
        	}
        });
        btnNewButton_1_2_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	tabbedPane.setSelectedComponent(studentCourseTablePanel);
            }
        });
        btnNewButton_1_2_5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
         		tabbedPane.setSelectedComponent(modulePanel);
        	}
        });
        
        ResultView result = new ResultView(email);
        tabbedPane.addTab("New tab", null, result, null);
        
        btnNewButton_1_2_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	tabbedPane.setSelectedComponent(result);
            }
        });
        
       ProfilePanel profile = new ProfilePanel(email);
        tabbedPane.addTab("New tab", null, profile, null);
        
        btnNewButton_1_2_5_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		tabbedPane.setSelectedComponent(profile);
        	}
        });
     
        int refreshInterval =  1000;
        Timer refreshTimer = new Timer(refreshInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel moduleTableModel = modulePanel.getModuleTableModel();
                moduleTableModel.setRowCount(0); 
                modulePanel.fetchModuleDataFromDatabase();
                modulePanel.revalidate();
                modulePanel.repaint();

           
                DefaultTableModel teacherTableModel = teacherTableViewPanel.getTeacherTableModel();
                teacherTableModel.setRowCount(0);
                teacherTableViewPanel.fetchTeacherDataFromDatabase(); 
                teacherTableViewPanel.revalidate();
                teacherTableViewPanel.repaint();
                
                
                
            }
        });
        refreshTimer.start();
    
        
    }
    

    public static void fadeIn(JPanel panel, int duration) {
        // Add fade-in animation logic here
    }
}
