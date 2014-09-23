package run;

import java.security.Policy;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import run.init.Init;
import run.init.Splash;
import run.init.policy.PluginPolicy;
import run.window.Window;
//--open /home/martino/Documents/OpenView/test_george.xml
/***
 * Where OpenView is launched
 * 
 * @author martino
 * 
 */
public class Main {
	private static String open_arg = "--open";
	private static String run_arg = "--run";

	/***
	 * Start OpenView
	 * 
	 * @param args
	 *            to be defined
	 */
	public static void main(String[] args) {

		Policy.setPolicy(new PluginPolicy());
		System.setSecurityManager(new SecurityManager());

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

		if (args.length > 0) {
			boolean open = false, run = false;
			String path = "";
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals(open_arg)) {
					open = true;
					run = false;
					if (i < args.length - 1)
						path = args[i + 1];
					else
						open = false;
				} else if (args[i].equals(run_arg)) {
					open = false;
					run = true;
					if (i < args.length - 1)
						path = args[i + 1];
					else
						run = false;
				}
			}
			if (!open && !run) {
				// Show up the main window
				new Window();
			} else if (open) {
				new Window(path, false);
			} else if (run) {
				new Window(path, true);
			}

		} else {
			// Show up the main window
			new Window();
		}
		// Hide the splash screen
		Splash.hide();
	}
}
