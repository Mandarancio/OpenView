package gui.components.ovgui;

import gui.adapters.TransferMouseAdapter;
import gui.components.OVComponent;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;

import java.awt.BorderLayout;

import javax.swing.JProgressBar;

import org.w3c.dom.Element;

import ui.ModernProgressBarUI;
import core.Setting;
import core.Value;

public class OVProgressBar extends OVComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3021937186883474076L;
	private static final String _Min = "Min";
	private static final String _Max = "Max";
	private static final String _Value = "Value";
	private JProgressBar progressBar_;

	public OVProgressBar(OVContainer father) {
		super(father);

		getSetting(ComponentSettings.SizeW).setValue(150);
		getSetting(ComponentSettings.SizeH).setValue(30);

		progressBar_ = new JProgressBar();
		this.add(progressBar_, BorderLayout.CENTER);

		Setting s = new Setting(_Min, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Value, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		addSetting(ComponentSettings.SpecificCategory, s);
		s = new Setting(_Max, 100, Integer.MIN_VALUE, Integer.MAX_VALUE);
		addSetting(ComponentSettings.SpecificCategory, s);

		progressBar_.setUI(new ModernProgressBarUI());
		progressBar_.setMinimum(0);
		progressBar_.setMaximum(100);
		progressBar_.setValue(0);
		initListeners();
	}

	public OVProgressBar(Element e, OVContainer father) {
		super(e, father);
		progressBar_ = new JProgressBar();
		this.add(progressBar_, BorderLayout.CENTER);
		progressBar_.setUI(new ModernProgressBarUI());
		try {
			progressBar_.setMinimum(getSetting(_Min).getValue().getInt());
			progressBar_.setMaximum(getSetting(_Max).getValue().getInt());
			progressBar_.setValue(getSetting(_Value).getValue().getInt());
		} catch (Exception ex) {
			ex.printStackTrace();
			progressBar_.setMinimum(0);
			progressBar_.setMaximum(100);
			progressBar_.setValue(0);
		}
		initListeners();
	}

	private void initListeners() {
		TransferMouseAdapter tma = new TransferMouseAdapter(mouseAdapter_);
		progressBar_.addMouseListener(tma);
		progressBar_.addMouseMotionListener(tma);
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		try {
			if (s.getName().equals(_Min)) {
				if (progressBar_ != null)
					progressBar_.setMinimum(v.getInt());
			} else if (s.getName().equals(_Max)) {
				if (progressBar_ != null)
					progressBar_.setMaximum(v.getInt());
			} else if (s.getName().equals(_Value)) {
				if (progressBar_ != null)
					progressBar_.setValue(v.getInt());
			} else
				super.valueUpdated(s, v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
