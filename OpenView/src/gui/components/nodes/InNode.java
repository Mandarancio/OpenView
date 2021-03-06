package gui.components.nodes;

import gui.components.OVComponent;
import gui.interfaces.NodeListener;
import gui.interfaces.OVNode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import core.EmitterInterface;
import core.Slot;
import core.ValueType;
import core.support.Utils;

import java.util.UUID;

public class InNode extends Slot implements OVNode {

    final static public int radius = 4;
    private Point location_;
    public boolean over = false;
    private boolean polymorf_;
    private ArrayList<OVLine> lines_ = new ArrayList<>();
    private ArrayList<NodeListener> nListeners_ = new ArrayList<>();
    private boolean hidden_;
    private OVComponent parent_;
    private UUID uuid_;

    public InNode(Point p, String label, ValueType type, OVComponent parent) {
        super(label, type);
        if (type == ValueType.VOID) {
            polymorf_ = true;
        }
        location_ = p;
        parent_ = parent;
        uuid_ = UUID.randomUUID();
    }

    public InNode(Element el, OVComponent parent) {
        super(el.getAttribute("label"), ValueType.valueOf(el
                .getAttribute("type")));
        uuid_ = UUID.fromString(el.getAttribute("uuid"));
        parent_ = parent;
        location_ = Utils.parsePoint(el.getAttribute("location"));
        polymorf_ = Boolean.parseBoolean(el.getAttribute("polyvalent"));
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

        return (a instanceof OutNode && isCompatible((OutNode) a) && !a
                .getParent().equals(parent_));
    }

    public void addLine(OVLine line) {
        lines_.add(line);

    }

    public OVLine getLine(OVNode n) {
        for (OVLine l : lines_) {
            if (l.a.equals(n) || l.b.equals(n)) {
                return l;
            }
        }
        return null;
    }

    public void delete() {
        for (OVLine l : lines_) {
            l.delete();
        }
        lines_.clear();
        deconnectAll();
        listeners_.clear();
    }

    @Override
    public boolean deconnect(EmitterInterface e) {
        OVLine l = getLine((OVNode) e);
        if (l != null) {
            lines_.remove(l);
        }

        if (polymorf_ && getConnections().size()==1) {
            setType(ValueType.VOID);
        }
        boolean flag = super.deconnect(e);
        if (flag) {
            trigDeconn();
        }
        return flag;
    }

    public Element getXML(Document doc) {
        Element e = doc.createElement(InNode.class.getSimpleName());
        e.setAttribute("uuid", uuid_.toString());
        e.setAttribute("label", getLabel());
        e.setAttribute("type", getType().toString());
        e.setAttribute("parent", parent_.getUUID().toString());
        e.setAttribute("location", Utils.codePoint(location_));
        e.setAttribute("polyvalent", Boolean.toString(polymorf_));
        return e;
    }

    @Override
    public boolean connect(EmitterInterface e) {
        if (polymorf_) {
            setType(e.getType());
            boolean flag = super.connect(e);
            if (flag) {
                trigConn();
            }
            return flag;
        }
        boolean flag = super.connect(e);
        if (flag) {
            trigConn();
        }
        return flag;
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
    public boolean visible() {
        return !hidden_;
    }

    public UUID getUUID() {
        return uuid_;
    }
}
