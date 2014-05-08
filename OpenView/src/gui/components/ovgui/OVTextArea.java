package gui.components.ovgui;

import gui.components.OVComponent;
import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.w3c.dom.Element;

import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

public class OVTextArea extends OVComponent implements SlotListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8344969331774549941L;

	public enum TextAreaTrigger {
		NONE, ALLCHANGE, EXTERNAL
	}

	/**
     *
     */
	private static final String Text = "Text";
	private static final String Trigger = "Trigger";
	private boolean loopFlag_ = false;
	private JTextArea textArea_;
	private OutNode textOut_;
	private InNode trigger_ = null;
	private TextAreaTrigger triggerMode_ = TextAreaTrigger.ALLCHANGE;

	public OVTextArea(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.SizeW).setValue(120);
		getSetting(ComponentSettings.SizeH).setValue(45);
		initTextArea();

		textOut_ = addOutput(Text, ValueType.STRING);

		Setting s = new Setting(Text, "");
		s.setGuiMode(false);
		addSetting(ComponentSettings.SpecificCategory, s);
		s.setOutput(false);
		s = new Setting(Trigger, triggerMode_);
		addNodeSetting(ComponentSettings.SpecificCategory, s);
	}

	public OVTextArea(Element e, OVContainer father) {
		super(e, father);
		initTextArea();
		for (OutNode n : outputs_) {
			if (n.getLabel().equals(Text)) {
				textOut_ = n;
				break;
			}
		}
		try {
			TextAreaTrigger mode = (TextAreaTrigger) getNodeSetting(Trigger)
					.getValue().getEnum();
			if (mode == TextAreaTrigger.EXTERNAL) {
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

	private void initTextArea() {
		textArea_ = new JTextArea();
		textArea_.setLineWrap(true);
		textArea_.setWrapStyleWord(true);
		textArea_.setBackground(this.getBackground());
		textArea_.setForeground(this.getForeground());
		// textArea_.setUI(new ModernTextFieldUI());
		textArea_.getDocument().addDocumentListener(new DocumentListener() {

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
					getSetting(Text).setValue(textArea_.getText());
					if (triggerMode_ == TextAreaTrigger.ALLCHANGE)
						triggerOutput();
				} else

					loopFlag_ = false;

			}
		});
		JScrollPane p = new JScrollPane(textArea_);
		p.setBackground(getBackground());

		this.add(p, BorderLayout.CENTER);
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		String setting = s.getName();
		if (setting.equals(Text)) {
			if (!loopFlag_) {
				loopFlag_ = true;
				textArea_.setText(v.getString());
			} else
				loopFlag_ = false;
		} else if (setting.equals(Trigger)) {
			try {
				TextAreaTrigger mode = (TextAreaTrigger) v.getEnum();
				if (mode != triggerMode_) {
					triggerMode_ = mode;
					if (triggerMode_ == TextAreaTrigger.EXTERNAL) {
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
