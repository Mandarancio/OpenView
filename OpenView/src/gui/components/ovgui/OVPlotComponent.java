package gui.components.ovgui;

import gui.components.OVComponent;
import gui.components.OVComponentContainer;
import gui.components.nodes.Line;
import gui.components.ovgui.plot.OVPlot;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.math.plot.Plot2DPanel;
import org.w3c.dom.Element;

import core.support.ClassKey;

public class OVPlotComponent extends OVComponentContainer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Plot2DPanel plot_;
    private JMenu menu_;

    public OVPlotComponent(OVContainer father) {
        super(father);
        this.setLayout(new BorderLayout());
        getSetting(ComponentSettings.SizeW).setValue(300);
        getSetting(ComponentSettings.SizeH).setValue(300);
        getSetting(ComponentSettings.Name).setValue("Plot");
        initPlot();
        initMenu();
    }

    public OVPlotComponent(Element e, OVContainer father) {
        super(e, father);
        this.setLayout(new BorderLayout());

        initPlot();
        initMenu();
    }

    private void initPlot() {
        plot_ = new Plot2DPanel();
        this.add(plot_, BorderLayout.CENTER);
    }

    @Override
    public void setMode(EditorMode mode) {

        if (mode != getMode()) {
            if (mode == EditorMode.GUI || mode.isExec()) {
                if (getMode() == EditorMode.NODE) {
                    hideAll();
                    this.setLayout(new BorderLayout());
                    plot_.setBounds(5, 5, getWidth() - 20, getHeight() - 20);
                    this.add(plot_, BorderLayout.CENTER);
                }
                if (getMode().isExec()) {
                    plot_.removeAllPlots();
                }
            } else {
                this.remove(plot_);
                this.setLayout(null);
                showAll();
            }
            super.setMode(mode);
        }
    }

    private void showAll() {
        for (Line l : lines_) {
            if (l.getParent() == null) {
                this.add(l);
            }
            l.setVisible(true);
        }
        for (OVComponent c : components_) {
            if (c.getParent() == null) {
                this.add(c);
            }
            c.setVisible(true);
        }
    }

    private void hideAll() {
        for (Line l : lines_) {
            this.remove(l);
        }
        for (OVComponent c : components_) {
            this.remove(c);
        }
    }

    @Override
    public void showMenu(Point point) {
        if (getMode() == EditorMode.NODE) {
            super.showCustomMenu(point, menu_); // To change body
        }																// of generated
        // methods,
        // choose Tools
        // | Templates.
    }

    @Override
    public boolean compatible(OVComponent c) {
        if (c instanceof OVPlot && getMode() == EditorMode.NODE) {
            return true;
        }
        return false;
    }

    public Plot2DPanel getPlot() {
        return plot_;
    }

    private void initMenu() {
        menu_=new JMenu("Plot");
        JMenuItem i=new JMenuItem(ClassKey.PlotL);
        i.setActionCommand(i.getText());
        menu_.add(i);
    }
}
