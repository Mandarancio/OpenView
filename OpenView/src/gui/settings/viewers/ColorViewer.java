package gui.settings.viewers;

import gui.support.Setting;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

import ui.components.JColorButton;
import core.Value;
import core.ValueType;
import core.support.Rule;

public class ColorViewer extends Viewer implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7504835395857455255L;

	private JColorButton button_;

	public ColorViewer(Setting s) throws Exception {
		super(s);
		button_ = new JColorButton();
		button_.setColor(s.getValue().getColor());
		button_.addActionListener(this);
		this.addMainComponent(button_);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Color newColor = JColorChooser.showDialog(this, "Choose Color",
				button_.getColor());
		if (newColor != null && !newColor.equals(button_.getColor())) {
			button_.setColor(newColor);
			setting_.setValue(newColor);
		}
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		try {
			if (!v.getColor().equals(button_.getColor())) {
				button_.setColor(v.getColor());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String name() {
		return "ColorViewer";
	}

	@Override
	public Viewer copy(Setting s) throws Exception {
		return new ColorViewer(s);
	}

	public static Rule rule() {
		return new Rule() {

			@Override
			public boolean check(Object... args) {
				if (args.length == 1 && args[0] instanceof Setting) {
					Setting s = (Setting) args[0];
					return s.getType() == ValueType.COLOR;
				}
				return false;
			}
			
			@Override
			public int orderValue() {
				return 3;
			}
		};
	}
	
}
