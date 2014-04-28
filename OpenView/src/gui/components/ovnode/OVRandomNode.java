package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;
import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.ValueType;

public class OVRandomNode extends OVNodeComponent implements SlotListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6826317169012758281L;
	private static final String Trigger = "Trigger";
	private static final String Value = "Value";
	private static final String Min = "Min";
	private static final String Max = "Max";
	private InNode trigger_;
	private OutNode output_;

	public OVRandomNode(OVContainer father) {
		super(father);
		trigger_ = addInput(Trigger, ValueType.VOID);
		output_ = addOutput(Value, ValueType.DOUBLE);

		trigger_.addListener(this);

		Setting s = new Setting(Min, -1.0,-Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Max, 1.0,-Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
	
		getSetting(ComponentSettings.Name).setValue("Rand");
	}

	@Override
	public void valueRecived(SlotInterface s, core.Value v) {
		if (s.equals(trigger_)) {
			double rv = Math.random();
			double max = 1.0;
			double min = 0.0;

			try {
				max=getSetting(Max).getValue().getDouble();
				min=getSetting(Min).getValue().getDouble();
			} catch (Exception e) {
				e.printStackTrace();
			}

			double range = max - min;
			rv = rv * range + min;

			output_.trigger(new core.Value(rv));
		}
	}

}
