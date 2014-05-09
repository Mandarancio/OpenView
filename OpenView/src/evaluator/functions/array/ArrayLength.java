/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluator.functions.array;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;
import evaluator.functions.Function;

/**
 *
 * @author martino
 */
public class ArrayLength extends AbstractFunction {

    public ArrayLength() {
        super("length", "length", "get the length of an array", 1);
    }

    @Override
    public Value evaluate(Value... arguments) throws Exception {
        if (arguments.length != 1) {
            throw new EvalException("lenght support only one argument!");
        }
        Value arr = arguments[0];
        if (arr.getType() == ValueType.ARRAY) {
            return new Value(arr.getArray().size());
        }

        throw new EvalException("lenght support only array!");
    }

    @Override
    public ValueType returnedType(ValueType... types) throws Exception {
        return ValueType.INTEGER;
    }

    @Override
    public boolean isTypeSupported(ValueType... types) {
        if (types.length == 1 && types[0] == ValueType.ARRAY) {
            return true;
        }
        return false;
    }

    @Override
    public Function clone() {
        return new ArrayLength();
    }
}
