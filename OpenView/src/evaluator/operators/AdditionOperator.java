package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public class AdditionOperator extends AbstractOperator {
	/**
	 * @param name
	 * @param priority
	 */
	public AdditionOperator() {
		super(
				"+",
				"Addition operator.\nIt can add numbers, strings, booleans and arrays.",
				0);
	}

	@Override
	public Value evaluate(Value... operands) throws EvalException, Exception {
		// we need two operands
		if (operands.length != 2)
			throw new EvalException("Error: wrong number of operands");

		// get operands
		Value l = operands[0];
		Value r = operands[1];

		// if they are String concatenate
		if (l.getType() == ValueType.STRING || r.getType() == ValueType.STRING)
			return new Value(new String(l.getString() + r.getString()));

		// if they are numerical
		if (l.getType().isNumeric() && r.getType().isNumeric()) {
			// compute the result
			if (r.getType() == ValueType.DOUBLE
					|| l.getType() == ValueType.DOUBLE)
				return new Value(new Double(l.getDouble() + r.getDouble()));

			// compute the result
			if (r.getType() == ValueType.LONG || l.getType() == ValueType.LONG) {
				return new Value(new Long(l.getLong() + r.getLong()));
			}

			// compute the result
			return new Value(new Integer(l.getInt() + r.getInt()));
		}

		// if they are boolean
		if (l.getType() == ValueType.BOOLEAN && l.getType() == r.getType()) {
			// perform OR
			return new Value(new Boolean(l.getBoolean() || r.getBoolean()));
		}

		// If they are both arrays
		if (l.getType().isArray() && r.getType().isArray()) {
			Value[] lValues = l.getValues();
			Value[] rValues = r.getValues();

			// Check dimension
			if (lValues.length == rValues.length) { // if they are OK
				Value result[] = new Value[lValues.length];

				// compute one by one the result
				for (int i = 0; i < result.length; i++) {
					result[i] = evaluate(lValues[i], rValues[i]);
				}

				return new Value(result);
			} else {
				throw new EvalException("Vectors dimensions differs!");
			}
		}

		// // if it is a Vector plus a scalar
		if ((l.getType().isArray() && (r.getType().isNumeric() || r.getType() == ValueType.BOOLEAN))) {
			Value scalar = r;
			Value[] vector = l.getValues();
			Value result[] = new Value[vector.length];
			//
			// // compute one by one the result
			for (int i = 0; i < result.length; i++) {
				result[i] = evaluate(vector[i], scalar);
			}

			return new Value(result);
		}
		// // if it is a Vector plus a scalar
		if ((r.getType().isArray() && (l.getType().isNumeric() || l.getType() == ValueType.BOOLEAN))) {
			Value scalar = l;
			Value[] vector = r.getValues();
			Value result[] = new Value[vector.length];
			//
			// // compute one by one the result
			for (int i = 0; i < result.length; i++) {
				result[i] = evaluate(vector[i], scalar);
			}

			return new Value(result);
		}

		throw new EvalException("Operands type is invalid!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators
	 * .Operator#returnType(cern.ade.inspector.dataAccess .ValueType[])
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
				&& (l == ValueType.VOID || r == ValueType.VOID))
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
	public AbstractOperator clone() {
		return new AdditionOperator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cern.ade.inspector.dataAccess.Servers.synServer.evaluator.operators
	 * .Operator#supportType(cern.ade.inspector. dataAccess.ValueType[])
	 */
	@Override
	public boolean isTypeSupported(ValueType... types) {
		try {
			returnedType(types);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
