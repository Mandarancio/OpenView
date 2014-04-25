package gui.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CategoryPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2625090851297892669L;
	private boolean compressed_ = false;
	private Dimension normalSize_ = new Dimension(0, 0);

	private MouseAdapter adapter_ = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getY() < 30)
					setCompressed(!isCompressed());
			}
		}
	};

	public CategoryPanel(String name) {
		this.setName(name);
		this.setBorder(BorderFactory.createEmptyBorder(15, 8, 2, 8));
		this.addMouseListener(adapter_);
		this.setLayout(new GridLayout(0,1));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(getForeground());
		g2d.drawString((isCompressed() ? "+" : "-"), 2,10);
		g2d.drawString(getName(), 15, 10);
	}

	public boolean isCompressed() {
		return compressed_;
	}

	public void setCompressed(boolean compressed) {
		if (compressed != compressed_) {
			this.compressed_ = compressed;
			if (!compressed) {
				this.setSize(getNormalSize());
				this.setPreferredSize(getNormalSize());
				this.setMaximumSize(getNormalSize());
				for (Component c : getComponents())
					c.setVisible(true);
			} else {
				for (Component c : getComponents())
					c.setVisible(false);
				this.setSize(getWidth(), 25);
				this.setPreferredSize(new Dimension(getWidth(), 25));
				this.setMaximumSize(new Dimension(getWidth(), 25));
			}
			if (getParent() != null)
				getParent().revalidate();
		}
	}

	public Dimension getNormalSize() {
		return normalSize_;
	}

	public void setNormalSize(Dimension normalSize_) {
		this.normalSize_ = normalSize_;
		this.setPreferredSize(new Dimension(100, normalSize_.height));
		this.setMinimumSize(new Dimension(10,normalSize_.height-4));
		this.setMaximumSize(normalSize_);

	}

}
