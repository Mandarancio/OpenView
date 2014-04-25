package gui.interfaces;

import core.Setting;
import core.Value;

public interface SettingListener {
	public void valueUpdated(Setting setting,Value v);
}
