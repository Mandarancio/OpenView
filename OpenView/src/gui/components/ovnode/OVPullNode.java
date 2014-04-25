package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.NodeListener;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

public class OVPullNode extends OVNodeComponent implements NodeListener,
		SlotListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String Trigger = "Trigger";
	private static final String Value = "Value";
	private static final String Output = "Output";
	private InNode input_;
	private OutNode output_;

	public OVPullNode(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.Name).setValue("Pull");

		input_ = addInput(Value, ValueType.VOID);
		input_.addNodeListener(this);
		InNode trigger = addInput(Trigger, ValueType.VOID);
		trigger.addListener(this);
		output_ = addOutput(Output, ValueType.VOID);

	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(Trigger)) {
			if (!input_.isFree()){
				Value vv= input_.pullValue();
				if (vv!=null){
					if (getMode()==EditorMode.DEBUG)
						input_.getLine((OVNode)input_.getConnections().get(0)).debugDisplay(vv.getString());
					output_.trigger(vv);

				}
			}
	
		
		}

	}

	@Override
	public void connected(OVNode n) {
		if (n.equals(input_)) {
			output_.setType(input_.getType());
		}
	}

	@Override
	public void deconneced(OVNode n) {
		output_.setType(ValueType.VOID);
	}


}
