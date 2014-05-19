package core.module;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenu;

public abstract class BaseModule {
	private String name_;
	private String version_;
	private String path_ = "";

	public BaseModule(String name, String version) {
		name_ = name;
		version_ = version;
	}

	public String getModuleName() {
		return name_;
	}

	public String getVersion() {
		return version_;
	}

	public abstract ArrayList<JMenu> getNodeMenus();

	public abstract ArrayList<JMenu> getGuiMenus();

	public abstract HashMap<String, Class<?>> getComponents();

	public String getPath() {
		return path_;
	}

	public void setPath(String path_) {
		this.path_ = path_;
	}

}
