package gui.layers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AssociatedNodeLayer extends NodeLayer {

	private boolean visible_=true;
	
	public AssociatedNodeLayer(Element e) {
		super(e);
		visible_=Boolean.parseBoolean(e.getAttribute("visible"));
	}

	public AssociatedNodeLayer(NodeLayer n) {
		super(n.getName());
		setUUID(n.getUUID());
	}

	public boolean isVisible() {
		return visible_;
	}

	public void setVisible(boolean visible_) {
		this.visible_ = visible_;
	}
	
	@Override
	public Element getXML(Document doc) {
		Element e=super.getXML(doc);
		e.setAttribute("visible", Boolean.toString(visible_));
		return e;
	}

}
