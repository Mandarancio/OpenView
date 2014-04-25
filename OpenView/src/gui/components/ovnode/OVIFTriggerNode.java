package gui.components.ovnode;

import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;
import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

public class OVIFTriggerNode extends OVNodeComponent implements SlotListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6116299768813356191L;
	private static final String Input = "Input", Trigger = "Trigger";
	private OutNode elseTrigger_;
	private OutNode ifTrigger_;
	private InNode trigger_;
	private boolean value_ = false;

	public OVIFTriggerNode(OVContainer father) {
		super(father);
		ifTrigger_ = addOutput("If", ValueType.VOID);
		elseTrigger_ = addOutput("Else", ValueType.VOID);
		InNode in = addInput(Input, ValueType.BOOLEAN);
		in.addListener(this);
		Setting s = new Setting(Trigger, new Boolean(false));
		addNodeSetting(ComponentSettings.SpecificCategory, s);
		getSetting(ComponentSettings.Name).setValue("IF");

	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(Input)) {
			try {
				boolean b = v.getBoolean();
				value_ = b;
				if (trigger_ == null) {
					if (b) {
						ifTrigger_.trigger(new Value(Void.TYPE));
					} else {
						elseTrigger_.trigger(new Value(Void.TYPE));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (s.getLabel().equals(Trigger)) {
			if (value_) {
				ifTrigger_.trigger(new Value(Void.TYPE));
			} else {
				elseTrigger_.trigger(new Value(Void.TYPE));
			}
		}
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		if (s.getName().equals(Trigger)) {
			try {
				boolean b = v.getBoolean();
				if (trigger_ == null && b) {
					trigger_ = addInput(Trigger, ValueType.VOID);
					trigger_.addListener(this);
				} else if (trigger_ != null && !b) {
					trigger_.removeListener(this);
					removeInput(trigger_);
					trigger_ = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.valueUpdated(s, v);
	}


}
