package gui.support;

import gui.interfaces.OVContainer;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JMenu;

import core.maker.OVBaseMaker;
import core.maker.OVMenuManager;

public class OVMaker extends OVBaseMaker {

    public enum OVMakerMode {

        GUI, NODE, NODEONLY
    }

    /**
     *
     */
    private static final long serialVersionUID = -6272300889162031511L;

    public OVMaker(Point p, OVMakerMode mode, OVContainer father) {
        super(p, father,initMenu(mode));
        showPopup();
    }

    public OVMaker(Point p, JMenu m, OVContainer father) {
        super(p, father, m);
        showPopup();
    }

    private static JMenu[] initMenu(OVMakerMode mode) {
    	
    	ArrayList<JMenu> menus=new ArrayList<>();
        if (mode == OVMakerMode.GUI) {
        	menus.addAll(OVMenuManager.getGuiMenus());
        } else if (mode == OVMakerMode.NODE) {
        	menus.addAll(OVMenuManager.getGuiMenus());
        	menus.addAll(OVMenuManager.getNodeMenus());
        } else if (mode == OVMakerMode.NODEONLY) {
        	menus.addAll(OVMenuManager.getNodeMenus());
        }
        return menus.toArray(new JMenu[menus.size()]);
    }

}
