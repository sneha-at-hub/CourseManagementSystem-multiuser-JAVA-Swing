package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Table extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel model;
	private StudentManager studentManager;

	public Table() {
		initialize();
		studentManager = new StudentManager(this);
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Student Data Table");
		setSize(600, 400);

		model = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);

		model.addColumn("Name");
		model.addColumn("Age");
		model.addColumn("Course");

		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		addStudent(new Student("John Doe", 20, "Computer Science"));
		addStudent(new Student("Jane Smith", 22, "Mathematics"));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, centerRenderer);

		JButton addButton = new JButton("Add Student");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentManager.openStudentForm();
			}
		});

		getContentPane().add(addButton, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void addStudent(Student student) {
		model.addRow(new Object[] { student.getName(), student.getAge(), student.getCourse() });
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Table());
	}

}

class Student {
	private String name;
	private int age;
	private String course;

	public Student(String name, int age, String course) {
		this.name = name;
		this.age = age;
		this.course = course;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getCourse() {
		return course;
	}
}

class StudentManager {
	private Table table;

	public StudentManager(Table table) {
		this.table = table;
	}

	public void openStudentForm() {
		new StudentForm(this);
	}

	public void addNewStudent(Student newStudent) {
		table.addStudent(newStudent);
	}
}

class StudentForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField ageField;
	private JTextField courseField;
	private StudentManager studentManager;

	public StudentForm(StudentManager studentManager) {
		this.studentManager = studentManager;
		initialize();
	}

	private void initialize() {
		setTitle("Add Student Form");
		setSize(300, 200);
		setLayout(new GridLayout(4, 2));

		JLabel nameLabel = new JLabel("Name:");
		nameField = new JTextField();

		JLabel ageLabel = new JLabel("Age:");
		ageField = new JTextField();

		JLabel courseLabel = new JLabel("Course:");
		courseField = new JTextField();

		JButton addButton = new JButton("Add Student");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addStudent();
			}
		});
		add(nameLabel);
		add(nameField);
		add(ageLabel);
		add(ageField);
		add(courseLabel);
		add(courseField);
		add(new JLabel());
		add(addButton);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addStudent() {

		try {
			String name = nameField.getText();
			int age = Integer.parseInt(ageField.getText());
			String course = courseField.getText();

			Student newStudent = new Student(name, age, course);
			studentManager.addNewStudent(newStudent);

			// Close the form
			dispose();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid age. Please enter a valid number.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
