package core.support;

import core.ValueType;
import gui.components.ovgui.OVTextArea.TextAreaTrigger;
import gui.components.ovgui.OVTextField.TextFieldTrigger;
import gui.components.ovnode.OVFileDialog.FileDialogMode;
import gui.components.ovnode.OVTimerTriggerNode.TimerMode;
import gui.components.ovnode.enums.TriggerMode;

import java.util.ArrayList;

/***
 * Manage the enums defined and used in the editor. (used to load and parse from
 * xml and values)
 * 
 * @author martino
 * 
 */
public class EnumManager {

	/***
	 * list of enums defined
	 */
	private static final ArrayList<Class<? extends Enum<?>>> enums_ = new ArrayList<>();

	/***
	 * initialziation of the basic enums defined in the main project
	 */
	public static void init() {
		enums_.add(FontStyle.class);
		enums_.add(OrientationEnum.class);
		enums_.add(TextAreaTrigger.class);
		enums_.add(TextFieldTrigger.class);
		enums_.add(TriggerMode.class);
		enums_.add(TimerMode.class);
		enums_.add(ValueType.class);
		enums_.add(FileDialogMode.class);
	}

	/***
	 * add enum class to the static list (used for add custom enums)
	 * 
	 * @param enumClass
	 *            the new enum class
	 */
	public static void addEnum(Class<? extends Enum<?>> enumClass) {
		enums_.add(enumClass);
	}

	/***
	 * parse an enum from a string of this type: ENUMNAME:ENUMVALUE
	 * 
	 * @param s
	 *            string to parse (ex: ValueType:DOUBLE)
	 * @return the actual enum parsed (if it's possible)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Enum<?> parseEnum(String s) {

		String className = s.split(":")[0];
		String value = s.split(":")[1];
		for (Class<? extends Enum> c : enums_) {
			if (c.getSimpleName().equals(className)) {
				return Enum.valueOf(c, value);
			}
		}
		return null;
	}

}
