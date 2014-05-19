/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components.ovnode.arduino;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gui.components.ovnode.OVNodeBlock;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import java.awt.Point;
import java.io.OutputStream;
import java.util.Enumeration;

import org.w3c.dom.Element;

/**
 *
 * @author martino
 */
public class OVArduBlock extends OVNodeBlock implements ArduInterface {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6283342456463848354L;
	private SerialPort port_;
    private OutputStream output_;
    private boolean connected_ = false;

    public OVArduBlock(OVContainer father) {
        super(father);
        getSetting(ComponentSettings.Name).setValue("A.ino");
    }

    public OVArduBlock(Element e, OVContainer father) {
        super(e,father);
    }
    
    

    @Override
    public void setMode(EditorMode mode_) {
        if (mode_ != getMode()) {
            if (getMode().isExec()) {
                deconnect();
            } else if (mode_.isExec()) {
                connect();
            }
        }
        super.setMode(mode_); //To change body of generated methods, choose Tools | Templates.
    }

    private void deconnect() {
        connected_ = false;
        try {
            output_.close();
            port_.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports
                    .nextElement();
            // get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                CommPort commPort = null;
                try {
                    commPort = curPort.open(curPort.getName(), 2000);
                    port_ = (SerialPort) commPort;
                    output_ = port_.getOutputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connected_ = true;
                return;
            }
        }
    }

    @Override
    public void gpioWrite(int port, boolean flag) {
        if (!connected_) {
            return;
        }
        int cmd = flag ? 1 : 0;
        int b = (port << 3) + cmd;
        System.err.println(flag + ", " + b);

        try {
            output_.write(b);
            output_.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMenu(Point point) {
//        super.showMenu(point, OVMaker.OVMakerMode.ARDUINO);
    }
}
