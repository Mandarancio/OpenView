package gui.layers;

import gui.components.OVComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
				int xind = (e.getX() - 2) / (cellSize_ + 2);
				int yind = (e.getY() - 2) / (cellSize_ + 2);
				int ind = xind + (yind * ((getWidth() - 4) / (cellSize_ + 2)));
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
		setForeground(Color.gray);
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
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(getForeground());
		g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
		if (cellSize_ > 0 && selectedComponent_ != null) {
			int x = 2;
			int y = 2;
			int h = cellSize_;
			for (AssociatedNodeLayer l : layers_) {
				if (l.equals(selectedComponent_.getActiveLayer())) {
					g.setColor(Color.DARK_GRAY);
				} else {
					g.setColor(Color.gray);
				}
				if (l.isVisible()) {
					g.fillRoundRect(x, y, h, h, 4, 4);
				} else {
					g.drawRoundRect(x, y, h, h, 4, 4);
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
