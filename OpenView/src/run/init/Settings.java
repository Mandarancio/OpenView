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
	private HashMap<String, Boolean> modules_ = new HashMap<>();

	Settings() {
	}

	public HashMap<String, Boolean> getModules() {
		return modules_;
	}

	public void addModule(String module, boolean active) {
		this.modules_.put(module, new Boolean(active));
	}

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

	public boolean hasModule(String moduleName) {
		return modules_.containsKey(moduleName);
	}

	public boolean isEnable(String module) {
		if (hasModule(module)) {
			return modules_.get(module).booleanValue();
		}
		return true;
	}

	public void removeModule(String m) {
		modules_.remove(m);
	}
}
