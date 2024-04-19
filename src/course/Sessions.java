package course;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
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

public class Sessions extends JPanel implements CourseDeleteObserver {

	private static final long serialVersionUID = 1L;
	private JTable sessionTable;

	private JButton editSessionButton;
	private JButton addSessionButton;

	public Sessions() {
		setBackground(new Color(240, 248, 255));
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		DefaultTableModel model = fetchDataFromDatabase();

		sessionTable = new JTable(model);
		sessionTable.getTableHeader().setBackground(Color.BLACK);
		sessionTable.getTableHeader().setForeground(Color.WHITE);
		sessionTable.setRowHeight(30);
		JTableHeader header = sessionTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		sessionTable.setGridColor(Color.LIGHT_GRAY);
		sessionTable.setBackground(new Color(240, 248, 255));

		JScrollPane scrollPane = new JScrollPane(sessionTable);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(new Color(240, 248, 255));
		FlowLayout fl_buttonsPanel = new FlowLayout();
		fl_buttonsPanel.setAlignment(FlowLayout.RIGHT);
		buttonsPanel.setLayout(fl_buttonsPanel);

		editSessionButton = new JButton("Edit Session");
		editSessionButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		editSessionButton.setBackground(new Color(30, 144, 255));
		editSessionButton.setFocusPainted(false);
		editSessionButton.setForeground(Color.WHITE);

		editSessionButton.setOpaque(true);
		editSessionButton.setBorderPainted(false);
		buttonsPanel.add(editSessionButton);

		addSessionButton = new JButton("Add Session");
		addSessionButton.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		addSessionButton.setBackground(new Color(30, 144, 255));
		addSessionButton.setFocusPainted(false);
		addSessionButton.setForeground(Color.WHITE);

		addSessionButton.setOpaque(true);
		addSessionButton.setBorderPainted(false);
		buttonsPanel.add(addSessionButton);

		add(buttonsPanel, BorderLayout.SOUTH);
		scheduleRemoveExpiredSessions();

		editSessionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSession();
			}
		});

		addSessionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSession();
			}
		});
	}

	private class TimerTaskRemoveExpiredSessions extends TimerTask {
		@Override
		public void run() {
			removeExpiredSessions();
		}
	}

	private void removeExpiredSessions() {
		DefaultTableModel model = (DefaultTableModel) sessionTable.getModel();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			String sessionEnd = (String) model.getValueAt(i, 3);
			String sessionCourseName = (String) model.getValueAt(i, 0);
			String sessionCourseLevel = (String) model.getValueAt(i, 1);
			if (isSessionExpired(sessionEnd)) {
				model.removeRow(i);

				deleteSessionFromDatabase(sessionCourseName, sessionCourseLevel);
			}
		}
	}

	private void deleteSessionFromDatabase(String courseName, String courseLevel) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String deleteSessionQuery = "DELETE FROM session WHERE session_name = ? AND session_level = ? AND session_end < NOW()";
			try (PreparedStatement deleteSessionStatement = connection.prepareStatement(deleteSessionQuery)) {
				deleteSessionStatement.setString(1, courseName);
				deleteSessionStatement.setString(2, courseLevel);

				int rowsAffected = deleteSessionStatement.executeUpdate();

				if (rowsAffected > 0) {
					System.out.println("Deleted from the database: " + courseName + " - " + courseLevel);
				} else {
					System.out.println("No rows deleted from the database.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// Handle the exception or log it
			JOptionPane.showMessageDialog(this, "Failed to delete the session from the database.");
		}
	}

	private boolean isSessionExpired(String sessionEnd) {
		if (sessionEnd == null || sessionEnd.isEmpty()) {
			return false;
		}

		try {
			Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(sessionEnd);

			Date currentDate = new Date();

			return endDate != null && endDate.before(currentDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void scheduleRemoveExpiredSessions() {
		java.util.Timer timer = new java.util.Timer();

		long interval = 1000;
		timer.scheduleAtFixedRate(new TimerTaskRemoveExpiredSessions(), 0, interval);
	}

	private DefaultTableModel fetchDataFromDatabase() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Course Name");
		model.addColumn("Course Level");
		model.addColumn("Session Start");
		model.addColumn("Session End");

		try (Connection connection = ConnectionManager.getConnection()) {
			String query = "SELECT * FROM session";
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(query)) {

				while (resultSet.next()) {
					String courseName = resultSet.getString("session_name");
					String courseLevel = resultSet.getString("session_level");
					String sessionStart = resultSet.getString("session_start");
					String sessionEnd = resultSet.getString("session_end");

					model.addRow(new Object[] { courseName, courseLevel, sessionStart, sessionEnd });
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return model;
	}

	private void editSession() {
		int selectedRow = sessionTable.getSelectedRow();
		if (selectedRow != -1) {
			String currentCourseName = (String) sessionTable.getValueAt(selectedRow, 0);
			String currentCourseLevel = (String) sessionTable.getValueAt(selectedRow, 1);
			String currentSessionStart = (String) sessionTable.getValueAt(selectedRow, 2);
			String currentSessionEnd = (String) sessionTable.getValueAt(selectedRow, 3);

			EditSession editForm = new EditSession((JFrame) SwingUtilities.getWindowAncestor(this), currentCourseName,
					currentCourseLevel, currentSessionStart, currentSessionEnd,
					(DefaultTableModel) sessionTable.getModel(), selectedRow);

			editForm.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a session.");
		}
	}

	private void addSession() {
		AddSession addForm = new AddSession((JFrame) SwingUtilities.getWindowAncestor(this),
				(DefaultTableModel) sessionTable.getModel());
		addForm.setVisible(true);
	}

	public DefaultTableModel getTableDataModel() {
		return (DefaultTableModel) sessionTable.getModel();
	}

	public void removeEditButton() {
		editSessionButton.setVisible(false);
	}

	public void removeAddButton() {
		addSessionButton.setVisible(false);
	}

	@Override
	public void onCourseDeleted(String courseName) {
		updateSessionTableOnCourseDeleted(courseName);
		deleteSessionFromDatabase(courseName);
	}

	private void updateSessionTableOnCourseDeleted(String courseName) {
		DefaultTableModel model = (DefaultTableModel) sessionTable.getModel();

		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			String sessionCourseName = (String) model.getValueAt(i, 0);

			if (sessionCourseName.equals(courseName)) {
				model.removeRow(i);

				deleteSessionFromDatabase(sessionCourseName);
			}
		}
	}

	private void deleteSessionFromDatabase(String courseName) {
		try (Connection connection = ConnectionManager.getConnection()) {
			String deleteSessionQuery = "DELETE FROM session WHERE session_name = ?";
			try (PreparedStatement deleteSessionStatement = connection.prepareStatement(deleteSessionQuery)) {
				deleteSessionStatement.setString(1, courseName);
				deleteSessionStatement.executeUpdate();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// Handle the exception or log it
			JOptionPane.showMessageDialog(this, "Failed to delete associated sessions from the database.");
		}
	}
}
