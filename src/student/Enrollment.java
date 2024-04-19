package student;

import javax.swing.*;

import course.Course;

import java.awt.*;

public class Enrollment extends Course {

	private static final long serialVersionUID = 1L;
	private JButton enroll;

	public Enrollment() {
		super();
		removeEditButton();
		removeAddButton();
		removeDeleteButton();
		enroll = new JButton("Enroll Course");
		enroll.setBackground(new Color(30, 144, 255));
		enroll.setFocusPainted(false);
		enroll.setForeground(Color.WHITE);

		enroll.setOpaque(true);
		enroll.setBorderPainted(false);

		getButtonsPanel().add(enroll);
		enroll.addActionListener(e -> enrollCourse());
	}

	protected void enrollCourse() {
		int selectedRow = getCourseTable().getSelectedRow();
		if (selectedRow != -1) {
			String selectedCourse = (String) getCourseTable().getValueAt(selectedRow, 0);
			JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			EnrollCourse enrollCourseForm = new EnrollCourse(parentFrame, this);

			enrollCourseForm.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a course to enroll.");
		}
	}

	public int getSelectedRow() {
		return getCourseTable().getSelectedRow();
	}

	public String getSelectedCourse(int rowIndex) {
		return (String) getCourseTable().getValueAt(rowIndex, 0);
	}

}
