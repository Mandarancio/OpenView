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
public class Abs extends AbstractFunction{
    private static final String description= "Returns the absolute value of a double value. If the argument is not negative, the argument is returned. If the argument is negative, the negation of the argument is returned. Special cases:\nIf the argument is positive zero or negative zero, the result is positive zero.\nIf the argument is infinite, the result is positive infinity.\nIf the argument is NaN, the result is NaN.";

    public Abs(){
        super("abs", "Abs",description , 1);
    }
    
    @Override
    public String name()
    {
        return "abs";
    }

    /*
     * (non-Javadoc)
     * 
     * @see cern.ade.synServer.evaluator.functions.Function#evaluate(java.util.ArrayList)
     */
    @Override
    public Value evaluate(Value... arguments) throws Exception
    {
        // Check the number of argument
        if (arguments.length == 1)
        {
            // get Value
            Value value = arguments[0];

            // If Double
            if (value.getType() == ValueType.DOUBLE)
                return new Value(new Double(Math.abs(value.getDouble())));

            // if integer
            if (value.getType() == ValueType.INTEGER)
                return new Value(new Integer(Math.abs(value.getInt())));

            // if integer
            if (value.getType() == ValueType.LONG)
                return new Value(new Long(Math.abs(value.getLong())));
            
            if (value.getType() == ValueType.ARRAY){
            	Value[] vector = value.getValues();
    			Value result[] = new Value[vector.length];
    			for (int i=0;i<vector.length;i++){
    				result[i]=evaluate(vector[i]);
    			}
    			return new Value(result);
            }
           

            // the type is incorrect
            throw new EvalException(this.name() + " function does not handle " + value.getType().toString() + " type");
        }

        // number of argument is incorrect
        throw new EvalException(this.name() + " function only allows one numerical parameter");
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Function clone()
    {
        return new Abs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators.Operator#returnType()
     */
    @Override
    public ValueType returnedType(ValueType... types) throws EvalException
    {
        // Check the number of argument
        if (types.length == 1)
        {
            // If numerical
            if (types[0].isNumeric() || types[0] == ValueType.NONE)
                return types[0];

            // if array
            if (types[0] == ValueType.ARRAY)
                return ValueType.ARRAY;

            // the type is incorrect
            throw new EvalException(this.name() + " function does not handle " + types[0].toString() + " type");
        }

        // number of argument is incorrect
        throw new EvalException(this.name() + " function only allows one numerical parameter");
    }

    /*
     * (non-Javadoc)
     * 
     * @see cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators.Operator#supportType(cern.ade.inspector.
     * dataAccess.ValueType[])
     */
    @Override
    public boolean isTypeSupported(ValueType... types)
    {
        try
        {
            returnedType(types);
            return true;
        }
        catch (EvalException ex)
        {
            return false;
        }
    }

}
