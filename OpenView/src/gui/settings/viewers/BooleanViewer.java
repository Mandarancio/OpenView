package gui.settings.viewers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import core.Value;
import gui.support.Setting;

public class BooleanViewer extends Viewer implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8699306925255380261L;

	private JCheckBox checkBox_;

	public BooleanViewer(Setting s) throws Exception {
		super(s);
		checkBox_ = new JCheckBox("", s.getValue().getBoolean());
		addMainComponent(checkBox_);
		s.addListener(this);
		checkBox_.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (setting_.getValue().getBoolean() != checkBox_.isSelected()) {
				setting_.setValue(new Boolean(checkBox_.isSelected()));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		try {
			boolean e = v.getBoolean();
			if (e != checkBox_.isSelected()) {
				checkBox_.setSelected(e);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
