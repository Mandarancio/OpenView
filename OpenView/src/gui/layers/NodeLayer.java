package gui.layers;

import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NodeLayer {
	private UUID uuid_;
	private String name_;

	public NodeLayer(String name) {
		setName(name);
		uuid_ = UUID.randomUUID();
	}

	public NodeLayer(Element e) {
		name_=e.getAttribute("name");
		uuid_=UUID.fromString(e.getAttribute("uuid"));
	}

	public UUID getUUID() {
		return uuid_;
	}

	protected void setUUID(UUID uuid_) {
		this.uuid_ = uuid_;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name_) {
		this.name_ = name_;
	}

	public Element getXML(Document doc) {
		Element e = doc.createElement(getClass().getSimpleName());
		e.setAttribute("uuid", uuid_.toString());
		e.setAttribute("name", name_);
		return e;
	}

}
