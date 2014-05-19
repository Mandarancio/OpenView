package run.window.support;

import gui.components.OVComponent;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class OVTreeModel extends DefaultTreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7309060538044852654L;
	private DefaultMutableTreeNode root_;

	public OVTreeModel() {
		super(new DefaultMutableTreeNode("Root"));
		root_ = (DefaultMutableTreeNode) getRoot();
	}

	public void createNode(OVComponent c) {
		if (c.getFather() instanceof OVComponent) {
			DefaultMutableTreeNode n = find((OVComponent) c.getFather());
			if (n == null) {
				root_.add(new DefaultMutableTreeNode(c));
				nodeStructureChanged(root_);

			} else {
				System.out.println("added to " + c);
				n.add(new DefaultMutableTreeNode(c));
				nodeStructureChanged(n);
			}
		} else {
			root_.add(new DefaultMutableTreeNode(c));
		}
		nodeStructureChanged(root_);
	}

	public DefaultMutableTreeNode find(OVComponent father) {
		return find(father, root_);
	}

	private DefaultMutableTreeNode find(OVComponent father,
			DefaultMutableTreeNode root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			TreeNode t = root.getChildAt(i);
			if (t instanceof DefaultMutableTreeNode) {
				if (((DefaultMutableTreeNode) t).getUserObject().equals(father))
					return (DefaultMutableTreeNode) t;
				if (t.getChildCount() > 0) {
					DefaultMutableTreeNode n = find(father,
							(DefaultMutableTreeNode) t);
					if (n != null)
						return n;
				}
			}

		}
		return null;
	}

	public void remove(OVComponent c) {
		DefaultMutableTreeNode n = find(c);
		if (n != null) {
			DefaultMutableTreeNode nr = ((DefaultMutableTreeNode) n.getParent());
			nr.remove(n);
			nodeStructureChanged(nr);
			nodeStructureChanged(root_);
		}

	}

}
