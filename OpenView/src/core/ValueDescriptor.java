package core;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/***
 * Descriptor class of a {@link Value}
 * 
 * @author martino
 * 
 */
public class ValueDescriptor {
	/***
	 * Label of the value
	 */
	private String label_ = "";
	/***
	 * Unit of the value
	 */
	private String unit_ = "";
	/***
	 * Type of the value
	 */
	private ValueType type_ = ValueType.NONE;
	/***
	 * List of possible value of the value (if is empty is analogic)
	 */
	private ArrayList<Value> possibilities_ = new ArrayList<>();

	/***
	 * Load the descriptor from an XML element
	 * 
	 * @param e
	 *            XML element to load
	 */
	public ValueDescriptor(Element e) {
		label_ = e.getAttribute("label");
		unit_ = e.getAttribute("unit");
		type_ = ValueType.valueOf(e.getAttribute("type"));
		NodeList nl = e.getElementsByTagName("possibilities");
		if (nl.getLength() > 0) {
			Element pe = (Element) nl.item(0);
			NodeList pnl = pe.getElementsByTagName(Value.class.getSimpleName());
			for (int i = 0; i < pnl.getLength(); i++) {
				Node n = pnl.item(i);
				if (n instanceof Element) {
					possibilities_.add(new Value((Element) n));
				}
			}
		}
	}

	/****
	 * {@link ValueDescriptor} of an object
	 * 
	 * @param obj
	 *            object to describe
	 */
	public ValueDescriptor(Object obj) {
		type_ = ValueType.getType(obj);
	}

	/***
	 * copy another {@link ValueDescriptor}
	 * 
	 * @param desc
	 *            descriptor to copy
	 */
	public ValueDescriptor(ValueDescriptor desc) {
		type_ = desc.getType();
		unit_ = desc.getUnit();
		label_ = desc.getLabel();
		possibilities_ = new ArrayList<>(desc.getPossibilities());
	}

	/***
	 * Basic descriptor from a {@link ValueType}
	 * 
	 * @param type
	 *            type of the value
	 */
	public ValueDescriptor(ValueType type) {
		type_ = type;
	}

	/***
	 * Advanced descriptor from a generic object
	 * 
	 * @param obj
	 *            object to describe
	 * @param unit
	 *            unit of the value
	 * @param label
	 *            label of the value
	 */
	public ValueDescriptor(Object obj, String unit, String label) {
		this(obj);
		unit_ = unit;
		label_ = label;
	}

	/**
	 * Advanced Descriptor from a {@link ValueType}
	 * 
	 * @param type
	 *            type of the value
	 * @param unit
	 *            unit of the value
	 * @param label
	 *            label of the value
	 */
	public ValueDescriptor(ValueType type, String unit, String label) {
		this(type);
		unit_ = unit;
		label_ = label;
	}

	/***
	 * get the label of the value
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label_;
	}

	/***
	 * Set the label of a value
	 * 
	 * @param label
	 *            label to set
	 */
	public void setLabel(String label) {
		this.label_ = label;
	}

	/***
	 * Get unit of the value
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return unit_;
	}

	/***
	 * Set the unit of the value
	 * 
	 * @param unit
	 *            unit to set
	 */
	public void setUnit(String unit) {
		this.unit_ = unit;
	}

	/***
	 * Get the value type of the value
	 * 
	 * @return the value type
	 */
	public ValueType getType() {
		return type_;
	}

	/***
	 * Set the value type
	 * 
	 * @param type
	 *            value type to set
	 */
	public void setType(ValueType type) {
		this.type_ = type;
	}

	/***
	 * Add a possible value to the descriptor
	 * 
	 * @param v
	 *            possible value to add
	 */
	public void addPossibility(Value v) {
		possibilities_.add(v);
	}

	/***
	 * Add one or more possible value to the descriptor
	 * 
	 * @param values
	 *            possible values to add
	 */
	public void addPossibilities(Value... values) {

		for (Value v : values) {
			possibilities_.add(v);
		}
	}

	/***
	 * get the list of possible values
	 * 
	 * @return list of possible values
	 */
	public ArrayList<Value> getPossibilities() {
		return possibilities_;
	}

	/***
	 * check if there are any possible values
	 * 
	 * @return true if there are at least one possible value
	 */
	public boolean hasPossbilities() {
		return possibilities_.size() > 0;
	}

	/***
	 * Get an XML element describing the ValueDescriptor
	 * 
	 * @param doc
	 *            the XML document that will contain the element
	 * @return the XML element
	 */
	public Element getXML(Document doc) {
		Element e = doc.createElement(ValueDescriptor.class.getSimpleName());
		e.setAttribute("type", type_.toString());
		e.setAttribute("unit", unit_);
		e.setAttribute("label", label_);
		Element pos = doc.createElement("possibilities");
		pos.setAttribute("count", Integer.toString(possibilities_.size()));
		for (Value v : possibilities_) {
			pos.appendChild(v.getXML(doc));
		}
		e.appendChild(pos);
		return e;
	}
}
