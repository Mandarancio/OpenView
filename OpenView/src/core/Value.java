package core;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import core.support.EnumManager;
import core.support.Utils;

/***
 * Class to manage many different data type with a single but powerful object
 * 
 * @author martino
 * 
 */
public class Value {
	/***
	 * Field where the data is stored
	 */
	private Object data_;
	/***
	 * Value descriptor
	 */
	private ValueDescriptor descriptor_;

	/***
	 * initialize with empty data and only the {@link ValueDescriptor} of the
	 * future datas
	 * 
	 * @param desc
	 *            {@link ValueDescriptor} of the future datas
	 */
	public Value(ValueDescriptor desc) {
		descriptor_ = desc;
		data_ = null;
	}

	/***
	 * Load a value from an XML element
	 * 
	 * @param e
	 *            XML element to load
	 */
	public Value(Element e) {
		String val = e.getAttribute("val");
		NodeList nl = e.getElementsByTagName(ValueDescriptor.class
				.getSimpleName());
		if (nl.getLength() != 0) {
			Element vde = (Element) nl.item(0);
			descriptor_ = new ValueDescriptor(vde);
			data_ = descriptor_.getType().parse(val);

		} else {
			Value v = parseValue(val);
			data_ = v.getData();
			descriptor_ = v.getDescriptor();
		}
	}

