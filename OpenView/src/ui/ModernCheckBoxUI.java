package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicCheckBoxUI;

public class ModernCheckBoxUI extends BasicCheckBoxUI {
	@Override
	public synchronized void paint(Graphics g, JComponent c) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int c_size = 18;
		g2d.setColor(new Color(21, 20, 22));
		int w = c.getWidth(), h = c.getHeight();
		g2d.fillRoundRect(5, h / 2 - c_size / 2, c_size, c_size, 8, 8);
		g2d.setColor(new Color(255, 255, 255, 30));
		g2d.drawRoundRect(5, h / 2 - c_size / 2, c_size - 1, c_size - 1, 8, 8);
		Font f = g2d.getFont();
		String txt = ((JCheckBox) c).getText();

		int ch = (int) f.getStringBounds(txt, g2d.getFontRenderContext())
				.getHeight();
		this.paintText(g2d, c, new Rectangle(15 + c_size, (h - ch) / 2, w - 20
				- c_size, ch), txt);
		if (((JCheckBox) c).isSelected()) {
			g2d.setColor(new Color(87, 238, 255));
			g2d.setStroke(new BasicStroke(2));
			g2d.drawLine(9, h / 2 - 2, 3 + c_size / 2, h / 2 + 2);
			g2d.drawLine(3 + c_size / 2, h / 2 + 2, c_size, h / 2 - c_size / 4);

			float rad = (c_size - 2) / 2;
			float dist[] = { 0.0f, 1.0f };
			Color colors[] = { new Color(87, 238, 255, 70),
					new Color(87, 238, 255, 0) };
			RadialGradientPaint rgp = new RadialGradientPaint(
					new Point2D.Double(5 + c_size / 2, h / 2), rad, dist,
					colors);
			g2d.setPaint(rgp);
			g2d.fillRect(5, h / 2 - c_size / 2, c_size, c_size);
		} else {
			float rad = (c_size - 2) / 2;
			float dist[] = { 0.0f, 1.0f };
			Color colors[] = { new Color(0, 0, 0, 180), new Color(0, 0, 0, 0) };
			RadialGradientPaint rgp = new RadialGradientPaint(
					new Point2D.Double(5 + c_size / 2, h / 2), rad, dist,
					colors);
			g2d.setPaint(rgp);
			g2d.fillRect(5, h / 2 - c_size / 2, c_size, c_size);
		}

	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setForeground(Color.lightGray);
		if (c.getParent() != null)
			c.setBackground(c.getParent().getBackground());
	}

}
