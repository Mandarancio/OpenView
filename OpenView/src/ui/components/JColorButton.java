package ui.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class JColorButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3453999104850171515L;
	private Color color_;
	public Color getColor() {
		return color_;
	}
	public void setColor(Color color_) {
		this.color_ = color_;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color_);
		int w=getHeight()-8;
		
		g.fillRect(4, (getHeight()-w)/2,getWidth()-8,w);
	}
}
