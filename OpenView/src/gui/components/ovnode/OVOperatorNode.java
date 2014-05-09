package gui.components.ovnode;

import evaluator.operators.Operator;
import evaluator.operators.OperatorManager;
import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.components.ovnode.enums.TriggerMode;
import gui.constants.ComponentSettings;
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

public class OVOperatorNode extends OVNodeComponent implements NodeListener,
        SlotListener {

    /**
     *
     */
    private static final long serialVersionUID = -3557751658634230282L;
    private static final String Trigger = "Trigger", Operator = "Operator";
    private static final OperatorManager operatorManager = new OperatorManager();
    private static final String Output = "Output";
    private Operator operator_;
    private ArrayList<InNode> opInputs_ = new ArrayList<>();
    private HashMap<InNode, Value> values_ = new HashMap<>();
    private OutNode output_;
    private InNode trigger_;
    private TriggerMode triggerMode_ = TriggerMode.AUTO;

    public OVOperatorNode(OVContainer father) {
        super(father);
        operator_ = operatorManager.getOperators().get(0);
        getSetting(ComponentSettings.Name).setValue("Operator");
        output_ = addOutput(Output, ValueType.VOID);
        checkInputs();

        Setting s = new Setting(Trigger, triggerMode_);
        addNodeSetting(ComponentSettings.SpecificCategory, s);
        Value v = new Value(operatorManager.getOperators().get(0).name());
        for (Operator o : operatorManager.getOperators()) {
            v.getDescriptor().addPossibility(new Value(o.name()));
        }
        s = new Setting(Operator, v);
        addNodeSetting(ComponentSettings.SpecificCategory, s);
    }

    public OVOperatorNode(Element e, OVContainer father) {
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

        Setting s = getNodeSetting(Operator);
        operator_ = operatorManager.get(s.getValue().getString());
        try {
            triggerMode_ = (TriggerMode) getNodeSetting(Trigger).getValue().getEnum();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintOVNode(Graphics2D g) {
        g.setColor(getForeground());
        paintText(operator_.name(), g, new Rectangle(0, 0, 60, 45),
                OrientationEnum.CENTER);
        g.setFont(getFont().deriveFont(10.0f));
        String text = getName();
        paintText(text, g, new Rectangle(0, 30, 60, 30), OrientationEnum.CENTER);

    }

    @Override
    public void valueRecived(SlotInterface s, Value v) {
        System.err.println(s.getLabel());
        if (s.getLabel().equals(Trigger)) {
            System.err.println("trigger");
            System.err.println("Inputs: "+operator_.input()+"\nCount: "+values_.keySet());
            Value[] vals = new Value[operator_.input()];
            int i = 0;
            for (InNode in : opInputs_) {
                if (values_.containsKey(in)) {
                    vals[i] = values_.get(in);
                } else {
                    return;
                }
                i++;
            }
            System.err.println("Done");
            try {
                output_.trigger(operator_.evaluate(vals));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            values_.put((InNode) s, v);
            if (triggerMode_ == TriggerMode.AUTO) {
                Value[] vals = new Value[operator_.input()];
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
                    output_.trigger(operator_.evaluate(vals));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void connected(OVNode n) {
        if (n instanceof InNode) {
            if (opInputs_.contains(n)) {
                ValueType inps[] = new ValueType[operator_.input()];
                int c = 0;
                for (InNode i : opInputs_) {
                    inps[c] = i.getType();
                    c++;
                }
                try {
                    output_.setType(operator_.returnedType(inps));
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
                ValueType inps[] = new ValueType[operator_.input()];

                int c = 0;
                for (InNode i : opInputs_) {
                    inps[c] = i.getType();
                    c++;
                }

                try {
                    output_.setType(operator_.returnedType(inps));
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
                if (e != triggerMode_) {
                    triggerMode_ = e;
                    if (e == TriggerMode.AUTO && trigger_ != null) {
                        this.removeInput(trigger_);
                        trigger_.removeListener(this);
                        trigger_ = null;
                    } else if (e == TriggerMode.EXTERNAL && trigger_ == null) {
                        trigger_ = this.addInput(Trigger, ValueType.VOID);
                        trigger_.addListener(this);

                    }
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else if (s.getName().equals(Operator)) {
            operator_ = operatorManager.get(v.getString()).clone();
            checkInputs();
            repaint();
        } else {
            super.valueUpdated(s, v); // To change body of generated methods,
        }										// choose Tools | Templates.
    }

    private void checkInputs() {
        while (opInputs_.size() > operator_.input()) {
            InNode n = opInputs_.get(opInputs_.size() - 1);
            removeInput(n);
            n.removeListener(this);
            opInputs_.remove(n);
        }
        while (opInputs_.size() < operator_.input()) {
            InNode in = addInput("in " + (opInputs_.size() + 1), ValueType.VOID);
            opInputs_.add(in);
            in.addNodeListener(this);
            in.addListener(this);
        }
    }

}
