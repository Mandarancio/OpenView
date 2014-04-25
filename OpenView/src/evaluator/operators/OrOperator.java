package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public class OrOperator extends AbstractOperator {
	/**
	 * Default constructor.
	 */
	public OrOperator() {
		super(
				"||",
				"Binary and boolean operator./nCan work with numbers, booleans and array.",
				2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cern.ade.synServer.evaluator.operators.Operator#evaluate(java.util.ArrayList
	 * )
	 */
	@Override
	public Value evaluate(Value... operands) throws Exception {
		// we want only two operands
		if (operands.length != 2)
			throw new EvalException("Error: wrong number of operands");

		// get operands
		Value l = operands[0];
		Value r = operands[1];

		if ((l.getType() == ValueType.LONG && r.getType() == ValueType.INTEGER)
				|| (l.getType() == ValueType.INTEGER && r.getType() == ValueType.LONG))
			return new Value(new Long(l.getLong() | r.getLong()));

		// if types match
		if (l.getType() == r.getType()) {
			if (l.getType() == ValueType.BOOLEAN)
				return new Value(new Boolean(l.getBoolean() || r.getBoolean()));

			if (l.getType() == ValueType.INTEGER)
				return new Value(new Integer(l.getInt() | r.getInt()));

			if (l.getType() == ValueType.LONG)
				return new Value(new Long(l.getLong() | r.getLong()));

		}

		throw new EvalException("Operands error! Type mismatch!");
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
		if (types.length != input())
			throw new EvalException(this.name()
					+ " Error: wrong number of operands");

		// get operands
		ValueType l = types[0];
		ValueType r = types[1];

		// if types match
		if (l == r) {
			if (l == ValueType.BOOLEAN)
				return (ValueType.BOOLEAN);

			if (l == ValueType.INTEGER)
				return (ValueType.INTEGER);

			if (l == ValueType.LONG)
				return (ValueType.LONG);

			if (l == ValueType.ARRAY)
				return (ValueType.ARRAY);

			if (l == ValueType.NONE)
				return ValueType.VOID;
		}

		if (l == ValueType.VOID) {
			if (r == ValueType.BOOLEAN)
				return ValueType.BOOLEAN;

			if (r == ValueType.ARRAY)
				return ValueType.ARRAY;

			if (r == ValueType.INTEGER)
				return ValueType.VOID;

			if (r == ValueType.LONG)
				return ValueType.VOID;
		}

		if (r == ValueType.VOID) {
			if (l == ValueType.BOOLEAN)
				return ValueType.BOOLEAN;

			if (l == ValueType.ARRAY)
				return ValueType.ARRAY;

			if (l == ValueType.INTEGER)
				return ValueType.INTEGER;

			if (l == ValueType.LONG)
				return ValueType.LONG;
		}

		// if it is a Vector plus a scalar
		if (l.isArray() && (r == ValueType.INTEGER)) {
			return (ValueType.ARRAY);
		}

		throw new EvalException(this.name() + " Operands error! Type mismatch!");
	}

	@Override
	public AbstractOperator clone() {
		return new AndOperator();
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
