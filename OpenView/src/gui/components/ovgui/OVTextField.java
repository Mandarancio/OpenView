package gui.components.ovgui;

import gui.components.OVComponent;
import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.w3c.dom.Element;

import ui.ModernTextFieldUI;
import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

public class OVTextField extends OVComponent implements SlotListener {
	public enum TextFieldTrigger {
		NONE, RETURN, ALLCHANGE, EXTERNAL
	}

	/**
     *
     */
	private static final long serialVersionUID = -441297102087153624L;
	private static final String Text = "Text";
	private static final String Trigger = "Trigger";
	private boolean loopFlag_ = false;
	private JTextField textField_;
	private OutNode textOut_;
	private InNode trigger_ = null;
	private TextFieldTrigger triggerMode_ = TextFieldTrigger.ALLCHANGE;

	public OVTextField(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.SizeW).setValue(120);
		getSetting(ComponentSettings.SizeH).setValue(45);
		initTextField();

		textOut_ = addOutput(Text, ValueType.STRING);

		Setting s = new Setting(Text, "", this);
		addSetting(ComponentSettings.SpecificCategory, s);
		s.setOutput(false);
		s = new Setting(Trigger, triggerMode_, this);
		addNodeSetting(ComponentSettings.SpecificCategory, s);

	}

	public OVTextField(Element e, OVContainer father) {
		super(e, father);
		initTextField();

		for (OutNode n : outputs_) {
			if (n.getLabel().equals(Text)) {
				textOut_ = n;
				break;
			}
		}
		try {
			TextFieldTrigger mode = (TextFieldTrigger) getNodeSetting(Trigger)
					.getValue().getEnum();
			if (mode == TextFieldTrigger.EXTERNAL) {
				for (InNode n : inputs_) {
					if (n.getLabel().equals(Trigger)) {
						trigger_ = n;
						trigger_.addListener(this);
						break;
					}
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		triggerSettings();
	}

	private void initTextField() {
		textField_ = new JTextField();
		textField_.setBackground(this.getBackground());
		textField_.setForeground(this.getForeground());
		textField_.setUI(new ModernTextFieldUI());
		textField_.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {

				trigger();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				trigger();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				trigger();
			}

			private void trigger() {
				if (!loopFlag_) {
					loopFlag_ = true;
					getSetting(Text).setValue(textField_.getText());
					if (triggerMode_ == TextFieldTrigger.ALLCHANGE)
						triggerOutput();
				} else

					loopFlag_ = false;

			}
		});

		textField_.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (triggerMode_ == TextFieldTrigger.RETURN)
					triggerOutput();
			}
		});

		this.add(textField_, BorderLayout.CENTER);

	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		String setting = s.getName();
		if (setting.equals(Text)) {
			if (!loopFlag_) {
				loopFlag_ = true;
				if (textField_ != null)
					textField_.setText(v.getString());
			} else
				loopFlag_ = false;
		} else if (setting.equals(Trigger)) {
			try {
				TextFieldTrigger mode = (TextFieldTrigger) v.getEnum();
				if (mode != triggerMode_) {
					triggerMode_ = mode;
					if (triggerMode_ == TextFieldTrigger.EXTERNAL) {
						trigger_ = addInput(Trigger, ValueType.VOID);
						trigger_.addListener(this);
					} else if (trigger_ != null) {
						removeInput(trigger_);
						trigger_ = null;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			super.valueUpdated(s, v);
	}

	protected void triggerOutput() {
		if (getMode().isExec()) {
			textOut_.trigger(getSetting(Text).getValue());
		}
	}

	@Override
	public void setMode(EditorMode mode) {
		loopFlag_ = false;
		super.setMode(mode);
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(Trigger)) {
			triggerOutput();
		}
	}

}
