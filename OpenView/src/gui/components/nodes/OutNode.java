package gui.components.nodes;

import gui.components.OVComponent;
import gui.enums.EditorMode;
import gui.interfaces.NodeListener;
import gui.interfaces.OVNode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import core.Emitter;
import core.SlotInterface;
import core.Value;
import core.ValueType;
import java.util.UUID;

public class OutNode extends Emitter implements OVNode {

    final static public int radius = 4;
    private Point location_;
    public boolean over = false;
    private boolean hidden_;
    private OVComponent parent_;
    private ArrayList<NodeListener> nListeners_ = new ArrayList<>();
    private ArrayList<Line> lines_ = new ArrayList<>();
    private UUID uuid_;

    public OutNode(Point p, String label, ValueType type, OVComponent parent) {
        super(label, type);
        parent_ = (parent);
        location_ = p;
        uuid_ = UUID.randomUUID();
    }

    public boolean contains(Point p) {
        int dx = p.x - location_.x, dy = p.y - location_.y;
        double d = Math.sqrt(dx * dx + dy * dy);
        return (d <= radius);
    }

    public void paint(Graphics2D g) {
        if (hidden_) {
            g.setColor(getType().getColor().darker());
        } else {
            g.setColor(getType().getColor());
        }
        g.fillOval(location_.x - radius, location_.y - radius, radius * 2,
                radius * 2);
        g.setColor(Color.black);
        g.drawOval(location_.x - radius, location_.y - radius, radius * 2,
                radius * 2);
    }

    public void setLocation(Point p) {
        location_ = p;
    }

    public Point getLocation() {
        return location_;
    }

    @Override
    public boolean compatible(OVNode a) {
        return (a instanceof InNode)
                && (((InNode) a).isCompatible(this) && !a.getParent().equals(
                        parent_));
    }

    public Element getXML(Document doc) {
        Element e = doc.createElement(getClass().getSimpleName());
        e.setAttribute("uuid", uuid_.toString());
        e.setAttribute("label",getLabel());
        e.setAttribute("type", getType().toString());
        
        return e;
    }

    public void delete() {
        for (Line l : lines_) {
            l.delete();
        }
        lines_.clear();
        for (SlotInterface s : connections_) {
            if (s instanceof InNode) {
                if (((InNode) s).getLine(this) != null) {
                    ((InNode) s).getLine(this).delete();
                }
            }
            s.deconnect(this);
        }
        parent_ = null;
        connections_.clear();
    }

    @Override
    public void addNodeListener(NodeListener l) {
        nListeners_.add(l);
    }

    @Override
    public void removeNodeListener(NodeListener l) {
        nListeners_.remove(l);
    }

    private void trigConn() {
        for (NodeListener l : nListeners_) {
            l.connected(this);
        }
    }

    private void trigDeconn() {
        for (NodeListener l : nListeners_) {
            l.deconneced(this);
        }
    }

    @Override
    public void hide() {
        hidden_ = true;
    }

    @Override
    public void show() {
        hidden_ = false;
    }

    @Override
    public OVComponent getParent() {
        return parent_;
    }

    @Override
    public boolean connect(SlotInterface s) {
        if (isPolyvalent()) {
            setType(s.getType());
            boolean flag = super.connect(s);
            if (flag) {
                trigConn();
            }
            return flag;
        }
        boolean flag = super.connect(s);
        if (flag) {
            trigConn();
        }
        return flag;
    }

    @Override
    public boolean deconnect(SlotInterface s) {
        Line l = getLine((OVNode) s);
        if (l != null) {
            lines_.remove(l);
        }

        if (isPolyvalent() && connections_.size() == 0) {
            setType(ValueType.VOID);
        }
        boolean flag = super.deconnect(s);
        if (flag) {
            trigDeconn();
        }
        return flag;
    }

    public void setPolyvalent(boolean poly) {
        super.setPolyvalent(poly);
    }

    @Override
    public void addLine(Line l) {
        lines_.add(l);
    }

    @Override
    public Line getLine(OVNode n) {
        for (Line l : lines_) {
            if (l.a.equals(n) || l.b.equals(n)) {
                return l;
            }
        }
        return null;
    }

    @Override
    public boolean visible() {
        return !hidden_;
    }

    @Override
    public void trigger(Value v) {
        super.trigger(v);
        if (parent_ != null && parent_.getMode() == EditorMode.DEBUG) {
            for (Line l : lines_) {
                if (v.getType() == ValueType.VOID
                        || v.getType() == ValueType.NONE) {
                    l.debugDisplay("");
                } else {
                    l.debugDisplay(v.getString());
                }
            }
        }
    }

    public void deconnectAll() {
        ArrayList<SlotInterface> connections = new ArrayList<>(connections_);
        for (SlotInterface s : connections) {
            deconnect(s);
        }
        connections.clear();
    }

    public UUID getUUID() {
        return uuid_;
    }
}
