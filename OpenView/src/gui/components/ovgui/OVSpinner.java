package gui.components.ovgui;

import java.awt.BorderLayout;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.w3c.dom.Element;

import core.Setting;
import core.Value;
import core.ValueType;
import gui.components.OVComponent;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

public class OVSpinner extends OVComponent implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4969953694409383806L;
	private static final String _Min = "Min";
	private static final String _Max = "Max";
	private static final String _Value = "Value";
	private static final String _Step = "Step";
	private SpinnerNumberModel model_;
	private JSpinner spinner_;
	private OutNode output_;

	public OVSpinner(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.SizeW).setValue(120);
		getSetting(ComponentSettings.SizeH).setValue(45);

		model_ = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.01);
		spinner_ = new JSpinner(model_);
		model_.addChangeListener(this);

		this.add(spinner_, BorderLayout.CENTER);
		Setting s = new Setting(_Min, 0.0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, this);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Value, 0.0, Integer.MIN_VALUE, Integer.MAX_VALUE, this);
		addSetting(ComponentSettings.SpecificCategory, s);
		s.setOutput(false);
		s = new Setting(_Max, 100.0, Integer.MIN_VALUE, Integer.MAX_VALUE, this);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Step, 0.01, Integer.MIN_VALUE, Integer.MAX_VALUE, this);
		addSetting(ComponentSettings.SpecificCategory, s);

		output_ = addOutput(_Value, ValueType.DOUBLE);

	}

	public OVSpinner(Element e, OVContainer father) {
		super(e, father);
		double v = 0, m = 0, M = 100, s = 0.01;
		try {
			v = getSetting(_Value).getValue().getDouble();
			m = getSetting(_Min).getValue().getDouble();
			M = getSetting(_Max).getValue().getDouble();
			s = getSetting(_Step).getValue().getDouble();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		model_ = new SpinnerNumberModel(v, m, M, s);
		spinner_ = new JSpinner(model_);
		this.add(spinner_, BorderLayout.CENTER);

		for (OutNode n : outputs_) {
			if (n.getLabel().equals(_Value)) {
				output_ = n;
				return;
			}
		}
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		try {
			if (s.getName().equals(_Min)) {
				if (model_ != null)
					model_.setMinimum(new Double(v.getDouble()));
			} else if (s.getName().equals(_Max)) {
				if (model_ != null)
					model_.setMaximum(new Double(v.getDouble()));
			} else if (s.getName().equals(_Value)) {
				if (model_ != null) {
					if (((Double) model_.getValue()).doubleValue() != v
							.getDouble()) {
						model_.setValue(new Double(v.getDouble()));
						if (getMode().isExec()) {
							output_.trigger(v);
						}
					}
				}
			} else if (s.getName().equals(_Step)) {
				if (model_ != null)
					model_.setStepSize(new Double(v.getDouble()));
			} else
				super.valueUpdated(s, v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (!getSetting(_Value).getValue().getData().equals(model_.getValue())) {
			getSetting(_Value).setValue(model_.getValue());
			if (getMode().isExec()) {
				output_.trigger(new Value(model_.getValue()));
			}
		}
	}

}
