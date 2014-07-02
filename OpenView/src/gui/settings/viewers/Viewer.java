package gui.settings.viewers;

import gui.interfaces.SettingListener;
import gui.support.Setting;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import core.Value;

public abstract class Viewer extends JPanel implements SettingListener {

	private static final long serialVersionUID = -2653260515004891031L;
	private JLabel nameLabel_;
	private JComponent mainComponent_;
	private SpringLayout layout_;
	protected Setting setting_;

	public Viewer(Setting s) {
		setting_ = s;
		nameLabel_ = new JLabel(s.getName());
		layout_ = new SpringLayout();
		this.setLayout(layout_);
		this.add(nameLabel_);
		layout_.putConstraint(SpringLayout.WEST, nameLabel_, 0,
				SpringLayout.WEST, this);
		layout_.putConstraint(SpringLayout.NORTH, nameLabel_, 0,
				SpringLayout.NORTH, this);
		layout_.putConstraint(SpringLayout.SOUTH, nameLabel_, 0,
				SpringLayout.SOUTH, this);
		layout_.putConstraint(SpringLayout.EAST, nameLabel_, 80,
				SpringLayout.WEST, this);
		s.addListener(this);
	}

	protected void addMainComponent(JComponent c) {
		this.add(c);
		mainComponent_ = c;
		layout_.putConstraint(SpringLayout.WEST, c, 5, SpringLayout.EAST,
				nameLabel_);
		layout_.putConstraint(SpringLayout.NORTH, c, 0, SpringLayout.NORTH,
				this);
		layout_.putConstraint(SpringLayout.SOUTH, c, 0, SpringLayout.SOUTH,
				this);
		layout_.putConstraint(SpringLayout.EAST, c, 0, SpringLayout.EAST, this);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (mainComponent_ != null) {
			mainComponent_.setEnabled(enabled);
		}
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		// TODO Auto-generated method stub

	}

	public static String name() {
		return "Viewer";
	}

	public abstract Viewer copy(Setting s) throws Exception;
}
