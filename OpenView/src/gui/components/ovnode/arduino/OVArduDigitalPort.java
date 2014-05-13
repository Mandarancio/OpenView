/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components.ovnode.arduino;

import core.Setting;
import core.Value;
import gui.components.ovnode.OVNodeComponent;
import gui.constants.ComponentSettings;
import gui.interfaces.OVContainer;
import org.w3c.dom.Element;

/**
 *
 * @author martino
 */
public class OVArduDigitalPort extends OVNodeComponent {

    private static final String _Value = "Value", _Port = "Port";
    private ArduInterface arduino_;

    public OVArduDigitalPort(OVContainer father) {
        super(father);
        getSetting(ComponentSettings.Name).setValue("GPIO");
        Setting s = new Setting(_Value, false);
        addBothSetting(ComponentSettings.SpecificCategory, s);
        s = new Setting(_Port, 17, 0, 53);
        addNodeSetting(ComponentSettings.SpecificCategory, s);
        s.setGuiMode(false);
        if (father instanceof ArduInterface) {
            arduino_ = (ArduInterface) father;
        }
    }

    public OVArduDigitalPort(Element e, OVContainer father) {
        super(e, father);
        if (father instanceof ArduInterface) {
            arduino_ = (ArduInterface) father;
        }
    }

    @Override
    public void setFather(OVContainer father) {
        super.setFather(father); //To change body of generated methods, choose Tools | Templates.
        if (father instanceof ArduInterface) {
            arduino_ = (ArduInterface) father;
        }

    }

    @Override
    public void valueUpdated(Setting s, Value v) {
        if (s.getName().equals(_Value)) {
            try {
                if (arduino_ != null) {
                    int port = getNodeSetting(_Port).getValue().getInt();
                    arduino_.gpioWrite(port, v.getBoolean());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.valueUpdated(s, v); //To change body of generated methods, choose Tools | Templates.
    }

}
