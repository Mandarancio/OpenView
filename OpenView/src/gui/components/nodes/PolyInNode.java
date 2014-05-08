package gui.components.nodes;

import gui.components.OVComponent;

import java.awt.Point;

import core.ValueType;
import org.w3c.dom.Element;

public class PolyInNode extends InNode {

    public PolyInNode(Point p, String label, OVComponent parent) {
        super(p, label, ValueType.VOID, parent);

    }

    public PolyInNode(Element e, OVComponent parent) {
        super(e, parent);
    }
}
