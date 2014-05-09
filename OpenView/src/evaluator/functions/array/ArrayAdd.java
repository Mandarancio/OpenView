package evaluator.functions.array;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;
import evaluator.functions.Function;

public class ArrayAdd extends AbstractFunction {

    public ArrayAdd() {
        super("add", "add", "", 2);
    }

    @Override
    public Value evaluate(Value... arguments) throws Exception {
        if (arguments.length != 2) {
            throw new EvalException("add support only two argument!");
        }
        Value arr = new Value(arguments[0]);
        Value val = arguments[1];
        if (arr.getType() == ValueType.ARRAY) {
            if (val.getType() == ValueType.ARRAY) {
                for (Value v : val.getValues()) {
                    arr.getArray().add(v);
                }
            } else {
                arr.getArray().add(val);
            }
            return new Value(arr);
        }
        throw new EvalException("add support only array and values!");

    }

    @Override
    public ValueType returnedType(ValueType... types) throws Exception {
        return ValueType.ARRAY;
    }

    @Override
    public boolean isTypeSupported(ValueType... types) {
        if (types[0] == ValueType.ARRAY) {
            return true;
        }
        return false;
    }

    @Override
    public Function clone() {
        return new ArrayAdd();
    }
}
