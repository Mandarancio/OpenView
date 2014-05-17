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
 *
 * @author martino
 */
public class OVClassManager {

    private final HashMap<String, Class<?>> classes_ = new HashMap<>();

    public void addClass(String key, Class<?> c) {
        classes_.put(key, c);
    }

    public boolean hasClass(String key) {
        return classes_.containsKey(key);
    }

    public boolean hasClassByClassName(String name) {
        for (Class<?> c : classes_.values()) {
            if (c.getSimpleName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public OVComponent newInstance(String key, OVContainer father) {
        Class<?> c = classes_.get(key);
        if (c != null) {
            try {
                Constructor con=getGUIConstructor(c);
                if (con!=null){
                    return (OVComponent)con.newInstance(father);
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public OVComponent newInstance(String name, Element e, OVContainer father) {
        for (Class<?> c : classes_.values()) {
            if (c.getSimpleName().equals(name)) {
                try {
                    Constructor con = getXMLConstructor(c);
                    if (con != null) {
                        return (OVComponent) con.newInstance(e, father);
                    }
                } catch (InstantiationException ex) {
                    Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(OVClassManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    void print() {
        System.out.println("Print:");
        System.out.println(classes_.keySet());
    }

    private Constructor getXMLConstructor(Class c) {
        for (Constructor con : c.getConstructors()) {
            if (con.getGenericParameterTypes().length == 2) {
                if (con.getGenericParameterTypes()[0] == Element.class && con.getGenericParameterTypes()[1] == OVContainer.class) {
                    return con;
                }
            }
        }
        return null;
    }

    private Constructor getGUIConstructor(Class c) {
        for (Constructor con : c.getConstructors()) {
            if (con.getGenericParameterTypes().length == 1) {
                if (con.getGenericParameterTypes()[0] == OVContainer.class) {
                    return con;
                }
            }
        }
        return null;
    }

}
