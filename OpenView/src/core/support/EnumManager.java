package core.support;

import gui.components.ovgui.OVTextArea.TextAreaTrigger;
import gui.components.ovgui.OVTextField.TextFieldTrigger;
import gui.components.ovgui.plot.OVPlot.PlotType;
import gui.components.ovnode.OVTimerTriggerNode;
import gui.components.ovnode.enums.TriggerMode;

public class EnumManager {

    public static Enum<?> parseEnum(String s) {

        String className = s.split(":")[0];
        String value = s.split(":")[1];

        if (className.equals("OrientationEnum")) {
            return OrientationEnum.valueOf(value);
        }
        if (className.equals("FontStyle")) {
            return FontStyle.valueOf(value);
        }
        if (className.equals("TextAreaTrigger")) {
            return TextAreaTrigger.valueOf(value);
        }
        if (className.equals("TextFieldTrigger")) {
            return TextFieldTrigger.valueOf(value);
        }
        if (className.equals("TriggerMode")) {
            return TriggerMode.valueOf(value);
        }
        if (className.equals("TimerMode")) {
            return OVTimerTriggerNode.TimerMode.valueOf(value);
        }
        if (className.equals("PlotType")){
        	return PlotType.valueOf(value);
        }
        return null;
    }
}
