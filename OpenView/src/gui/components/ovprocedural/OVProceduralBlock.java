package gui.components.ovprocedural;

import gui.components.OVComponent;
import gui.components.nodes.InNode;
import gui.components.nodes.NodeGroup;
import gui.components.ovnode.OVNodeBlock;
import gui.interfaces.OVContainer;
import gui.support.OVMaker.OVMakerMode;

import java.awt.Point;

import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

public class OVProceduralBlock extends OVNodeBlock implements SlotListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3701179378439388194L;
	private static final String Trigger = "Trigger";

	private InNode trigger_;

	public OVProceduralBlock(OVContainer father) {
		super(father);
		removeInNodes();
		trigger_ = addInput(Trigger, ValueType.VOID);
		trigger_.addListener(this);
		addDynamicInNode();
	}

	private void removeInNodes() {
		for (NodeGroup g : innerNodes_) {
			removeInput(g.getInNode());
			g.getOutNode().delete();
			g.delete();
		}
		innerNodes_.clear();
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.equals(trigger_)) {
			// do something
		}
	}

	@Override
	public void showMenu(Point point) {
		super.showMenu(point, OVMakerMode.PROCEDURAL);
	}

	@Override
	public Point validate(Point p) {
		if (getSelectedComponent() == null)
			return super.validate(p);
		for (OVComponent c : components_) {
			if (!c.equals(getSelectedComponent())) {
				if (Math.abs(p.x - (c.getX() + c.getWidth())) < 15) {
					Point pp=new Point();
					pp.x=c.getX()+c.getWidth();
					pp.y=c.getY();
					return pp;
				} else if (Math.abs(c.getX()
						- (p.x + getSelectedComponent().getWidth())) < 15) {
					Point pp=new Point();
					pp.x=c.getX()-getSelectedComponent().getWidth();
					pp.y=c.getY();
					return pp;

				} else if (Math.abs(p.y - (c.getY() + c.getHeight())) < 15) {
					Point pp=new Point();
					pp.x=c.getX();
					pp.y=c.getY()+c.getHeight();
					return pp;
				} else if (Math.abs(c.getY()
						- (p.y + getSelectedComponent().getHeight())) < 15) {
					Point pp=new Point();
					pp.x=c.getX();
					pp.y=c.getY()-getSelectedComponent().getHeight();
					return pp;

				}
			}
		}

		return super.validate(p);
	}

	private OVComponent getSelectedComponent() {
		for (OVComponent c : components_)
			if (c.isSelected())
				return c;
		return null;
	}
}
