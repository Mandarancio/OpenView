package run.init;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/***
 * User settings class representation
 * 
 * @author martino
 * 
 */
public class Settings {
	/***
	 * Map of the active/de-active modules
	 */
	private HashMap<String, Boolean> modules_ = new HashMap<>();

	/***
	 * Hidden Constructor
	 */
	Settings() {
	}

	/***
	 * Get the active/de-active module map
	 * 
	 * @return the map
	 */
	public HashMap<String, Boolean> getModules() {
		return modules_;
	}

	/***
	 * Set the status of a specified module
	 * 
	 * @param module
	 *            Name of the module
	 * @param active
	 *            Status of the module
	 */
	public void addModule(String module, boolean active) {
		this.modules_.put(module, new Boolean(active));
	}

	/***
	 * Load the settings from an XML element
	 * 
	 * @param e
	 *            XML element
	 */
	public void load(Element e) {
		NodeList list = e.getElementsByTagName("module");
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			if (n instanceof Element) {
				Element el = (Element) n;
				if (el.getParentNode().equals(e)) {
					String name = el.getAttribute("name");
					Boolean value = Boolean.valueOf(el.getAttribute("enabled"));
					modules_.put(name, value);
				}
			}

		}
	}

	/***
	 * Save settings in a XML element
	 * 
	 * @param doc
	 *            XML document that will host the element
	 * @return XML element
	 */
	public Element save(Document doc) {
		Element e = doc.createElement(Settings.class.getSimpleName());
		for (String s : modules_.keySet()) {
			Element m = doc.createElement("module");
			m.setAttribute("name", s);
			m.setAttribute("enabled", modules_.get(s).toString());
			e.appendChild(m);
		}
		return e;
	}

	/***
	 * Check if a module is defined in the modules map
	 * 
	 * @param moduleName
	 *            name of the module
	 * @return
	 */
	public boolean hasModule(String moduleName) {
		return modules_.containsKey(moduleName);
	}

	/***
	 * Check the status of a module
	 * 
	 * @param module
	 *            name of the module
	 * @return the status of the module (if not defined return true)
	 */
	public boolean isEnable(String module) {
		if (hasModule(module)) {
			return modules_.get(module).booleanValue();
		}
		return true;
	}

	/***
	 * remove a module from the map
	 * 
	 * @param m
	 *            name of the module
	 */
	public void removeModule(String m) {
		modules_.remove(m);
	}
}
