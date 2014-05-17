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
 *
 * @author martino
 */
public class OVClassFactory {

    private static OVClassManager manager_ = new OVClassManager();

    static public OVClassManager getManager() {
        return manager_;
    }

    static public void addClass(String key, Class<OVComponent> c) {
        manager_.addClass(key, c);
    }

    static public OVComponent getInstance(String key, OVContainer father) {
        return manager_.newInstance(key, father);
    }

    static public OVComponent getInstance(String name, Element e, OVContainer father) {
        return manager_.newInstance(name, e, father);
    }

    static public boolean hasClass(String key) {
        return manager_.hasClass(key);
    }

    static public boolean hasClassByClassName(String name) {
        return manager_.hasClassByClassName(name);
    }

    static public void print() {
        manager_.print();
    }

}
