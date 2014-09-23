package run.window;

import gui.enums.EditorMode;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import run.init.SettingsUtils;
import run.window.support.XMLBuilder;
import ui.icons.IconsLibrary;

/***
 * OpenView Window
 * 
 * @author martino
 * 
 */
public class Window extends JFrame {
	/**
	 * uid
	 */
	private static final long serialVersionUID = 7446192599263749847L;
	/***
	 * Main Panel
	 */
	private MainPanel panel_;

	/***
	 * Initialize geometric and functional informations
	 */
	public Window() {
		initGUI(false);
	}

	private void initGUI(boolean runMode) {
		this.setTitle("Open View");
		this.setMinimumSize(new Dimension(600, 500));
		this.setIconImage(IconsLibrary.getIcon(IconsLibrary.AppIcon).getImage());
		panel_ = new MainPanel(runMode);
		this.setContentPane(panel_);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setVisible(true);

		this.setSize(900, 700);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SettingsUtils.save();
			}
		});
	}

	public Window(String path, boolean run) {
		initGUI(run);
		load(path, run);
	}

	public void load(String path, boolean run) {
		File f = new File(path);
		if (f.exists()) {
			try {
				Document doc = XMLBuilder.loadDoc(f);
				panel_.getEditor().loadXML(doc.getDocumentElement());
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}

		}
		if (run)
			panel_.getEditor().setMode(EditorMode.RUN);
	}
}
