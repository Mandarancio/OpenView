package run;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

public class Test {
	private final static int TIMEOUT = 2000;

	public static void main(String[] args) {
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
				OutputStream output = makeOutput(serial);
				write(output, 10);
				deconnect(serial, input, output);
			}
		}
		System.exit(0);
	}

	private static void deconnect(SerialPort serial, InputStream input,
			OutputStream output) {
		if (serial != null)
			serial.close();
		try {
			if (input != null)
				input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (output != null)
				output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(OutputStream output, int i) {
		try {
			output.write(i);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serialPort;
	}
}
