package evaluator.functions.array;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;

public class ArrayRemove extends AbstractFunction {

	public ArrayRemove() {
		super("remove", "remove", "", 2);
	}

	@Override
	public Value evaluate(Value... arguments) throws Exception {
		if (arguments.length != 2)
			throw new EvalException("remove support only two argument!");
		Value arr = arguments[0];
		Value val = arguments[1];
		if (arr.getType() == ValueType.ARRAY) {
			if (val.getType().isNumeric()) {
				arr.getArray().remove(val.getInt());
			}else
				throw new EvalException("remove support only array and number!");
			return new Value(arr);
		}

		throw new EvalException("remove support only array and number!");

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
