package gui.components.ovnode;

import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Element;

import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;
import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

public class OVTimerTriggerNode extends OVNodeComponent implements SlotListener {

	public enum TimerMode {
		SINGLE, INFINITE
	}

	private class MyTask extends TimerTask {

		@Override
		public void run() {
			triggerOutput();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 281624649721982859L;
	private static final String Trigger = "Trigger", Starter = "Start",
			Stopper = "Stop";
	private static final String Delay = "Delay", Mode = "Mode";
	private TimerMode timerMode_ = TimerMode.INFINITE;
	private OutNode trigger_;
	private InNode starter_, stopper_;
	private Timer timer_;

	public OVTimerTriggerNode(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.Name).setValue("Timer");
		trigger_ = addOutput(Trigger, ValueType.VOID);
		starter_ = addInput(Starter, ValueType.VOID);
		starter_.addListener(this);
		stopper_ = addInput(Stopper, ValueType.VOID);
		stopper_.addListener(this);
		Setting s = new Setting(Mode, TimerMode.INFINITE);
		addNodeSetting(ComponentSettings.SpecificCategory, s);

		s = new Setting(Delay, 1000, 10, 10000);
		addBothSetting(ComponentSettings.SpecificCategory, s);

	}

	public OVTimerTriggerNode(Element e, OVContainer father) {
		super(e, father);
		for (InNode n : inputs_) {
			if (n.getLabel().equals(Starter)) {
				starter_=n;
				starter_.addListener(this);
			}
			else if (n.getLabel().equals(Stopper)){
				stopper_=n;
				stopper_.addListener(this);
			}
		}
		for (OutNode n : outputs_) {
			if (n.getLabel().equals(Trigger))
				trigger_ = n;
		}
	}
	
	private void triggerOutput() {
		// if (getMode().isExec())
		trigger_.trigger(new Value(Void.TYPE));
	}

	private void startTimer() {
		if (timer_ == null) {
			try {
				timer_ = new Timer();
				int delay = getSetting(Delay).getValue().getInt();
				if (timerMode_ == TimerMode.SINGLE) {
					timer_.schedule(new MyTask(), delay);
				} else {
					timer_.schedule(new MyTask(), delay, delay);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void stopTimer() {
		if (timer_ != null) {
			timer_.cancel();
			timer_.purge();
			timer_ = null;
		}
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		String setting = s.getName();
		if (setting.equals(Mode)) {
			try {
				TimerMode m = (TimerMode) v.getEnum();
				if (timerMode_ != m) {
					timerMode_ = m;
					if (m == TimerMode.INFINITE) {
						stopper_ = addInput(Stopper, ValueType.VOID);
						stopper_.addListener(this);
						getSetting(Delay).setMin(new Value(10));
					} else {
						removeInput(stopper_);
						stopper_ = null;
						getSetting(Delay).setMin(new Value(1));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			super.valueUpdated(s, v);
	}

	@Override
	public void setMode(EditorMode mode) {
		if (getMode() != mode) {
			if (mode.isExec()) {
				if (starter_.isFree()) {
					try {
						startTimer();
					} catch (Exception e) {
						startTimer();
						e.printStackTrace();
					}
				}

			} else if (!mode.isExec()) {
				stopTimer();
			}
		}
		super.setMode(mode);
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(Starter)) {
			startTimer();
		} else if (s.getLabel().equals(Stopper)) {
			stopTimer();
		}
	}

}
