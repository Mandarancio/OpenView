package gui.components.ovprocedural;

import gui.interfaces.OVContainer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import ovscript.AssignBlock;
import core.support.OrientationEnum;

public class OVPAssign extends OVProceduralNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8942421817509654658L;

	public OVPAssign(OVContainer father) {
		super(father, new AssignBlock());
		setBackground(new Color(0, 50, 0, 180));
	}

	@Override
	protected void paintOVNode(Graphics2D g2d) {
		g2d.setColor(Color.lightGray);
		paintText("=", g2d, new Rectangle(0, 0, getWidth(), getHeight()),
				OrientationEnum.CENTER);
	}
}
