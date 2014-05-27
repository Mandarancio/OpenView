package gui.interfaces;

import gui.enums.DragAction;

import java.awt.Point;

/***
 * Generic interface for a drag and drop component
 * 
 * @author martino
 * 
 */
public interface DragComponent {

	/**
	 * left click action
	 * 
	 * @param point
	 */
	void leftClick(Point point);

	/**
	 * double click action
	 * 
	 * @param point
	 */
	void doubleClick(Point point);

	/***
	 * right click action
	 * 
	 * @param point
	 */
	void rightClick(Point point);

	/***
	 * compute the drag action
	 * 
	 * @param point
	 * @return computed drag action
	 */
	DragAction drag(Point point);

	/***
	 * drop action
	 * 
	 * @param dragAction
	 *            previous drag action
	 */
	void drop(DragAction dragAction);

	/***
	 * Move component of a delta-x,delta-y
	 * 
	 * @param dx
	 *            delta-x
	 * @param dy
	 *            delta-y
	 */
	void moveOf(int dx, int dy);

	/***
	 * Move line to position
	 * 
	 * @param x
	 * @param y
	 */
	void moveLineTo(int x, int y);

	/***
	 * resize component
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	void resizeTo(int w, int h);

	/***
	 * mouse moved (on the component) action
	 * 
	 * @param p
	 *            position
	 */
	void mouseMoved(Point p);

	/***
	 * mouse exited from the component action
	 * 
	 */
	void mouseExited();

}
