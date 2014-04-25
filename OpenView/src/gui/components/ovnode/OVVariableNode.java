package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.NodeListener;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueDescriptor;
import core.ValueType;
import core.support.OrientationEnum;

public class OVVariableNode extends OVNodeComponent implements SlotListener,
		NodeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1642569664632596894L;
	private static final String Trigger = "Trigger";
	private static final String Value = "Value";
	private OutNode output_;
	private Value value_ = new Value(Void.TYPE);
	private InNode input_;
	private boolean __lock = false;

	public OVVariableNode(OVContainer father) {
		super(father);
		getSetting(ComponentSettings.Name).setValue("K");
		input_ = addInput(Value, ValueType.VOID);
		InNode trigger = addInput(Trigger, ValueType.VOID);
		output_ = addOutput(Value, ValueType.VOID);

		Setting s = new Setting(Value, "void");
		addNodeSetting(ComponentSettings.SpecificCategory, s);

		trigger.addListener(this);
		input_.addListener(this);
		input_.addNodeListener(this);
	}

	@Override
	public void doubleClick(Point point) {
		String s = (String) JOptionPane.showInputDialog(this, "Value: ",
				"Set value Dialog", JOptionPane.PLAIN_MESSAGE, null, null,
				value_.getString());

		// If a string was returned, say so.
		if ((s != null)) {
			setValue(s);
			getNodeSetting(Value).setValue(s);
			return;
		}
	}

	@Override
	public void valueRecived(SlotInterface s, Value v) {
		if (s.getLabel().equals(Trigger)) {
			output_.trigger(value_);
		} else if (s.getLabel().equals(Value) && v != null) {

			__lock = true;
			getNodeSetting(Value).setValue(v.getString());
			value_ = v;
			repaint();
			output_.setValue(value_);
		}
	}

	@Override
	protected void paintOVNode(Graphics2D g) {
		g.setColor(getForeground());
		paintText(getName(), g, new Rectangle(0, 0, 60, 45),
				OrientationEnum.CENTER);
		g.setFont(getFont().deriveFont(10.0f));
		String text = value_.getDescriptor().getType().toString();
		paintText(text, g, new Rectangle(0, 30, 60, 30), OrientationEnum.CENTER);

	}

	@Override
	public void valueUpdated(Setting s, core.Value v) {
		if (s.getName().equals(Value)) {
			if (!__lock) {
				setValue(v.getString());
			} else
				__lock = false;
		} else
			super.valueUpdated(s, v);
	}

	private void setValue(String s) {
		if (s.length() == 0 || s.equals("void")) {
			value_ = new Value(Void.TYPE);
		} else if (s.equals("true")) {
			value_ = new Value(true);
		} else if (s.equals("false")) {
			value_ = new Value(false);

		} else if (s.contains(".")) {
			try {
				value_ = new Value(Double.valueOf(s));
			} catch (NumberFormatException e) {
				value_ = new Value(s);
			}
		} else {
			try {
				value_ = new Value(Integer.valueOf(s));
			} catch (NumberFormatException e) {
				value_ = new Value(s);
			}
		}
		input_.setType(value_.getDescriptor().getType());
		output_.setType(value_.getDescriptor().getType());
		output_.setValue(value_);
		repaint();

	}

	@Override
	public void connected(OVNode n) {
		if (output_.getType() == ValueType.VOID) {
			value_ = new Value(new ValueDescriptor(input_.getType()));
			System.out.println("here");
			output_.setType(input_.getType());
			repaint();
		}

	}

	@Override
	public void deconneced(OVNode n) {
		if (value_.getData() == null) {
			input_.setType(ValueType.VOID);
			output_.setType(ValueType.VOID);
			value_.getDescriptor().setType(ValueType.VOID);
			repaint();
		}
	}

}
