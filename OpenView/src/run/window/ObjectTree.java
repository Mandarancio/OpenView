package run.window;

import gui.components.OVComponent;
import gui.constants.ComponentSettings;
import gui.interfaces.SettingListener;
import gui.support.Setting;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import run.window.support.OVTreeModel;
import core.Value;

public class ObjectTree extends JTree implements SettingListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7964686809856171667L;
	private ArrayList<OVComponent> components_ = new ArrayList<>();
	private OVTreeModel model_;
	private boolean __locked = false;

	public ObjectTree() {
		super(new OVTreeModel());
		model_ = (OVTreeModel) this.getModel();
		this.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				if (!__locked) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
					if (node == null) {
						return;
					} else {
						if (node.getUserObject() instanceof OVComponent) {
							((OVComponent) node.getUserObject()).select();
						}
					}
				} else
					__locked = false;
			}
		});
	}

	public void addComponent(OVComponent c) {
		ObjectManager.addComponent(c);
		String name = c.getSetting(ComponentSettings.Name).getValue()
				.getString();
		if (!isAvaiable(name)) {
			name = removePrefix(name);
			name = checkName(c.getSetting(ComponentSettings.Name), name, 1);
		}
		c.getSetting(ComponentSettings.Name).setValue(name);
		c.getSetting(ComponentSettings.Name).addListener(this);
		components_.add(c);
		model_.createNode(c);
		select(c);
	}

	private boolean isAvaiable(String name) {
		return isAvaiable(name, null);
	}

	private boolean isAvaiable(String name, OVComponent cmp) {
		for (OVComponent c : components_) {
			if (!c.equals(cmp)) {
				Setting s = c.getSetting(ComponentSettings.Name);
				String n = s.getValue().getString();
				if (n.equals(name))
					return false;
			}

		}
		return true;
	}

	private String checkName(Setting setting, String name, int id) {

		String v = name + Integer.toString(id);
		for (OVComponent c : components_) {
			Setting s = c.getSetting(ComponentSettings.Name);
			if (!s.equals(setting)) {
				String n = s.getValue().getString();
				if (n.equals(v))
					return checkName(setting, name, id + 1);
			}
		}
		return v;
	}

	public void removeComponent(OVComponent c) {
		model_.remove(c);
		components_.remove(c);
		ObjectManager.removeComponent(c);
	}

	@Override
	public void valueUpdated(Setting setting, Value v) {
		String name = v.getString();
		if (!isAvaiable(name, setting.getParent())) {
			int ind = getPrefix(name);
			if (ind != 0)
				name = removePrefix(name);
			else
				ind = 1;

			name = checkName(setting, name, ind);
			v.setData(name);
		}
	}

	private int getPrefix(String name) {
		if (name.length() > 0) {
			char c = name.charAt(name.length() - 1);
			if (c >= '0' && c <= '9') {
				return c2i(c) + 10
						* getPrefix(name.substring(0, name.length() - 2));
			}
		}
		return 0;
	}

	private String removePrefix(String name) {
		if (name.length() <= 1)
			return name;
		char c = name.charAt(name.length() - 1);
		if (c >= '0' && c <= '9') {
			return removePrefix(name.substring(0, name.length() - 1));
		}
		return name;
	}

	private int c2i(char c) {
		switch (c) {
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		}
		return 0;
	}

	public void select(OVComponent c) {
		DefaultMutableTreeNode n = model_.find(c);
		if (n != null) {
			__locked = true;
			TreeNode[] nodes = model_.getPathToRoot(n);
			this.setExpandsSelectedPaths(true);

			setSelectionPath(new TreePath(nodes));
		}
	}

	public void deselect() {
		setSelectionPath(new TreePath(model_.getRoot()));
	}

}
