package core.support;

import gui.components.ovgui.OVTextArea.TextAreaTrigger;
import gui.components.ovgui.OVTextField.TextFieldTrigger;
import gui.components.ovnode.OVTimerTriggerNode.TimerMode;
import gui.components.ovnode.enums.TriggerMode;

import java.util.ArrayList;

public class EnumManager {
	private static ArrayList<Class<? extends Enum<?>>> enums_ = new ArrayList<>();

	public static void init() {
		enums_.add(FontStyle.class);
		enums_.add(OrientationEnum.class);
		enums_.add(TextAreaTrigger.class);
		enums_.add(TextFieldTrigger.class);
		enums_.add(TriggerMode.class);
		enums_.add(TimerMode.class);
	}

	public static void addEnum(Class<? extends Enum<?>> enumClass) {
		enums_.add(enumClass);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Enum<?> parseEnum(String s) {

		String className = s.split(":")[0];
		String value = s.split(":")[1];
		for (Class<? extends Enum<?>> c : enums_) {
			if (c.getSimpleName().equals(className)) {
				Class<Enum> enumClass = (Class<Enum>) c;
				return Enum.valueOf(enumClass, value);
			}
		}
		return null;
	}
}
