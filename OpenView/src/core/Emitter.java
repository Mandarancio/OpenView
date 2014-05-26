package core;

import java.util.ArrayList;

/***
 * Basic interface of {@link EmitterInterface}. Implements all the basic
 * functionality
 * 
 * @author martino
 * 
 */
public class Emitter implements EmitterInterface {
	/***
	 * Value type of the emitter
	 */
	private ValueType type_ = ValueType.NONE;
	/***
	 * Label of the emitter
	 */
	private String label_ = "";
	/***
	 * Output value buffered
	 */
	private Value value_ = null;
	/***
	 * List of the slot connected
	 */
	protected ArrayList<SlotInterface> connections_ = new ArrayList<>();
	/***
	 * Description of the emitter
	 */
	private String description_ = "";
	/***
	 * polyvalent flag of the emitter
	 */
	private boolean polyvalent_ = false;

	/***
	 * Constructor of the emitter with the ValueType and the label
	 * 
	 * @param label
	 *            label of the emitter
	 * @param type
	 *            value type of the emitter
	 */
	public Emitter(String label, ValueType type) {
		type_ = type;
		label_ = label;
	}

	
	@Override
	public boolean connect(SlotInterface s) {
		if (s.isCompatible(this)) {
			return connections_.add(s);
		}
		return false;
	}

	@Override
	public boolean deconnect(SlotInterface s) {
		return connections_.remove(s);
	}

	@Override
	public ValueType getType() {
		return type_;
	}

	public void setType(ValueType t) {
		if (type_ != t) {
			type_ = t;
			for (SlotInterface s : connections_) {
				s.emitterUpdated(this);
			}
		}
	}

	@Override
	public String getLabel() {
		return label_;
	}

	@Override
	public void trigger(Value v) {
		value_ = v;
		for (SlotInterface s : connections_) {
			s.trigger(v);
		}
	}

	@Override
	public Value readValue() {
		return value_;
	}

	@Override
	public String getDescription() {
		return description_;
	}

	public void setLabel(String l) {
		label_ = l;
	}

	public void setDescription(String d) {
		description_ = d;
	}

	@Override
	public void setValue(Value v) {
		if (v.getDescriptor().getType().isCompatible(type_)) {
			value_ = v;
		}
	}

	public boolean isPolyvalent() {
		return polyvalent_;
	}

	protected void setPolyvalent(boolean polyvalent_) {
		this.polyvalent_ = polyvalent_;
	}

	@Override
	public boolean isFree() {
		return connections_.size() == 0;
	}

}
