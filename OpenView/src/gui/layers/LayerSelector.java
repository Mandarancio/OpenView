package gui.layers;

import gui.components.OVComponent;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;

public class LayerSelector extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 742668056947023459L;

	private ArrayList<AssociatedNodeLayer> layers_;
	private OVComponent selectedComponent_;

	private int cellSize_ = 0;

	private MouseAdapter mouseAdapter_ = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (selectedComponent_ != null) {
				int ind = (int) Math.round(e.getX() / cellSize_)
						+ (int) Math.round(e.getY() / cellSize_);
				if (ind < layers_.size()) {
					layers_.get(ind).setVisible(!layers_.get(ind).isVisible());
					LayerSelector.this.repaint();
					selectedComponent_.checkLayer();
				}
			}
		}
	};

	public LayerSelector() {
		cellSize_ = 20;
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 47));
		this.addMouseListener(mouseAdapter_);
	}

	public void select(OVComponent c) {
		selectedComponent_ = c;
		layers_ = c.getNodeLayers();
	}

	public void deselect() {
		selectedComponent_ = null;
		layers_ = null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(getForeground());
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		if (cellSize_ > 0 && selectedComponent_ != null) {
			int x = 2;
			int y = 2;
			int h = cellSize_;
			for (AssociatedNodeLayer l : layers_) {
				if (l.isVisible()) {
					g.fillRect(x, y, h, h);
				} else {
					g.drawRect(x, y, h, h);
				}
				x += h + 2;
				if (x + h > getWidth()) {
					x = 2;
					y += h + 2;
				}
			}
		}
	}

}
