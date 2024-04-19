package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundButton extends JButton {

	private static final long serialVersionUID = 1L;
	private int radius;
	private Color borderColor;
	private Color backgroundColor;
	private boolean clicked;

	public RoundButton(String text, Color borderColor, Color backgroundColor, int radius) {
		super(text);
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.radius = radius;
		this.clicked = false;

		setFocusPainted(false);
		setOpaque(false);
		setForeground(Color.white);
		setFont(new Font(".AppleSystemUIFont", Font.PLAIN, 13));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				clicked = true;
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				clicked = false;
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();

		// Anti-aliasing for smoother edges
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		RoundRectangle2D roundRect = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

		if (clicked) {
			g2d.setColor(backgroundColor.darker()); // Darker color when clicked
		} else {
			g2d.setColor(backgroundColor);
		}

		g2d.fill(roundRect);

		g2d.setColor(borderColor);
		g2d.draw(roundRect);

		// Center the text
		FontMetrics metrics = g.getFontMetrics();
		int x = (getWidth() - metrics.stringWidth(getText())) / 2;
		int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

		g2d.setColor(getForeground());
		g2d.drawString(getText(), x, y);

		g2d.dispose();
	}

	@Override
	public Insets getInsets() {
		return new Insets(radius + 2, radius + 2, radius + 2, radius + 2); // Increased for the shadow
	}
}
