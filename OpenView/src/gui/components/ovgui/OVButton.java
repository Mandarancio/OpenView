package gui.components.ovgui;

import gui.components.nodes.OutNode;
import gui.enums.DragAction;
import gui.interfaces.OVContainer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import org.w3c.dom.Element;

import core.Value;
import core.ValueType;

public class OVButton extends OVLabel {

	/**
     *
     */
	private static final long serialVersionUID = -5600444257093006064L;
	private DragAction action_;
	private OutNode signalNode_;

	public OVButton(OVContainer father) {
		super(father);
		getSetting("Text").setValue("Button");
		signalNode_ = addOutput("Click", ValueType.VOID);
	}

	public OVButton(Element e, OVContainer father) {
		super(e, father);
		for (OutNode o : outputs_) {
			if (o.getLabel().equals("Click")) {
				signalNode_ = o;
				break;
			}
		}
		if (signalNode_ == null) {
			signalNode_ = addOutput("Click", ValueType.VOID);
			System.err.println("something very wrong! (OVButton line 43)");
		}
	}

	@Override
	protected void paintOVComponent(Graphics2D g2d) {
		Color c = new Color(255, 255, 255, 100);
		if (getMode().isExec()) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			c = getBackground();
			if (action_ == DragAction.PRESSED) {
				c = c.darker();
			} else if (over_) {
				c = c.brighter();
			}
			if (isEnabled()) {
				g2d.setColor(c);
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
				if (action_ == DragAction.PRESSED) {
					c = new Color(0, 0, 0, 100);
				} else {
					c = new Color(255, 255, 255, 100);
				}
			}
			g2d.setColor(Color.lightGray.darker());
			g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

		}
		super.paintOVComponent(g2d);
		if (isEnabled()) {
			GradientPaint gradient = new GradientPaint(0f, 0f, c, 0.0f,
					getHeight() / 3.0f, new Color(c.getRed(), c.getGreen(),
							c.getBlue(), 0));
			g2d.setPaint(gradient);
			g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
		}
	}

	@Override
	public DragAction drag(Point point) {
		action_ = super.drag(point);
		repaint();
		return action_;
	}

	@Override
	public void drop(DragAction dragAction_) {
		super.drop(dragAction_);
		if (getMode().isExec() && action_ == DragAction.PRESSED) {
			signalNode_.trigger(new Value(Void.TYPE));
		}
		action_ = DragAction.NOTHING;
		repaint();
	}
}