	/***
	 * Initialize the {@link Value} from a generic {@link Object}
	 * 
	 * @param obj
	 *            {@link Object} to store
	 */
	public Value(Object obj) {

		if (obj instanceof Value) {
			copyValue((Value) obj);
		} else {
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
			case ARRAY:
				copyArray(obj);
				break;
			default:
				data_ = obj;
				break;
			}
		}
	}

	/***
	 * Method to copy another {@link Value}
	 * 
	 * @param val
	 *            value to copy
	 */
	private void copyValue(Value val) {
		descriptor_ = new ValueDescriptor(val.getDescriptor());
		Object obj = val.getData();
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
		case ARRAY:
			copyArray(obj);
			break;
		default:
			data_ = obj;
			break;
		}
	}

	/***
	 * Method to copy an array of Value
	 * 
	 * @param obj
	 *            object where the array is stored
	 */
	private void copyArray(Object obj) {
		if (obj instanceof ArrayList<?>) {
			ArrayList<Value> value = new ArrayList<>();
			@SuppressWarnings("unchecked")
			ArrayList<Object> data = (ArrayList<Object>) obj;
			for (Object o : data) {
				value.add(new Value(o));
			}
			data_ = value;

		} else {

			ArrayList<Value> values = new ArrayList<>();

			Object[] array = (Object[]) obj;
			for (Object o : array) {
				values.add(new Value(o));
			}

			data_ = values;
		}
	}

	/***
	 * Initialize the value parsing a string (with a well-known
	 * {@link ValueType})
	 * 
	 * @param val
	 *            value string to parse
	 * @param type
	 *            {@link ValueType} of the data
	 */
	public Value(String val, ValueType type) {
		descriptor_ = new ValueDescriptor(type);
		data_ = type.parse(val);
	}

	/***
	 * Initialize a value with an int value (it will be converted in an
	 * {@link Integer})
	 * 
	 * @param val
	 *            data to store
	 */
	public Value(int val) {
		data_ = new Integer(val);
		descriptor_ = new ValueDescriptor(ValueType.INTEGER);
	}

	/***
	 * Initialize a value with a double value (it will be converted in
	 * {@link Double})
	 * 
	 * @param val
	 *            data to store
	 */
	public Value(double val) {
		data_ = new Double(val);
		descriptor_ = new ValueDescriptor(ValueType.DOUBLE);
	}

	/***
	 * Initialize a value with a boolean value (it will be converted in
	 * {@link Boolean})
	 * 
	 * @param val
	 *            data to store
	 */
	public Value(boolean val) {
		data_ = new Boolean(val);
		descriptor_ = new ValueDescriptor(ValueType.BOOLEAN);
	}

	/***
	 * Initialize a value with a void value
	 */
	public Value() {
		this(Void.TYPE);
	}

	/***
	 * Get the {@link ValueDescriptor}
	 * 
	 * @return the descriptor
	 */
	public ValueDescriptor getDescriptor() {
		return descriptor_;
	}

	/***
	 * Get the value type stored in the descriptor
	 * 
	 * @return {@link ValueType} of the descriptor
	 */
	public ValueType getType() {
		return descriptor_.getType();
	}

	/***
	 * get the raw data stored in the {@link Value}
	 * 
	 * @return the raw data as {@link Object}
	 */
	public Object getData() {
		return data_;
	}

	/***
	 * Set data as {@link String} to parse using the {@link ValueType}
	 * 
	 * @param val
	 *            value to parse
	 * @param type
	 *            value type of the data
	 * @return return if data type is compatible
	 */
	public boolean setData(String val, ValueType type) {
		if (!type.isCompatible(getType()))
			return false;
		data_ = type.parse(val);
		return true;
	}

	/***
	 * Set data using a raw {@link Object}
	 * 
	 * @param data
	 *            to be stored
	 * @return if data was compatible
	 */
	public boolean setData(Object data) {
		if (descriptor_.getType().isCompatible(ValueType.getType(data))) {
			this.data_ = data;
			if (getType() == ValueType.VOID)
				descriptor_.setType(ValueType.getType(data));
			return true;
		}
		return false;
	}

	/***
	 * Set integer data
	 * 
	 * @param data
	 *            to be stored
	 * @return if data was compatible
	 */
	public boolean setData(int data) {
		return setData(new Integer(data));
	}

	/***
	 * Set double data
	 * 
	 * @param data
	 *            to be stored
	 * @return if data was compatible
	 */
	public boolean setData(double data) {
		return setData(new Double(data));
	}

	/***
	 * Set boolean data
	 * 
	 * @param data
	 *            to be stored
	 * @return if data was compatible
	 */
	public boolean setData(boolean data) {
		return setData(new Boolean(data));
	}

	/***
	 * Get data as double
	 * 
	 * @return the double value
	 * @throws Exception
	 *             if data is not Numeric
	 */
	public double getDouble() throws Exception {
		if (descriptor_.getType().isNumeric()) {
			return ((Number) data_).doubleValue();
		} else
			throw new Exception("Not numeric!");
	}

	/***
	 * Get data as int
	 * 
	 * @return the int value
	 * @throws Exception
	 *             if data is not Numeric
	 */
	public int getInt() throws Exception {
		if (descriptor_.getType().isNumeric()) {
			return ((Number) data_).intValue();
		} else
			throw new Exception("Not numeric!");

	}

	/***
	 * Get data as String
	 * 
	 * @return the string value
	 */
	public String getString() {
		if (data_ == null)
			return "void";
		else if (data_ instanceof Void)
			return "void";
		else if (getType() == ValueType.COLOR)
			return Utils.codeColor((Color) data_);
		else if (getType() == ValueType.ARRAY) {
			@SuppressWarnings("unchecked")
			ArrayList<Value> vals = (ArrayList<Value>) data_;
			if (vals.size() == 0)
				return "[]";
			String str = "[" + vals.get(0).getString();
			for (int i = 1; i < vals.size(); i++) {
				str += ", " + vals.get(i).getString();
			}
			str += "]";
			return str;
		} else if (getType() == ValueType.FILE) {
			String str = "file:";
			str += ((File) data_).getAbsolutePath();
			return str;
		}
		return data_.toString();
	}

	/***
	 * Get data as an {@link ArrayList} of Values
	 * 
	 * @return the arraylist list
	 * @throws Exception
	 *             if data is not an Array
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Value> getArray() throws Exception {
		if (getType() == ValueType.ARRAY && data_ != null) {
			return (ArrayList<Value>) data_;
		}
		throw new Exception("Data is not an array but a " + getType());
	}

	/***
	 * Get data as array of Value
	 * 
	 * @return the array value
	 * @throws Exception
	 *             if data is not and Array
	 */
	public Value[] getValues() throws Exception {
		ArrayList<Value> values = this.getArray();
		return values.toArray(new Value[values.size()]);
	}

	/***
	 * Get data as {@link Enum}
	 * 
	 * @return the enum value
	 * @throws Exception
	 *             if data is not an Enum
	 */
	public Enum<?> getEnum() throws Exception {
		if (getDescriptor().getType() == ValueType.ENUM)
			return (Enum<?>) data_;
		throw new Exception("Not Enum!");
	}

	/***
	 * Get data as {@link Color}
	 * 
	 * @return the color value
	 * @throws Exception
	 *             if data is not a color
	 */
	public Color getColor() throws Exception {
		if (getType() == ValueType.COLOR && data_ != null) {
			return (Color) data_;
		}
		throw new Exception("No data!");
	}

	/***
	 * Get data as long
	 * 
	 * @return the long value
	 * @throws Exception
	 *             if data is not Numeric
	 */
	public long getLong() throws Exception {
		if (getType().isNumeric())
			return ((Number) data_).longValue();
		throw new Exception("Data is not a long!");

	}

	/***
	 * Get data as boolean
	 * 
	 * @return the boolean value
	 * @throws Exception
	 *             if data is not {@link Boolean}
	 */
	public boolean getBoolean() throws Exception {
		if (getType() == ValueType.BOOLEAN)
			return ((Boolean) data_).booleanValue();
		throw new Exception("Data is not a boolean! is " + getType());

	}

	@Override
	public String toString() {
		return "(" + getString() + "," + getType() + ")";
	}

	/***
	 * Get data as short
	 * 
	 * @return the short value
	 * @throws Exception
	 *             if data is not Numeric
	 */
	public short getShort() throws Exception {
		if (getType().isNumeric())
			return ((Number) data_).shortValue();
		throw new Exception("Data is not a number");
	}

	/***
	 * Get data as {@link File}
	 * 
	 * @return the file
	 * @throws Exception
	 *             if data is not a {@link File}
	 */
	public File getFile() throws Exception {
		if (getType() == ValueType.FILE)
			return (File) data_;
		throw new Exception("Data is not a File but a " + getType());
	}

	/***
	 * Get XML element that describe the {@link Value}
	 * 
	 * @param doc
	 *            document that create the element
	 * @return XML element
	 */
	public Element getXML(Document doc) {
		Element e = doc.createElement(getClass().getSimpleName());
		if (getType() == ValueType.ENUM) {
			e.setAttribute("val", getData().getClass().getSimpleName() + ":"
					+ getString());
		} else if (getType() == ValueType.STRING) {
			e.setAttribute("val", "'" + getString() + "'");

		} else {
			e.setAttribute("val", getString());
		}
		e.appendChild(descriptor_.getXML(doc));
		return e;
	}

	/***
	 * Parse an unknown string to {@link Value}
	 * 
	 * @param s
	 *            string to parse
	 * @return {@link Value} parsed
	 */
	public final static Value parseValue(String s) {
		Value value_ = null;
		if (s.length() == 0 || s.equals("void")) {
			value_ = new Value(Void.TYPE);
		} else if (s.equals("true")) {
			value_ = new Value(true);
		} else if (s.equals("false")) {
			value_ = new Value(false);
		} else if (s.startsWith("file:")) {
			value_ = new Value(ValueType.FILE.parse(s));
		} else if (s.startsWith("port:")) {
			value_ = new Value(ValueType.PORT.parse(s));
		} else if (s.startsWith("[") && s.endsWith("]")) {
			value_ = new Value(ValueType.ARRAY.parse(s));
		} else if (s.startsWith("'") && s.endsWith("'")) {
			String str = s.substring(1, s.length() - 1);
			return new Value(str);
		} else if (s.startsWith("\"") && s.endsWith("\"")) {
			String str = s.substring(1, s.length() - 1);
			return new Value(str);
		} else if (s.contains(".")) {
			try {
				value_ = new Value(Double.valueOf(s));
			} catch (NumberFormatException e) {
				value_ = new Value(s);
			}
		} else {
			try {
				value_ = new Value(Integer.valueOf(s));
			} catch (NumberFormatException e) {
				if (s.contains(":")) {
					try {
						Enum<?> en = EnumManager.parseEnum(s);
						if (en != null)
							value_ = new Value(en);
						else
							value_ = new Value(s);
					} catch (Exception e1) {
						value_ = new Value(s);
					}
				} else if (s.contains(",") && s.split(",").length == 4) {
					try {
						Color c = Utils.parseColor(s);
						value_ = new Value(c);
					} catch (Exception e1) {
						value_ = new Value(s);
					}
				} else
					value_ = new Value(s);
			}
		}
		return value_;
	}
}
