package gui.components.ovnode;

import evaluator.functions.Function;
import evaluator.functions.FunctionManager;
import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.components.ovnode.enums.TriggerMode;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.NodeListener;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;

import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;
import core.support.OrientationEnum;

public class OVFunctionNode extends OVNodeComponent implements NodeListener,
		SlotListener {

	/**
     *
     */
	private static final long serialVersionUID = -3557751658634230282L;
	private static final String Trigger = "Trigger", Function = "Function";
	private static final String Output = "Output";
	private Function function_;
	private ArrayList<InNode> opInputs_ = new ArrayList<>();
	private HashMap<InNode, Value> values_ = new HashMap<>();
	private OutNode output_;
	private InNode trigger_;
	private TriggerMode triggerMode_ = TriggerMode.AUTO;
	private long _last = -1;

	public OVFunctionNode(OVContainer father) {
		super(father);
		function_ = FunctionManager.getFunctions().get(0);
		getSetting(ComponentSettings.Name).setValue("Function");
		output_ = addOutput(Output, ValueType.VOID);
		checkInputs();

		Setting s = new Setting(Trigger, triggerMode_);
		addNodeSetting(ComponentSettings.SpecificCategory, s);
		Value v = new Value(FunctionManager.getFunctions().get(0).name());
		for (Function o : FunctionManager.getFunctions()) {
			v.getDescriptor().addPossibility(new Value(o.name()));
		}
		s = new Setting(Function, v);
		addNodeSetting(ComponentSettings.SpecificCategory, s);
	}

	public OVFunctionNode(Element e, OVContainer father) {
		super(e, father);
		ArrayList<InNode> ins = new ArrayList<>(inputs_);
		for (InNode n : ins) {
			if (n.getLabel().startsWith("in ")) {
				opInputs_.add(n);
				n.addListener(this);
				n.addNodeListener(this);
			} else if (n.getLabel().equals(Trigger)) {
				if (trigger_ != null) {
					trigger_.removeListener(this);
					removeInput(trigger_);
					trigger_ = null;
				}
				trigger_ = n;
				n.addListener(this);
			}
		}
		ins.clear();
		for (OutNode n : outputs_) {
			if (n.getLabel().equals(Output)) {
				output_ = n;
			}
		}

		Setting s = getNodeSetting(Function);
		function_ = FunctionManager.get(s.getValue().getString()).clone();
		try {
			triggerMode_ = (TriggerMode) getNodeSetting(Trigger).getValue()
					.getEnum();
		} catch (Exception ex) {
		}
	}

	@Override
	protected void paintOVNode(Graphics2D g) {
		g.setColor(getForeground());
		if (function_ != null) {
			paintText(function_.name(), g, new Rectangle(0, 0, 60, 45),
					OrientationEnum.CENTER);
		}

		g.setFont(getFont().deriveFont(10.0f));
		String text = getName();
		paintText(text, g, new Rectangle(0, 30, 60, 30), OrientationEnum.CENTER);

	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.equals(trigger_)) {
			Value[] vals = new Value[function_.input()];
			int i = 0;
			for (InNode in : opInputs_) {
				if (values_.containsKey(in)) {
					vals[i] = values_.get(in);
				} else {
					return;
				}
				i++;
			}
			try {
				output_.trigger(function_.evaluate(vals));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (!s.getLabel().equals(Trigger)) {
			if (triggerMode_ == TriggerMode.AUTO) {
				if (_last < 0) {
					_last = System.currentTimeMillis();
				} else {
					long current = System.currentTimeMillis();
					long dt = current - _last;
					_last = current;
					if (values_.get((InNode) s) != null
							&& values_.get((InNode) s).getData() != null) {
						if (dt < 10
								&& values_.get((InNode) s).getData()
										.equals(v.getData()))
							return;
					}
				}
				values_.put((InNode) s, v);

				Value[] vals = new Value[function_.input()];
				int i = 0;
				for (InNode in : opInputs_) {
					if (values_.containsKey(in)) {
						vals[i] = values_.get(in);
					} else {
						return;
					}
					i++;
				}
				try {
					output_.trigger(function_.evaluate(vals));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				values_.put((InNode) s, v);
			}
		}
	}

	@Override
	public void connected(OVNode n) {
		if (n instanceof InNode) {
			if (opInputs_.contains(n) && function_.input() == opInputs_.size()) {
				ValueType inps[] = new ValueType[function_.input()];
				int c = 0;
				for (InNode i : opInputs_) {
					inps[c] = i.getType();
					c++;
				}
				try {
					output_.setType(function_.returnedType(inps));
				} catch (Exception e) {
					output_.setType(ValueType.VOID);
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void deconneced(OVNode n) {
		if (n instanceof InNode) {
			if (opInputs_.contains(n)) {
				ValueType inps[] = new ValueType[function_.input()];

				int c = 0;
				for (InNode i : opInputs_) {
					inps[c] = i.getType();
					c++;
				}

				try {
					output_.setType(function_.returnedType(inps));
				} catch (Exception e) {
					output_.setType(ValueType.VOID);
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void valueUpdated(Setting s, Value v) {
		if (s.getName().equals(Trigger)) {
			try {
				TriggerMode e = (TriggerMode) v.getEnum();
				triggerMode_ = e;
				if (e == TriggerMode.AUTO && trigger_ != null) {
					this.removeInput(trigger_);
					trigger_.removeListener(this);
					trigger_ = null;
				} else if (e == TriggerMode.EXTERNAL && trigger_ == null) {

					trigger_ = this.addInput(Trigger, ValueType.VOID);
					trigger_.addListener(this);
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (s.getName().equals(Function)) {
			function_ = FunctionManager.get(v.getString()).clone();
			checkInputs();
			repaint();
		} else {
			super.valueUpdated(s, v); // To change body of generated methods,
		} // choose Tools | Templates.
	}

	private void checkInputs() {
		if (function_ != null) {
			while (opInputs_.size() > function_.input()) {
				InNode n = opInputs_.get(opInputs_.size() - 1);
				removeInput(n);
				n.removeListener(this);
				opInputs_.remove(n);
			}
			while (opInputs_.size() < function_.input()) {
				InNode in = addInput("in " + (opInputs_.size() + 1),
						ValueType.VOID);
				opInputs_.add(in);
				in.addNodeListener(this);
				in.addListener(this);
			}
		}
	}

	@Override
	public void setMode(EditorMode mode_) {
		super.setMode(mode_);
		_last = -1;
	}
}
