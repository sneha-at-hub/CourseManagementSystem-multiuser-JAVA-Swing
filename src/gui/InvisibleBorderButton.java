package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class InvisibleBorderButton extends JButton implements ActionListener {
	private static final long serialVersionUID = 1L;
	private boolean selected = false;

	private final int fixedIconX = 8;
	private final int fixedIconY = 8;
	private final int fixedTextX = 40;
	private final int fixedTextY = 22;

	public InvisibleBorderButton(String text, String imagePath) {
		super(text);
		setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(18, 18, Image.SCALE_DEFAULT)));
		initialize();
	}

	private void initialize() {
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);

		addActionListener(this);

	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(getBackground());
		}

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int diameter = 2 * 12;
		int x = 0;
		int y = 0;
		int width = getWidth();
		int height = getHeight();

		g2d.fill(new RoundRectangle2D.Double(x, y, width, height, diameter, diameter));
		Icon icon = getIcon();
		if (icon != null && isSpecificIcon(icon)) {
			icon.paintIcon(this, g, fixedIconX, fixedIconY);
		}

		// Draw the text at fixed coordinates
		g2d.setColor(getForeground());
		g2d.drawString(getText(), fixedTextX, fixedTextY);

		g2d.dispose();
	}

	private boolean isSpecificIcon(Icon icon) {
		return icon.equals(getIcon());
	}

	public void changeImage(String imagePath) {
		setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(18, 18, Image.SCALE_DEFAULT)));
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		selected = !selected;
		repaint();
	}
}
