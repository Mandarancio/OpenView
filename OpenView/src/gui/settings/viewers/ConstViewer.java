package gui.settings.viewers;

import javax.swing.JLabel;

import gui.support.Setting;

public class ConstViewer extends Viewer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4139041201716868452L;
	private JLabel valueLabel_;
	public ConstViewer(Setting s) {
		super(s);
		valueLabel_=new JLabel(s.getValue().toString());
		valueLabel_.setHorizontalAlignment(JLabel.RIGHT);
		this.addMainComponent(valueLabel_);
	}
	
}
