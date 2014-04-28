package core;

import java.awt.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import core.support.Utils;

public class Value {

	private Object data_;
	private ValueDescriptor descriptor_;

	public Value(ValueDescriptor desc) {
		descriptor_ = desc;
		data_ = null;
	}

	public Value(Object obj) {
		descriptor_ = new ValueDescriptor(obj);
		switch (getType()) {
		case FLOAT:
			data_ = new Float((Float) obj);
			break;
		case DOUBLE:
			data_ = new Double((Double) obj);
			break;
		case INTEGER:
			data_ = new Integer((Integer) obj);
			break;
		case LONG:
			data_ = new Long((Long) obj);
			break;
		case SHORT:
			data_ = new Short((Short) obj);
			break;
		case BYTE:
			data_ = new Byte((Byte) obj);
			break;
		case STRING:
			data_ = obj.toString();
			break;
		case BOOLEAN:
			data_ = new Boolean((Boolean) obj);
			break;
		default:
			data_ = obj;
			break;
		}

	}

	public Value(String val, ValueType type) {
		descriptor_ = new ValueDescriptor(type);
		data_ = type.parse(val);
	}

	public Value(int val) {
		data_ = new Integer(val);
		descriptor_ = new ValueDescriptor(ValueType.INTEGER);
	}

	public Value(double val) {
		data_ = new Double(val);
		descriptor_ = new ValueDescriptor(ValueType.DOUBLE);
	}

	public Value(boolean val) {
		data_ = new Boolean(val);
		descriptor_ = new ValueDescriptor(ValueType.BOOLEAN);
	}

	public ValueDescriptor getDescriptor() {
		return descriptor_;
	}

	public Object getData() {
		return data_;
	}

	public void setData(String val, ValueType type) {
		if (!type.isCompatible(getType()))
			return;
		data_ = type.parse(val);
	}

	public boolean setData(Object data) {
		if (descriptor_.getType().isCompatible(ValueType.getType(data))) {
			this.data_ = data;

			return true;
		}
		return false;
	}

	public boolean setData(int data) {
		return setData(new Integer(data));
	}

	public boolean setData(double data) {
		return setData(new Double(data));
	}

	public boolean setData(boolean data) {
		return setData(new Boolean(data));
	}

	public double getDouble() throws Exception {
		if (descriptor_.getType().isNumeric()) {
			return ((Number) data_).doubleValue();
		} else
			throw new Exception("Not numeric!");
	}

	public int getInt() throws Exception {
		if (descriptor_.getType().isNumeric()) {
			return ((Number) data_).intValue();
		} else
			throw new Exception("Not numeric!");

	}

	public String getString() {
		if (data_ == null)
			return "void";
		else if (data_ instanceof Void)
			return "void";
		else if (getType() == ValueType.COLOR)
			return Utils.codeColor((Color) data_);
		return data_.toString();
	}

	public Enum<?> getEnum() throws Exception {
		if (getDescriptor().getType() == ValueType.ENUM)
			return (Enum<?>) data_;
		throw new Exception("Not Enum!");
	}

	public Element getXML(Document doc) {
		Element e = doc.createElement("value");
		e.setAttribute("val", getString());
		e.appendChild(descriptor_.getXML(doc));
		return e;
	}

	public Color getColor() throws Exception {
		if (getType() == ValueType.COLOR && data_ != null) {
			return (Color) data_;
		}
		throw new Exception("No data!");
	}

	public ValueType getType() {
		return descriptor_.getType();
	}

	public long getLong() throws Exception {
		if (getType().isNumeric())
			return ((Number) data_).longValue();
		throw new Exception("Data is not a long!");

	}

	public boolean getBoolean() throws Exception {
		if (getType() == ValueType.BOOLEAN)
			return ((Boolean) data_).booleanValue();
		throw new Exception("Data is not a boolean!");

	}

	@Override
	public String toString() {
		return "(" + getString() + "," + getType() + ")";
	}

	public short getShort() throws Exception {
		if (getType().isNumeric())
			return ((Number) data_).shortValue();
		throw new Exception("Data is not a number");
	}

	public static Value parse(String s) {
		Value val;
		if (s.length() == 0 || s.equals("void")) {
			val = new Value(Void.TYPE);
		} else if (s.equals("true")) {
			val = new Value(true);
		} else if (s.equals("false")) {
			val = new Value(false);

		} else if (s.contains(".")) {
			try {
				val = new Value(Double.valueOf(s));
			} catch (NumberFormatException e) {
				val = new Value(s);
			}
		} else {
			try {
				val = new Value(Integer.valueOf(s));
			} catch (NumberFormatException e) {
				val = new Value(s);
			}
		}
		return val;
	}
}
