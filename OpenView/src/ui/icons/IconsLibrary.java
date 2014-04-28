package ui.icons;

import java.util.Hashtable;

import javax.swing.ImageIcon;

public class IconsLibrary {
	
    private static Hashtable<String, ImageIcon> icons                              = new Hashtable<>();
    public static String AppIcon="OpenView.png";
    
    public static ImageIcon getIcon(String iconFileName)
    {
        if (icons.containsKey(iconFileName))
            return icons.get(iconFileName);

        ImageIcon icon = new ImageIcon(IconsLibrary.class.getResource(iconFileName));
        icons.put(iconFileName, icon);
        return icon;
    }

    public static ImageIcon getIcon(Object c, String iconFileName)
    {
        if (icons.containsKey(iconFileName))
            return icons.get(iconFileName);
        ImageIcon icon = new ImageIcon(c.getClass().getResource(iconFileName));
        icons.put(iconFileName, icon);
        return icon;
    }
}
