package core;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.enums.EditorMode;
import gui.interfaces.SettingListener;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Setting implements SlotListener {
	private Value value_;
	private HashMap<EditorMode, Value> values_ = new HashMap<>();
	private Value minValue_, maxValue_;
	private String name_ = "";
	private boolean constant_ = false;
	private boolean guiMode_ = true, nodeMode_ = true;
	private boolean input_ = true, output_ = true;
	private EditorMode mode_ = EditorMode.GUI;

	private ArrayList<SettingListener> listeners_ = new ArrayList<>();
	private InNode inNode_ = null;
	private OutNode outNode_ = null;
	
	private boolean autoTriggered_=false;

	private Setting(String name) {
		name_ = (name);
	}

	public Setting(String name, Value val) {
		this(name);
		value_ = val;
		values_.put(EditorMode.GUI, value_);
	}

	public Setting(String name, int val, int min, int max) {
		this(name, new Value(val));
		minValue_ = new Value(min);
		maxValue_ = new Value(max);
	}

	public Setting(String name, double val, double min, double max) {
		this(name, new Value(val));
		minValue_ = new Value(min);
		maxValue_ = new Value(max);
	}

	public Setting(String name, Object data) {
		this(name, new Value(data));
	}

	public Setting(String name, ValueDescriptor desc) {
		this(name, new Value(desc));
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
		if (constant_)
			return;
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
							&& v.getData().equals(getValue().getData()))
						outNode_.setValue(v);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (value_.setData(val) && !silent) {
			triggerListener();
			if (outNode_ != null && val.equals(getValue().getData()))
				outNode_.setValue(new Value(val));
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
			if (l != null)
				l.valueUpdated(this, value_);
		}
		if (isAutoTriggered() && outNode_!=null){
			outNode_.trigger(value_);
		}
	}

	public ValueType getType() {
		// TODO Auto-generated method stub
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
				value_ = new Value(values_.get(EditorMode.GUI).getData());
				if (outNode_ != null)
					outNode_.setValue(value_);
			} else if (mode_ == EditorMode.DEBUG) {
				Value v = values_.get(EditorMode.NODE);
				if (v == null)
					v = values_.get(EditorMode.GUI);
				value_ = new Value(v.getData());
				if (outNode_ != null)
					outNode_.setValue(value_);
			} else {
				Value v = values_.get(mode_);
				if (v != null) {
					value_ = v;
				} else {
					if ((mode_ == EditorMode.GUI && guiMode_)
							|| (mode_ == EditorMode.NODE && nodeMode_)) {
						value_ = new Value(value_.getData());

					}
					values_.put(mode_, value_);
				}
			}
			triggerListener();
		}
	}

	public Element getXML(Document doc) {
		Element e = doc.createElement("setting");
		e.setAttribute("name", name_);
		e.setAttribute("constant", Boolean.toString(constant_));
		e.setAttribute("gui_mode", Boolean.toString(guiMode_));
		e.setAttribute("node_mode", Boolean.toString(nodeMode_));
		e.appendChild(value_.getXML(doc));
		if (minValue_ != null)
			e.appendChild(minValue_.getXML(doc));
		if (maxValue_ != null)
			e.appendChild(maxValue_.getXML(doc));

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
}
