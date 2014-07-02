package gui.settings.viewers;

import gui.support.Setting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.JComboBox;

import core.Value;
import core.ValueType;
import core.support.Rule;

public class ComboViewer extends Viewer implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2780720720966189749L;
	private JComboBox<String> box_;

	public ComboViewer(Setting s) throws Exception {
		super(s);
		box_ = new JComboBox<>();
		if (s.getType() == ValueType.ENUM) {
			@SuppressWarnings("unchecked")
			EnumSet<?> set = EnumSet.allOf(s.getValue().getEnum().getClass());
			for (Object obj : set.toArray()) {
				box_.addItem(obj.toString());
			}
		} else if (s.getValue().getDescriptor().hasPossbilities()) {
			for (Value v : s.getValue().getDescriptor().getPossibilities()) {
				box_.addItem(v.getString());
			}
		} else {
			throw new Exception(
					"Setting is not an Enum and has no possibilities");
		}
		box_.setSelectedItem(s.getValue().getString());
		this.addMainComponent(box_);

		box_.addActionListener(this);
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		if (!box_.getSelectedItem().equals(v.getString())) {
			box_.setSelectedItem(v.getString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String val = box_.getSelectedItem().toString();
		if (setting_.getType() == ValueType.ENUM) {
			if (!setting_.getValue().getString().equals(val))
				try {
					@SuppressWarnings("unchecked")
					Enum<?> t = Enum.valueOf(setting_.getValue().getEnum()
							.getClass(), val);
					setting_.setValue(t);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
		} else {
			if (!setting_.getValue().getString().equals(val)) {
				setting_.setValue(setting_.getValue().getDescriptor()
						.getPossibilities().get(box_.getSelectedIndex())
						.getData());
			}
		}
	}

	public static String name() {
		return "ComboViewer";
	}

	@Override
	public Viewer copy(Setting s) throws Exception {
		return new ComboViewer(s);
	}

	public static Rule rule() {
		return new Rule() {

			@Override
			public boolean check(Object... args) {
				if (args.length == 1 && args[0] instanceof Setting) {
					Setting s = (Setting) args[0];
					return s.getType() == ValueType.ENUM
							|| s.getValue().getDescriptor().hasPossbilities();
				}
				return false;
			}
		};
	}
}
