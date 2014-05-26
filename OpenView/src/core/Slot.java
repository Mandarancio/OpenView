package core;

import java.util.ArrayList;

public class Slot implements SlotInterface {

    private ArrayList<EmitterInterface> connections_ = new ArrayList<>();
    private String label_ = "";
    private ValueType type_;
    private String description_ = "";
    protected ArrayList<SlotListener> listeners_ = new ArrayList<>();

    public Slot(String label, ValueType type) {
        type_ = type;
        label_ = label;
    }

    @Override
    public boolean connect(EmitterInterface e) {
        if (isCompatible(e)) {
            return connections_.add(e);
        }
        return false;
    }

    @Override
    public boolean deconnect(EmitterInterface e) {
        if (!isFree()) {
            return connections_.remove(e);
        }
        return false;
    }

    @Override
    public void deconnectAll() {
        connections_.clear();
    }

    @Override
    public ValueType getType() {
        return type_;
    }

    @Override
    public void trigger(Value v) {
        for (SlotListener l : listeners_) {
            l.valueRecived(this, v);
        }
    }

    @Override
    public String getLabel() {
        return label_;
    }

    @Override
    public String getDescription() {
        return description_;
    }

    @Override
    public boolean isFree() {
        return (connections_.size() == 0);
    }

    @Override
    public void addListener(SlotListener l) {
        listeners_.add(l);
    }

    @Override
    public void removeListener(SlotListener l) {
        listeners_.remove(l);
    }

    @Override
    public ArrayList<EmitterInterface> getConnections() {
        return connections_;
    }

    @Override
    public void setType(ValueType t) {
        type_ = t;

    }

    @Override
    public boolean isCompatible(EmitterInterface e) {
        return (!connections_.contains(e) && (type_ == ValueType.VOID
                || e.isPolyvalent() || this.getType().isCompatible(e.getType())));
    }

    @Override
    public void emitterUpdated(EmitterInterface e) {
        if (this.connections_.contains(e)) {
            if (!this.isCompatible(e)) {
                this.deconnect(e);
            }
        }
    }

    @Override
    public Value pullValue() {
        if (!isFree()) {
            return connections_.get(0).readValue();
        }
        return null;
    }

}
