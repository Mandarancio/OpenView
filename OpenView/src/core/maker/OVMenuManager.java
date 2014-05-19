package core.maker;

import java.util.ArrayList;

import javax.swing.JMenu;

public class OVMenuManager {
	private static ArrayList<JMenu> guiMenus_=new ArrayList<>();
	private static ArrayList<JMenu> nodeMenus_=new ArrayList<>();
	
	static public ArrayList<JMenu> getGuiMenus(){
		return guiMenus_;
	}
	
	static public ArrayList<JMenu> getNodeMenus(){
		return nodeMenus_;
	}
	
	static public void addGUIMenu(JMenu m){
		guiMenus_.add(m);
	}
	
	static public void addNodeMenu(JMenu m){
		nodeMenus_.add(m);
	}
}
