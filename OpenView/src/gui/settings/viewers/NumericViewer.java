package gui.settings.viewers;

import gui.support.Setting;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Value;
import core.support.Rule;

public class NumericViewer extends Viewer implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3759486423580481751L;

	private SpinnerNumberModel model_;
	private JSpinner spinner_;

	public NumericViewer(Setting s) throws Exception {
		super(s);
		if (s.getType().isInteger()) {
			int val = s.getValue().getInt();
			int min = Integer.MIN_VALUE;
			int max = Integer.MAX_VALUE;
			if (s.getMax() != null) {
				max = s.getMax().getInt();
			}
			if (s.getMin() != null) {
				min = s.getMin().getInt();
			}
			model_ = new SpinnerNumberModel(val, min, max, 1);
		} else {
			double val = s.getValue().getDouble();
			double min = -Double.MAX_VALUE;
			double max = Double.MAX_VALUE;
			if (s.getMax() != null) {
				max = s.getMax().getDouble();
			}
			if (s.getMin() != null) {
				min = s.getMin().getDouble();
			}
			model_ = new SpinnerNumberModel(val, min, max, 0.01);
		}
		spinner_ = new JSpinner(model_);
		this.addMainComponent(spinner_);

		spinner_.addChangeListener(this);
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		try {
			if (setting_.getType().isDecimal()) {
				spinner_.setValue(v.getDouble());
			} else {
				spinner_.setValue(v.getInt());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		setting_.setValue(spinner_.getValue());
	}

	public static String name() {
		return "NumericViewer";
	}

	@Override
	public Viewer copy(Setting s) throws Exception {
		return new NumericViewer(s);
	}

	public static Rule rule() {
		return new Rule() {

			@Override
			public boolean check(Object... args) {
				if (args.length == 1 && args[0] instanceof Setting) {
					Setting s = (Setting) args[0];
					return s.getType().isNumeric();
				}
				return false;
			}
			@Override
			public int orderValue() {
				return 1;
			}
		};

	}

}
