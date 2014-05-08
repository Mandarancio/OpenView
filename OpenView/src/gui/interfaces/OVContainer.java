package gui.interfaces;

import gui.components.OVComponent;
import gui.components.nodes.Line;
import gui.support.OVMaker.OVMakerMode;
import gui.support.OVToolTip;

import java.awt.Dimension;
import java.awt.Point;

import core.support.OrientationEnum;

public interface OVContainer {
	public void addComponent(OVComponent c);
	public boolean compatible(OVComponent c);
	public void removeComponent(OVComponent c);
	public void select(OVComponent c);
	public void deselect(OVComponent c);
	public void deselectAll();
	public Point validate(Point p);
	public Dimension validate(Dimension d);
	public OVToolTip showToolTip(String tooltip, Point p, OrientationEnum orientation);
	public void hideToolTip(OVToolTip tooltip);
	public Point getAbsoluteLocation(OVComponent c,Point location);
	public Line createLine(OVNode n, OVComponent ovComponent);
	public void confirmLine(Line l);
	public void removeLine(Line line);
	public void showMenu(Point point);
	public void showMenu(Point p,OVMakerMode mode);
	public OVContainer parent();
	public OVContainer superParent();
	public boolean contains(OVComponent c);
	public void removeSelected();
	public void clickEvent(Point p,Object source);
	public OVNode getNode(String parent, String uuid);
}
