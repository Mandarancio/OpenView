/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.maker;

import gui.components.OVComponent;
import gui.interfaces.OVContainer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 * This is the actual class manager. It manages, trough a map, all the
 * components classes and the key of its.
 * 
 * @author martino
 */
public class OVClassManager {

	private final HashMap<String, Class<? extends OVComponent>> classes_ = new HashMap<>();

	/***
	 * "no modifier" constructor: In this way the constructor is not accessible
	 * outside this package.
	 */
	OVClassManager() {

	};

	/***
	 * Add a new class to the class manager. The key is used from the
	 * OVBaseMaker to associate the action to the correct Component class.
	 * 
	 * @param key
	 *            a more simple name used in the menus referring to the class
	 * @param c
	 *            the actual class of the component
	 */
	public void addClass(String key, Class<? extends OVComponent> c) {
		classes_.put(key, c);
	}

	/***
	 * Method to know if a key string is in the key-set
	 * 
	 * @param key
	 *            the key string of the class. (used in menus)
	 * @return true if the key is in the key-set
	 */
	public boolean hasClass(String key) {
		return classes_.containsKey(key);
	}

	/***
	 * Method to know if a class (using the simple class name) is on the map
	 * 
	 * @param name
	 *            simple name of the class (class.getSimpleName())
	 * @return true if the class is in the map
	 */
	public boolean hasClassByClassName(String name) {
		for (Class<?> c : classes_.values()) {
			if (c.getSimpleName().equals(name)) {
				return true;
			}
		}
		return false;
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
	public OVComponent newInstance(String key, OVContainer father) {
		Class<? extends OVComponent> c = classes_.get(key);
		if (c != null) {
			try {
				Constructor<?> con = getGUIConstructor(c);
				if (con != null) {
					return (OVComponent) con.newInstance(father);
				}
			} catch (InstantiationException ex) {
				Logger.getLogger(OVClassManager.class.getName()).log(
						Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				Logger.getLogger(OVClassManager.class.getName()).log(
						Level.SEVERE, null, ex);
			} catch (IllegalArgumentException ex) {
				Logger.getLogger(OVClassManager.class.getName()).log(
						Level.SEVERE, null, ex);
			} catch (InvocationTargetException ex) {
				Logger.getLogger(OVClassManager.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		return null;
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
	public OVComponent newInstance(String name, Element e, OVContainer father) {
		for (Class<? extends OVComponent> c : classes_.values()) {
			if (c.getSimpleName().equals(name)) {
				try {
					Constructor<?> con = getXMLConstructor(c);
					if (con != null) {
						return (OVComponent) con.newInstance(e, father);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return null;
	}

	/***
	 * print the key set of the manager [used for debug]
	 */
	void print() {
		System.out.println("Print:");
		System.out.println(classes_.keySet());
	}

	/***
	 * get the xml constructor of the OVComponent class selected (if any)
	 * 
	 * @param c
	 *            class of the OVComponent
	 * @return the xml constructor (if any)
	 */
	private Constructor<?> getXMLConstructor(Class<? extends OVComponent> c) {
		for (Constructor<?> con : c.getConstructors()) {
			if (con.getGenericParameterTypes().length == 2) {
				if (con.getGenericParameterTypes()[0] == Element.class
						&& con.getGenericParameterTypes()[1] == OVContainer.class) {
					return con;
				}
			}
		}
		return null;
	}

	/***
	 * get the gui (or simple) constructor of the OVComponent class selected (if
	 * any)
	 * 
	 * @param c
	 *            class of the OVComponent
	 * @return the gui constructor (if any)
	 */
	private Constructor<?> getGUIConstructor(Class<? extends OVComponent> c) {
		for (Constructor<?> con : c.getConstructors()) {
			if (con.getGenericParameterTypes().length == 1) {
				if (con.getGenericParameterTypes()[0] == OVContainer.class) {
					return con;
				}
			}
		}
		return null;
	}

}
