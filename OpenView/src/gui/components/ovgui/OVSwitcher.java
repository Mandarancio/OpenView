package gui.components.ovgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.w3c.dom.Element;

import core.Value;
import core.ValueType;
import ui.components.BooleanSwitch;
import gui.components.OVComponent;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;
import gui.support.Setting;

public class OVSwitcher extends OVComponent implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2820907555078164954L;
	private static final String _Value = "Value";
	private static final String _RefVisibible = "R. Visible";
	private static final String _Reference = "Reference";
	private BooleanSwitch switch_;
	private OutNode output_;

	public OVSwitcher(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.SizeW).setValue(60);
		getSetting(ComponentSettings.SizeH).setValue(45);
		initSwitch();

		Setting s = new Setting(_Value, new Boolean(false), this);
		s.setOutput(false);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_RefVisibible, new Boolean(false), this);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Reference, new Boolean(false), this);
		addSetting(ComponentSettings.SpecificCategory, s);

		output_ = addOutput(_Value, ValueType.BOOLEAN);
	}

	public OVSwitcher(Element e, OVContainer father) {
		super(e, father);
		initSwitch();
		for (OutNode n : outputs_) {
			if (n.getLabel().equals(_Value)) {
				output_ = n;
			}
		}
		triggerSettings();
	}

	private void initSwitch() {
		switch_ = new BooleanSwitch();
		switch_.setSelected(false);
		switch_.setBackground(getBackground());
		switch_.setForeground(getForeground());
		switch_.addActionListener(this);
		this.add(switch_, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			if (getSetting(_Value).getValue().getBoolean() != switch_
					.isSelected()) {
				getSetting(_Value).setValue(new Boolean(switch_.isSelected()));
				if (getMode().isExec()) {
					output_.trigger(new Value(switch_.isSelected()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		try {
			if (s.getName().equals(_RefVisibible)) {
				if (switch_ != null)
					switch_.setReferenceVisible(v.getBoolean());
			} else if (s.getName().equals(_Reference)) {
				if (switch_ != null)
					switch_.setReference(v.getBoolean());
			} else if (s.getName().equals(_Value)) {
				if (switch_ != null) {
					if (switch_.isSelected() != v.getBoolean()) {
						switch_.setSelected(v.getBoolean());
						if (getMode().isExec()) {
							output_.trigger(v);
						}
					}
				}
			}
			super.valueUpdated(s, v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
