package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int radius;
	private Color backgroundColor;
	private Color borderColor;

	public RoundedPanel(int radius, Color backgroundColor, Color borderColor) {
		this.radius = radius;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int x = 0;
		int y = 0;
		int width = getWidth() - 1;
		int height = getHeight() - 1;
		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(x, y, width, height, radius, radius);

		g2d.setColor(backgroundColor);
		g2d.fill(roundedRectangle);

		g2d.setColor(borderColor);
		g2d.draw(roundedRectangle);

		g2d.dispose();
	}
}
