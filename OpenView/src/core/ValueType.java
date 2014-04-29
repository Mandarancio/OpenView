package core;

import java.awt.Color;
import java.util.Collection;

import core.Message.MessageType;
import core.support.Utils;

public enum ValueType {
	NONE, BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, STRING, ARRAY, VOID, MESSAGE, COLOR, ENUM;

	public boolean isNumeric() {
		return (this == BYTE || this == SHORT || this == INTEGER
				|| this == LONG || this == FLOAT || this == DOUBLE);
	}
	
	public boolean isCompatible(ValueType type){
		if (this==type || this==NONE) return true;
		if (this.isNumeric() && type.isNumeric()) return true;
		return false;
	}
	
	static public ValueType getType(Object obj){
		if (obj == null )return VOID;
		else if (obj.getClass().isArray()) return ARRAY;
		else if (obj instanceof Double) return DOUBLE;
		else if (obj instanceof Float) return FLOAT;
		else if (obj instanceof Long) return LONG;
		else if (obj instanceof Integer) return INTEGER;
		else if (obj instanceof Short) return SHORT;
		else if (obj instanceof Byte) return BYTE;
		else if (obj instanceof String) return STRING;
		else if (obj instanceof Boolean) return BOOLEAN;
		else if (obj instanceof Message) return MESSAGE;
		else if (obj instanceof Void) return VOID;
		else if (obj instanceof Collection<?>) return ARRAY;
		else if (obj instanceof Color) return COLOR;
		else if (obj instanceof Enum<?>) return ENUM;
		return NONE;
	}

	public Color getColor() {
		if (this == ARRAY) return new Color(255,200,200);
		else if (this == DOUBLE || this==FLOAT) return new Color(200,255,200);
		else if (this == BYTE || this == INTEGER || this ==SHORT || this == LONG) return new Color(200,200,255);
		else if (this == BOOLEAN) return new Color(255,255,200);
		else if (this == STRING) return new Color(200,255,255);
		else if (this == ENUM) return new Color(255,200,255);
		else if (this == COLOR) return new Color(240,200,210);
		return new Color(200,200,200);
	}

	public boolean isArray() {
		return this==ARRAY;
	}

	public Object parse(String val) {
		if (this==DOUBLE){
			return Double.valueOf(val);
		}else if (this==FLOAT){
			return Float.valueOf(val);
		}else if (this==INTEGER){
			return Integer.valueOf(val);
		}else if (this==SHORT){
			return Short.valueOf(val);
		}else if (this==LONG){
			return Long.valueOf(val);
		}else if (this==BYTE){
			return Byte.valueOf(val);
		}else if (this==BOOLEAN){
			return Boolean.valueOf(val);
		}else if (this==MESSAGE){
			return new Message(val,MessageType.INFO);
		}else if (this==COLOR){
			return Utils.parseColor(val);
		}else if (this == VOID || this ==NONE) {
			return Void.TYPE;
		}else if (this==ENUM){
			return val;
		}
		return val;
	}

    public boolean isNone() {
        return this==VOID || this==NONE;
    }

}
