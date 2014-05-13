package run;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

import core.Value;
import core.ValueDescriptor;
import core.ValueType;

public class Test {
	public static void main(String[] args) {
		Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
		ValueDescriptor vd=new ValueDescriptor(ValueType.STRING);
		
		while (ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier) ports
					.nextElement();

			// get only serial ports
			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					vd.addPossibility(new Value(curPort.getName(),ValueType.STRING));
			}
		}
		
		Value v=new Value(vd);
		v.setData(vd.getPossibilities().get(0).getData());
		System.out.println(v);
	}
}
