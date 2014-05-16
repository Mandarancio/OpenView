package gui.layers;

import gui.EditorPanel;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class LayerManager extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 542029444966566694L;
	private LayerTreeModel model_;
	private boolean __locked = false;
	private EditorPanel container_;

	public LayerManager() {
		super(new LayerTreeModel());
		model_ = (LayerTreeModel) getModel();
		this.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				if (!__locked) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
					if (node == null) {
						return;
					} else {
						if (node.getUserObject() instanceof String) {
							container_.setSelectedLayer(null);
						} else if (node.getUserObject() instanceof NodeLayer) {
							container_.setSelectedLayer((NodeLayer) node
									.getUserObject());
						}
					}
				} else
					__locked = false;
			}
		});
	}

	public void addLayer(NodeLayer l) {
		DefaultMutableTreeNode n = model_.find(l.getName());
		if (n != null) {
			select(n);
		} else {
			container_.setSelectedLayer(l);
			model_.addLayer(l);
			select(l);
		}
	}

	public void removeLayer(NodeLayer l) {
		model_.removeLayer(l);
	}

	public void select(NodeLayer c) {
		DefaultMutableTreeNode n = model_.find(c);
		select(n);
	}

	private void select(DefaultMutableTreeNode n) {
		if (n != null) {
			__locked = true;
			TreeNode[] nodes = model_.getPathToRoot(n);
			this.setExpandsSelectedPaths(true);
			setSelectionPath(new TreePath(nodes));
		}
	}

	public void removeSelected() {
		if (getSelectionPath() != null) {
			Object o = getSelectionPath().getLastPathComponent();
			if (o != null && o instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) o;
				if (n.getUserObject() != null
						&& n.getUserObject() instanceof NodeLayer) {
					removeLayer((NodeLayer) n.getUserObject());
				}
			}
		}
	}

	public void setMainContainer(EditorPanel container) {
		container_ = container;
	}
}
