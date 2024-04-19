package student;

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
import java.sql.SQLException;
import java.util.Vector;

public class ResultView extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable resultTable;
	private DefaultTableModel resultTableModel;
	private String email;
	private JButton viewResultSlipButton;

	public ResultView(String email) {
		this.email = email;
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		createResultTable();
		resultTableModel = new DefaultTableModel();
		initializeResultTableModel();
		resultTable = new JTable(resultTableModel);

		setupTableAppearance();

		JScrollPane scrollPane = new JScrollPane(resultTable);
		add(scrollPane, BorderLayout.CENTER);
		viewResultSlipButton = new JButton("View Result Slip");
		viewResultSlipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showResultSlipPanel();
			}
		});
		add(viewResultSlipButton, BorderLayout.SOUTH);

		fetchResultDataFromDatabase();
	}

	private void initializeResultTableModel() {
		resultTableModel.addColumn("ID");
		resultTableModel.addColumn("Marks");
		resultTableModel.addColumn("Module Name");
		resultTableModel.addColumn("Level");
		resultTableModel.addColumn("Student Name");
		resultTableModel.addColumn("Email");
	}

	private void fetchResultDataFromDatabase() {
		try (Connection connection = ConnectionManager.getConnection()) {
			String query = "SELECT id, marks, module_name, level, student_name, email FROM resultslip WHERE email = ?";
			System.out.println("Executing SQL Query: " + query);

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, email);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						Object[] rowData = { resultSet.getInt("id"), resultSet.getInt("marks"),
								resultSet.getString("module_name"), resultSet.getInt("level"),
								resultSet.getString("student_name"), resultSet.getString("email") };
						resultTableModel.addRow(rowData);
					}
				}
			}
		} catch (SQLException ex) {
			System.err.println("Error executing SQL query: " + ex.getMessage());
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void createResultTable() {

	}

	private void showResultSlipPanel() {
		if (resultTableModel.getRowCount() > 0) {
			// Assuming the student's name, ID, and college name are available in the first
			// row
			Vector<Object> firstRowData = resultTableModel.getDataVector().elementAt(0);

			JPanel resultSlipPanel = new JPanel(new BorderLayout());
			resultSlipPanel.setBackground(Color.WHITE);
			resultSlipPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

			JPanel headerPanel = new JPanel(new GridLayout(4, 1, 0, 5)); // Adjusted for four labels
			headerPanel.setBackground(new Color(50, 205, 50));
			headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

			JLabel collegeLabel = new JLabel("Herald College Kathmandu");
			collegeLabel.setForeground(Color.WHITE);
			collegeLabel.setFont(new Font("Arial", Font.BOLD, 18));
			collegeLabel.setHorizontalAlignment(SwingConstants.CENTER);

			JLabel nameLabel = new JLabel("Student Name: " + firstRowData.elementAt(4).toString());
			nameLabel.setForeground(Color.WHITE);
			nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

			JLabel idLabel = new JLabel("Student ID: " + firstRowData.elementAt(0).toString());
			idLabel.setForeground(Color.WHITE);
			idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
			idLabel.setHorizontalAlignment(SwingConstants.CENTER);

			JLabel levelLabel = new JLabel("Student Level: " + firstRowData.elementAt(3).toString());
			levelLabel.setForeground(Color.WHITE);
			levelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
			levelLabel.setHorizontalAlignment(SwingConstants.CENTER);

			headerPanel.add(collegeLabel);
			headerPanel.add(nameLabel);
			headerPanel.add(idLabel);
			headerPanel.add(levelLabel);

			resultSlipPanel.add(headerPanel, BorderLayout.NORTH);

			DefaultTableModel tableModel = new DefaultTableModel();
			tableModel.addColumn("Module");
			tableModel.addColumn("Marks");

			for (int i = 0; i < resultTableModel.getRowCount(); i++) {
				Vector<Object> rowData = resultTableModel.getDataVector().elementAt(i);
				Vector<Object> simplifiedRowData = new Vector<>();
				simplifiedRowData.add(rowData.elementAt(2)); // Module
				simplifiedRowData.add(rowData.elementAt(1)); // Marks
				tableModel.addRow(simplifiedRowData);
			}

			JTable table = new JTable(tableModel);

			// Set table appearance
			table.setBackground(Color.WHITE);
			table.setGridColor(Color.WHITE); // Set grid color to match the background
			table.setShowGrid(false); // Hide gridlines

			// Customize the font and cell renderer if needed
			table.setFont(new Font("Arial", Font.PLAIN, 14));

			// Add the table to the resultSlipPanel
			resultSlipPanel.add(new JScrollPane(table), BorderLayout.CENTER);

			JDialog dialog = new JDialog();
			dialog.setTitle("Result Slip - All Modules");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.getContentPane().add(resultSlipPanel);
			dialog.setSize(new Dimension(500, 600)); // Adjust size
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "No result data available.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private Vector<Object> getColumnIdentifiers(DefaultTableModel model) {
		Vector<Object> columnIdentifiers = new Vector<>();
		for (int i = 0; i < model.getColumnCount(); i++) {
			columnIdentifiers.add(model.getColumnName(i));
		}
		return columnIdentifiers;
	}

	private void setupTableAppearance() {
		resultTable.getTableHeader().setBackground(Color.BLACK);
		resultTable.getTableHeader().setForeground(Color.WHITE);
		resultTable.setRowHeight(25);
		JTableHeader header = resultTable.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 25));
		header.setFont(new Font("SansSerif", Font.PLAIN, 12));
		header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		resultTable.setGridColor(Color.LIGHT_GRAY);
		resultTable.setBackground(new Color(240, 248, 255));
	}
}
