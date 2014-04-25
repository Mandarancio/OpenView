package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public class LeftShiftOperator extends AbstractOperator {
	/**
	 * Default constructor.
	 */
	public LeftShiftOperator() {
		super("<<", "Binary left shift.", 2);
	}

	@Override
	public Value evaluate(Value... operands) throws Exception {
		// we want only two operands
		if (operands.length != 2)
			throw new EvalException(this.name()
					+ " Error: wrong number of operands");

		// get operands
		Value l = operands[0];
		Value r = operands[1];

		// if types match
		if (l.getType() == r.getType()) {
			if (l.getType() == ValueType.INTEGER)
				return new Value(new Integer(l.getInt() << r.getInt()));

			if (l.getType() == ValueType.LONG)
				return new Value(new Long(l.getLong() << r.getLong()));

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
		if (types.length != input())
			throw new EvalException(this.name()
					+ " Error: wrong number of operands");

		// get operands
		ValueType l = types[0];
		ValueType r = types[1];

		if ((l == ValueType.LONG && r == ValueType.INTEGER)
				|| (l == ValueType.INTEGER && r == ValueType.LONG))
			return ValueType.LONG;

		// if types match
		if (l == r) {
			if (r == ValueType.INTEGER)
				return (ValueType.INTEGER);

			if (r == ValueType.LONG)
				return (ValueType.LONG);

			if (l == ValueType.VOID)
				return ValueType.VOID;

			throw new EvalException(this.name()
					+ " Function Operands error! Type mismatch!");
		}

		if (l == ValueType.VOID) {
			if (r == ValueType.ARRAY)
				return (ValueType.ARRAY);

			if (r == ValueType.INTEGER)
				return (ValueType.VOID);
		}

		if (r == ValueType.VOID) {
			if (l == ValueType.ARRAY)
				return (ValueType.ARRAY);

			if (l == ValueType.INTEGER)
				return (ValueType.INTEGER);
		}

		// if it is a Vector plus a scalar
		if (l.isArray() && (r == ValueType.INTEGER)) {
			return (ValueType.ARRAY);
		}

		throw new EvalException(this.name()
				+ " Function Operands error! Type mismatch!");
	}

	@Override
	public AbstractOperator clone() {
		return new LeftShiftOperator();
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