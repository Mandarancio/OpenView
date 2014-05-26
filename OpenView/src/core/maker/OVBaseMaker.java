/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.maker;

import gui.components.OVComponent;
import gui.interfaces.OVContainer;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Base class of the editor popup menu used to add components to the project.
 * 
 * @author martino
 */
public class OVBaseMaker extends JPopupMenu implements ActionListener {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -799968719414943845L;
	/**
	 * The container that called the menu. Used to create and add the component
	 */
	private OVContainer father_;
	/**
	 * The click point, for place the new component in the right place.
	 */
	private final Point point_;

	/***
	 * Create a popup menu in the point p, with a list of sub-menus.
	 * 
	 * @param p
	 *            point to show the menu and to place the component.
	 * @param father
	 *            The container that called the menu, where the components will
	 *            be added.
	 * @param menus
	 *            list of sub-menus to show.
	 */
	public OVBaseMaker(Point p, OVContainer father, JMenu... menus) {
		father_ = father;
		point_ = p;
		for (JMenu m : menus) {
			initListeners(m);
		}
		initMenu(menus);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String key = ae.getActionCommand();
		if (OVClassFactory.hasClass(key)) {
			create(OVClassFactory.getInstance(key, father_));
		}
	}

	/***
	 * Short way to show the popup in the right position
	 */
	protected void showPopup() {
		this.show((JComponent) father_, point_.x, point_.y);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	/***
	 * Add all the sub-menus to the main popup
	 * 
	 * @param menus
	 *            list of sub-menus
	 */
	protected void initMenu(JMenu... menus) {
		for (JMenu m : menus) {
			this.add(m);
		}
	}

	/***
	 * Move the component to the right position and add it to the father.
	 * Finally hide the menu.
	 * 
	 * @param c
	 *            component to add
	 */
	protected void create(OVComponent c) {
		if (c != null && father_ != null) {
			Point p = father_.validate(point_);
			c.moveTo(p.x, p.y);
			father_.addComponent(c);

			this.setVisible(false);
			father_ = null;
		}
	}

	/**
	 * Add to each menu items the action listener. It is recorsive.
	 * 
	 * @param menu
	 */
	private void initListeners(JMenu menu) {
		Component[] cmps = menu.getMenuComponents();
		for (Component c : cmps) {
			if (c instanceof JMenu) {
				initListeners((JMenu) c);
			} else if (c instanceof JMenuItem) {
				JMenuItem i = (JMenuItem) c;
				for (ActionListener l : i.getActionListeners()) {
					i.removeActionListener(l);
				}
				i.addActionListener(this);
			}
		}
	}

}
