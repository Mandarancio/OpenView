/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.components.ovprocedural;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import core.Setting;
import core.Value;
import core.support.OrientationEnum;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;
import proceduralScript.Var;

/**
 * 
 * @author martino
 */
public class OVPVar extends OVProceduralNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8959038724281167452L;
	public static final String VarName="Var name";
	
	public OVPVar(OVContainer father) {
		super(father, new Var("v"));
		getSetting(ComponentSettings.Name).setValue("v");
		Setting s =new Setting(VarName, "v");
		addNodeSetting(ComponentSettings.SpecificCategory,s);
	}
	
	
	@Override
	protected void paintOVNode(Graphics2D g2d) {
		g2d.setColor(Color.lightGray);
		paintText(getNodeSetting(VarName).getValue().getString(), g2d, new Rectangle(0, 0, getWidth(), getHeight()),
				OrientationEnum.CENTER);
	}
	
	@Override
	public void valueUpdated(Setting s, Value v) {
		if (s.getName().equals(VarName)){
			repaint();
		}else
			super.valueUpdated(s, v);
	}
}
