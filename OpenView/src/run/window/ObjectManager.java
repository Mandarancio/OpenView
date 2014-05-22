package run.window;

import gui.components.OVComponent;
import gui.constants.ComponentSettings;

import java.util.ArrayList;

public class ObjectManager {
	private static ArrayList<OVComponent> components_ = new ArrayList<>();

	public static void addComponent(OVComponent c) {
		if (!components_.contains(c))
			components_.add(c);
	}

	public static void removeComponent(OVComponent c) {
		components_.remove(c);
	}

	public static boolean hasComponent(String name) {
		for (OVComponent c : components_) {
			String n = c.getSetting(ComponentSettings.Name).getValue()
					.getString();
			if (n.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static OVComponent getComponent(String name) {
		for (OVComponent c : components_) {
			String n = c.getSetting(ComponentSettings.Name).getValue()
					.getString();
			if (n.equals(name)) {
				return c;
			}
		}
		return null;
	}
}
