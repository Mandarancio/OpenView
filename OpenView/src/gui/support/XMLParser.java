package gui.support;

import gui.components.OVComponent;
import gui.components.nodes.Line;
import gui.components.ovgui.OVButton;
import gui.components.ovgui.OVCheckBox;
import gui.components.ovgui.OVLabel;
import gui.components.ovgui.OVPlotComponent;
import gui.components.ovgui.OVTextArea;
import gui.components.ovgui.OVTextField;
import gui.components.ovnode.OVForTrigger;
import gui.components.ovnode.OVFunctionNode;
import gui.components.ovnode.OVIFTriggerNode;
import gui.components.ovnode.OVOperatorNode;
import gui.components.ovnode.OVPullNode;
import gui.components.ovnode.OVRandomNode;
import gui.components.ovnode.OVTimerTriggerNode;
import gui.components.ovnode.OVVariableNode;
import gui.interfaces.OVContainer;

import org.w3c.dom.Element;

public class XMLParser {
	static public void loadComponent(Element e, OVContainer father) {

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
		}
		if (c != null)
			father.addComponent(c);
	}

	public static Line parseLine(Element e, OVContainer parent) {

		return new Line(e, parent);
	}
}
