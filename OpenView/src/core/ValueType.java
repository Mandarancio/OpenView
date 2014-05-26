package core;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import core.Message.MessageType;
import core.support.EnumManager;
import core.support.Utils;

/***
 * Value Type describe the type of all the major object used in the project
 * 
 * @author martino
 * 
 */
public enum ValueType {
	NONE, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, STRING, ARRAY, VOID, MESSAGE, COLOR, ENUM, COMPLEX, FILE, PORT;

	/***
	 * Is the type a numeric value
	 * 
	 * @return true if is numeric
	 */
	public boolean isNumeric() {
		return (this == BYTE || this == SHORT || this == INTEGER
				|| this == LONG || this == FLOAT || this == DOUBLE);
	}

	/***
	 * Check the comaptibility between this and another {@link ValueType}. To be
	 * compatible the two types should be equals or NONE/VOID or numerics
	 * 
	 * @param type
	 *            type to compare
	 * @return if are compatible
	 */
	public boolean isCompatible(ValueType type) {
		if (this == type || this == NONE || this == VOID)
			return true;
		if (this.isNumeric() && type.isNumeric())
			return true;
		return false;
	}

	/***
	 * Compute the value type of a generic Object. None if unknown.
	 * 
	 * @param obj
	 *            Object to check
	 * @return value type computed ( ValueType.NONE if unknown)
	 */
	static public ValueType getType(Object obj) {
		if (obj == null)
			return VOID;
		else if (obj instanceof ArrayList<?>)
			return ARRAY;
		else if (obj.getClass().isArray())
			return ARRAY;
		else if (obj instanceof Double)
			return DOUBLE;
		else if (obj instanceof Float)
			return FLOAT;
		else if (obj instanceof Long)
			return LONG;
		else if (obj instanceof Integer)
			return INTEGER;
		else if (obj instanceof Short)
			return SHORT;
		else if (obj instanceof Byte)
			return BYTE;
		else if (obj instanceof String)
			return STRING;
		else if (obj instanceof Boolean)
			return BOOLEAN;
		else if (obj instanceof Message)
			return MESSAGE;
		else if (obj instanceof Void)
			return VOID;
		else if (obj instanceof Collection<?>)
			return ARRAY;
		else if (obj instanceof Color)
			return COLOR;
		else if (obj instanceof Complex)
			return COMPLEX;
		else if (obj instanceof Enum<?>)
			return ENUM;
		else if (obj instanceof File)
			return FILE;
		return NONE;
	}

	/***
	 * Get the color representing the {@link ValueType}
	 * 
	 * @return the choosen color
	 */
	public Color getColor() {
		if (this == ARRAY)
			return new Color(255, 107, 107);
		else if (this == DOUBLE || this == FLOAT)
			return new Color(199, 244, 100);
		else if (this == BYTE || this == INTEGER || this == SHORT
				|| this == LONG)
			return new Color(136, 196, 37);
		else if (this == BOOLEAN)
			return new Color(255, 255, 200);
		else if (this == STRING)
			return new Color(200, 255, 255);
		else if (this == ENUM)
			return new Color(255, 200, 255);
		else if (this == COLOR)
			return new Color(240, 200, 210);
		else if (this == FILE)
			return new Color(196, 77, 88);
		else if (this == PORT)
			return new Color(78, 205, 196);
		return new Color(255, 196, 140);
	}

	/***
	 * Check if is array
	 * 
	 * @return if is an array
	 */
	public boolean isArray() {
		return this == ARRAY;
	}

	/***
	 * Parse a string to the right object
	 * 
	 * @param val
	 *            value to parse
	 * @return parsed object
	 */
	public Object parse(String val) {
		if (this == DOUBLE) {
			return Double.valueOf(val);
		} else if (this == FLOAT) {
			return Float.valueOf(val);
		} else if (this == INTEGER) {
			return Integer.valueOf(val);
		} else if (this == SHORT) {
			return Short.valueOf(val);
		} else if (this == LONG) {
			return Long.valueOf(val);
		} else if (this == BYTE) {
			return Byte.valueOf(val);
		} else if (this == BOOLEAN) {
			return Boolean.valueOf(val);
		} else if (this == MESSAGE) {
			return new Message(val, MessageType.INFO);
		} else if (this == COLOR) {
			return Utils.parseColor(val);
		} else if (this == VOID || this == NONE) {
			return Void.TYPE;
		} else if (this == ENUM) {
			Enum<?> e = EnumManager.parseEnum(val);
			if (e != null)
				return e;
			return val;
		} else if (this == ARRAY) {
			return parseArrary(val);
		} else if (this == STRING) {
			if ((val.startsWith("'") && val.endsWith("'"))
					|| (val.startsWith("\"") && val.endsWith("\""))) {
				return val.subSequence(1, val.length() - 1);
			}
			return val;
		} else if (this == FILE) {
			// FILE file:PATH
			if (val.startsWith("file:")) {
				String path = val.substring(5);
				return new File(path);
			}
			return null;
		}
		return val;
	}

	/***
	 * Method to parse an array
	 * 
	 * @param val
	 *            value to parse
	 * @return {@link ArrayList} of {@link Value}
	 */
	final private static Object parseArrary(String val) {
		String array = val.substring(1, val.length() - 1);
		if (array.length() == 0) {
			return new ArrayList<Value>();
		}
		String values[] = splitArray(array);
		if (values.length == 1) {
			if (array.contains(":")) {
				values = array.split(":");
				if (values.length == 3) {
					String sdv = values[1];
					String sstart = values[0];
					String send = values[2];
					double dv = ((Double) ValueType.DOUBLE.parse(sdv))
							.doubleValue();
					double start = ((Double) ValueType.DOUBLE.parse(sstart))
							.doubleValue();
					double end = ((Double) ValueType.DOUBLE.parse(send))
							.doubleValue();
					int l = (int) Math.round((end - start) / dv);
					if (start + dv * l != end)
						l++;
					l++;
					Value vals[] = new Value[l];
					double v = start;
					vals[0] = new Value(start);
					for (int i = 1; i < l - 1; i++) {
						v += dv;
						vals[i] = new Value(v);
					}
					vals[l - 1] = new Value(end);
					return vals;
				}
			}
		}

		ArrayList<Value> vals = new ArrayList<>();
		for (String value : values) {
			vals.add(Value.parseValue(value));
		}
		return vals;
	}

	/***
	 * Method to split a string representing an array
	 * 
	 * @param string
	 *            value to split
	 * @return elements of the array
	 */
	final private static String[] splitArray(String string) {
		ArrayList<String> values = new ArrayList<>();
		char array[] = string.toCharArray();
		int pc = 0;
		int begin = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == ',' && pc == 0) {
				values.add(string.substring(begin, i));
				begin = i + 1;
			}
			if (array[i] == '[')
				pc++;
			else if (array[i] == ']')
				pc--;
			else if (array[i] == '\'') {
				i++;
				for (; i < array.length; i++) {
					if (array[i] == '\'')
						break;
				}
			} else if (array[i] == '"') {
				i++;
				for (; i < array.length; i++) {
					if (array[i] == '"')
						break;
				}
			} else if (array[i] == ' ') {
				begin++;
			}
		}

		values.add(string.substring(begin, string.length()));

		return values.toArray(new String[values.size()]);
	}

	/***
	 * check if is None type
	 * 
	 * @return if is none
	 */
	public boolean isNone() {
		return this == VOID || this == NONE;
	}

}
