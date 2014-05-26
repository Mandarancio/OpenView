/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package evaluator.functions.utils;

import core.Value;
import core.ValueType;
import evaluator.functions.AbstractFunction;
import evaluator.functions.Function;

/**
 * 
 * @author martino
 */
public class TypeOf extends AbstractFunction {

	public TypeOf() {
		super("typeOf", "typeOf", "Retrive the type of a value", 1);
	}

	@Override
	public Function clone() {
		return new TypeOf();
	}

	@Override
	public Value evaluate(Value... arguments) throws Exception {
		if (arguments.length == 1) {
			return new Value(arguments[0].getType());
		}
		throw new Exception("Number of arguments was wrong!");
	}

	@Override
	public ValueType returnedType(ValueType... types) throws Exception {
		return ValueType.ENUM;
	}

	@Override
	public boolean isTypeSupported(ValueType... types) {
		return true;
	}

}
