package core;

import java.util.ArrayList;

/***
 * Basic implementation of {@link SlotInterface}
 * 
 * @author martino
 * 
 */
public class Slot implements SlotInterface {

	/**
	 * List of connected {@link EmitterInterface}
	 */
	private ArrayList<EmitterInterface> connections_ = new ArrayList<>();
	/***
	 * Label of the slot
	 */
	private String label_ = "";
	/***
	 * Type of the slot
	 */
	private ValueType type_;
	/***
	 * Description of the slot
	 */
	private String description_ = "";

	/***
	 * list of the listener
	 */
	protected ArrayList<SlotListener> listeners_ = new ArrayList<>();

	/****
	 * Initialize the slot with label and type
	 * 
	 * @param label
	 *            label of the slot
	 * @param type
	 *            type of the slot
	 */
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
			}else if (this.getType()==ValueType.VOID||getType()==ValueType.NONE){
				this.setType(e.getType());
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

	/***
	 * Set the description of the label
	 * 
	 * @param description
	 *            to set
	 */
	public void setDescription(String description) {
		this.description_ = description;
	}

}
