package gui;

import gui.adapters.ContainerMouseAdapter;
import gui.components.OVComponent;
import gui.components.nodes.Line;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.support.OVMaker;
import gui.support.OVMaker.OVMakerMode;
import gui.support.OVToolTip;
import gui.support.XMLParser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.support.OrientationEnum;

public class EditorPanel extends JLayeredPane implements OVContainer,
        KeyListener {

    /**
     *
     */
    private static final long serialVersionUID = 1075006905710700282L;
    private static final int GridStep = 15;
    private ArrayList<OVComponent> components_ = new ArrayList<>();
    private ArrayList<Line> lines_ = new ArrayList<>();
    private RightPanel rightPanel_;
    private ContainerMouseAdapter mouseAdapter_;
    private boolean gridVisible_ = false;
    private boolean gridEnable_ = false;

    private ObjectManager objectManager_;
    private EditorMode mode_ = EditorMode.GUI;

    public EditorPanel(RightPanel panel) {
        this.objectManager_ = panel.getManager();
        this.setLayout(null);
        this.setBackground(new Color(69, 70, 64));
        this.setForeground(new Color(200, 200, 200));
        this.rightPanel_ = panel;
        this.mouseAdapter_ = new ContainerMouseAdapter(this);
        this.addMouseListener(mouseAdapter_);
        this.addKeyListener(this);
    }

    private void setKeyListener(OVComponent c) {
        KeyListener[] list = c.getKeyListeners();
        if (list != null && list.length > 0) {
            for (KeyListener l : list) {
                if (l.equals(this)) {
                    return;
                }
            }
        }
        c.addKeyListener(this);
    }

    @Override
    public void addComponent(OVComponent c) {
        boolean addFlag = false;

        for (OVComponent ovc : components_) {
            if (!ovc.equals(c) && ovc.isContainer()) {
                if (((OVContainer) ovc).contains(c)
                        && ((OVContainer) ovc).compatible(c)) {
                    ((OVContainer) ovc).addComponent(c);
                    addFlag = true;
                    break;
                }
            }
        }
        if (this.compatible(c) || addFlag) {
            objectManager_.addComponent(c);
            setKeyListener(c);
            if (!addFlag) {
                components_.add(c);
                this.add(c);
                select(c);
                c.setMode(mode_);
            }
        }
    }

    @Override
    public void removeComponent(OVComponent c) {
        objectManager_.removeComponent(c);
        if (components_.contains(c)) {
            rightPanel_.deselect();
            c.delete();
            components_.remove(c);
            this.remove(c);
        }
    }

    @Override
    public void select(OVComponent c) {
        deselectAll();
        c.setSelected(true);
        if (c.getParent().equals(this)) {
            this.moveToFront(c);
        }
        rightPanel_.select(c);
        rightPanel_.repaint();
    }

    @Override
    public void deselect(OVComponent c) {
        c.setSelected(false);
        rightPanel_.deselect();
    }

    @Override
    public void deselectAll() {
        for (OVComponent c : components_) {
            if (c.isSelected()) {
                c.setSelected(false);
                c.repaint();
            }
            if (c.isContainer()) {
                ((OVContainer) c).deselectAll();
            }
        }
        for (Line l : lines_) {
            if (l.isSelected()) {
                l.setSelected(false);
                l.repaint();
            }
        }
        rightPanel_.deselect();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getBackground().brighter());
        if (isGridVisible() && mode_ != EditorMode.RUN) {
            for (int x = GridStep; x < getWidth(); x += GridStep) {
                g.drawLine(x, 0, x, getHeight());
            }
            for (int y = GridStep; y < getHeight(); y += GridStep) {
                g.drawLine(0, y, getWidth(), y);
            }
        }
    }

    public boolean isGridVisible() {
        return gridVisible_;
    }

    public void setGridVisible(boolean gridVisible_) {
        this.gridVisible_ = gridVisible_;
    }

    public boolean isGridEnabled() {
        return gridEnable_;
    }

    public void setGridEnabled(boolean gridEnable_) {
        this.gridEnable_ = gridEnable_;
    }

    @Override
    public Point validate(Point p) {
        if (isGridEnabled()) {
            int dx = p.x % GridStep;
            if (dx >= GridStep / 2.0) {
                dx = (-GridStep + dx);
            }
            int dy = p.y % GridStep;
            if (dy >= GridStep / 2.0) {
                dy = (-GridStep + dy);
            }
            return new Point(p.x - dx, p.y - dy);
        }
        return p;
    }

    @Override
    public Dimension validate(Dimension d) {
        if (isGridEnabled()) {
            int dx = d.width % GridStep;
            if (dx >= GridStep / 2.0) {
                dx = (-GridStep + dx);
            }
            int dy = d.height % GridStep;
            if (dy >= GridStep / 2.0) {
                dy = (-GridStep + dy);
            }
            return new Dimension(d.width - dx, d.height - dy);
        }
        return d;
    }

    public EditorMode getMode() {
        return mode_;
    }

    public void setMode(EditorMode mode) {
        if (this.mode_ != mode) {
            this.mode_ = mode;
            deselectAll();
            rightPanel_.setMode(mode);
            for (OVComponent component : components_) {
                component.setMode(mode);
            }
            if (mode_ == EditorMode.NODE || mode_ == EditorMode.DEBUG) {
                for (Line l : lines_) {
                    l.setVisible(true);
                }
            } else {
                for (Line l : lines_) {
                    l.setVisible(false);
                }
            }
            clearToolTip();
            repaint();
            requestFocus();
            rightPanel_.repaint();

        }
    }

    @Override
    public OVToolTip showToolTip(String tooltip, Point p,
            OrientationEnum orientation) {
        System.out.println(p);
        OVToolTip toolTip = new OVToolTip(tooltip, p, orientation, getFont());
        this.add(toolTip);
        this.moveToFront(toolTip);
        return toolTip;
    }

    @Override
    public void hideToolTip(OVToolTip toolTip) {
        if (toolTip != null) {
            this.remove(toolTip);
            this.repaint(toolTip.getBounds());

        }
    }

    @Override
    public Point getAbsoluteLocation(OVComponent c, Point location) {
        return SwingUtilities.convertPoint(c, location, this);
    }

    @Override
    public Line createLine(OVNode n, OVComponent ovComponent) {
        Line l = new Line(n, ovComponent, this);
        lines_.add(l);
        this.add(l);
        this.moveToBack(l);
        l.setSelected(true);

        for (OVComponent c : components_) {
            c.hideNodes(n);
        }
        return l;
    }

    @Override
    public void confirmLine(Line l) {
        for (OVComponent c : components_) {
            c.showNodes();
        }
        if (l == null) {
            return;
        }

        Point p = l.p2;

        if (l.a.getLocation().x + l.ca.getX() == p.x) {
            p = l.p1;
        }

        for (OVComponent c : components_) {
            if (c.isVisible() && c.getBounds().contains(p)) {
                OVNode n = c.getNode(SwingUtilities.convertPoint(this, p, c));
                if (n != null && n.compatible(l.a) && l.a.compatible(n)) {
                    l.connect(n, c);
                    l.setSelected(false);
                    l.addKeyListener(this);
                    return;
                }
            }
        }
        this.remove(l);
        this.repaint(l.getBounds());
        l.delete();
        lines_.remove(l);
        return;
    }

    @Override
    public void removeLine(Line line) {
        lines_.remove(line);
        this.remove(line);
        this.repaint(line.getBounds());
    }

    @Override
    public void keyPressed(KeyEvent arg0) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (mode_ != EditorMode.RUN) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {

                removeSelected();
            }
        }
    }

    public void removeSelected() {

        ArrayList<OVComponent> list = new ArrayList<>();
        for (OVComponent c : components_) {
            if (c.isSelected()) {
                list.add(c);
            }
            if (c.isContainer()) {
                ((OVContainer) c).removeSelected();
            }
        }
        for (OVComponent c : list) {
            repaint(c.getBounds());
            c.getFather().removeComponent(c);
        }
        ArrayList<Line> ll = new ArrayList<>(lines_);
        for (Line l : ll) {
            if (l.isSelected()) {
                l.getFather().removeLine(l);
                l.deconnect();
                l.removeKeyListener(this);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void showMenu(Point point) {
        showMenu(point, (mode_ == EditorMode.GUI ? OVMakerMode.GUI
                : OVMakerMode.NODE));
    }

    @Override
    public void showMenu(Point p, OVMakerMode mode) {
        new OVMaker(p, mode, this);

    }

    @Override
    public OVContainer parent() {
        return null;
    }

    @Override
    public OVContainer superParent() {
        return this;
    }

    @Override
    public boolean contains(OVComponent c) {
        return true;
    }

    @Override
    public void clickEvent(Point p, Object source) {
        for (Line l : lines_) {
            if (!l.equals(source) && l.getBounds().contains(p)) {
                l.click(new Point(p.x - l.getX(), p.y - l.getY()), source);

            }
        }
    }

    private void clearToolTip() {
        Component[] cc = getComponents();
        for (Component c : cc) {
            if (c instanceof OVToolTip) {
                hideToolTip((OVToolTip) c);
            }
        }
    }

    @Override
    public boolean compatible(OVComponent c) {

        return true;
    }

    public Element getXML(Document doc) {
        Element e = doc.createElement(EditorPanel.class.getSimpleName());
        for (OVComponent c : components_) {
            e.appendChild(c.getXML(doc));
        }
        for (Line l : lines_) {
            e.appendChild(l.getXML(doc));
        }
        return e;
    }

    public void loadXML(Element el) {
        clearAll();
        NodeList nl = el.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n != null && n instanceof Element) {
                Element e = (Element) n;
                if (e.getParentNode().equals(el)) {
                    if (!e.getTagName().equals(Line.class.getSimpleName())) {
                        XMLParser.loadComponent(e, this);
                    }
                }
            }
        }

        nl = el.getElementsByTagName(Line.class.getSimpleName());
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n != null && n instanceof Element) {
                Element e = (Element) n;
                if (e.getParentNode().equals(el)) {
                    Line l = XMLParser.parseLine(e, this);
                    lines_.add(l);
                    this.add(l);
                    this.moveToBack(l);
                    l.setSelected(false);
                    l.addKeyListener(this);
                    if (mode_ == EditorMode.RUN || mode_ == EditorMode.GUI) {
                        l.setVisible(false);
                    }
                }
            }
        }
    }

    public void clearAll() {
        for (Line l : lines_) {
            l.delete();
            remove(l);
        }
        for (OVComponent c : components_) {
            c.delete();
            remove(c);
        }
        components_.clear();
        lines_.clear();
        removeAll();
        repaint();
    }

    @Override
    public OVNode getNode(String parent, String uuid) {
        for (OVComponent c : components_) {
            if (c.getUUID().toString().equals(parent)) {
                return c.getNode(uuid);
            }
        }
        return null;
    }

    @Override
    public ObjectManager getObjectManager() {
        return objectManager_;
    }

}
