
package course;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connection.ConnectionManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Course extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable courseTable;
	private JButton editCourseButton;
	private JPanel buttonsPanel;
	private JButton addCourseButton;
	private List<CourseDeleteObserver> observers = new ArrayList<>();

	private JButton deleteCourseButton;

	public Course() {
		setBackground(new Color(240, 255, 255));
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		DefaultTableModel model = fetchDataFromDatabase();

		courseTable = new JTable(model);
		courseTable.getTableHeader().setBackground(Color.BLACK);
		courseTable.getTableHeader().setForeground(Color.WHITE);

		courseTable.setRowHeight(30);
		JTableHeader header = courseTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		courseTable.setGridColor(Color.LIGHT_GRAY);
		courseTable.setBackground(new Color(240, 248, 255));

		JScrollPane scrollPane = new JScrollPane(courseTable);
		add(scrollPane, BorderLayout.CENTER);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(new Color(240, 248, 255));
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		editCourseButton = new JButton("Edit Course");

		editCourseButton.setBackground(new Color(30, 144, 255));
		editCourseButton.setFocusPainted(false);
		editCourseButton.setForeground(Color.WHITE);

		editCourseButton.setOpaque(true);
		editCourseButton.setBorderPainted(false);

		buttonsPanel.add(editCourseButton);

		addCourseButton = new JButton("Add Course");

		addCourseButton.setBackground(new Color(30, 144, 255));
		addCourseButton.setFocusPainted(false);
		addCourseButton.setForeground(Color.WHITE);

		addCourseButton.setOpaque(true);
		addCourseButton.setBorderPainted(false);

		buttonsPanel.add(addCourseButton);

		deleteCourseButton = new JButton("Delete Course");

		deleteCourseButton.setBackground(new Color(30, 144, 255));
		deleteCourseButton.setFocusPainted(false);
		deleteCourseButton.setForeground(Color.WHITE);

		deleteCourseButton.setOpaque(true);
		deleteCourseButton.setBorderPainted(false);

		buttonsPanel.add(deleteCourseButton);

		add(buttonsPanel, BorderLayout.SOUTH);

		editCourseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editCourse();
			}
		});

		addCourseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCourse();
			}
		});

		deleteCourseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteCourse();
			}
		});
	}

	private DefaultTableModel fetchDataFromDatabase() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Course");
		model.addColumn("Total Year");

		try (Connection connection = ConnectionManager.getConnection()) {
			String query = "SELECT * FROM course";
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(query)) {

				while (resultSet.next()) {
					String courseName = resultSet.getString("course_name");
					int totalYear = resultSet.getInt("total_year");
					model.addRow(new Object[] { courseName, totalYear });
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return model;
	}

	private void editCourse() {
		int selectedRow = courseTable.getSelectedRow();
		if (selectedRow != -1) {
			String currentCourse = (String) courseTable.getValueAt(selectedRow, 0);
			int currentYear = (int) courseTable.getValueAt(selectedRow, 1);

			EditCourse editForm = new EditCourse((JFrame) SwingUtilities.getWindowAncestor(this),
					currentCourse, currentYear, (DefaultTableModel) courseTable.getModel(), selectedRow);

			editForm.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a course.");
		}
	}

	private void addCourse() {
		// Create and show the add form
		AddCourse addForm = new AddCourse((JFrame) SwingUtilities.getWindowAncestor(this),
				(DefaultTableModel) courseTable.getModel());
		addForm.setVisible(true);
	}

	private void deleteCourse() {
		int selectedRow = courseTable.getSelectedRow();
		if (selectedRow != -1) {
			DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
			String courseNameToDelete = (String) model.getValueAt(selectedRow, 0);

			if (deleteCourseFromDatabase(courseNameToDelete)) {
				notifyObservers(courseNameToDelete);

				model.removeRow(selectedRow);
			} else {
				JOptionPane.showMessageDialog(this, "Failed to delete the course from the database.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a course.");
		}
	}

	private boolean deleteCourseFromDatabase(String courseName) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String deleteCourseQuery = "DELETE FROM course WHERE course_name = ?";
			try (PreparedStatement deleteCourseStatement = connection.prepareStatement(deleteCourseQuery)) {
				deleteCourseStatement.setString(1, courseName);
				deleteCourseStatement.executeUpdate();
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void addObserver(CourseDeleteObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers(String courseName) {
		for (CourseDeleteObserver observer : observers) {
			observer.onCourseDeleted(courseName);
		}
	}

	public DefaultTableModel getTableDataModel() {
		return (DefaultTableModel) courseTable.getModel();

	}

	public void removeEditButton() {
		editCourseButton.setVisible(false);
	}

	public void removeAddButton() {
		addCourseButton.setVisible(false);
	}

	public void removeDeleteButton() {
		deleteCourseButton.setVisible(false);
	}

	public void addEnrollButton() {
		JButton enrollCourseButton = new JButton("Enroll Course");
		enrollCourseButton.addActionListener(e -> enrollCourse());
		buttonsPanel.add(enrollCourseButton);
	}

	public JTable getCourseTable() {
		return courseTable;
	}

	protected JPanel getButtonsPanel() {
		return buttonsPanel;
	}

	protected void enrollCourse() {
		int selectedRow = getCourseTable().getSelectedRow();
		if (selectedRow != -1) {
			String selectedCourse = (String) getCourseTable().getValueAt(selectedRow, 0);

			JOptionPane.showMessageDialog(this, "Enrolling in " + selectedCourse);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a course to enroll.");
		}
	}

}