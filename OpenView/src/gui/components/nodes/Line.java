package gui.components.nodes;

import gui.components.OVComponent;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.support.OVToolTip;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.swing.JComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.support.OrientationEnum;

public class Line extends JComponent implements ComponentListener {

    /**
     *
     */
    private static final long serialVersionUID = -3278293020521128640L;
    private MouseAdapter mouseListener_ = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                click(e.getPoint(), Line.this);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                father_.showMenu(new Point(getX() + e.getX(), getY() + e.getY()));
            }
        }
    ;
    };
	public OVNode a, b;
    public OVComponent ca, cb;
    public Point p1, p2;
    private Path2D.Double path_;
    private boolean selected_ = false;
    private Double pathContainer_;
    private OVContainer father_;
    private boolean __debug;
    private Timer __timer;
    private OVToolTip __tooltip;
    private UUID uuid_;

    public Line(OVNode n, OVComponent c, OVContainer father) {
        father_ = father;
        a = n;
        ca = c;
        setLocation(father.getAbsoluteLocation(ca, a.getLocation()));
        setSize(10, 10);
        b = null;
        cb = null;
        p2 = getLocation();
        p1 = p2;
        ca.addComponentListener(this);
        this.addMouseListener(mouseListener_);
        uuid_ = UUID.randomUUID();
    }

    public Line(Element e, OVContainer father) throws LineException {
        father_ = father;
        uuid_ = UUID.fromString(e.getAttribute("uuid"));
        this.addMouseListener(mouseListener_);
        NodeList nl = e.getChildNodes();
        InNode in = null;
        OutNode out = null;
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n != null && n instanceof Element) {
                Element el = (Element) n;
                if (el.getParentNode().equals(e)) {
                    if (el.getTagName().equals(InNode.class.getSimpleName())
                            && in == null) {
                        String uuid = el.getAttribute("uuid");
                        String parent = el.getAttribute("parent");
                        in = (InNode) father.getNode(parent, uuid);
                    } else if (el.getTagName().equals(
                            OutNode.class.getSimpleName())
                            && out == null) {
                        String uuid = el.getAttribute("uuid");
                        String parent = el.getAttribute("parent");
                        out = (OutNode) father.getNode(parent, uuid);
                    }
                }
            }
        }

        if (in != null && out != null) {
            a = out;
            ca = out.getParent();
            this.addMouseListener(mouseListener_);
            b = in;
            cb = in.getParent();
            p1 = new Point();
            p2 = new Point();

            out.connect(in);
            in.connect(out);
            in.addLine(this);
            out.addLine(this);
            cb.addComponentListener(this);
            ca.addComponentListener(this);
            updateBounds();
        } else {
            throw new LineException("something in the constructor of line went seriusly bad!");
        }
    }

    public void connect(OVNode n, OVComponent c) {
        b = n;
        cb = c;

        InNode in = (a instanceof InNode ? (InNode) a : (InNode) b);
        OutNode out = (a instanceof InNode ? (OutNode) b : (OutNode) a);
        out.connect(in);

        in.connect(out);
        in.addLine(this);

        out.addLine(this);

        cb.addComponentListener(this);
        updateBounds();
    }

    public void delete() {
        this.removeMouseListener(mouseListener_);
        mouseListener_ = null;

        father_.removeLine(this);

        ca.removeComponentListener(this);

        if (cb != null) {

            cb.removeComponentListener(this);
        }
    }

    public void deconnect() {
        InNode in = (a instanceof InNode ? (InNode) a : (InNode) b);
        OutNode out = (a instanceof InNode ? (OutNode) b : (OutNode) a);
        if (in != null) {
            in.deconnect(out);
            in = null;
        }
        if (out != null) {
            out.deconnect(in);
            out = null;
        }
    }

    public void deconectInput(InNode n) {
        if (a != null && b != null) {
            if (a.equals(n)) {
                Point p = p2;
                p2 = p1;
                p1 = p;
                a = b;
                ca = cb;
                b = null;
                cb = null;
            } else {
                b = null;
                cb = null;
            }
        } else {
            delete();
        }
    }

    private void updateBounds() {
        if (b == null || cb == null || a instanceof OutNode) {
            p1 = father_.getAbsoluteLocation(ca, a.getLocation());
            if (p2.x < p1.x && a instanceof InNode && b == null && cb == null) {
                Point p = new Point(p1);
                p1 = p2;
                p2 = p;
            } else if (b != null && cb != null) {
                p2 = father_.getAbsoluteLocation(cb, b.getLocation());
            }
        } else if (b instanceof OutNode) {
            p1 = father_.getAbsoluteLocation(cb, b.getLocation());
            p2 = father_.getAbsoluteLocation(ca, a.getLocation());
        }

        path_ = new Path2D.Double();
        pathContainer_ = new Path2D.Double();
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        int dx = Math.abs(x2 - x1) / 2;
        int dy = 0;
        int t = 5;
        if (x1 > x2) {
            dy = -(y2 - y1) / 2;
        }

        path_.moveTo(x1, y1);
        path_.curveTo(x1 + dx, y1 - dy, x2 - dx, y2 + dy, x2, y2);
        pathContainer_.moveTo(x1, y1 - t);
        pathContainer_.curveTo(x1 + dx, y1 - t - dy, x2 - dx, y2 - t + dy, x2,
                y2 - t);
        pathContainer_.lineTo(x2, y2 + t);
        pathContainer_.curveTo(x2 - dx, y2 + t + dy, x1 + dx, y1 + t - dy, x1,
                y1 + t);

        pathContainer_.closePath();
        setBounds(path_.getBounds().x - 5, path_.getBounds().y - 5,
                path_.getBounds().width + 10, path_.getBounds().height + 10);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(-getX(), -getY());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3));
        if (isSelected() || __debug) {
            g2d.setColor(Color.yellow);
        } else {
            g2d.setColor(Color.lightGray);
        }

        if (path_ != null) {
            g2d.draw(path_);
        }
        g2d.setStroke(new BasicStroke());

        g2d.fillOval(p2.x - 4, p2.y - 4, 8, 8);
        g2d.fillOval(p1.x - 4, p1.y - 4, 8, 8);

        g2d.translate(getX(), getY());

    }

    public void updateP2(Point p) {
        p2 = father_.getAbsoluteLocation(ca, p);
        updateBounds();
        repaint();
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
        this.setVisible(false);
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
        updateBounds();

    }

    @Override
    public void componentResized(ComponentEvent arg0) {
        updateBounds();
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
        this.setVisible(true);

    }

    public void click(Point point, Object source) {
        if (ca.getMode() == EditorMode.NODE) {
            Point p = new Point(point);
            p.x += getX();
            p.y += getY();
            if (pathContainer_.contains(p) || path_.contains(p)) {
                father_.deselectAll();
                requestFocus();
                selected_ = true;
                repaint();
                return;
            } else {
                if (source == this || source == null) {
                    father_.clickEvent(p, this);
                }
                if (isSelected()) {
                    setSelected(false);
                    repaint();
                }
            }
        }
    }

    public boolean isSelected() {
        return selected_;
    }

    public void setSelected(boolean selected_) {
        this.selected_ = selected_;
    }

    public OVContainer getFather() {
        return father_;
    }

    public void debugDisplay(String val) {
        if (isVisible()) {
            __debug = true;
            parentRepaint();
            getFather().hideToolTip(__tooltip);
            if (val != null && val.length() > 0) {
                if (val.length() > 10) {
                    val = val.substring(0, 7) + "...";
                }
                OVNode n = getOutNode();
                if (n != null && n.visible()) {
                    Point p = new Point(n.getLocation());
                    if (!n.getParent().equals(getFather())) {
                        p.x += n.getParent().getX();
                        p.y += n.getParent().getY();
                    }
                    __tooltip = getFather().showToolTip(val, p,
                            OrientationEnum.RIGHT);
                }
            }
            if (__timer != null) {
                __timer.cancel();
                __timer.purge();
            }
            __timer = new Timer();

            __timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    __debug = false;
                    getFather().hideToolTip(__tooltip);
                    __tooltip = null;
                    parentRepaint();
                }
            }, 1000);
        }
    }

    private void parentRepaint() {
        if (getParent() != null) {
            getParent().repaint(getX(), getY(), getWidth(), getHeight());

        }
    }

    OVNode getInNode() {
        if (a instanceof InNode) {
            return a;
        } else {
            return b;
        }
    }

    OVNode getOutNode() {
        if (a instanceof OutNode) {
            return a;
        } else {
            return b;
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        if (aFlag != isVisible()) {
            super.setVisible(aFlag);
            if (__tooltip != null) {
                father_.hideToolTip(__tooltip);
                __tooltip = null;
            }
        }
    }

    public Element getXML(Document doc) {
        Element e = doc.createElement(Line.class.getSimpleName());
        e.setAttribute("uuid", uuid_.toString());
        e.appendChild(a.getXML(doc));
        e.appendChild(b.getXML(doc));
        return e;
    }

    public UUID getUUID() {
        return uuid_;
    }

}
