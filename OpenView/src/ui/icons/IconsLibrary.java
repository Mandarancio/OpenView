package ui.icons;

import java.util.Hashtable;

import javax.swing.ImageIcon;

public class IconsLibrary {

	private static Hashtable<String, ImageIcon> icons = new Hashtable<>();
	public static String AppIcon = "OpenView.png";
	public static String FileOpen = "file-open.png";
	public static String FileSave = "file-save.png";
	public static String Run = "run.png";
	public static String RunDebug = "run-debug.png";
	public static String Stop = "stop.png";

	public static ImageIcon getIcon(String iconFileName) {
		if (icons.containsKey(iconFileName))
			return icons.get(iconFileName);

		ImageIcon icon = new ImageIcon(
				IconsLibrary.class.getResource(iconFileName));
		icons.put(iconFileName, icon);
		return icon;
	}

	public static ImageIcon getIcon(Object c, String iconFileName) {
		if (icons.containsKey(iconFileName))
			return icons.get(iconFileName);
		ImageIcon icon = new ImageIcon(c.getClass().getResource(iconFileName));
		icons.put(iconFileName, icon);
		return icon;
	}

	public static ImageIcon getIcon(String iconFileName, int size) {
		ImageIcon icon;
		if (icons.containsKey(iconFileName))
			icon = icons.get(iconFileName);
		else {
			icon = new ImageIcon(IconsLibrary.class.getResource(iconFileName));
			icons.put(iconFileName, icon);
		}
		return new ImageIcon(icon.getImage().getScaledInstance(size, size,
				java.awt.Image.SCALE_SMOOTH));

	}
}
