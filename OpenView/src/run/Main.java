package run;

import core.maker.OVClassFactory;
import core.maker.OVClassManager;
import core.support.ClassKey;
import gui.Window;
import gui.components.ovgui.OVButton;
import gui.components.ovgui.OVCheckBox;
import gui.components.ovgui.OVGauge;
import gui.components.ovgui.OVLabel;
import gui.components.ovgui.OVPlotComponent;
import gui.components.ovgui.OVProgressBar;
import gui.components.ovgui.OVSpinner;
import gui.components.ovgui.OVSwitcher;
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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void init() {
        OVClassManager m = OVClassFactory.getManager();
        m.addClass(ClassKey.Button, OVButton.class);
        m.addClass(ClassKey.Check, OVCheckBox.class);
        m.addClass(ClassKey.Progress, OVProgressBar.class);
        m.addClass(ClassKey.Label, OVLabel.class);
        m.addClass(ClassKey.Gauge, OVGauge.class);
        m.addClass(ClassKey.TextArea, OVTextArea.class);
        m.addClass(ClassKey.TextField, OVTextField.class);
        m.addClass(ClassKey.Switch, OVSwitcher.class);
        m.addClass(ClassKey.Spinner, OVSpinner.class);

        m.addClass(ClassKey.Plot, OVPlotComponent.class);
        m.addClass(ClassKey.PlotL, OVPlot.class);

        m.addClass(ClassKey.Container, OVContainer.class);

        m.addClass(ClassKey.Variable, OVVariableNode.class);
        m.addClass(ClassKey.Operator, OVOperatorNode.class);
        m.addClass(ClassKey.Function, OVFunctionNode.class);
        m.addClass(ClassKey.For, OVForTrigger.class);
        m.addClass(ClassKey.Timer, OVTimerTriggerNode.class);
        m.addClass(ClassKey.IFTrigger, OVIFTriggerNode.class);
        m.addClass(ClassKey.Comment, OVComment.class);
        m.addClass(ClassKey.Random, OVRandomNode.class);
        m.addClass(ClassKey.Pull, OVPullNode.class);
        m.addClass(ClassKey.TextFile, OVTextFile.class);
        m.addClass(ClassKey.CSVFile, OVCSVFile.class);
        m.addClass(ClassKey.ProceduralBlock, OVProceduralBlock.class);

        m.addClass(ClassKey.NodeBlock, OVNodeBlock.class);
    }

    public static void main(String[] args) {
        init();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        new Window();
    }
}
