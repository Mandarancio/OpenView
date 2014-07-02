package core.module;

import evaluator.functions.Function;
import evaluator.functions.FunctionManager;
import evaluator.operators.Operator;
import evaluator.operators.OperatorManager;
import gui.components.OVComponent;
import gui.settings.viewers.Viewer;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenu;

import core.maker.OVClassManager;
import core.maker.OVMenuManager;
import core.support.EnumManager;
import core.support.Rule;

/***
 * This is the class to define the extra module api. Each module should
 * implement is own Base class that extend this class.
 * 
 * @author martino
 * 
 */
public abstract class BaseModule {
	/***
	 * Informal name of the module
	 */
	private String name_;
	/***
	 * Module version (as string)
	 */
	private String version_;
	/***
	 * Path where the module is stored
	 */
	private String path_ = "";
	/***
	 * author of the module
	 */
	private String author_ = "";

	/***
	 * author mail
	 */
	private String mailAddress_ = "";

	/***
	 * Define the basic module information in the class constructor
	 * 
	 * @param name
	 *            of the module
	 * @param version
	 *            of the module
	 * @param author
	 *            of the module
	 * @param mail
	 *            mail address of the author of the module
	 */
	public BaseModule(String name, String version, String author, String mail) {
		name_ = name;
		version_ = version;
		setMailAddress(mail);
		setAuthor(author);
	}

	/***
	 * Get the module name
	 * 
	 * @return name of the module
	 */
	public String getModuleName() {
		return name_;
	}

	/***
	 * Get the module version as string
	 * 
	 * @return version
	 */
	public String getVersion() {
		return version_;
	}

	/***
	 * return the list of the node related sub-menus to add to the
	 * {@link OVMenuManager}
	 * 
	 * @return the list of sub-menus
	 */
	public ArrayList<JMenu> getNodeMenus() {
		return new ArrayList<>();
	}

	/***
	 * return the list of the gui related sub-menus to add to the
	 * {@link OVMenuManager}
	 * 
	 * @return the list of sub-menus
	 */
	public ArrayList<JMenu> getGuiMenus() {
		return new ArrayList<>();
	}

	public HashMap<Rule, Class<? extends Viewer>> getViewer() {
		return new HashMap<>();
	}

	/***
	 * return the map (key string and class) of the components classes to add to
	 * the {@link OVClassManager}
	 * 
	 * @return the components classes map
	 */
	public HashMap<String, Class<? extends OVComponent>> getComponents() {
		return new HashMap<>();
	}

	/***
	 * return the list of the enums defined in the module to add to the
	 * {@link EnumManager}
	 * 
	 * @return list of enums
	 */
	public ArrayList<Class<? extends Enum<?>>> getEnums() {
		return new ArrayList<>();
	}

	/***
	 * list of functions defined in the modules to add to the
	 * {@link FunctionManager}
	 * 
	 * @return list of functions
	 */
	public ArrayList<Function> getFunctions() {
		return new ArrayList<>();
	}

	/***
	 * list of operators defined in the modules to add to the
	 * {@link OperatorManager}
	 * 
	 * @return list of operators
	 */
	public ArrayList<Operator> getOperators() {
		return new ArrayList<>();
	}

	/***
	 * get the jar module path
	 * 
	 * @return the module path
	 */
	public String getPath() {
		return path_;
	}

	/***
	 * set the jar path
	 * 
	 * @param path
	 *            of the module.jar
	 */
	public void setPath(String path) {
		this.path_ = path;
	}

	/**
	 * get the author of the module
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author_;
	}

	/***
	 * set the author of the module
	 * 
	 * @param author
	 * 
	 */
	protected void setAuthor(String author) {
		this.author_ = author;
	}

	/***
	 * get the mail adress of the authod
	 * 
	 * @return mail adress
	 */
	public String getMailAddress() {
		return mailAddress_;
	}

	/***
	 * Set the mail adress of the author
	 * 
	 * @param mailAdress
	 *            mail adress
	 * 
	 */
	protected void setMailAddress(String mailAdress) {
		this.mailAddress_ = mailAdress;
	}

}
