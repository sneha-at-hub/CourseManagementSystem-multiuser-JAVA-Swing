package student;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Vector;

public class ResultSlipPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ResultSlipPanel(Vector<Object> rowData) {
		setLayout(new GridLayout(5, 2));

		add(new JLabel("ID:"));
		add(new JLabel(rowData.elementAt(0).toString()));

		add(new JLabel("Marks:"));
		add(new JLabel(rowData.elementAt(1).toString()));

		add(new JLabel("Module Name:"));
		add(new JLabel(rowData.elementAt(2).toString()));

		add(new JLabel("Level:"));
		add(new JLabel(rowData.elementAt(3).toString()));

		add(new JLabel("Student Name:"));
		add(new JLabel(rowData.elementAt(4).toString()));
	}

}