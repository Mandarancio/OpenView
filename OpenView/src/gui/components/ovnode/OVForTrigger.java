package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
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
	private OutNode index_;
	private OutNode trigger_;
	private boolean status_=false;

	public OVForTrigger(OVContainer father) {
		super(father);

		Setting s = new Setting(Start, 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Stop, 10.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Step, 1.0, -Double.MAX_VALUE, Double.MAX_VALUE);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Delay, 0,0,10000);
		addBothSetting(ComponentSettings.SpecificCategory, s);
		
		trigger_=addOutput("Trigger", ValueType.VOID);
		index_=addOutput("Index", ValueType.DOUBLE);
		
		InNode start=addInput(Start, ValueType.VOID);
		start.addListener(this);
		
		getSetting(ComponentSettings.Name).setValue("For");
	}

	private void run() {
		(new Thread(){
			@Override
			public void run() {
				try {
					double i = getSetting(Start).getValue().getDouble();
					double stop = getSetting(Stop).getValue().getDouble();
					double step = getSetting(Step).getValue().getDouble();
					int delay=getSetting(Delay).getValue().getInt();
					if (step < 0) {
						if (i < stop) {
							double tmp = i;
							i = stop;
							stop = tmp;
						}
						for (; i > stop; i += step) {
							if (status_==false)
								break;
							trigger_.trigger(new Value());
							index_.trigger(new Value(i));
							if (delay>0){
								Thread.sleep(delay);								
							}
						}
						status_=false;
					} else if (step > 0) {
						if (i > stop) {
							double tmp = i;
							i = stop;
							stop = tmp;
						}
						for (; i < stop; i += step) {
							if (status_==false)
								break;
							trigger_.trigger(new Value());
							index_.trigger(new Value(i));
							if (delay>0){
								Thread.sleep(delay);
							}
						}
						status_=false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					status_=false;
				}			
				super.run();
			}
		}).start();
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(Start)){
			if (!status_){
				status_=true;
				run();
			}
		}
	}

}
