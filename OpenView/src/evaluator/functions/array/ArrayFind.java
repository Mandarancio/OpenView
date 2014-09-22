package evaluator.functions.array;

import java.util.ArrayList;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;
import evaluator.functions.Function;

public class ArrayFind extends AbstractFunction {
	public ArrayFind() {
		super("find", "find", "", 3);
	}

	@Override
	public Value evaluate(Value... arguments) throws Exception {
		if (arguments.length < 2 && arguments.length > 3) {
			throw new EvalException("add support only two or three arguments!");
		}
		Value arr = new Value(arguments[0]);
		Value val = arguments[1];

		if (arr.getType() == ValueType.ARRAY) {
			if (arguments.length == 3 && arguments[2].getType().isNumeric()) {
				ArrayList<Value> array = arr.getArray();
				double value = val.getDouble();
				double err = arguments[2].getDouble();
				for (int i = 0; i < array.size(); i++) {
					if (array.get(i).getType().isNumeric()) {
						double v = array.get(i).getDouble();
						if (Math.abs(v - value) < Math.abs(err)) {
							return new Value(i);
						}
					}
				}
			} else {
				ArrayList<Value> array = arr.getArray();

				for (int i = 0; i < array.size(); i++) {
					if (array.get(i).getData().equals(val.getData()))
						return new Value(i);
				}
			}
			return new Value(-1);
		}
		throw new EvalException("add support only array and values!");

	}

	@Override
	public ValueType returnedType(ValueType... types) throws Exception {
		return ValueType.INTEGER;
	}

	@Override
	public boolean isTypeSupported(ValueType... types) {
		if (types.length == 2) {
			if (types[0] == ValueType.ARRAY) {
				return true;
			}
		} else if (types.length == 3) {
			if (types[0] == ValueType.ARRAY && types[1].isNumeric()
					&& types[2].isNumeric())
				return true;
		}
		return false;
	}

	@Override
	public Function clone() {
		return new ArrayFind();
	}
	
	public static void main(String[] args) throws Exception {
		Double d[]=new Double[]{new Double(0),new Double(1),new Double(2),new Double(3),new Double(4),new Double(5)};
		Value v=new Value(d);
		System.out.println(v);
		ArrayFind find=new ArrayFind();
		System.out.println(v.getArray().get(find.evaluate(v,new Value(2.1),new Value(0.2)).getInt()));
		System.exit(0);
	}

}
