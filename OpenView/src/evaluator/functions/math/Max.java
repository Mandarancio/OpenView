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
public class Max extends AbstractFunction {

    static final private String description = "";

    public Max() {
        super("max", "Max", description, 2);
    }

    @Override
    public Value evaluate(Value... arguments) throws Exception {
        // Check the number of argument
        if (arguments.length == 2) {
            // get Value
            Value value = arguments[0];
            Value value2 = arguments[1];

            // If numerical
            if (value.getType().isNumeric() && value2.getType().isNumeric()) {
                return new Value(new Double(Math.max(value.getDouble(), value2.getDouble())));
            }

      

            // the type is incorrect
            throw new EvalException(this.name() + " function does not handle " + value.getType().toString() + " type");
        }

        // number of argument is incorrect
        throw new EvalException(this.name() + " function needs 2 numerical parameters");
    }

    @Override
    public ValueType returnedType(ValueType... types) throws Exception {
        // Check the number of argument
        if (types.length == input()) {
            if (types[0].isNone() && types[1].isNone()) {
                return ValueType.NONE;
            }

            // If numerical
            if (types[0].isNumeric() && (types[1].isNumeric() || types[1].isNone())) {
                return ValueType.DOUBLE;
            }

            // if array
            if (types[0].isArray() && (types[1].isNumeric() || types[1].isNone() || types[1].isArray())) {
                return ValueType.ARRAY;
            }

            if (types[1].isArray()) {
                return ValueType.ARRAY;
            }

            if (types[1].isNone() || types[1].isNumeric()) {
                return ValueType.NONE;
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
        return new Max();
    }
    
    
}
