package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class TestFrame extends JPanel {
    private static final long serialVersionUID = 1L;
	private int radius;
    private Color initialBackgroundColor;
    private Color borderColor;
    private Color hoverColor;
    private ImageIcon icon;

    public TestFrame(int radius, Color initialBackgroundColor, Color borderColor) {
        this.radius = radius;
        this.initialBackgroundColor = initialBackgroundColor;
        this.borderColor = borderColor;
        this.hoverColor = Color.WHITE;
        setBackground(initialBackgroundColor);
        

        setOpaque(false);

        // Listen for changes in components
        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                registerMouseListeners(e.getChild());
            }
        });

        // Register initial mouse listeners
        registerMouseListeners(this);
    }

    private void registerMouseListeners(Component component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(initialBackgroundColor);
            }
        });

        if (component instanceof Container) {
            Component[] components = ((Container) component).getComponents();
            for (Component child : components) {
                registerMouseListeners(child);
            }
        }
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        repaint();
    }

    public void setBackgroundColor(Color color) {
        initialBackgroundColor = color;
        setBackground(color);
        repaint();
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

        // Fill the background with the specified color
        g2d.setColor(getBackground());
        g2d.fill(roundedRectangle);

        // Draw the rounded border with the specified color
        g2d.setColor(borderColor);
        g2d.draw(roundedRectangle);

        // Draw the icon if it is set
        if (icon != null) {
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int xPos = (width - iconWidth) / 2;
            int yPos = (height - iconHeight) / 2;
            icon.paintIcon(this, g2d, xPos, yPos);
        }

        g2d.dispose();
    }
}
