/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.maker;

import gui.components.OVComponent;
import gui.interfaces.OVContainer;
import org.w3c.dom.Element;

/**
 * Class used to access statically at the Class Manager ({@link OVClassManager}
 * ). It manage all the components class and permit to add new classes during
 * the run time.
 * 
 * @author martino
 */
public final class OVClassFactory {

	/***
	 * The real manager of the classes
	 */
	private static OVClassManager manager_ = new OVClassManager();

	/***
	 * Direct access to the class manager
	 * 
	 * @return the class manager
	 */
	static public OVClassManager getManager() {
		return manager_;
	}

	/***
	 * Add a new class to the class manager. The key is used from the
	 * OVBaseMaker to associate the action to the correct Component class.
	 * 
	 * @param key
	 *            a more simple name used in the menus referring to the class
	 * @param c
	 *            the actual class of the component
	 */
	static public void addClass(String key, Class<? extends OVComponent> c) {
		manager_.addClass(key, c);
	}

	/***
	 * Create a new instance of a component using the key string to get the
	 * class.
	 * 
	 * @param key
	 *            the key string of the class. (used in menus)
	 * @param father
	 *            the container that will host the component
	 * @return the component. It will be null if the key is not in the key-set.
	 */
	static public OVComponent getInstance(String key, OVContainer father) {
		return manager_.newInstance(key, father);
	}

	/***
	 * Create a new instance of a component using the simple class name as key
	 * and a xml element to define it's own properties. (used for loading files
	 * and copy/paste)
	 * 
	 * @param name
	 *            simple name of the class (class.getSimpleName())
	 * @param e
	 *            xml element that define the properties of the new element
	 * @param father
	 *            the container that will host the component
	 * @return the component. It will be null if the class is not in the map
	 */
	static public OVComponent getInstance(String name, Element e,
			OVContainer father) {
		return manager_.newInstance(name, e, father);
	}

	/***
	 * Method to know if a key string is in the key-set
	 * 
	 * @param key
	 *            the key string of the class. (used in menus)
	 * @return true if the key is in the key-set
	 */
	static public boolean hasClass(String key) {
		return manager_.hasClass(key);
	}

	/***
	 * Method to know if a class (using the simple class name) is on the map
	 * 
	 * @param name
	 *            simple name of the class (class.getSimpleName())
	 * @return true if the class is in the map
	 */
	static public boolean hasClassByClassName(String name) {
		return manager_.hasClassByClassName(name);
	}

	/**
	 * print the status of the manager.
	 */
	static public void print() {
		manager_.print();
	}

}
