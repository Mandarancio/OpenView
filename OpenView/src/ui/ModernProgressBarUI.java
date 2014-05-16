package ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class ModernProgressBarUI extends BasicProgressBarUI {

	@Override
	public void paint(Graphics g, JComponent c) {
		Color bg=c.getParent().getBackground();
		g.setColor(bg);

		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setClip(new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), 8, 8));
		g2d.setColor(bg.darker());
		g2d.fillRoundRect(0, 9, c.getWidth(), c.getHeight() , 10, 10);
		g2d.setColor(new Color(255, 255, 255, 30));
		g2d.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 10, 10);
		g2d.setColor(new Color(255, 255, 255,10));

		g2d.fillRoundRect(4, 4,c.getWidth()-8,c.getHeight()-8,8,8);

		if (progressBar.getValue() > progressBar.getMinimum()) {
			double ratio = (progressBar.getValue() - progressBar.getMinimum())/(double)(progressBar.getMaximum()-progressBar.getMinimum());
			if (ratio>1)
				ratio=1;
			g2d.setColor(new Color(87, 238, 255));
			GradientPaint gradient=new GradientPaint(0f,0f, new Color(255,255,255,130),0.0f,30f,new Color(255,255,255,0));
	
			if (progressBar.getOrientation()==SwingConstants.HORIZONTAL)
			{
				g2d.fillRoundRect(4, 4,(int)Math.round((c.getWidth()-8)*ratio),c.getHeight()-8,8,8);
				g2d.setPaint(gradient);
				
				g2d.fillRoundRect(4, 4,(int)Math.round((c.getWidth()-8)*ratio),c.getHeight()-8,8,8);

			}
			else
			{
				g2d.fillRoundRect(4, 4,c.getWidth()-8,(int)Math.round((c.getHeight()-8)*ratio),8,8);
				g2d.setPaint(gradient);
				g2d.fillRoundRect(4, 4,(int)Math.round((c.getWidth()-8)*ratio),c.getHeight()-8,8,8);

			}
		}
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		if (c.getParent() != null)
			c.setBackground(c.getParent().getBackground());
		c.setBorder(null);
	}
}
