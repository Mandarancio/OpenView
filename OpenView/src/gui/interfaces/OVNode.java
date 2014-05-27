package gui.interfaces;

import gui.components.OVComponent;
import gui.components.nodes.OVLine;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/***
 * basic graphic node interface
 * 
 * @author martino
 * 
 */
public interface OVNode {
	/***
	 * set node location
	 * 
	 * @param p
	 *            location
	 */
	public void setLocation(Point p);

	/***
	 * get node location
	 * 
	 * @return location
	 */
	public Point getLocation();

	/***
	 * check if a point is on the node
	 * 
	 * @param p
	 *            point
	 * @return
	 */
	public boolean contains(Point p);

	/***
	 * Paint the node
	 * 
	 * @param g
	 */
	public void paint(Graphics2D g);

	/***
	 * Check if is compatible with another node
	 * 
	 * @param a
	 *            other node
	 * @return
	 */
	public boolean compatible(OVNode a);

	/***
	 * Add a node listener
	 * 
	 * @param l
	 *            listener
	 */
	public void addNodeListener(NodeListener l);

	/***
	 * remove a node listener
	 * 
	 * @param l
	 *            listener
	 */
	public void removeNodeListener(NodeListener l);

	/***
	 * Hide node
	 */
	public void hide();

	/***
	 * Show node
	 */
	public void show();

	/***
	 * Get node parent
	 * 
	 * @return parent
	 */
	public OVComponent getParent();

	/***
	 * Add a connecting line to the node
	 * 
	 * @param l
	 *            line
	 */
	public void addLine(OVLine l);

	/***
	 * Get connection line between this and other node
	 * 
	 * @param n
	 *            other node
	 * @return connection line (if any)
	 */
	public OVLine getLine(OVNode n);

	/**
	 * check if is visible
	 * 
	 * @return
	 */
	public boolean visible();

	/***
	 * get XML element representing the node
	 * 
	 * @param doc
	 * @return
	 */
	public Element getXML(Document doc);

	/***
	 * get UUID of the node
	 * 
	 * @return
	 */
	public UUID getUUID();
}
