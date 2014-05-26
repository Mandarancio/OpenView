package run;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import run.init.Init;
import run.init.SettingsUtils;
import run.init.Splash;
import run.window.Window;

/***
 * Where OpenView is launched
 * 
 * @author martino
 * 
 */
public class Main {

	/***
	 * Start OpenView
	 * 
	 * @param args
	 *            to be defined
	 */
	public static void main(String[] args) {
		Splash.show();
		Splash.setStatus("Load look and feel...");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		Init.init();
		Window w = new Window();
		Splash.hide();

		w.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SettingsUtils.save();
			}
		});
	}
}
