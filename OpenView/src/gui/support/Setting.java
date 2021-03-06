package gui.support;

import gui.components.OVComponent;
import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.enums.EditorMode;
import gui.interfaces.SettingListener;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueDescriptor;
import core.ValueType;

public class Setting implements SlotListener {

	private Value value_;
	private HashMap<EditorMode, Value> values_ = new HashMap<>();
	private Value minValue_, maxValue_;
	private String name_ = "";
	private boolean constant_ = false;
	private boolean guiMode_ = true, nodeMode_ = true;
	private boolean input_ = true, output_ = true;
	private EditorMode mode_ = EditorMode.GUI;

	private Object userData_ = null;

	private ArrayList<SettingListener> listeners_ = new ArrayList<>();
	private InNode inNode_ = null;
	private OutNode outNode_ = null;

	private boolean autoTriggered_ = false;
	private OVComponent parent_;

	private Setting(String name, OVComponent comp) {
		name_ = (name);
		parent_ = comp;
	}

	public Setting(String name, Value val, OVComponent comp) {
		this(name, comp);
		value_ = val;
		values_.put(EditorMode.GUI, value_);

	}

	public Setting(String name, int val, int min, int max, OVComponent comp) {
		this(name, new Value(val), comp);
		minValue_ = new Value(min);
		maxValue_ = new Value(max);
	}

	public Setting(String name, double val, double min, double max,
			OVComponent comp) {
		this(name, new Value(val), comp);
		minValue_ = new Value(min);
		maxValue_ = new Value(max);
	}

	public Setting(String name, Object data, OVComponent comp) {
		this(name, new Value(data), comp);
	}

	public Setting(String name, ValueDescriptor desc, OVComponent comp) {
		this(name, new Value(desc), comp);
	}

