package gui.support;

import gui.components.OVComponent;
import gui.components.OVComponentContainer;
import gui.components.nodes.Line;
import gui.components.ovgui.OVButton;
import gui.components.ovgui.OVCheckBox;
import gui.components.ovgui.OVGauge;
import gui.components.ovgui.OVLabel;
import gui.components.ovgui.OVPlotComponent;
import gui.components.ovgui.OVProgressBar;
import gui.components.ovgui.OVSpinner;
import gui.components.ovgui.OVTextArea;
import gui.components.ovgui.OVTextField;
import gui.components.ovgui.plot.OVPlot;
import gui.components.ovnode.OVCSVFile;
import gui.components.ovnode.OVComment;
import gui.components.ovnode.OVForTrigger;
import gui.components.ovnode.OVFunctionNode;
import gui.components.ovnode.OVIFTriggerNode;
import gui.components.ovnode.OVNodeBlock;
import gui.components.ovnode.OVNodeComponent;
import gui.components.ovnode.OVOperatorNode;
import gui.components.ovnode.OVPullNode;
import gui.components.ovnode.OVRandomNode;
import gui.components.ovnode.OVTextFile;
import gui.components.ovnode.OVTimerTriggerNode;
import gui.components.ovnode.OVVariableNode;
import gui.components.ovnode.arduino.OVArduBlock;
import gui.components.ovnode.arduino.OVArduDigitalPort;
import gui.components.ovprocedural.OVProceduralBlock;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import org.w3c.dom.Element;

public class XMLParser {

	static public OVComponent parseElement(Element e, OVContainer father) {
		String name = e.getTagName();
		OVComponent c = null;
		if (name.equals(OVLabel.class.getSimpleName())) {
			c = new OVLabel(e, father);
		} else if (name.equals(OVButton.class.getSimpleName())) {
			c = new OVButton(e, father);
		} else if (name.equals(OVCheckBox.class.getSimpleName())) {
			c = new OVCheckBox(e, father);
		} else if (name.equals(OVTextArea.class.getSimpleName())) {
			c = new OVTextArea(e, father);
		} else if (name.equals(OVTextField.class.getSimpleName())) {
			c = new OVTextField(e, father);
		} else if (name.equals(OVPlotComponent.class.getSimpleName())) {
			c = new OVPlotComponent(e, father);
		} else if (name.equals(OVVariableNode.class.getSimpleName())) {
			c = new OVVariableNode(e, father);
		} else if (name.equals(OVOperatorNode.class.getSimpleName())) {
			c = new OVOperatorNode(e, father);
		} else if (name.equals(OVFunctionNode.class.getSimpleName())) {
			c = new OVFunctionNode(e, father);
		} else if (name.equals(OVTimerTriggerNode.class.getSimpleName())) {
			c = new OVTimerTriggerNode(e, father);
		} else if (name.equals(OVForTrigger.class.getSimpleName())) {
			c = new OVForTrigger(e, father);
		} else if (name.equals(OVIFTriggerNode.class.getSimpleName())) {
			c = new OVIFTriggerNode(e, father);
		} else if (name.equals(OVPullNode.class.getSimpleName())) {
			c = new OVPullNode(e, father);
		} else if (name.equals(OVRandomNode.class.getSimpleName())) {
			c = new OVRandomNode(e, father);
		} else if (name.equals(OVProceduralBlock.class.getSimpleName())) {
			c = new OVProceduralBlock(e, father);
		} else if (name.equals(OVComponentContainer.class.getSimpleName())) {
			c = new OVComponentContainer(e, father);
		} else if (name.equals(OVNodeBlock.class.getSimpleName())) {
			c = new OVNodeBlock(e, father);
		} else if (name.equals(OVComment.class.getSimpleName())) {
			c = new OVComment(e, father);
		} else if (name.equals(OVTextFile.class.getSimpleName())) {
			c = new OVTextFile(e, father);
		} else if (name.equals(OVCSVFile.class.getSimpleName())) {
			c = new OVCSVFile(e, father);
		} else if (name.equals(OVGauge.class.getSimpleName())) {
			c = new OVGauge(e, father);
		} else if (name.equals(OVArduBlock.class.getSimpleName())) {
			c = new OVArduBlock(e, father);
		} else if (name.equals(OVArduDigitalPort.class.getSimpleName())) {
			c = new OVArduDigitalPort(e, father);
		} else if (name.equals(OVPlot.class.getSimpleName())) {
			c = new OVPlot(e, father);
		} else if (name.equals(OVProgressBar.class.getSimpleName())) {
			c = new OVProgressBar(e, father);
		} else if (name.equals(OVSpinner.class.getSimpleName())) {
			c = new OVSpinner(e, father);
		}
		return c;
	}

	static public void loadComponent(Element e, OVContainer father) {
		OVComponent c = parseElement(e, father);

		if (c != null) {
			father.addComponent(c);
			if ((c instanceof OVNodeComponent || c instanceof OVProceduralBlock)
					&& father.getMode() == EditorMode.GUI) {
				c.setVisible(false);
			}
		}
	}

	public static Line parseLine(Element e, OVContainer parent) {
		try {
			return new Line(e, parent);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
