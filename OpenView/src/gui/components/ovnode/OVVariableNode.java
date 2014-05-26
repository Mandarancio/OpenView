package gui.components.ovnode;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.constants.ComponentSettings;
import gui.interfaces.NodeListener;
import gui.interfaces.OVContainer;
import gui.interfaces.OVNode;
import gui.support.Setting;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
		getSetting(ComponentSettings.Name).setValue("V");
		input_ = addInput(Value, ValueType.VOID);
		InNode trigger = addInput(Trigger, ValueType.VOID);
		output_ = addOutput(Value, ValueType.VOID);

		Setting s = new Setting(Value, "void", this);
		addNodeSetting(ComponentSettings.SpecificCategory, s);

		trigger.addListener(this);
		input_.addListener(this);
		input_.addNodeListener(this);
	}

	public OVVariableNode(Element e, OVContainer father) {
		super(e, father);
		for (InNode n : inputs_) {
			if (n.getLabel().equals(Value)) {
				input_ = n;
				input_.addListener(this);
				input_.addNodeListener(this);
			} else if (n.getLabel().equals(Trigger)) {
				n.addListener(this);
			}
		}
		for (OutNode n : outputs_) {
			if (n.getLabel().equals(Value)) {
				output_ = n;
				break;
			}
		}
		NodeList nl = e.getElementsByTagName(core.Value.class.getSimpleName());
		if (nl.getLength() != 0) {
			value_ = new Value((Element) nl.item(0));
		}
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
			} else {
				__lock = false;
			}
		} else {
			super.valueUpdated(s, v);
		}
	}

	private void setValue(String s) {
		value_ = core.Value.parseValue(s);
		input_.setType(value_.getDescriptor().getType());
		output_.setType(value_.getDescriptor().getType());
		output_.setValue(value_);
		repaint();

	}

	@Override
	public void connected(OVNode n) {
		if (output_.getType() == ValueType.VOID) {
			value_ = new Value(new ValueDescriptor(input_.getType()));
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

	@Override
	public Element getXML(Document doc) {
		Element e = super.getXML(doc);
		e.appendChild(value_.getXML(doc));
		return e;
	}

}
