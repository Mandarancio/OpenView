/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluator.functions.math;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;
import evaluator.functions.Function;

/**
 *
 * @author martino
 */
public class Exp extends AbstractFunction {

    private static final String description = "Returns Euler's number e raised to the power of a double value.";

    public Exp() {
        super("exp", "Exp", description, 1);
    }

    @Override
    public Value evaluate(Value... arguments) throws Exception {
// Check the number of argument
        if (arguments.length == 1) {
            // get Value
            Value value = arguments[0];

            // If numerical
            if (value.getType().isNumeric()) {
                return new Value(new Double(Math.exp(value.getDouble())));
            }

            // the type is incorrect
            throw new EvalException(this.name() + " function does not handle " + value.getType().toString() + " type");
        }

        // number of argument is incorrect
        throw new EvalException(this.name() + " function only allows one numerical parameter");
    }

    @Override
    public ValueType returnedType(ValueType... types) throws Exception {
        // Check the number of argument
        if (types.length == 1) {
            // if array
            if (types[0] == ValueType.NONE) {
                return ValueType.NONE;
            }

            // If numerical
            if (types[0].isNumeric()) {
                return ValueType.DOUBLE;
            }

            // if array
            if (types[0] == ValueType.ARRAY) {
                return ValueType.ARRAY;
            }

            // the type is incorrect
            throw new EvalException(this.name() + " function does not handle " + types[0].toString() + " type");
        }

        // number of argument is incorrect
        throw new EvalException(this.name() + " function only allows one numerical parameter");
    }

    @Override
    public boolean isTypeSupported(ValueType... types) {
        try {
            returnedType(types);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Function clone() {
        return new Exp();
    }
    
    
}
