package core.maker;

import java.util.ArrayList;

import javax.swing.JMenu;

/***
 * Class to manage dinamically the list of sub menus to show on the editor. In
 * this way during the load of the modules it's possible to add more submenus.
 * 
 * @author martino
 * 
 */
public class OVMenuManager {
	/**
	 * list of gui related sub-menus
	 */
	private static ArrayList<JMenu> guiMenus_ = new ArrayList<>();
	/***
	 * list of node (or logic) related sub-menus
	 */
	private static ArrayList<JMenu> nodeMenus_ = new ArrayList<>();

	/**
	 * retrive the list of gui related sub-menus
	 * 
	 * @return gui related sub-menus
	 */
	static public ArrayList<JMenu> getGuiMenus() {
		return guiMenus_;
	}

	/***
	 * retrive the list of node (logic) related sub-menus
	 * 
	 * @return node related sub-menus
	 */
	static public ArrayList<JMenu> getNodeMenus() {
		return nodeMenus_;
	}

	/***
	 * add a gui-related sub-menu
	 * 
	 * @param m
	 *            sub-menu
	 */
	static public void addGUIMenu(JMenu m) {
		guiMenus_.add(m);
	}

	/***
	 * add a node related sub-menu
	 * 
	 * @param m
	 *            sub-menu
	 */
	static public void addNodeMenu(JMenu m) {
		nodeMenus_.add(m);
	}
}
