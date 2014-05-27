package gui.interfaces;

import gui.support.Setting;
import core.Value;

/***
 * setting listener
 * 
 * @author martino
 * 
 */
public interface SettingListener {
	/***
	 * method to handle an update of the setting
	 * 
	 * @param setting
	 *            setting changed
	 * @param v
	 *            updated value
	 */
	public void valueUpdated(Setting setting, Value v);
}
