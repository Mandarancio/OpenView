package gui.components.nodes;

import gui.components.OVComponent;

import java.awt.Point;

import core.ValueType;

public class PolyInNode extends InNode{

	public PolyInNode(Point p, String label,  OVComponent parent) {
		super(p, label, ValueType.VOID, parent);
	
	}

}
