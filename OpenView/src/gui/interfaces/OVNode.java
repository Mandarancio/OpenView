package gui.interfaces;

import gui.components.OVComponent;
import gui.components.nodes.OVLine;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface OVNode {
	public void setLocation(Point p);

	public Point getLocation();

	public boolean contains(Point p);

	public void paint(Graphics2D g);

	public boolean compatible(OVNode a);

	public void addNodeListener(NodeListener l);

	public void removeNodeListener(NodeListener l);

	public void hide();

	public void show();

	public OVComponent getParent();

	public void addLine(OVLine l);

	public OVLine getLine(OVNode n);

	public boolean visible();

	public Element getXML(Document doc);

	public UUID getUUID();
}
