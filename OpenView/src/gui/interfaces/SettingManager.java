package gui.interfaces;

import gui.components.OVComponent;
import gui.enums.EditorMode;

public interface SettingManager {
	public void select(OVComponent c);

	public void deselect();
	
	public void setMode(EditorMode mode);
}
