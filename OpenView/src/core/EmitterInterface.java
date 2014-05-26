package core;

/***
 * Emitter interface is one of the two base elements of all the inter-object
 * communication. It provide a generic emitter/slot infrastructure using the
 * {@link Value} to pass any kind of data. It emit value to all the connected
 * slots.
 * 
 * @author martino
 * 
 */
public interface EmitterInterface {
	/***
	 * Connect the emitter to a {@link SlotInterface}
	 * 
	 * @param s
	 *            slot to connect
	 * @return if the connection has be done
	 */
	public boolean connect(SlotInterface s);

	/***
	 * De-connect a {@link SlotInterface} to the emitter
	 * 
	 * @param s
	 *            slot to de-connect
	 * @return if the slot it has be de-connected
	 */
	public boolean deconnect(SlotInterface s);

	/***
	 * Get the value type of this emitter.
	 * 
	 * @return the value type
	 */
	public ValueType getType();

	/***
	 * Get the label of this emitter
	 * 
	 * @return the label
	 */
	public String getLabel();

	/***
	 * Get the description of the emitter
	 * 
	 * @return the description
	 */
	public String getDescription();

	/***
	 * Trigger all the connected {@link SlotInterface} with a value
	 * 
	 * @param v
	 *            value to trig
	 */
	public void trigger(Value v);

	/***
	 * Set a value in the output buffer
	 * 
	 * @param v
	 *            value to buffer
	 */
	public void setValue(Value v);

	/***
	 * Get the last buffered value
	 * 
	 * @return buffered value
	 */
	public Value readValue();

	/***
	 * Look if the Emitter is free or not
	 * 
	 * @return true if at least the emitter has one connection
	 */
	public boolean isFree();

	/***
	 * return if is a Polyvalent emitter. In case it possible to connect any
	 * kind of slot, and the data consistency it will be not checked.
	 * 
	 * @return true if is a polyvalent emitter
	 */
	public boolean isPolyvalent();
}
