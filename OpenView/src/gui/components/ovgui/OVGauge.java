package gui.components.ovgui;

import gui.components.OVComponent;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

import java.awt.BorderLayout;

import org.w3c.dom.Element;

import ui.components.gauge.GaugeMonitor;
import ui.components.listeners.ValueEvent;
import ui.components.listeners.ValueListener;
import core.Setting;
import core.Value;
import core.ValueType;

public class OVGauge extends OVComponent implements ValueListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 245450089021966364L;
	private static final String _Value = "Value", _Min = "Min", _Max = "Max",
			_Reference = "Reference", _ReferenceEnabled = "Visible",
			_Editable = "Editable", _Unit = "Unit", _Limits = "Limits",
			_LWarning = "L warning", _LAlert = "L alert",
			_RWarning = "R warning", _RAlert = "R alert";
	private GaugeMonitor gauge_;

	private OutNode output_;

	public OVGauge(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.SizeW).setValue(180);
		getSetting(ComponentSettings.SizeH).setValue(120);
		gauge_ = new GaugeMonitor(0, -100.0, +100.0);
		gauge_.setRefernce(0.0);
		gauge_.addValueListener(this);
		gauge_.setSimmetricalWarning(0);
		gauge_.setSimmetricalAlert(0);

		this.add(gauge_, BorderLayout.CENTER);

		Setting s = new Setting(_Value, 0.0, -Double.MAX_VALUE,
				Double.MAX_VALUE);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Min, -100.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Max, 100.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Unit, "");
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Reference, 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		s.setOutput(false);
		addSetting(_Reference, s);
		s = new Setting(_ReferenceEnabled, true);
		addSetting(_Reference, s);
		s = new Setting(_Editable, true);
		addSetting(_Reference, s);
		s = new Setting(_LWarning, -100.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addSetting(_Limits, s);
		s = new Setting(_LAlert, -100.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addSetting(_Limits, s);
		s = new Setting(_RWarning, 100.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addSetting(_Limits, s);
		s = new Setting(_RAlert, 100.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addSetting(_Limits, s);

		output_ = addOutput(_Reference, ValueType.DOUBLE);
	}

	public OVGauge(Element e, OVContainer father) {
		super(e, father);
		gauge_ = new GaugeMonitor(0, -100.0, +100.0);
		gauge_.addValueListener(this);
		this.add(gauge_, BorderLayout.CENTER);

		triggerSettings();
		for (OutNode o : outputs_) {
			if (o.getLabel().equals(_Reference)) {
				output_ = o;
			}
		}
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		try {
			if (gauge_ != null) {
				if (s.getName().equals(_Value)) {
					gauge_.setValue(v.getDouble());
				} else if (s.getName().equals(_Min)) {
					gauge_.setMin(v.getDouble());
				} else if (s.getName().equals(_Max)) {
					gauge_.setMax(v.getDouble());
				} else if (s.getName().equals(_Reference)) {
					gauge_.setRefernce(v.getDouble());
				} else if (s.getName().equals(_ReferenceEnabled)) {
					gauge_.setReferenceEnabled(v.getBoolean());
					gauge_.repaint();
				} else if (s.getName().equals(_Editable)) {
					gauge_.setEditable(v.getBoolean());
				} else if (s.getName().equals(_Unit)) {
					gauge_.setUnit(v.getString());
					repaint();
				} else if (s.getName().equals(_LWarning)) {
					gauge_.setMinWarning(v.getDouble());
					gauge_.repaint();
				} else if (s.getName().equals(_LAlert)) {
					gauge_.setMinAlert(v.getDouble());
					gauge_.repaint();
				} else if (s.getName().equals(_RWarning)) {
					gauge_.setMaxWarning(v.getDouble());
					gauge_.repaint();
				} else if (s.getName().equals(_RAlert)) {
					gauge_.setMaxAlert(v.getDouble());
					gauge_.repaint();
				} else {
					super.valueUpdated(s, v);
				}
			} else
				super.valueUpdated(s, v);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void referencePositionChanged(ValueEvent e) {
		try {
			if (e.getReference() != getSetting(_Reference).getValue()
					.getDouble()) {
				getSetting(_Reference).setValue(e.getReference());
				output_.trigger(new Value(e.getReference()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
