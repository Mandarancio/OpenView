package gui.components.ovnode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import core.Setting;
import core.support.OrientationEnum;
import core.support.Utils;
import gui.components.OVComponent;
import gui.components.OVComponentContainer;
import gui.components.nodes.Line;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.support.OVMaker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OVNodeBlock extends OVComponentContainer {

    /**
     *
     */
    private static final long serialVersionUID = -2403414123570859932L;
    private boolean expand_ = true;
    private Dimension oldSize_;

    public OVNodeBlock(OVContainer father) {
        super(father);
        getSetting(ComponentSettings.Name).setValue("Block");
        __minY = 20;
        updateNodes();
        this.setBackground(new Color(0, 0, 0, 180));
        for (Setting s : getSettings()) {
            s.setNodeMode(false);
        }
        oldSize_ = new Dimension(getSize());
        expand_ = true;
    }

    public OVNodeBlock(Element e, OVContainer father) {
        super(e, father);

        __minY = 20;

        expand_ = Boolean.parseBoolean(e.getAttribute("expand"));
        oldSize_ = Utils.parseDimension(e.getAttribute("size"));

        this.setBackground(new Color(0, 0, 0, 180));

        if (!expand_) {
            comprime();
        }
    }

    @Override
    public void setMode(EditorMode mode_) {
        super.setMode(mode_);
        if (mode_ != EditorMode.NODE && mode_ != EditorMode.DEBUG) {
            setVisible(false);
        } else {

            setVisible(true);
            if (!expand_) {
                comprime();
            }
        }
    }

    @Override
    protected void paintOVNode(Graphics2D g) {
        if (isExpand()) {

            g.setColor(getForeground());

            paintText(getName(), g, new Rectangle(6, 3, getWidth() - 20, 15),
                    OrientationEnum.LEFT);
            g.drawLine(0, 20, getWidth(), 20);
            g.setFont(getFont().deriveFont(10.0f));
        } else {
            super.paintOVNode(g);
        }
    }

    @Override
    protected void paintOVComponent(Graphics2D g2d) {
        paintOVNode(g2d);
    }

    @Override
    protected void paintInnerNodes(Graphics2D g2d) {
        if (isExpand()) {
            super.paintInnerNodes(g2d);
        }
    }

    @Override
    public void doubleClick(Point point) {
        if (isExpand() && point.y <= 20) {
            comprime();
        } else if (!isExpand()) {
            expand();
        }
        super.doubleClick(point);

    }

    private void expand() {
        expand_ = true;
        for (OVComponent c : components_) {
            c.setVisible(true);
        }

        for (Line l : lines_) {
            l.setVisible(true);
        }
        __minY = 20;
        setSize(oldSize_);
        getSetting(ComponentSettings.SizeW).setValue(oldSize_.width);
        getSetting(ComponentSettings.SizeH).setValue(oldSize_.height);
        resizable_ = true;
        repaint();
    }

    private void comprime() {
        if (expand_) {
            oldSize_ = new Dimension(getSize());
        }

        expand_ = false;
        for (OVComponent c : components_) {
            c.setVisible(false);
        }

        for (Line l : lines_) {
            l.setVisible(false);
        }
        __minY = 0;

        getSetting(ComponentSettings.SizeW).setValue(60);
        getSetting(ComponentSettings.SizeH).setValue(60);
        resizable_ = false;
        repaint();
    }

    public boolean isExpand() {
        return expand_;
    }

    @Override
    public Line createLine(OVNode n, OVComponent ovComponent) {
        if (expand_) {
            return super.createLine(n, ovComponent);
        }
        return null;
    }

    @Override
    public void showMenu(Point point) {
        super.showMenu(point, OVMaker.OVMakerMode.NODEONLY); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean compatible(OVComponent c) {
        if (c instanceof OVNodeBlock || c instanceof OVNodeComponent) {
            return true;
        }
        return false;
    }

    @Override
    public Element getXML(Document doc) {
        Element e = super.getXML(doc);
        e.setAttribute("expand", Boolean.toString(expand_));
        e.setAttribute("size", Utils.codeDimension(oldSize_));
        return e; //To change body of generated methods, choose Tools | Templates.
    }

}
