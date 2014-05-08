package gui.components.nodes;

import gui.components.OVComponent;
import gui.interfaces.OVNode;

import java.awt.Point;

import core.SlotInterface;
import core.ValueType;
import org.w3c.dom.Element;

public class PolyOutNode extends OutNode {

    public PolyOutNode(Point p, String label, OVComponent parent) {
        super(p, label, ValueType.VOID, parent);
        setPolyvalent(true);
    }

    public PolyOutNode(Element e, OVComponent parent) {
        super(e, parent);
    }

    @Override
    public boolean compatible(OVNode a) {
        if (this.connections_.size() == 0 && a instanceof InNode) {
            return true;
        } else {
            return super.compatible(a);
        }
    }

    @Override
    public boolean connect(SlotInterface s) {
        setType(s.getType());
        return super.connect(s);
    }
}
