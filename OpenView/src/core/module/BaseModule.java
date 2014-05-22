package core.module;

import evaluator.functions.Function;
import evaluator.operators.Operator;
import gui.components.OVComponent;

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

	public ArrayList<JMenu> getNodeMenus() {
		return new ArrayList<>();
	}

	public ArrayList<JMenu> getGuiMenus() {
		return new ArrayList<>();
	}

	public HashMap<String, Class<? extends OVComponent>> getComponents() {
		return new HashMap<>();
	}

	public ArrayList<Class<? extends Enum<?>>> getEnums() {
		return new ArrayList<>();
	}

	public ArrayList<Function> getFunctions() {
		return new ArrayList<>();
	}

	public ArrayList<Operator> getOperators() {
		return new ArrayList<>();
	}

	public String getPath() {
		return path_;
	}

	public void setPath(String path_) {
		this.path_ = path_;
	}

}
