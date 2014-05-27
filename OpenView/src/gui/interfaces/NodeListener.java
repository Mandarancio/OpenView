package gui.interfaces;

/***
 * Node listener
 * 
 * @author martino
 * 
 */
public interface NodeListener {
	/***
	 * method triggered when a node is connected
	 * 
	 * @param n
	 *            node connected
	 */
	public void connected(OVNode n);

	/***
	 * method triggered when a node is deconnected
	 * 
	 * @param n
	 *            node deconnected
	 */
	public void deconneced(OVNode n);
}
