package run.init;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Splash extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5256997382171301742L;

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(245, 245, 255));
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}
