package gui.support;

import gui.components.OVComponent;
import gui.components.ovgui.OVButton;
import gui.components.ovgui.OVCheckBox;
import gui.components.ovgui.OVLabel;
import gui.components.ovgui.OVTextArea;
import gui.components.ovgui.OVTextField;
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
		} else if (name.equals(OVVariableNode.class.getSimpleName())) {
			c = new OVVariableNode(e, father);
		}
		if (c != null)
			father.addComponent(c);
	}
}
