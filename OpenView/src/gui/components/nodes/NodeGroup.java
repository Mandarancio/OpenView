/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components.nodes;

import gui.interfaces.NodeListener;
import gui.interfaces.OVNode;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;

/**
 *
 * @author martino
 */
public class NodeGroup implements SlotListener, NodeListener {

    private InNode inNode_;
    private OutNode outNode_;

    public NodeGroup(InNode in, OutNode out) {
        inNode_ = in;
        outNode_ = out;
        in.addNodeListener(this);
        in.addListener(this);
        out.addNodeListener(this);
    }

    public void delete() {
        inNode_.removeListener(this);
        inNode_.removeNodeListener(this);
        outNode_.removeNodeListener(this);
    }

    @Override
    public void valueRecived(SlotInterface s, Value v) {
        if (s.equals(inNode_)) {
            outNode_.trigger(v);
        }
    }

    public OutNode getOutNode() {
        return outNode_;
    }

    public InNode getInNode() {
        return inNode_;
    }

    @Override
    public void connected(OVNode n) {
        if (n.equals(inNode_)) {
            if (outNode_.isFree()){
                outNode_.setType(inNode_.getType());
            }
        } else {
            if (inNode_.isFree()){
                inNode_.setType(outNode_.getType());
            }
        }

    }

    @Override
    public void deconneced(OVNode n) {
        if (n.equals(inNode_)) {
            if (isFree() ||( inNode_.getConnections().size()==1 && outNode_.isFree())) {
                outNode_.setType(ValueType.VOID);
                inNode_.setType(ValueType.VOID);
            } else if (inNode_.isFree() && !outNode_.isFree()) {
                inNode_.setType(outNode_.getType());
            }
        } else {
            if (isFree()) {
                outNode_.setType(ValueType.VOID);
                inNode_.setType(ValueType.VOID);
            } else if (!inNode_.isFree() && outNode_.isFree()) {
                outNode_.setType(inNode_.getType());
            }
        }
    }

    public boolean isFree() {
        return inNode_.isFree() && outNode_.isFree();
    }

}
