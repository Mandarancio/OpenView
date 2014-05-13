package run;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class Test {

    private final static int TIMEOUT = 2000;
    private final static int command=137;
    public static void main(String[] args) {
        test(command);
        Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
        ArrayList<CommPortIdentifier> portList = new ArrayList<>();
        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports
                    .nextElement();
            // get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portList.add(curPort);
            }
        }

        if (portList.size() > 0) {
            SerialPort serial = connect(portList.get(0));

            if (serial != null) {
                InputStream input = makeInput(serial);
                initListener(serial, input);
                OutputStream output = makeOutput(serial);
                for (int i = 0; i < 10; i++) {
                    write(output, command);
                }
                System.out.println("Done");
                deconnect(serial, input, output);
            }
        }

    }

    public static void initListener(SerialPort serialPort, final InputStream input) {
        try {
            serialPort.addEventListener(new SerialPortEventListener() {

                @Override
                public void serialEvent(SerialPortEvent evt) {
                    System.err.println("HERE");
                    if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                        try {

                            byte singleData = (byte) input.read();

                            System.err.println("Data:" + singleData);
                        } catch (Exception e) {
                            e.printStackTrace();;
                        }
                    }
                }
            });
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    public static void deconnect(SerialPort serial, InputStream input,
            OutputStream output) {
        if (serial != null) {
            serial.close();
        }
        try {
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (output != null) {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(OutputStream output, int i) {
        try {
            output.write((byte) i);
            output.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static OutputStream makeOutput(SerialPort serial) {
        try {
            return serial.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream makeInput(SerialPort serial) {
        try {
            return serial.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SerialPort connect(CommPortIdentifier port) {
        String selectedPort = port.getName();
        System.out.println("Connect to : " + selectedPort);
        CommPort commPort = null;
        SerialPort serialPort = null;
        try {
            commPort = port.open("Test", TIMEOUT);

            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serialPort;
    }

    public static void test(int c) {
        int cmd = c & 0x07;
        int port = c & 0x1F;
        System.out.println(cmd+"("+port+")");
    }
}
