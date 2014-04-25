package gui.components.ovprocedural;

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
		super.showMenu(point,OVMakerMode.PROCEDURAL);
	}
}
