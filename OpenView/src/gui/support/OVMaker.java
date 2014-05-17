package gui.support;

import core.maker.OVBaseMaker;
import core.support.ClassKey;
import gui.interfaces.OVContainer;

import java.awt.Point;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class OVMaker extends OVBaseMaker {

    public enum OVMakerMode {

        GUI, NODE, PROCEDURAL, NODEONLY, ARDUINO, PLOT
    }

    /**
     *
     */
    private static final long serialVersionUID = -6272300889162031511L;

    public OVMaker(Point p, OVMakerMode mode, OVContainer father) {
        super(p, father);
        initMenu(initMenu(mode));
        shuowPopup();
    }

    private JMenu[] initMenu(OVMakerMode mode) {
        if (mode == OVMakerMode.GUI) {
            return new JMenu[]{initGUI()};
        } else if (mode == OVMakerMode.NODE) {
            JMenu[] arr = new JMenu[2];
            arr[0] = initGUI();
            arr[1] = initNode();
            return arr;
        } else if (mode == OVMakerMode.NODEONLY) {
            return new JMenu[]{initNode()};
        } else if (mode == OVMakerMode.ARDUINO) {
            return new JMenu[]{ initArduino()};
        } else if (mode == OVMakerMode.PLOT) {
            return new JMenu[]{initPlot()};
        }
        return null;
    }

    private JMenu initPlot() {
        JMenu menu = new JMenu("Plots");

        JMenuItem i = new JMenuItem(ClassKey.PlotL);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        return (menu);
    }

    private JMenu initArduino() {
        JMenu menu = new JMenu("Ports");

        JMenuItem i = new JMenuItem(ClassKey.GPIOport);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        return (menu);
    }

    private JMenu initNode() {
        JMenu menu = new JMenu("Basic Node");

        JMenuItem i = new JMenuItem(ClassKey.Variable);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        JMenu submenu = new JMenu("File");

        i = new JMenuItem(ClassKey.TextFile);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        i = new JMenuItem(ClassKey.CSVFile);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        menu.add(submenu);

        submenu = new JMenu("Triggers");

        i = new JMenuItem(ClassKey.IFTrigger);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        i = new JMenuItem(ClassKey.For);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        i = new JMenuItem(ClassKey.Timer);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        menu.add(submenu);

        i = new JMenuItem(ClassKey.Pull);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Operator);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Function);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Random);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.NodeBlock);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        //
        i = new JMenuItem(ClassKey.ProceduralBlock);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Comment);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Arduino);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        //
        return (menu);
    }

    private JMenu initGUI() {
        JMenu menu = new JMenu("Basic GUI");

        JMenuItem i = new JMenuItem(ClassKey.Label);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Button);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.TextField);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.TextArea);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Check);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Spinner);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Switch);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Progress);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Container);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Plot);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(ClassKey.Gauge);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        return menu;
    }
}
