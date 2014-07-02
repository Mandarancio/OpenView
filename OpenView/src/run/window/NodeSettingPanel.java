package run.window;

import gui.components.OVComponent;
import gui.enums.EditorMode;
import gui.interfaces.SettingListener;
import gui.interfaces.SettingManager;
import gui.layers.LayerSelector;
import gui.settings.CategoryPanel;
import gui.settings.viewers.Viewer;
import gui.settings.viewers.ViewerManager;
import gui.support.Setting;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class NodeSettingPanel extends JPanel implements SettingManager {

	/**
     *
     */
	private static final long serialVersionUID = -5297687376220230306L;
	private HashMap<Setting, SettingListener> listeners_ = new HashMap<>();
	private EditorMode mode_;
	private LayerSelector layerSelector_ = new LayerSelector();

	public NodeSettingPanel() {
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

	}

	@Override
	public void select(OVComponent c) {
		if (c == null) {
			return;
		}
		initComponents(c);
		this.add(new JPanel());
		this.add(layerSelector_);
		revalidate();

		layerSelector_.select(c);
	}

	@Override
	public void deselect() {
		this.removeAll();
		for (Setting s : listeners_.keySet()) {
			s.removeListener(listeners_.get(s));
		}
		layerSelector_.deselect();
		listeners_.clear();
		repaint();
	}

	private void initComponents(OVComponent c) {
		for (String category : c.getNodeSettingCategories()) {
			CategoryPanel cp = new CategoryPanel(category);
			ArrayList<Setting> settings = c.getNodeSettingCategory(category);
			cp.setNormalSize(new Dimension(getWidth(),
					25 * (settings.size() + 1)));
			for (Setting s : settings) {
				addComponent(s, cp);

			}
			add(cp);
			cp.setVisible(true);
		}
	}

	private void addComponent(final Setting s, CategoryPanel cp) {
		Viewer p;
		try {
			p = ViewerManager.initViewer(s);
			listeners_.put(s, p);
			cp.add(p);
			if (mode_.isExec()) {
				p.setEnabled(false);
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| SecurityException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void setMode(EditorMode mode) {
		mode_ = mode;
	}

}
