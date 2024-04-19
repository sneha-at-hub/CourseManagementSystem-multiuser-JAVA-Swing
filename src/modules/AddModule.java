package modules;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import connection.ConnectionManager;
import gui.RoundButton;

public class AddModule extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField moduleIdField;
	private JTextField moduleNameField;
	private JTextField semesterField;
	private JTextField creditField;
	private JComboBox<String> levelComboBox;
	private JComboBox<String> courseComboBox;
	private DefaultTableModel moduleTableModel;

	public AddModule(JFrame parentFrame, DefaultTableModel moduleTableModel) {
		super(parentFrame, "Add Module", true);
		getContentPane().setLayout(new BorderLayout());
        setSize(450, 550);
    	setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel formPanel = new JPanel();
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		courseComboBox = new JComboBox<>(getCourseNamesFromDatabase());
		courseComboBox.setBounds(172, 73, 229, 43);
		formPanel.setLayout(null);
		JLabel courseLabel = new JLabel("Course Name:");
		courseLabel.setBounds(57, 72, 115, 43);
		formPanel.add(courseLabel);
		formPanel.add(courseComboBox);

		moduleIdField = new JTextField();
		moduleIdField.setBounds(172, 128, 237, 33);
		JLabel moduleIdLabel = new JLabel("Module ID:");
		moduleIdLabel.setBounds(57, 123, 99, 43);
		formPanel.add(moduleIdLabel);
		formPanel.add(moduleIdField);

		moduleNameField = new JTextField();
		moduleNameField.setBounds(172, 182, 237, 33);
		JLabel moduleNameLabel = new JLabel("Module Name:");
		moduleNameLabel.setBounds(57, 177, 107, 43);
		formPanel.add(moduleNameLabel);
		formPanel.add(moduleNameField);

		semesterField = new JTextField();
		semesterField.setBounds(172, 237, 237, 33);
		JLabel semesterLabel = new JLabel("Semester:");
		semesterLabel.setBounds(57, 232, 84, 43);
		formPanel.add(semesterLabel);
		formPanel.add(semesterField);

		creditField = new JTextField();
		creditField.setBounds(172, 290, 237, 33);
		JLabel creditLabel = new JLabel("Credit:");
		creditLabel.setBounds(57, 285, 72, 43);
		formPanel.add(creditLabel);
		formPanel.add(creditField);

		// Added level combo box
		levelComboBox = new JComboBox<>(new String[] { "4", "5", "6" });
		levelComboBox.setBounds(172, 341, 237, 43);
		JLabel levelLabel = new JLabel("Level:");
		levelLabel.setBounds(57, 340, 72, 43);
		formPanel.add(levelLabel);
		formPanel.add(levelComboBox);

		getContentPane().add(formPanel, BorderLayout.CENTER);

		JButton addModuleButton = new RoundButton("Add", Color.white, new Color(50, 144, 255), 30);
		addModuleButton.setBounds(83, 17, 100, 37);
		addModuleButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		addModuleButton.addActionListener(e -> addModule());

		JButton cancelButton = new RoundButton("Cancel", new Color(176, 196, 222), new Color(176, 196, 222), 30);
		cancelButton.setBounds(270, 17, 100, 37);
		cancelButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		cancelButton.addActionListener(e -> dispose());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setBounds(6, 435, 435, 78);
		formPanel.add(buttonPanel);
		buttonPanel.setLayout(null);
		buttonPanel.add(addModuleButton);
		buttonPanel.add(cancelButton);

		JLabel lblAddModule = new JLabel("Add Module");
		lblAddModule.setForeground(new Color(50, 144, 255));
		lblAddModule.setFont(new Font("Oriya MN", Font.PLAIN, 20));
		lblAddModule.setBounds(57, 27, 200, 37);
		formPanel.add(lblAddModule);

		this.moduleTableModel = moduleTableModel;

		setLocationRelativeTo(parentFrame);
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

	private int parseInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private void addModule() {
	    try {
	        String courseName = Objects.requireNonNull(courseComboBox.getSelectedItem()).toString();
	        int moduleId = parseInteger(moduleIdField.getText());
	        String moduleName = moduleNameField.getText();
	        int semester = parseInteger(semesterField.getText());
	        int credit = parseInteger(creditField.getText());
	        int level = Integer.parseInt(Objects.requireNonNull(levelComboBox.getSelectedItem()).toString());

	        if (moduleId < 0 || semester < 1 || credit < 0) {
	            JOptionPane.showMessageDialog(this,
	                    "Please enter valid numeric values for Module ID, Semester, and Credit.");
	            return;
	        }

	        // Check if the module with the given ID already exists
	        if (moduleExists(moduleId)) {
	            JOptionPane.showMessageDialog(this, "Module with ID " + moduleId + " already exists.");
	            return;
	        }

	        String query = "INSERT INTO modules (module_id, course_name, module_name, semester, credit, level) VALUES (?, ?, ?, ?, ?, ?)";
	        try (Connection connection = ConnectionManager.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setInt(1, moduleId);
	            preparedStatement.setString(2, courseName);
	            preparedStatement.setString(3, moduleName);
	            preparedStatement.setInt(4, semester);
	            preparedStatement.setInt(5, credit);
	            preparedStatement.setInt(6, level);
	            preparedStatement.executeUpdate();
	        }

	        moduleTableModel.addRow(new Object[]{moduleId, courseName, moduleName, semester, credit, level});

	        moduleIdField.requestFocus();

	        dispose();
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this,
	                "Please enter valid numeric values for Module ID, Semester, and Credit.");
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error adding module. Please check the input values.");
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "An unexpected error occurred.");
	    }
	}

	// Helper method to check if a module with the given ID already exists
	private boolean moduleExists(int moduleId) throws SQLException {
	    try (Connection connection = ConnectionManager.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM modules WHERE module_id = ?")) {
	        preparedStatement.setInt(1, moduleId);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            return resultSet.next();
	        }
	    }
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Test");
			DefaultTableModel defaultTableModel = new DefaultTableModel();
			AddModule addModuleForm = new AddModule(frame, defaultTableModel);
			addModuleForm.setVisible(true);
		});
	}
}
