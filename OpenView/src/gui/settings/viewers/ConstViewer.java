package gui.settings.viewers;

import gui.support.Setting;

import javax.swing.JLabel;

import core.support.Rule;

public class ConstViewer extends Viewer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4139041201716868452L;
	private JLabel valueLabel_;

	public ConstViewer(Setting s) {
		super(s);
		valueLabel_ = new JLabel(s.getValue().getString());
		valueLabel_.setHorizontalAlignment(JLabel.RIGHT);
		this.addMainComponent(valueLabel_);
	}

	public static String name() {
		return "ConstViewer";
	}

	@Override
	public Viewer copy(Setting s) {
		return new ConstViewer(s);
	}

	public static Rule rule() {
		return new Rule() {

			@Override
			public boolean check(Object... args) {
				if (args.length == 1 && args[0] instanceof Setting) {
					Setting s = (Setting) args[0];
					return s.isConstant();
				}
				return false;
			}
			@Override
			public int orderValue() {
				return 6;
			}
		};
	}
}
