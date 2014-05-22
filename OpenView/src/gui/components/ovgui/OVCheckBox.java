package gui.components.ovgui;

import gui.adapters.TransferMouseAdapter;
import gui.components.OVComponent;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import org.w3c.dom.Element;

import ui.ModernCheckBoxUI;
import core.Setting;
import core.Value;

public class OVCheckBox extends OVComponent {

	/**
	 * 
	 */

	private static final long serialVersionUID = -7107369950355543842L;
	private static final String Selected = "Selected";
	private static final String Text = "Text";

	private JCheckBox checkBox_;
	private boolean __lock = false;

	public OVCheckBox(OVContainer father) {
		super(father);
		String txt = "Check me";
		Setting s = new Setting(Text, txt,this);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(Selected, new Boolean(false),this);
		s.setAutoTriggered(true);
		addSetting(ComponentSettings.SpecificCategory, s);
		initCheckBox();

		getSetting(ComponentSettings.SizeW).setValue(120);
		getSetting(ComponentSettings.SizeH).setValue(45);
	}

	public OVCheckBox(Element e, OVContainer father) {
		super(e, father);
		initCheckBox();
		triggerSettings();
	}

	private void initCheckBox() {
		String txt = "Check me";

		checkBox_ = new JCheckBox();
		checkBox_.setUI(new ModernCheckBoxUI());
		checkBox_.setForeground(getForeground());
		checkBox_.setBackground(getBackground());
		this.add(checkBox_, BorderLayout.CENTER);
		checkBox_.setText(txt);

		checkBox_.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				__lock = true;
				getSetting(Selected).setValue(
						new Boolean(checkBox_.isSelected()));
				if (!isSelected())
					select();
			}
		});

		TransferMouseAdapter tma = new TransferMouseAdapter(mouseAdapter_);
		checkBox_.addMouseListener(tma);
		checkBox_.addMouseMotionListener(tma);
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		if (s.getName().equals(Selected)) {
			if (!__lock) {
				try {
					if (checkBox_ != null)
						checkBox_.setSelected(v.getBoolean());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				__lock = false;
		} else if (s.getName().equals(Text)) {
			if (checkBox_ != null)
				checkBox_.setText(v.getString());
		}
		super.valueUpdated(s, v);
	}

}
