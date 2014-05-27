package gui.interfaces;

import gui.components.OVComponent;
import gui.enums.EditorMode;

/***
 * Simple interface of a setting manager panel
 * 
 * @author martino
 * 
 */
public interface SettingManager {
	/***
	 * Select a component
	 * 
	 * @param c
	 *            selected component
	 */
	public void select(OVComponent c);

	/***
	 * deselect current component
	 */
	public void deselect();

	/***
	 * Set edit mode
	 * 
	 * @param mode
	 *            edit mode
	 */
	public void setMode(EditorMode mode);
}
