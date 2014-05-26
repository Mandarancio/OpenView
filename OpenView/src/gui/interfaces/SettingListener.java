package gui.interfaces;

import gui.support.Setting;
import core.Value;

public interface SettingListener {
	public void valueUpdated(Setting setting,Value v);
}
