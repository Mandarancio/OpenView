package gui.components.ovprocedural;

import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import ovscript.Const;
import core.Setting;
import core.Value;
import core.support.OrientationEnum;

public class OVPConst extends OVProceduralNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6064245230079383665L;
	private static String Value = "Value";
	private Value value_;
	private Const const_;
	private boolean __lock = false;

	public OVPConst(OVContainer father) {
		super(father, new Const(0));
		getSetting(ComponentSettings.Name).setValue("K");
		setBackground(new Color(0, 0, 50, 180));
		const_ = (Const) getBlock();
		Setting s = new Setting(Value, "0");
		value_=new Value(0);
		addNodeSetting(ComponentSettings.SpecificCategory, s);
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		if (s.getName().equals(Value)) {
			if (!__lock) {
				setValue(v.getString());
			} else
				__lock = false;
		} else
			super.valueUpdated(s, v);
	}

	private void setValue(String s) {
		if (s.length() == 0 || s.equals("void")) {
			value_ = new Value(Void.TYPE);
		} else if (s.equals("true")) {
			value_ = new Value(true);
		} else if (s.equals("false")) {
			value_ = new Value(false);

		} else if (s.contains(".")) {
			try {
				value_ = new Value(Double.valueOf(s));
			} catch (NumberFormatException e) {
				value_ = new Value(s);
			}
		} else {
			try {
				value_ = new Value(Integer.valueOf(s));
			} catch (NumberFormatException e) {
				value_ = new Value(s);
			}
		}
		const_.setValue(value_);
		repaint();
	}
	
	@Override
	protected void paintOVNode(Graphics2D g2d) {
		g2d.setColor(Color.lightGray);
		paintText(value_.getString(), g2d, new Rectangle(0, 0, getWidth(), getHeight()),
				OrientationEnum.CENTER);
	}
}
