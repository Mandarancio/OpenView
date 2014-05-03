package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

/**
 * The boolean not operator.
 */
public class NotOperator extends AbstractOperator {

	/**
	 * Default constructor.
	 */
	public NotOperator() {
		super("!", "Boolean and binary not.", 2, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cern.ade.synServer.evaluator.operators.Operator#evaluate(java.util.ArrayList
	 * )
	 */
	@Override
	public Value evaluate(Value... operands) throws Exception
			 {
		// we want only two operandInspectorValueTypes
		if (operands.length != 1)
			throw new EvalException("Error: wrong number of operands ("+operands.length+")");
                
		// get operands
		Value l = operands[0];

		// if types match
		if (l.getType() == ValueType.BOOLEAN) {
			return new Value(new Boolean(!l.getBoolean()));
		}

		if (l.getType() == ValueType.INTEGER) {
			return new Value(new Integer(~l.getInt()));
		}

		if (l.getType() == ValueType.LONG) {
			return new Value(new Long(~l.getLong()));
		}

	

		throw new EvalException(this.name() + " Operands error! Type mismatch!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators.Operator
	 * #returnType(cern.ade.inspector.dataAccess .ValueType[])
	 */
	@Override
	public ValueType returnedType(ValueType... types) throws EvalException {
		// we want only two operands
		if (types.length != 1)
			throw new EvalException("Error: wrong number of operands");

		if (types[0] == ValueType.BOOLEAN)
			return (ValueType.BOOLEAN);

		if (types[0] == ValueType.INTEGER)
			return (ValueType.INTEGER);

		if (types[0] == ValueType.LONG)
			return (ValueType.LONG);

		if (types[0] == ValueType.ARRAY)
			return (ValueType.ARRAY);

		throw new EvalException(this.name()
				+ " Operands error! Type is incorrect !");
	}

	@Override
	public AbstractOperator clone() {
		return new NotOperator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators.Operator
	 * #supportType(cern.ade.inspector. dataAccess.ValueType[])
	 */
	@Override
	public boolean isTypeSupported(ValueType... types) {
		// use the return type to check the supported type
		try {
			returnedType(types);
			return true;
		} catch (EvalException ex) {
			return false;
		}
	}
}