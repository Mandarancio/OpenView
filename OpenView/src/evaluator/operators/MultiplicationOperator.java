package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public class MultiplicationOperator extends AbstractOperator {

	public MultiplicationOperator() {
		super("*", "Multiplication Operator.", 2);
	}

	@Override
	public Value evaluate(Value... operands) throws Exception {
		// we need two operands
		if (operands.length != 2)
			throw new EvalException("Error: wrong number of operands");

		// get operands
		Value l = operands[0];
		Value r = operands[1];

		// if they are numerical
		if (l.getType().isNumeric() && r.getType().isNumeric()) {
			if (l.getType() == ValueType.DOUBLE || r.getType() == ValueType.DOUBLE)
				return new Value(new Double(l.getDouble() * r.getDouble()));

			if (l.getType() == ValueType.LONG || r.getType() == ValueType.LONG)
				return new Value(new Long(l.getLong() * r.getLong()));

			return new Value(new Integer(l.getInt() * r.getInt()));
		}

		// if they are boolean
		if (l.getType() == ValueType.BOOLEAN && l.getType() == r.getType()) {
			// perform AND
			return new Value(new Boolean(l.getBoolean() && r.getBoolean()));
		}

		

		throw new EvalException(this.name() + " Operands type is invalid!");
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
		// we need two operands
		if (types.length != input())
			throw new EvalException(this.name()
					+ " Error: wrong number of operands");

		// get operands
		ValueType l = types[0];
		ValueType r = types[1];

		// if they are numerical
		if (l.isNumeric() && (r.isNumeric())) {
			if (l == ValueType.DOUBLE || r == ValueType.DOUBLE)
				return (ValueType.DOUBLE);

			if (l == ValueType.LONG || r == ValueType.LONG)
				return (ValueType.LONG);

			return (ValueType.INTEGER);
		}

		if (l.isNumeric() && r == ValueType.VOID)
			// It maybe not true but indicative
			return ValueType.DOUBLE;

		if (l == ValueType.VOID && r.isNumeric())
			return ValueType.VOID;

		// if they are boolean
		if (l == ValueType.BOOLEAN || r == ValueType.BOOLEAN) {
			return (ValueType.BOOLEAN);
		}

		// If they are arrays
		if (l.isArray()
				&& (r.isArray() || r == ValueType.VOID || r.isNumeric())) {
			return (ValueType.ARRAY);
		}

		// If there are arrays
		if (l == ValueType.VOID && r.isArray()) {
			return (ValueType.ARRAY);
		}

		if (l == ValueType.VOID && r == ValueType.VOID)
			return ValueType.VOID;

		throw new EvalException(this.name() + " Operands type is invalid!");
	}

	@Override
	public MultiplicationOperator clone() {
		return new MultiplicationOperator();
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