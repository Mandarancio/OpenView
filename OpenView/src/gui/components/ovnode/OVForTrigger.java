package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;
import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

public class OVForTrigger extends OVNodeComponent implements SlotListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -436450683846380258L;
	private static final String Start = "Start", Stop = "Stop", Step = "Step";
	private static final String Delay = "Delay";
	private static final String EndTrigger = "End trigger";
	private OutNode index_;
	private OutNode trigger_;
	private OutNode endTrigger_ = null;
	private boolean status_ = false;

	public OVForTrigger(OVContainer father) {
		super(father);

		Setting s = new Setting(Start, 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Stop, 10.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Step, 1.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Delay, 0, 0, 10000);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(EndTrigger, false);
		addNodeSetting(ComponentSettings.SpecificCategory, s);

		trigger_ = addOutput("Trigger", ValueType.VOID);
		index_ = addOutput("Index", ValueType.DOUBLE);

		InNode start = addInput(Start, ValueType.VOID);
		start.addListener(this);

		getSetting(ComponentSettings.Name).setValue("For");
	}

	private void run() {
		(new Thread() {
			@Override
			public void run() {
				try {
					double i = getSetting(Start).getValue().getDouble();
					double stop = getSetting(Stop).getValue().getDouble();
					double step = getSetting(Step).getValue().getDouble();
					int delay = getSetting(Delay).getValue().getInt();
					System.out.println("delay : "+delay);
					if (step < 0) {
						if (i < stop) {
							double tmp = i;
							i = stop;
							stop = tmp;
						}
						for (; i > stop; i += step) {
							if (status_ == false)
								break;
							trigger_.trigger(new Value());
							index_.trigger(new Value(i));
							if (delay > 0) {
								Thread.sleep(delay);
							}
						}
						status_ = false;
					} else if (step > 0) {
						if (i > stop) {
							double tmp = i;
							i = stop;
							stop = tmp;
						}
						for (; i < stop; i += step) {
							if (status_ == false)
								break;
							trigger_.trigger(new Value());
							index_.trigger(new Value(i));
							if (delay > 0) {
								Thread.sleep(delay);
							}
						}
						status_ = false;
					}
					if (endTrigger_ != null)
						endTrigger_.trigger(new Value());
				} catch (Exception e) {
					e.printStackTrace();
					status_ = false;
				}
				super.run();
			}
		}).start();
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(Start)) {
			if (!status_) {
				status_ = true;
				run();
			}
		}
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		if (s.getName().equals(EndTrigger)) {
			try {
				boolean b=v.getBoolean();
				if (b && endTrigger_==null){
					endTrigger_=addOutput("End trigger", ValueType.VOID);
				}
				else if (!b && endTrigger_!=null){
					removeOutput(endTrigger_);
					endTrigger_=null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			super.valueUpdated(s, v);
	}

	@Override
	public void setMode(EditorMode mode_) {
		if (mode_ != getMode()) {
			if (status_)
				status_ = false;
			super.setMode(mode_);
		}
	}
}
