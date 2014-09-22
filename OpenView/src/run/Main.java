package run;

import java.security.Policy;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import run.init.Init;
import run.init.Splash;
import run.init.policy.PluginPolicy;
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
		
		Policy.setPolicy(new PluginPolicy());
		System.setSecurityManager(new SecurityManager());
		
		String prop = System.getProperty("java.ext.dirs");
		System.out.println(prop);
		prop += ":" + "/home/martino/.openview/modules/OVPlot" + ":"
				+ "/home/martino/.openview/modules/OVPlot/jars";
		System.setProperty("java.ext.dirs", prop);
		System.out.println(System.getProperty("java.ext.dirs"));

		// Show splash screen
		Splash.show();
		// Set systen look and feel
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
		// Initialize all the sub modules
		Init.init();
		// Show up the main window
		new Window();
		// Hide the splash screen
		Splash.hide();
	}
}
