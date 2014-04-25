package gui.interfaces;

import gui.enums.DragAction;

import java.awt.Point;

public interface DragComponent {

	void leftClick(Point point);

	void doubleClick(Point point);

	void rightClick(Point point);

	DragAction drag(Point point);

	void drop(DragAction dragAction_);

	void moveOf(int dx, int dy);
	
	void moveLineTo(int x, int y);

	void resizeTo(int w, int h);
	
	void mouseMoved(Point p);
	
	void mouseExited();

	
}
