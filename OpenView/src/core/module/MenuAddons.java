package core.module;

import gui.support.OVMaker.OVMakerMode;

import javax.swing.JMenu;

public abstract class MenuAddons extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6319173294101270147L;

	public abstract OVMakerMode getMenuMode();
}