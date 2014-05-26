package core;

/***
 * Simple {@link SlotInterface} listener
 * 
 * @author martino
 * 
 */
public interface SlotListener {
	/***
	 * Trigger this method when a {@link Value} is received from the
	 * {@link Slot}
	 * 
	 * @param s
	 *            {@link Slot} that received the {@link Value}
	 * @param v
	 *            {@link Value} received
	 */
	public void valueRecived(SlotInterface s, Value v);

}
