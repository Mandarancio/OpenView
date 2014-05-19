package run.init;

import ui.splash.JSplash;

public class Splash {
	private static JSplash splash = new JSplash();

	public static void show() {
		splash.setVisible(true);
	}

	public static void hide() {
		splash.setVisible(false);
	}

	public static void setStatus(String str) {
		splash.setStatus(str);
	}
}
