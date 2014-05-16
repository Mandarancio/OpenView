package gui.support;

import gui.components.OVComponent;
import gui.components.OVComponentContainer;
import gui.components.ovgui.OVButton;
import gui.components.ovgui.OVCheckBox;
import gui.components.ovgui.OVGauge;
import gui.components.ovgui.OVLabel;
import gui.components.ovgui.OVPlotComponent;
import gui.components.ovgui.OVTextArea;
import gui.components.ovgui.OVTextField;
import gui.components.ovgui.plot.OVPlot;
import gui.components.ovnode.OVCSVFile;
import gui.components.ovnode.OVComment;
import gui.components.ovnode.OVForTrigger;
import gui.components.ovnode.OVFunctionNode;
import gui.components.ovnode.OVIFTriggerNode;
import gui.components.ovnode.OVNodeBlock;
import gui.components.ovnode.OVOperatorNode;
import gui.components.ovnode.OVPullNode;
import gui.components.ovnode.OVRandomNode;
import gui.components.ovnode.OVTextFile;
import gui.components.ovnode.OVTimerTriggerNode;
import gui.components.ovnode.OVVariableNode;
import gui.components.ovnode.arduino.OVArduBlock;
import gui.components.ovnode.arduino.OVArduDigitalPort;
import gui.components.ovprocedural.OVProceduralBlock;
import gui.interfaces.OVContainer;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class OVMaker extends JPopupMenu implements ActionListener {

    public enum OVMakerMode {

        GUI, NODE, PROCEDURAL, NODEONLY, ARDUINO, PLOT
    }

    /**
     *
     */
    private static final long serialVersionUID = -6272300889162031511L;
    private static final String Timer = "Timer", Pull = "Pull",
            Variable = "Variable", Operator = "Operator",
            Function = "Function", NodeBlock = "Block", Comment = "Comment",
            TextFile = "Text File", IFTrigger = "IF trigger",
            Random = "Random", For = "For trigger", CSVFile = "CSV File";
    private static final String Label = "Label", Button = "Button",
            TextArea = "Text Area", TextField = "Text Field",
            Container = "Container", Gauge = "Gauge", Arduino = "Arduino", PlotL="Plot";
    private static final String GPIOport = "GPIO";
    private static final String ProceduralBlock = "Procedural block";
    private static final String Check = "Check box";
    private static final String Plot = "Plot component";
    private OVContainer father_;
    private Point point_;

    public OVMaker(Point p, OVMakerMode mode, OVContainer father) {
        father_ = father;
        point_ = p;
        this.initMenu(mode);
        this.show((JComponent) father, p.x, p.y);
    }

    private void initMenu(OVMakerMode mode) {
        if (mode == OVMakerMode.GUI) {
            initGUI();
        } else if (mode == OVMakerMode.NODE) {
            initGUI();
            initNode();
        } else if (mode == OVMakerMode.NODEONLY) {
            initNode();
        } else if (mode == OVMakerMode.ARDUINO) {
            initArduino();
        } else if (mode == OVMakerMode.PLOT){
        	initPlot();
        }
    }
    private void initPlot(){
    	JMenu menu = new JMenu("Plots");

        JMenuItem i = new JMenuItem(PlotL);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        add(menu);
    }
    private void initArduino() {
        JMenu menu = new JMenu("Ports");

        JMenuItem i = new JMenuItem(GPIOport);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        add(menu);
    }

    private void initNode() {
        JMenu menu = new JMenu("Basic Node");

        JMenuItem i = new JMenuItem(Variable);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        JMenu submenu = new JMenu("File");

        i = new JMenuItem(TextFile);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        i = new JMenuItem(CSVFile);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        menu.add(submenu);

        submenu = new JMenu("Triggers");

        i = new JMenuItem(IFTrigger);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        i = new JMenuItem(For);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        i = new JMenuItem(Timer);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        submenu.add(i);

        menu.add(submenu);

        i = new JMenuItem(Pull);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Operator);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Function);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Random);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(NodeBlock);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);
        //
        i = new JMenuItem(ProceduralBlock);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Comment);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Arduino);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        //
        add(menu);
    }

    private void initGUI() {
        JMenu menu = new JMenu("Basic GUI");

        JMenuItem i = new JMenuItem(Label);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Button);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(TextField);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(TextArea);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Check);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Plot);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Gauge);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        i = new JMenuItem(Container);
        i.setActionCommand(i.getText());
        i.addActionListener(this);
        menu.add(i);

        add(menu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals(Button)) {
            create(new OVButton(father_));
        } else if (cmd.equals(Label)) {
            create(new OVLabel(father_));
        } else if (cmd.equals(TextField)) {
            create(new OVTextField(father_));
        } else if (cmd.equals(Variable)) {
            create(new OVVariableNode(father_));
        } else if (cmd.equals(Timer)) {
            create(new OVTimerTriggerNode(father_));
        } else if (cmd.equals(Pull)) {
            create(new OVPullNode(father_));
        } else if (cmd.equals(Container)) {
            create(new OVComponentContainer(father_));
        } else if (cmd.equals(Operator)) {
            create(new OVOperatorNode(father_));
        } else if (cmd.equals(TextArea)) {
            create(new OVTextArea(father_));
        } else if (cmd.equals(NodeBlock)) {
            create(new OVNodeBlock(father_));
        } else if (cmd.equals(IFTrigger)) {
            create(new OVIFTriggerNode(father_));
        } else if (cmd.equals(ProceduralBlock)) {
            create(new OVProceduralBlock(father_));
            // create(new OVScalarBlock(father_));
        } else if (cmd.equals(Check)) {
            create(new OVCheckBox(father_));
        } else if (cmd.equals(Random)) {
            create(new OVRandomNode(father_));
        } else if (cmd.equals(Function)) {
            create(new OVFunctionNode(father_));
        } else if (cmd.equals(For)) {
            create(new OVForTrigger(father_));
        } else if (cmd.equals(Plot)) {
            create(new OVPlotComponent(father_));
        } else if (cmd.equals(Comment)) {
            create(new OVComment(father_));
        } else if (cmd.equals(TextFile)) {
            create(new OVTextFile(father_));
        } else if (cmd.equals(CSVFile)) {
            create(new OVCSVFile(father_));
        } else if (cmd.equals(Gauge)) {
            create(new OVGauge(father_));
        } else if (cmd.equals(Arduino)) {
            create(new OVArduBlock(father_));
        } else if (cmd.equals(GPIOport)) {
            create(new OVArduDigitalPort(father_));
        } else if (cmd.equals(PlotL)){
        	create(new OVPlot(father_));
        }
    }

    private void create(OVComponent c) {
        Point p = father_.validate(point_);
        c.moveTo(p.x, p.y);
        father_.addComponent(c);

        this.setVisible(false);
        father_ = null;
    }

}