	public Setting(Element e, OVComponent parent) {
		parent_ = parent;
		name_ = e.getAttribute("name");
		constant_ = Boolean.parseBoolean(e.getAttribute("constant"));
		guiMode_ = Boolean.parseBoolean(e.getAttribute("gui_mode"));
		nodeMode_ = Boolean.parseBoolean(e.getAttribute("node_mode"));
		output_ = Boolean.parseBoolean(e.getAttribute("output"));
		input_ = Boolean.parseBoolean(e.getAttribute("input"));
		mode_ = EditorMode.GUI;

		NodeList nl = e.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n instanceof Element) {
				Element el = (Element) n;
				if (el.getTagName().equals("min")) {
					minValue_ = new Value(el);
				} else if (el.getTagName().equals("max")) {
					maxValue_ = new Value(el);
				} else if (el.getTagName().equals(EditorMode.GUI.toString())) {
					value_ = new Value(el);
					values_.put(EditorMode.GUI, value_);
				} else if (el.getTagName().equals(EditorMode.NODE.toString())) {
					if (value_ == null) {
						value_ = new Value(el);
						values_.put(EditorMode.NODE, value_);
					} else {
						values_.put(EditorMode.NODE, new Value(el));
					}
				} else if (el.getTagName()
						.equals(OutNode.class.getSimpleName())) {
					setOutputNode(new OutNode(el, parent));
					parent.addOutput(outNode_);
				} else if (el.getTagName().equals(InNode.class.getSimpleName())) {
					setInputNode(new InNode(el, parent));
					parent.addInput(inNode_);
				}
			}
		}
	}

	public Value getValue() {
		return value_;
	}

	public void setValue(Object val) {

		setValue(val, false);
	}

	public void setValue(int val) {
		setValue(new Integer(val));
	}

	public void setValue(double val) {
		setValue(new Double(val));
	}

	public void setValue(Object val, boolean silent) {
		if (constant_) {
			return;
		}
		if (minValue_ != null && maxValue_ != null) {
			Value v = new Value(val);
			if (v.getDescriptor().getType().isNumeric()) {
				try {
					if (v.getDouble() >= minValue_.getDouble()
							&& v.getDouble() <= maxValue_.getDouble()) {
						if (value_.setData(val) && !silent) {
							triggerListener();
						}

					} else if (v.getDouble() < minValue_.getDouble()
							&& value_.setData(minValue_.getData()) && !silent) {
						triggerListener();
					} else if (v.getDouble() > maxValue_.getDouble()
							&& value_.setData(maxValue_.getData()) && !silent) {
						triggerListener();
					}
					if (outNode_ != null
							&& v.getData().equals(getValue().getData())) {
						outNode_.setValue(v);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (value_.setData(val) && !silent) {
			triggerListener();
			if (outNode_ != null && val.equals(getValue().getData())) {
				outNode_.setValue(new Value(val));
			}
		}
	}

	public String getName() {
		return name_;
	}

	public boolean isConstant() {
		return constant_;
	}

	public void setConstant(boolean flag) {
		constant_ = flag;
	}

	public ArrayList<SettingListener> getListeners() {
		return listeners_;
	}

	public void addListener(SettingListener list) {
		listeners_.add(list);
	}

	public void removeListener(SettingListener list) {
		listeners_.remove(list);
	}

	private void triggerListener() {
		for (SettingListener l : listeners_) {
			if (l != null) {
				l.valueUpdated(this, value_);
			}
		}
		if (isAutoTriggered() && outNode_ != null) {
			outNode_.trigger(value_);
		}
	}

	public ValueType getType() {
		return value_.getDescriptor().getType();
	}

	public Value getMin() {
		return minValue_;
	}

	public Value getMax() {
		return maxValue_;
	}

	public void setMin(Value value) {
		minValue_ = value;
	}

	public void setMax(Value value) {
		maxValue_ = value;
	}

	public void setInputNode(InNode in) {
		inNode_ = in;
		inNode_.addListener(this);
	}

	public InNode getInputNode() {
		return inNode_;
	}

	public void removeNode() {
		inNode_.delete();

		inNode_ = null;
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		setValue(v.getData());
	}

	public boolean isGuiMode() {
		return guiMode_;
	}

	public void setGuiMode(boolean guiMode_) {
		this.guiMode_ = guiMode_;
	}

	public boolean isNodeMode() {
		return nodeMode_;
	}

	public void setNodeMode(boolean nodeMode_) {
		this.nodeMode_ = nodeMode_;

	}

	public EditorMode getMode() {
		return mode_;

	}

	public void setMode(EditorMode mode_) {
		if (mode_ != this.mode_) {
			this.mode_ = mode_;
			if (mode_ == EditorMode.RUN) {
				Value v;
				if (guiMode_) {
					v = values_.get(EditorMode.GUI);
				} else {
					v = values_.get(EditorMode.NODE);
				}

				if (v == null) {
					value_ = new Value(value_);
				} else {
					value_ = new Value(v);
				}
				if (outNode_ != null) {
					outNode_.setValue(value_);
				}
			} else if (mode_ == EditorMode.DEBUG) {
				Value v = values_.get(EditorMode.NODE);
				if (v == null) {
					v = values_.get(EditorMode.GUI);
				}
				value_ = new Value(v.getData());
				if (outNode_ != null) {
					outNode_.setValue(value_);
				}
			} else {
				Value v = values_.get(mode_);
				if (v != null) {
					value_ = v;
				} else {
					if ((mode_ == EditorMode.GUI && guiMode_)
							|| (mode_ == EditorMode.NODE && nodeMode_)) {
						value_ = new Value(value_);

					}
					values_.put(mode_, value_);
				}
			}
			triggerListener();
		}
	}

	public Element getXML(Document doc) {
		Element e = doc.createElement(getClass().getSimpleName());
		e.setAttribute("name", name_);
		e.setAttribute("constant", Boolean.toString(constant_));
		e.setAttribute("gui_mode", Boolean.toString(guiMode_));
		e.setAttribute("node_mode", Boolean.toString(nodeMode_));
		e.setAttribute("output", Boolean.toString(output_));
		e.setAttribute("input", Boolean.toString(input_));
		for (EditorMode mode : values_.keySet()) {
			if (!mode.isExec()) {
				if ((mode == EditorMode.GUI && guiMode_)
						|| (mode == EditorMode.NODE && nodeMode_)) {
					Element el = values_.get(mode).getXML(doc);
					doc.renameNode(el, null, mode.toString());
					e.appendChild(el);
				}
			}
		}
		if (minValue_ != null) {
			Element min = minValue_.getXML(doc);
			doc.renameNode(min, null, "min");
			e.appendChild(min);
		}
		if (maxValue_ != null) {
			Element max = maxValue_.getXML(doc);
			doc.renameNode(max, null, "max");
			e.appendChild(max);
		}
		if (inNode_ != null) {
			e.appendChild(inNode_.getXML(doc));
		}
		if (outNode_ != null) {
			e.appendChild(outNode_.getXML(doc));
		}
		return e;
	}

	public Object getOutputNode() {
		return outNode_;
	}

	public void setOutputNode(OutNode addOutput) {
		outNode_ = addOutput;
		outNode_.setValue(value_);
	}

	public void removeOutputNode() {
		outNode_.delete();
		outNode_ = null;
	}

	public boolean isInput() {
		return input_;
	}

	public void setInput(boolean input_) {
		this.input_ = input_;
	}

	public boolean isOutput() {
		return output_;
	}

	public void setOutput(boolean output_) {
		this.output_ = output_;
	}

	public boolean isAutoTriggered() {
		return autoTriggered_;
	}

	public void setAutoTriggered(boolean autoTriggered_) {
		this.autoTriggered_ = autoTriggered_;
	}

	public void trigg() {
		triggerListener();
	}

	@Override
	public String toString() {
		return "Setting[" + name_ + "," + getValue() + "," + mode_ + "]\n"
				+ values_;
	}

	public OVComponent getParent() {
		return parent_;
	}

	public Object getUserData() {
		return userData_;
	}

	public void setUserData(Object userData_) {
		this.userData_ = userData_;
	}

}
