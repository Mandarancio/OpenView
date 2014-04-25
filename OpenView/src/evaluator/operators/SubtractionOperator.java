package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public class SubtractionOperator extends AbstractOperator {

	/**
	 * @param name
	 * @param priority
	 */
	public SubtractionOperator() {
		super("-", "Substraction operator.", 5);
	}

	@Override
	public Value evaluate(Value... operands) throws Exception {
		// we need two operands
		if (operands.length > input() || operands.length == 0)
			throw new EvalException("Error: wrong number of operands");

		if (operands.length == input()) {
			// get operands
			Value l = operands[0];
			Value r = operands[1];

			// if they are numerical
			if (l.getType().isNumeric() && r.getType().isNumeric()) {
				// compute the result
				if (r.getType() == ValueType.DOUBLE
						|| l.getType() == ValueType.DOUBLE)
					return new Value(new Double(l.getDouble() - r.getDouble()));

				// compute the result
				if (r.getType() == ValueType.LONG || l.getType() == ValueType.LONG) {
					return new Value(new Long(l.getLong() - r.getLong()));
				}

				// compute the result
				return new Value(new Integer(l.getInt() - r.getInt()));
			}

			// if they are String remove sub string
			if (l.getType() == ValueType.STRING || r.getType() == ValueType.STRING)
				return new Value(new String(l.getString().replaceAll(
						r.getString(), "")));


		} else {
			// get operands
			Value l = operands[0];
			// if they are String remove sub string
			if (l.getType() == ValueType.STRING)
				throw new EvalException(this.name()
						+ "function Operands type is invalid!");

			// if they are numerical
			if (l.getType().isNumeric()) {
				if (l.getType() == ValueType.LONG)
					return new Value(new Long(-l.getLong()));
				// compute the result
				if (l.getType() == ValueType.INTEGER)
					return new Value(new Integer(-l.getInt()));
				return new Value(new Double(-l.getDouble()));
			}


		}

		throw new EvalException(this.name()
				+ " function Operands type is invalid!");
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
			throw new EvalException("Error: wrong number of operands");

		// get operands
		ValueType l = types[0];
		ValueType r = types[1];

		if (l == ValueType.VOID && r == ValueType.VOID)
			return ValueType.VOID;

		// if they are String
		if (l == ValueType.STRING || r == ValueType.STRING)
			return (ValueType.STRING);

		// if they are numerical
		if (l.isNumeric() && r.isNumeric()) {
			if (r == ValueType.DOUBLE || l == ValueType.DOUBLE)
				return (ValueType.DOUBLE);

			if (r == ValueType.LONG || l == ValueType.LONG)
				return (ValueType.LONG);

			return (ValueType.INTEGER);
		}

		if (l.isNumeric() && r == ValueType.VOID)
			return ValueType.VOID;

		if (r.isNumeric() && l == ValueType.VOID)
			return ValueType.VOID;

		// if they are boolean
		if (l == ValueType.BOOLEAN && l == r)
			return (ValueType.BOOLEAN);

		if ((l == ValueType.BOOLEAN || r == ValueType.BOOLEAN)
				&& (l == ValueType.NONE || r == ValueType.VOID))
			return ValueType.BOOLEAN;

		// If they are both arrays
		if (l.isArray() && r.isArray())
			return (ValueType.ARRAY);

		if (l == ValueType.VOID && r.isArray())
			return ValueType.ARRAY;

		if (l.isArray() && r.isNumeric()) {
			return (ValueType.ARRAY);
		}

		if (l.isArray() && r == ValueType.VOID)
			return ValueType.ARRAY;

		if (l == ValueType.VOID && r.isNumeric())
			return ValueType.VOID;

		throw new EvalException("Operands type is invalid!");
	}

	@Override
	public SubtractionOperator clone() {
		return new SubtractionOperator();
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
		// we need two operands
		if (types.length != input())
			return false;

		if (types[0] == ValueType.NONE
				&& ((types[1] == ValueType.ARRAY || types[1].isNumeric())))
			return true;

		if (types[0].isNumeric()
				&& ((types[1] == ValueType.NONE || types[1].isNumeric())))
			return true;

		if (types[0].isArray()
				&& ((types[1] == ValueType.NONE || types[1].isNumeric() || types[1]
						.isArray())))
			return true;

		return false;
	}

}
