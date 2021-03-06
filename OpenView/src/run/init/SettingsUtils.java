package run.init;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import run.window.support.XMLBuilder;

/***
 * Utils to manage the user settings and {@link Settings} class
 * 
 * @author martino
 * 
 */
public class SettingsUtils {
	/**
	 * static settings object
	 */
	private static Settings settings_ = new Settings();

	/***
	 * Direct access to the settings object
	 * 
	 * @return the settings object
	 */
	public static Settings getSettings() {
		return settings_;
	}

	/***
	 * Load the user settings
	 */
	public static void load() {
		String path = FilesUtil.settingsFilePath();

		File f = new File(path);
		if (f.exists()) {
			try {
				Document doc = XMLBuilder.loadDoc(f);
				settings_.load(doc.getDocumentElement());
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/***
	 * cleanup the non-existing modules from the settings
	 */
	public static void cleanup() {
		ArrayList<String> mods = new ArrayList<>(settings_.getModules()
				.keySet());
		for (String m : mods) {
			if (!ModuleUtil.exist(m)) {
				settings_.removeModule(m);
			}
		}
	}

	/***
	 * save user settings
	 */
	public static void save() {
		try {
			String path = FilesUtil.settingsFilePath();
			File f = new File(path);
			Document doc = XMLBuilder.makeDoc();
			doc.appendChild(settings_.save(doc));
			XMLBuilder.saveDoc(doc, f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
