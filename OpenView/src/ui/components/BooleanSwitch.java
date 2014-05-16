package ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class BooleanSwitch extends JComponent {

	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseClicked(final java.awt.event.MouseEvent e) {
			if (!editable_)
				return;
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					if (e.getButton() == MouseEvent.BUTTON1 && isEnabled()) {
						boolean selection;
						if (e.getPoint().x < BooleanSwitch.this.getWidth() / 2) {
							selection = false;
						} else
							selection = true;
						if (selection != BooleanSwitch.this.isSelected()) {
							BooleanSwitch.this.setSelected(selection);
							for (ActionListener listener : actionListeners_) {
								listener.actionPerformed(new ActionEvent(
										BooleanSwitch.this, 0, switchAction));
							}
						}
					}
				}
			});
		}
	};

	public static final String switchAction = "SWITCHED";
	/**
	 * 
	 */
	private static final long serialVersionUID = 5700981701844233572L;
	private boolean showReference_ = false;
	private boolean reference_ = false;
	private boolean selected_ = false;
	private ArrayList<ActionListener> actionListeners_ = new ArrayList<ActionListener>();
	private boolean editable_ = true;

	public BooleanSwitch() {
		// this.setBackground(new Color(25,23,28));
		this.addMouseListener(mouseAdapter);
	}

	public BooleanSwitch(String tooltip) {
		this();
		this.setToolTipText(tooltip);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(this.getBackground());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int radius = (int) Math.round(getWidth() * 10 / 100.);
		boolean dark = (getBackground().getRGB() < Color.gray.getRGB());

		g2d.setColor(getBackground().darker().darker());
		g2d.fillRoundRect(1, 5, getWidth() - 2, getHeight() - 10, radius,
				radius);

		g2d.setColor(new Color(255, 255, 255, 30));
		g2d.drawRoundRect(1, 5, getWidth() - 3, getHeight() - 11, radius,
				radius);
		if (dark)
			g2d.setColor(getBackground().brighter());
		else
			g2d.setColor(getBackground());// new Color(112, 109, 117,
													// 100));
		g2d.fillRoundRect(4, 8, getWidth() - 8, getHeight() - 16, radius - 2,
				radius - 2);

		Color grad[] = new Color[2];
		Color bg = getBackground();
		if (dark) {
			grad[0] = getBackground().brighter().brighter();
			grad[1] = getBackground().brighter();
			bg = bg.brighter();
		} else {
			grad[0] = getBackground().darker();
			grad[1] = getBackground();
		}

		if (!isSelected()) {
			g2d.setClip(0, 0, getWidth() / 2, getHeight());
			g2d.setColor(bg);// palette.getBackground());//new Color(41, 40,
								// 42));
			g2d.fillRoundRect(6, 9, getWidth() / 2, getHeight() - 18,
					radius - 6, radius - 6);
			GradientPaint unselectedGradient = new GradientPaint(
					getWidth() / 2, 0f, grad[1], getWidth(), 0f, grad[0]);
			g2d.setPaint(unselectedGradient);
			g2d.setClip(getWidth() / 2, 0, getWidth(), getHeight());

			g2d.fill(new RoundRectangle2D.Double(getWidth() / 2 - 6, 9,
					getWidth() / 2 - 2, getHeight() - 18, radius - 3,
					radius - 3));
			g2d.setClip(0, 0, getWidth(), getHeight());

			g2d.setColor(new Color(255, 96, 0));

			int w = getWidth() * 10 / 100;
			g2d.fillRoundRect(getWidth() / 4 - w / 4 - 2, getHeight() / 2 - 1,
					w, 2, radius - 8, radius - 8);

			float rad = getHeight() / 2 - 10;
			if (rad < 0)
				rad = 0;
			float dist[] = { 0.0f, 1.0f };
			Color colors[] = { new Color(255, 96, 0, 50),
					new Color(255, 96, 0, 0) };
			RadialGradientPaint rgp = new RadialGradientPaint(
					new Point2D.Double(getWidth() / 4 - w / 4 + w / 2 - 2,
							getHeight() / 2), rad, dist, colors);
			g2d.setPaint(rgp);

			g2d.fillRect(0, 0, getWidth() / 2, getHeight());

			g2d.setColor(getBackground().darker().darker());// new
																	// Color(21,
																	// 20, 22));
			int h = (getHeight() / 3) - 4;

			g2d.setStroke(new BasicStroke(2));
			g2d.draw(new Ellipse2D.Double(3 * getWidth() / 4 - h / 2 - 2,
					getHeight() / 2 - h / 2 - 1, h, h));

		} else {
			GradientPaint unselectedGradient = new GradientPaint(0, 0f,
					grad[0], getWidth() / 2, 0, grad[1]);

			g2d.setPaint(unselectedGradient);
			g2d.setClip(0, 0, getWidth() / 2, getHeight());
			g2d.fill(new RoundRectangle2D.Double(7, 9, getWidth() / 2,
					getHeight() - 18, 7, 7));

			g2d.setClip(getWidth() / 2, 0, getWidth(), getHeight());
			g2d.setColor(bg);// palette.getBackground());//new Color(41, 40,
								// 42));
			g2d.fillRoundRect(getWidth() / 2 - 5, 9, getWidth() / 2,
					getHeight() - 18, 4, 4);

			g2d.setClip(0, 0, getWidth(), getHeight());

			g2d.setColor(getBackground().darker().darker());// (new
																	// Color(21,
																	// 20, 23));
			int w = getWidth() * 10 / 100;
			g2d.fillRoundRect(getWidth() / 4 - w / 4 - 2, getHeight() / 2 - 1,
					w, 2, radius - 8, radius - 8);

			// g2d.setColor(new Color(87, 238, 255));
			g2d.setColor(new Color(142, 215, 0));
			int h = (getHeight() / 3) - 4;
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(new Ellipse2D.Double(3 * getWidth() / 4 - 2 - h / 2,
					getHeight() / 2 - h / 2 - 1, h, h));

			float rad = getHeight() / 2 - 10;
			if (rad < 0)
				rad = 0;
			float dist[] = { 0.0f, 1.0f };
			Color colors[] = { new Color(142, 215, 0, 70),
					new Color(142, 215, 0, 0) };
			RadialGradientPaint rgp = new RadialGradientPaint(
					new Point2D.Double(3 * getWidth() / 4 - 2, getHeight() / 2),
					rad, dist, colors);
			g2d.setPaint(rgp);
			g2d.fillRect(getWidth() / 2, 0, getWidth() / 2, getHeight());

		}

		if (isReferenceVisible()) {
			Color referenceColor = new Color(87, 238, 255);
			if (!dark) {
				referenceColor = new Color(47, 198, 215);
			}
			if (!getReference()) {
				g2d.setColor(referenceColor);
			} else
				g2d.setColor(getBackground().darker());
			g2d.drawOval(getWidth() / 4 - 1, getHeight() - 4, 2, 2);
			if (getReference())
				g2d.setColor(referenceColor);
			else
				g2d.setColor(getBackground().darker());
			g2d.drawOval(3 * getWidth() / 4 - 3, getHeight() - 4, 2, 2);

		}
		g2d.setStroke(new BasicStroke());
		g2d.setColor(new Color(getForeground().getRed(), getForeground().getGreen(), getForeground().getBlue(),
				40));
		g2d.drawLine(getWidth() / 2, 10, getWidth() / 2, getHeight() - 10);
	}

	public void setReferenceVisible(boolean showReference_) {
		this.showReference_ = showReference_;
		repaint();
	}

	public boolean isReferenceVisible() {
		return showReference_;
	}

	public void setReference(boolean reference_) {
		if (reference_ == this.reference_)
			return;
		this.reference_ = reference_;
		if (showReference_)
			this.repaint();
	}

	public boolean getReference() {
		return reference_;
	}

	public void setSelected(boolean selected) {
		if (selected != selected_) {
			this.selected_ = selected;
			repaint();
		}
	}

	public void setEditable(boolean editable) {
		editable_ = editable;
	}

	public boolean isEditable() {
		return editable_;
	}

	public boolean isSelected() {
		return selected_;
	}

	public void addActionListener(ActionListener listenr) {
		actionListeners_.add(listenr);
	}

}
