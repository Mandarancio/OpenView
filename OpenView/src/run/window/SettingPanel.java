package run.window;

import gui.components.OVComponent;
import gui.enums.EditorMode;
import gui.interfaces.SettingListener;
import gui.interfaces.SettingManager;
import gui.settings.CategoryPanel;
import gui.settings.viewers.Viewer;
import gui.settings.viewers.ViewerManager;
import gui.support.Setting;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingPanel extends JPanel implements SettingManager {

	/**
     *
     */
	private static final long serialVersionUID = -5297687376220230306L;
	private HashMap<Setting, SettingListener> listeners_ = new HashMap<>();
	private OVComponent component_;
	private EditorMode mode_ = EditorMode.GUI;

	public SettingPanel() {
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	@Override
	public void select(OVComponent c) {
		if (c == null) {
			return;
		}
		component_ = c;
		initComponents(c);
		revalidate();
	}

	@Override
	public void deselect() {
		component_ = null;
		this.removeAll();
		for (Setting s : listeners_.keySet()) {
			s.removeListener(listeners_.get(s));
		}
		listeners_.clear();
		repaint();
	}

	private void initComponents(OVComponent c) {
		for (String category : c.getSettingCategories()) {
			CategoryPanel cp = new CategoryPanel(category);
			ArrayList<Setting> settings = c.getSettingCategory(category);
			cp.setNormalSize(new Dimension(getWidth(),
					25 * (settings.size() + 1)));
			boolean flag = false;
			int i = 0;
			for (Setting s : settings) {
				if (((mode_ == EditorMode.GUI || mode_.isExec()) && s
						.isGuiMode())
						|| (mode_ == EditorMode.NODE && s.isNodeMode())) {
					addComponent(s, cp);
					flag = true;
					i++;
				}
			}
			if (flag) {
				cp.setNormalSize(new Dimension(getWidth(), 25 * (i + 1)));
				this.add(cp);
			}
		}
	}

	private void addComponent(final Setting s, CategoryPanel cp) {
		if (mode_ == EditorMode.GUI || mode_ == EditorMode.DEBUG) {
			Viewer v;

			try {
				v = ViewerManager.initViewer(s);
				listeners_.put(s, v);
				cp.add(v);
				if (mode_.isExec()) {
					v.setEnabled(false);
				}
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (mode_ == EditorMode.NODE) {
			JPanel p = new JPanel();
			p.setMaximumSize(new Dimension(getWidth(), 25));
			p.setLayout(null);
			JLabel l = new JLabel(s.getName());
			l.setBounds(0, 0, 80, 25);
			p.add(l);
			if (!s.isConstant()) {
				if (s.isInput()) {
					final JCheckBox inbox = new JCheckBox("in");
					if (s.getInputNode() != null) {
						inbox.setSelected(true);
					}
					inbox.setHorizontalAlignment(JLabel.RIGHT);
					inbox.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							component_.refreshInputs(s, inbox.isSelected());
						}
					});

					inbox.setBounds(80, 0, (getWidth() - 110) / 2, 25);
					p.add(inbox);
				}
				if (s.isOutput()) {
					final JCheckBox outbox = new JCheckBox("out");
					if (s.getOutputNode() != null) {
						outbox.setSelected(true);
					}
					outbox.setHorizontalAlignment(JLabel.RIGHT);
					outbox.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							component_.refreshOutputs(s, outbox.isSelected());
						}
					});

					outbox.setBounds(80 + (getWidth() - 110) / 2, 0,
							(getWidth() - 110) / 2, 25);
					p.add(outbox);
				}
			}
			cp.add(p);
		}

	}

	@Override
	public void setMode(EditorMode mode) {
		if (mode != mode_) {
			mode_ = mode;
			OVComponent c = component_;
			deselect();
			select(c);
		}

	}

}
