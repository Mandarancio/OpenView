package gui.layers;

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
						if (node.getUserObject() instanceof NodeLayer) {
							//select
						}
					}
				} else
					__locked = false;
			}
		}); 
	}

	public void addLayer(NodeLayer l) {
		model_.addLayer(l);
		select(l);
	}
	
	public void removeLayer(NodeLayer l){
		model_.removeLayer(l);
	}

	public void select(NodeLayer c) {
		DefaultMutableTreeNode n = model_.find(c);
		if (n != null) {
			__locked = true;
			TreeNode[] nodes = model_.getPathToRoot(n);
			this.setExpandsSelectedPaths(true);
			setSelectionPath(new TreePath(nodes));
		}
	}
}
