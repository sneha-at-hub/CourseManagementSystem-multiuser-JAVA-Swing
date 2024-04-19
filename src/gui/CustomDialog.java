package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel messageLabel;
	private JLabel imageLabel;

	public CustomDialog() {
		initializeComponents();
	}

	private void initializeComponents() {
		setBounds(100, 100, 279, 326);
		setTitle("Custom Dialog");

		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		imageLabel = new JLabel();
		imageLabel.setBounds(105, 55, 64, 64);
		contentPane.add(imageLabel);

		messageLabel = new JLabel();
		messageLabel.setBackground(Color.DARK_GRAY);
		messageLabel.setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));
		messageLabel.setBounds(62, 131, 159, 100);
		contentPane.add(messageLabel);
		setUndecorated(true);

		JButton okButton = new RoundButton("OK", Color.white, new Color(50, 144, 255), 20);
		okButton.setBounds(91, 224, 90, 35);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(okButton);
	}

	public void setDialogDetails(ImageIcon imageIcon, String message) {
		imageLabel.setIcon(imageIcon);
		messageLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>");
	}
}
