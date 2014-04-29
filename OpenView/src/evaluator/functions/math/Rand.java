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
public class Rand extends AbstractFunction {

    private static final String description = "Returns the sine of a value; the returned angle is in the range 0.0 through pi.";

    public Rand() {
        super("rand", "Rand", description, 0);
    }

    @Override
    public Value evaluate(Value... arguments) throws Exception {
// Check the number of argument
        if (arguments.length == 0) {
            return new Value(new Double(Math.random()));
        }

        // number of argument is incorrect
        throw new EvalException(this.name() + " function only allows no parameter");
    }

    @Override
    public ValueType returnedType(ValueType... types) throws Exception {
        return ValueType.DOUBLE;
    }

    @Override
    public boolean isTypeSupported(ValueType... types) {
        return true;
    }

    @Override
    public Function clone() {
        return new Rand();
    }
    
    
}
