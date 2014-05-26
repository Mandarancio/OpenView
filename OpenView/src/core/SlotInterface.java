package core;

import java.util.ArrayList;

/***
 * Slot interface is one of the two base elements of all the inter-object
 * communication. It provide a generic emitter/slot infrastructure using the
 * {@link Value} to pass any kind of data. It receive the value emitted from the
 * slots
 * 
 * @author martino
 * 
 */
public interface SlotInterface {
	/***
	 * Connect a {@link EmitterInterface} to the {@link SlotInterface}
	 * 
	 * @param e
	 *            {@link EmitterInterface} to connect
	 * @return if the connection is done
	 */
	public boolean connect(EmitterInterface e);

	/***
	 * Deconnect a {@link EmitterInterface} to the {@link SlotInterface}
	 * 
	 * @param e
	 *            {@link EmitterInterface} to deconnect
	 * @return if the deconnection is done
	 */
	public boolean deconnect(EmitterInterface e);

	/***
	 * deconnect all emitters
	 */
	public void deconnectAll();

	/***
	 * get list of emitter connected to the slot
	 * 
	 * @return list of {@link EmitterInterface} connected
	 */
	ArrayList<EmitterInterface> getConnections();

	/***
	 * Get value type of the slot
	 * 
	 * @return the valueType of the slot
	 */
	public ValueType getType();

	/**
	 * set the type of the slot
	 * 
	 * @param t
	 *            type to set
	 */
	public void setType(ValueType t);

	/***
	 * Action used from the {@link EmitterInterface} to pass the {@link Value}
	 * 
	 * @param v
	 *            {@link Value} received
	 */
	public void trigger(Value v);

	/***
	 * Get the label of the Slot
	 * 
	 * @return lable
	 */
	public String getLabel();

	/***
	 * get the description
	 * 
	 * @return description
	 */
	public String getDescription();

	/***
	 * Check if the slot is connected or not
	 * 
	 * @return if has at least a connection
	 */
	public boolean isFree();

	/***
	 * Look if is compatible with an {@link EmitterInterface}
	 * 
	 * @param e
	 *            {@link EmitterInterface} to check
	 * @return if is compatible
	 */
	public boolean isCompatible(EmitterInterface e);

	/***
	 * Add {@link SlotListener} to the list of listener
	 * 
	 * @param l
	 *            listener
	 */
	public void addListener(SlotListener l);

	/***
	 * remove {@link SlotListener} to the list of listener
	 * 
	 * @param l
	 *            listener
	 */
	public void removeListener(SlotListener l);

	/***
	 * If one emitter changed status
	 * 
	 * @param e
	 *            {@link EmitterInterface} that has changed
	 */
	public void emitterUpdated(EmitterInterface e);

	/***
	 * Force the reading of the output buffer of the first connected emitter
	 * 
	 * @return Value read
	 */
	public Value pullValue();
}
