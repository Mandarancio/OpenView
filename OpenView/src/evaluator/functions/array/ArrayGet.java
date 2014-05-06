package evaluator.functions.array;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;

public class ArrayGet extends AbstractFunction {

	public ArrayGet() {
		super("get", "get", "", 2);
	}

	@Override
	public Value evaluate(Value... arguments) throws Exception {
		if (arguments.length != 2)
			throw new EvalException("add support only two argument!");
		Value arr = arguments[0];
		Value val = arguments[1];
		if (arr.getType() == ValueType.ARRAY) {
			if (val.getType().isNumeric())
				return new Value(arr.getArray().get(val.getInt()));
		}

		throw new EvalException("add support only array and number!");

	}

	@Override
	public ValueType returnedType(ValueType... types) throws Exception {
		return ValueType.NONE;
	}

	@Override
	public boolean isTypeSupported(ValueType... types) {
		if (types[0] == ValueType.ARRAY && types[1].isNumeric())
			return true;

		return false;
	}

}
