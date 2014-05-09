package evaluator.functions.converters;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;
import evaluator.functions.Function;

public class ToString extends AbstractFunction {

    public ToString() {
        super("toString", "toString", "", 1);
    }

    @Override
    public Value evaluate(Value... arguments) throws Exception {
        if (arguments.length != 1) {
            throw new EvalException("toString support only one argument!");
        }
        return new Value(arguments[0].getString(), ValueType.STRING);
    }

    @Override
    public ValueType returnedType(ValueType... types) throws Exception {
        return ValueType.STRING;
    }

    @Override
    public boolean isTypeSupported(ValueType... types) {
        return true;
    }

    @Override
    public Function clone() {
        return new ToString();
    }

}
