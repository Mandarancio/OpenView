package run.init;

import ui.splash.JSplash;

/***
 * Simple splash util
 * 
 * @author martino
 * 
 */
public class Splash {
	private static JSplash splash = new JSplash();

	/***
	 * Show the splash
	 */
	public static void show() {
		splash.setVisible(true);
	}

	/***
	 * Hide the splash
	 */
	public static void hide() {
		splash.setVisible(false);
	}

	/***
	 * Update the status of the splash
	 * 
	 * @param str
	 *            string to visualize in the splash
	 */
	public static void setStatus(String str) {
		splash.setStatus(str);
	}
}
