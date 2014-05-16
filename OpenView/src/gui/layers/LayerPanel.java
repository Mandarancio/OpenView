package gui.layers;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class LayerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5396176760473594301L;

	private SpringLayout layout_;

	private LayerManager layerManager_;

	public LayerPanel() {
		layout_ = new SpringLayout();
		setLayout(layout_);

		JButton bp = new JButton("+");
		this.add(bp);
		JButton bm = new JButton("-");
		this.add(bm);

		layout_.putConstraint(SpringLayout.NORTH, bp, 2, SpringLayout.NORTH,
				this);
		layout_.putConstraint(SpringLayout.WEST, bp, 2, SpringLayout.WEST, this);
		layout_.putConstraint(SpringLayout.SOUTH, bp, 30, SpringLayout.NORTH,
				bp);
		layout_.putConstraint(SpringLayout.EAST, bp, 30, SpringLayout.WEST, bp);
		layout_.putConstraint(SpringLayout.NORTH, bm, 2, SpringLayout.NORTH,
				this);
		layout_.putConstraint(SpringLayout.WEST, bm, 2, SpringLayout.EAST, bp);
		layout_.putConstraint(SpringLayout.SOUTH, bm, 30, SpringLayout.NORTH,
				bm);
		layout_.putConstraint(SpringLayout.EAST, bm, 30, SpringLayout.WEST, bm);

		// add layer manager
		layerManager_ = new LayerManager();
		JScrollPane panel = new JScrollPane(layerManager_,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(panel);
		layout_.putConstraint(SpringLayout.NORTH, panel, 2, SpringLayout.SOUTH, bm);
		layout_.putConstraint(SpringLayout.WEST, panel, 2, SpringLayout.WEST, this);
		layout_.putConstraint(SpringLayout.EAST, panel, -2, SpringLayout.EAST, this);
		layout_.putConstraint(SpringLayout.SOUTH, panel, -2, SpringLayout.SOUTH, this);
		
	}

}