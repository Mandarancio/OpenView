package gui.support;

import core.maker.OVClassFactory;
import gui.components.OVComponent;
import gui.components.nodes.Line;
import gui.components.ovnode.OVNodeComponent;
import gui.components.ovprocedural.OVProceduralBlock;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import org.w3c.dom.Element;

public class XMLParser {

	static public OVComponent parseElement(Element e, OVContainer father) {
		String name = e.getTagName();
		OVComponent c = OVClassFactory.getInstance(name, e, father);
                
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
