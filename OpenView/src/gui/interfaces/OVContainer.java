package gui.interfaces;

import gui.components.OVComponent;
import gui.components.nodes.OVLine;
import gui.enums.EditorMode;
import gui.support.OVMaker.OVMakerMode;
import gui.support.OVToolTip;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JMenu;

import run.window.ObjectTree;
import core.support.OrientationEnum;

/***
 * Interface for ovcomponents container
 * 
 * @author martino
 * 
 */
public interface OVContainer {

	/***
	 * Add component
	 * 
	 * @param c
	 *            component to add
	 */
	public void addComponent(OVComponent c);

	/***
	 * Check if the component is compatible
	 * 
	 * @param c
	 *            component to check
	 * @return
	 */
	public boolean compatible(OVComponent c);

	/***
	 * Remove component
	 * 
	 * @param c
	 *            component to remove
	 */
	public void removeComponent(OVComponent c);

	/***
	 * select a component
	 * 
	 * @param c
	 *            component to select
	 */
	public void select(OVComponent c);

	/***
	 * deselect a component
	 * 
	 * @param c
	 *            component to the deselect
	 */
	public void deselect(OVComponent c);

	/***
	 * Deselect all components
	 */
	public void deselectAll();

	/***
	 * Validate a component position
	 * 
	 * @param p
	 *            component position
	 * @return validate position
	 */
	public Point validate(Point p);

	/***
	 * Validate a component dimension
	 * 
	 * @param d
	 *            component dimension
	 * @return validate dimension
	 */
	public Dimension validate(Dimension d);

	/***
	 * Display a tool-tip
	 * 
	 * @param tooltip
	 *            text to show
	 * @param p
	 *            position of the tool-tip
	 * @param orientation
	 *            tool-tip orientation
	 * @return tool-tip object
	 */
	public OVToolTip showToolTip(String tooltip, Point p,
			OrientationEnum orientation);

	/***
	 * Hide (remove) a tool-tip
	 * 
	 * @param tooltip
	 *            tool-tip to remove
	 */
	public void hideToolTip(OVToolTip tooltip);

	/***
	 * Convert a relative component position to absolute (relative to editor
	 * panel)
	 * 
	 * @param c
	 *            component
	 * @param location
	 *            position to convert
	 * @return absolute location
	 */
	public Point getAbsoluteLocation(OVComponent c, Point location);

	/***
	 * Create a new line from a node
	 * 
	 * @param n
	 *            node of origin
	 * @param ovComponent
	 *            parent component
	 * @return Line
	 */
	public OVLine createLine(OVNode n, OVComponent ovComponent);

	/***
	 * Confirm (or not) a line
	 * 
	 * @param l
	 *            line to confirm
	 */
	public void confirmLine(OVLine l);

	/**
	 * Remove a line
	 * 
	 * @param line
	 *            line to remove
	 */
	public void removeLine(OVLine line);

	/***
	 * Display the pop-up menu
	 * 
	 * @param point
	 *            position
	 */
	public void showMenu(Point point);

	/***
	 * Display a mode-dependent pop-up menu
	 * 
	 * @param p
	 *            position
	 * @param mode
	 *            mode
	 */
	public void showMenu(Point p, OVMakerMode mode);

	/***
	 * Display a custom pop-up menu
	 * 
	 * @param p
	 *            position
	 * @param m
	 *            menu
	 */
	public void showCustomMenu(Point p, JMenu m);

	/***
	 * Get the parent container
	 * 
	 * @return parent
	 */
	public OVContainer parent();

	/***
	 * get the super-parent (aka EditorPanel)
	 * 
	 * @return super-parent
	 */
	public OVContainer superParent();

	/***
	 * check if contains a component
	 * 
	 * @param c
	 *            component
	 * @return
	 */
	public boolean contains(OVComponent c);

	/***
	 * remove all selected components
	 */
	public void removeSelected();

	/***
	 * Click event handler
	 * 
	 * @param p
	 *            position
	 * @param source
	 */
	public void clickEvent(Point p, Object source);

	/***
	 * Retrive a node from parent-UUID and node-UUID
	 * 
	 * @param parent
	 *            parent UUID
	 * @param uuid
	 *            node UUID
	 * @return
	 */
	public OVNode getNode(String parent, String uuid);

	/***
	 * Get edit mode
	 * 
	 * @return
	 */
	public EditorMode getMode();

	/***
	 * Get object tree
	 * @return object tree
	 */
	public ObjectTree getObjectTree();
}
