package gui;

import gui.components.OVComponent;
import gui.enums.EditorMode;
import gui.interfaces.SettingManager;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class RightPanel extends JSplitPane implements SettingManager {
	private ArrayList<SettingManager> managers_ = new ArrayList<>();
	private EditorMode mode_ = EditorMode.GUI;
	private ObjectManager manager_;
	private JTabbedPane tabPane_;
	/**
	 * 
	 */
	private static final long serialVersionUID = 6562729020471545125L;

	public RightPanel() {
		super(JSplitPane.VERTICAL_SPLIT);
		setDividerLocation(400);
		setManager(new ObjectManager());
		SettingPanel p = new SettingPanel();
		tabPane_ = new JTabbedPane();

		tabPane_.addTab("Settings", new JScrollPane(p,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		managers_.add(p);
		this.setRightComponent(new JScrollPane(manager_,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		this.setLeftComponent(tabPane_);
		revalidate();
	}

	@Override
	public void select(OVComponent c) {
		for (SettingManager m : managers_)
			m.select(c);
		manager_.select(c);
	}

	@Override
	public void deselect() {
		for (SettingManager m : managers_)
			m.deselect();
		manager_.deselect();
	}

	@Override
	public void setMode(EditorMode mode) {
		if (mode == mode_)
			return;

		if (mode == EditorMode.GUI) {
			deselect();
			tabPane_.removeAll();
			managers_.clear();
			SettingPanel p = new SettingPanel();
			tabPane_.addTab("Settings", new JScrollPane(p,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

			p.setMode(mode);
			managers_.add(p);

		} else if (mode == EditorMode.NODE) {
			deselect();
			tabPane_.removeAll();
			managers_.clear();

			NodeSettingPanel np = new NodeSettingPanel();
			tabPane_.addTab("Settings", new JScrollPane(np,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			managers_.add(np);

			np.setMode(mode);

			SettingPanel p = new SettingPanel();
			tabPane_.addTab("Nodes", new JScrollPane(p,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			managers_.add(p);

			p.setMode(mode);

		} else if (mode.isExec()) {
			deselect();
			tabPane_.removeAll();
			managers_.clear();

			SettingPanel p = new SettingPanel();
			tabPane_.addTab("GUI Setting", new JScrollPane(p,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			managers_.add(p);

			p.setMode(mode);

			NodeSettingPanel np = new NodeSettingPanel();
			tabPane_.addTab("Node Settings", new JScrollPane(np,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			managers_.add(np);

			np.setMode(mode);

		}
		mode_ = mode;
	}

	public ObjectManager getManager() {
		return manager_;
	}

	private void setManager(ObjectManager manager_) {
		this.manager_ = manager_;
	}

}
