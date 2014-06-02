package gui.layers;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class LayerTreeModel extends DefaultTreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 285206890998305984L;
	private DefaultMutableTreeNode root_;

	public LayerTreeModel() {
		super(new DefaultMutableTreeNode("Root Layer"));
		root_ = (DefaultMutableTreeNode) getRoot();
	}

	public void addLayer(NodeLayer layer) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(layer);
		root_.add(node);
		nodeStructureChanged(root_);

	}

	public void removeLayer(NodeLayer layer) {
		DefaultMutableTreeNode n = find(layer);
		if (n != null) {
			DefaultMutableTreeNode nr = ((DefaultMutableTreeNode) n.getParent());
			nr.remove(n);
			nodeStructureChanged(nr);
			nodeStructureChanged(root_);
		}
	}

	public DefaultMutableTreeNode find(String name) {
		for (int i = 0; i < root_.getChildCount(); i++) {
			TreeNode t = root_.getChildAt(i);
			if (t instanceof DefaultMutableTreeNode) {
				NodeLayer l = (NodeLayer) ((DefaultMutableTreeNode) t)
						.getUserObject();
				if (l.getName().equals(name))
					return (DefaultMutableTreeNode) t;

			}

		}
		return null;
	}

	public DefaultMutableTreeNode find(NodeLayer layer) {
		for (int i = 0; i < root_.getChildCount(); i++) {
			TreeNode t = root_.getChildAt(i);
			if (t instanceof DefaultMutableTreeNode) {
				if (((DefaultMutableTreeNode) t).getUserObject().equals(layer))
					return (DefaultMutableTreeNode) t;

			}

		}
		return null;
	}

	public DefaultMutableTreeNode root() {
		return root_;
	}

}
