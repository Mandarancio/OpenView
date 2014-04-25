package gui.support;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import core.support.OrientationEnum;

public class OVToolTip extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532139951089307810L;

	public OVToolTip(String toolTip, Point p, OrientationEnum o, Font f) {
		setName(toolTip);
		this.setFont(f);
		computeBox(toolTip,p, o);
	}

	private void computeBox(String toolTip,Point p, OrientationEnum o) {
		Rectangle2D r=getFont().getStringBounds(toolTip, new FontRenderContext(new AffineTransform(), true, false));
		if (o == OrientationEnum.RIGHT){
			this.setBounds(p.x+10,(int)( p.y-r.getHeight()/2.0), (int)r.getWidth()+10, (int)r.getHeight()+10);
		}else if (o==OrientationEnum.LEFT){
			this.setBounds(p.x-20-(int)r.getWidth(),(int)( p.y-r.getHeight()/2.0), (int)r.getWidth()+10, (int)r.getHeight()+10);

		}else if (o==OrientationEnum.CENTER){
			this.setBounds(p.x-(int)r.getWidth()/2-5,(int)( p.y-r.getHeight()/2.0),(int)r.getWidth()+10,(int)r.getHeight()+10);
		}
	}

	@Override
	protected void paintComponent(Graphics bg) {
		Graphics2D g = (Graphics2D) bg;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(0,0,0,120));
		g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
		g.setColor(Color.white);
		g.drawRoundRect(0, 0, getWidth()-1,getHeight()-1,10,10);
		g.setFont(getFont());
		Rectangle2D r = g.getFontMetrics().getStringBounds(getName(), g);
		g.setColor(Color.white);
		int x = 2;
		x = (int) Math.round((getWidth() - r.getWidth()) / 2.0);

		g.drawString(getName(), x,(int) Math.round(getHeight() / 2.0 - r.getCenterY()));
	}

}
