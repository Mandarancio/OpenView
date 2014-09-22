package gui.settings.viewers;

import gui.support.Setting;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import core.Value;
import core.ValueType;
import core.support.Rule;

public class StringViewer extends Viewer implements DocumentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 737239822622988210L;
	private JTextField textField_;
	private boolean flag_ = false;

	public StringViewer(Setting s) {
		super(s);

		textField_ = new JTextField(s.getValue().getString());
		textField_.setHorizontalAlignment(JTextField.RIGHT);
		textField_.getDocument().addDocumentListener(this);

		this.addMainComponent(textField_);
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		if (!flag_) {
			if (!v.getString().equals(textField_.getText())) {
				flag_ = true;
				textField_.setText(v.getString());
			}
		} else {
			flag_ = false;
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		trigger();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		trigger();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		trigger();
	}

	private void trigger() {
		if (!flag_) {
			if (!setting_.getValue().getString().equals(textField_.getText())) {
				flag_ = true;
				setting_.setValue(textField_.getText());
			}
		} else {
			flag_ = false;
		}
	}

	public static String name() {
		return "StringViewer";
	}

	@Override
	public Viewer copy(Setting s) {
		return new StringViewer(s);
	}

	public static Rule rule() {
		return new Rule() {

			@Override
			public boolean check(Object... args) {
				if (args.length == 1 && args[0] instanceof Setting) {
					Setting s = (Setting) args[0];
					return s.getType() == ValueType.STRING;
				}
				return false;
			}
			@Override
			public int orderValue() {
				return 0;
			}
		};
	}

}
