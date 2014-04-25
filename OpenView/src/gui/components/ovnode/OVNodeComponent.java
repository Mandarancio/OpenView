package gui.components.ovnode;

import gui.components.OVComponent;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import java.awt.Color;
import java.awt.Graphics2D;

import core.Setting;

public class OVNodeComponent extends OVComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8013287894980758135L;

	public OVNodeComponent(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.SizeW).setValue(60);
		getSetting(ComponentSettings.SizeH).setValue(60);
		resizable_=false;
		this.setBackground(new Color(0,0,0,180));
		
		
		
		for (Setting s:  getSettings()){
			s.setNodeMode(false);
		}		
	}

	

	@Override
	public void setMode(EditorMode mode_) {
		super.setMode(mode_);
		if (mode_ != EditorMode.NODE && mode_!=EditorMode.DEBUG)
			setVisible(false);
		else
			setVisible(true);
	}

	@Override
	protected void paintOVComponent(Graphics2D g2d) {
		paintOVNode(g2d);
	}
}
