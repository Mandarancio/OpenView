package gui.constants;

import gui.components.OVComponent;

/***
 * Some generic names for {@link OVComponent} settings
 * 
 * @author martino
 * 
 */
public interface ComponentSettings {
	// some settings name
	/***
	 * X coordinate
	 */
	static final public String PosX = "x";
	/***
	 * Y coordinate
	 */
	static final public String PosY = "y";
	/***
	 * component width
	 */
	static final public String SizeW = "Width";
	/***
	 * component height
	 */
	static final public String SizeH = "Height";
	/***
	 * component name
	 */
	static final public String Name = "Name";
	/***
	 * component family
	 */
	static final public String Family = "Family";
	/***
	 * enabled component flag
	 */
	static final public String Enable = "Enabled";
	/***
	 * component background
	 */
	static final public String Background = "Background";
	/***
	 * component foreground
	 */
	static final public String Foreground = "Foreground";

	// some settings category
	/***
	 * Generic category
	 */
	static final public String GenericCategory = "Generic";
	/***
	 * Geometric category
	 */
	static final public String GeometryCategory = "Geometry";
	/***
	 * No category
	 */
	static final public String NoneCategory = "";
	/***
	 * Component specific category
	 */
	static final public String SpecificCategory = "Specific";
}
