package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connection.ConnectionManager;
import course.CourseDeleteObserver;
import panel.TeacherTableObserver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TeachersView extends JPanel implements CourseDeleteObserver {

	private static final long serialVersionUID = 1L;
	private JTable teacherTable;
	private JButton viewDetailsButton;
	private JButton editTeacherButton;
	private JButton addTeacherButton;
	private JButton deleteTeacherButton;
	private final List<TeacherTableObserver> observers = new ArrayList<>();

	public TeachersView() {
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		Object[][] data = fetchTeacherDataFromDatabase();

		String[] columnNames = { "ID", "Name", "Module", "Course", "Email", "Phone" };

		DefaultTableModel model = new DefaultTableModel(data, columnNames);

		teacherTable = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(teacherTable);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setBackground(new Color(240, 248, 255));

		viewDetailsButton = new JButton("View Details");
		viewDetailsButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		viewDetailsButton.setBackground(new Color(30, 144, 255));
		viewDetailsButton.setFocusPainted(false);
		viewDetailsButton.setForeground(Color.WHITE);

		viewDetailsButton.setOpaque(true);
		viewDetailsButton.setBorderPainted(false);
		buttonsPanel.add(viewDetailsButton);

		editTeacherButton = new JButton("Edit Teacher");

		editTeacherButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		editTeacherButton.setBackground(new Color(30, 144, 255));
		editTeacherButton.setOpaque(true);
		editTeacherButton.setFocusPainted(false);
		editTeacherButton.setBorderPainted(false);
		editTeacherButton.setForeground(Color.WHITE);

		buttonsPanel.add(editTeacherButton);

		addTeacherButton = new JButton("Add Teacher");
		addTeacherButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		addTeacherButton.setBackground(new Color(30, 144, 255));
		addTeacherButton.setOpaque(true);
		addTeacherButton.setFocusPainted(false);
		addTeacherButton.setBorderPainted(false);
		addTeacherButton.setForeground(Color.WHITE);
		buttonsPanel.add(addTeacherButton);

		deleteTeacherButton = new JButton("Delete Teacher");
		deleteTeacherButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		deleteTeacherButton.setBackground(new Color(176, 196, 222));
		deleteTeacherButton.setOpaque(true);
		deleteTeacherButton.setFocusPainted(false);
		deleteTeacherButton.setBorderPainted(false);
		buttonsPanel.add(deleteTeacherButton);

		teacherTable.getTableHeader().setBackground(Color.BLACK);
		teacherTable.getTableHeader().setForeground(Color.WHITE);

		teacherTable.setRowHeight(25); // You can adjust the value based on your preference
		JTableHeader header = teacherTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25)); // Adjust the height as needed
		header.setFont(new Font("SansSerif", Font.PLAIN, 12)); // You can customize the font if needed
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border for better visibility

		teacherTable.setGridColor(Color.LIGHT_GRAY);
		teacherTable.setBackground(new Color(240, 248, 255));

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

		add(contentPanel, BorderLayout.CENTER);

		viewDetailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewDetails();
			}
		});

		editTeacherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editTeacher();
			}
		});

		addTeacherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addTeacher();
			}
		});

		deleteTeacherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteTeacher();
			}
		});

		setOpaque(true);
		setBackground(new Color(255, 255, 255));
		fetchDataAndDisplay();

	}

	private void editTeacher() {
		int selectedRow = teacherTable.getSelectedRow();

		if (selectedRow != -1) {
			int teacherId = (int) teacherTable.getValueAt(selectedRow, 0);
			String currentName = (String) teacherTable.getValueAt(selectedRow, 1);
			String currentSubject = (String) teacherTable.getValueAt(selectedRow, 2);
			String currentCourse = (String) teacherTable.getValueAt(selectedRow, 3);
			String currentEmail = (String) teacherTable.getValueAt(selectedRow, 4);
			String currentPhone = (String) teacherTable.getValueAt(selectedRow, 5);

			EditTeacher editForm = new EditTeacher((JFrame) SwingUtilities.getWindowAncestor(this), teacherId,
					currentName, currentSubject, currentCourse, currentEmail, currentPhone);

			editForm.setVisible(true);

			if (editForm.isUpdateSuccessful()) {

				DefaultTableModel model = (DefaultTableModel) teacherTable.getModel();
				model.setValueAt(editForm.getUpdatedName(), selectedRow, 1);
				model.setValueAt(editForm.getUpdatedSubject(), selectedRow, 2);
				model.setValueAt(editForm.getUpdatedCourse(), selectedRow, 3);
				model.setValueAt(editForm.getUpdatedEmail(), selectedRow, 4);
				model.setValueAt(editForm.getUpdatedPhone(), selectedRow, 5);

				observers.forEach(observer -> observer.onTeacherEdited(editForm.getUpdatedName()));
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a teacher.");
		}
	}

	public JTable getTeacherTable() {
		return teacherTable;
	}

	public JButton getViewDetailsButton() {
		return viewDetailsButton;
	}

	public JButton getEditTeacherButton() {
		return editTeacherButton;
	}

	public JButton getAddTeacherButton() {
		return addTeacherButton;
	}

	public JButton getDeleteTeacherButton() {
		return deleteTeacherButton;
	}

	private void viewDetails() {
		int selectedRow = teacherTable.getSelectedRow();
		if (selectedRow != -1) {
			String selectedName = (String) teacherTable.getValueAt(selectedRow, 0);
			String selectedSubject = (String) teacherTable.getValueAt(selectedRow, 1);
			String selectedCourse = (String) teacherTable.getValueAt(selectedRow, 2);
			String selectedEmail = (String) teacherTable.getValueAt(selectedRow, 3);
			String selectedPhone = (String) teacherTable.getValueAt(selectedRow, 4);

			// Display teacher details
			String details = "Name: " + selectedName + "\nSubject: " + selectedSubject + "\nCourse: " + selectedCourse
					+ "\nEmail: " + selectedEmail + "\nPhone: " + selectedPhone;
			JOptionPane.showMessageDialog(this, details, "Teacher Details", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a teacher.");
		}
	}

	private DefaultTableModel fetchDataFromDatabase() {
		String[] columnNames = { "ID", "Name", "Module", "Course", "Email", "Phone" };

		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet resultSet = statement
						.executeQuery("SELECT id, teacher_name, subject, course, email, phone FROM teacher")) {

			resultSet.last();
			int rowCount = resultSet.getRow();
			resultSet.beforeFirst();

			Object[][] data = new Object[rowCount][6];

			int row = 0;
			while (resultSet.next()) {
				data[row][0] = resultSet.getInt("id");
				data[row][1] = resultSet.getString("teacher_name");
				data[row][2] = resultSet.getString("subject");
				data[row][3] = resultSet.getString("course");
				data[row][4] = resultSet.getString("email");
				data[row][5] = resultSet.getString("phone");
				row++;
			}

			return new DefaultTableModel(data, columnNames);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching teacher data from the database: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return new DefaultTableModel();
		}
	}

	private Object[][] fetchTeacherDataFromDatabase() {
		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet resultSet = statement
						.executeQuery("SELECT id, teacher_name, subject, course, email, phone FROM teacher")) {

			resultSet.last();
			int rowCount = resultSet.getRow();
			resultSet.beforeFirst();
			Object[][] data = new Object[rowCount][6];

			int row = 0;
			while (resultSet.next()) {
				data[row][0] = resultSet.getInt("id");
				data[row][1] = resultSet.getString("teacher_name");
				data[row][2] = resultSet.getString("subject");
				data[row][3] = resultSet.getString("course");
				data[row][4] = resultSet.getString("email");
				data[row][5] = resultSet.getString("phone");
				row++;
			}

			return data;

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching teacher data from the database: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return new Object[0][0];
		}
	}

	private void addTeacher() {
		// Create and show the add form
		AddTeacher addForm = new AddTeacher((JFrame) SwingUtilities.getWindowAncestor(this),
				(DefaultTableModel) teacherTable.getModel());
		addForm.setVisible(true);

		observers.forEach(observer -> observer.onTeacherAdded(addForm.getNewName()));
	}

	private void deleteTeacher() {
		int selectedRow = teacherTable.getSelectedRow();

		if (selectedRow != -1) {
			int teacherId = (int) teacherTable.getValueAt(selectedRow, 0);

			int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this teacher?",
					"Confirm Deletion", JOptionPane.YES_NO_OPTION);

			if (confirmResult == JOptionPane.YES_OPTION) {

				if (deleteTeacherFromDatabase(teacherId)) {
					DefaultTableModel model = (DefaultTableModel) teacherTable.getModel();
					model.removeRow(selectedRow);

					String teacherIdString = String.valueOf(teacherId);
					observers.forEach(observer -> observer.onTeacherDeleted(teacherIdString));
				}
			}

		} else {
			JOptionPane.showMessageDialog(this, "Please select a teacher.");
		}
	}

	private boolean deleteTeacherFromDatabase(int teacherId) {
		try (Connection connection = ConnectionManager.getConnection();
				Statement statement = connection.createStatement()) {

			String deleteQuery = "DELETE FROM teacher WHERE id = " + teacherId;
			int rowsDeleted = statement.executeUpdate(deleteQuery);

			return rowsDeleted > 0;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error deleting teacher from the database: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	@Override

	public void onCourseDeleted(String courseName) {
		if (deleteTeacherRecords(courseName)) {
			fetchDataAndDisplay();
		} else {
			JOptionPane.showMessageDialog(this, "No Teacher data is present for this course.");
		}
	}

	private boolean deleteTeacherRecords(String courseName) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String deleteTeacherQuery = "DELETE FROM teacher WHERE course = ?";
			try (PreparedStatement deleteTeacherStatement = connection.prepareStatement(deleteTeacherQuery)) {
				deleteTeacherStatement.setString(1, courseName);
				int rowsAffected = deleteTeacherStatement.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private void fetchDataAndDisplay() {
		DefaultTableModel model = fetchDataFromDatabase();
		teacherTable.setModel(model);
	}

	public void addObserver(TeacherTableObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(TeacherTableObserver observer) {
		observers.remove(observer);
	}

	public void loadUserData(int userID) {

	}

}
