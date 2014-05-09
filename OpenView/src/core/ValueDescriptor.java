package core;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ValueDescriptor {

    private String label_ = "";
    private String unit_ = "";
    private ValueType type_ = ValueType.NONE;
    private ArrayList<Value> possibilities_ = new ArrayList<>();

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

    public ValueDescriptor(Object obj) {
        type_ = ValueType.getType(obj);
    }

    public ValueDescriptor(ValueDescriptor desc) {
        type_ = desc.getType();
        unit_ = desc.getUnit();
        label_ = desc.getLabel();
        possibilities_ = new ArrayList<>(desc.getPossibilities());
    }

    public ValueDescriptor(ValueType type) {
        type_ = type;
    }

    public ValueDescriptor(Object obj, String unit, String label) {
        this(obj);
        unit_ = unit;
        label_ = label;
    }

    public ValueDescriptor(ValueType type, String unit, String label) {
        this(type);
        unit_ = unit;
        label_ = label;
    }

    public String getLabel() {
        return label_;
    }

    public void setLabel(String label_) {
        this.label_ = label_;
    }

    public String getUnit() {
        return unit_;
    }

    public void setUnit(String unit_) {
        this.unit_ = unit_;
    }

    public ValueType getType() {
        return type_;
    }

    public void setType(ValueType type_) {
        this.type_ = type_;
    }

    public void addPossibility(Value v) {
        possibilities_.add(v);
    }

    public void addPossibilities(Value... values) {

        for (Value v : values) {
            possibilities_.add(v);
        }
    }

    public ArrayList<Value> getPossibilities() {
        return possibilities_;
    }

    public boolean hasPossbilities() {
        return possibilities_.size() > 0;
    }

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
