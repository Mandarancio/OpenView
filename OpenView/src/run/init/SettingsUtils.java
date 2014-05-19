package run.init;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import run.window.support.XMLBuilder;

public class SettingsUtils {
	private static Settings settings_ = new Settings();
	private static final String user_path = "/.openview/settings.xml";

	public static Settings getSettings() {
		return settings_;
	}

	public static void load() {
		String path = System.getProperty("user.home") + user_path;

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

	public static void save() {
		try {
			String path = System.getProperty("user.home") + user_path;
			File f = new File(path);
			Document doc = XMLBuilder.makeDoc();
			doc.appendChild(settings_.save(doc));
			XMLBuilder.saveDoc(doc, f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
