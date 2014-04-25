package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public class DivisionOperator extends AbstractOperator {

	public DivisionOperator() {
		super("/", "Division operator.", 3);
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
			// compute the result
			return new Value(new Double(l.getDouble() / r.getDouble()));
		}

		throw new EvalException("Operands type is invalid!");

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

		if (r == ValueType.VOID && l == ValueType.VOID)
			return ValueType.VOID;

		// if they are numerical
		if (l.isNumeric() && (r.isNumeric() || r == ValueType.VOID)) {
			return (ValueType.DOUBLE);
		}

		// If they are both arrays
		if (l.isArray()
				&& (r.isArray() || r.isNumeric() || r == ValueType.VOID)) {
			return (ValueType.ARRAY);
		}

		if (r != ValueType.VOID && l == ValueType.VOID) {
			if (r == ValueType.ARRAY)
				return ValueType.ARRAY;
			if (r.isNumeric())
				return ValueType.VOID;
		}

		throw new EvalException("Operands type is invalid!");
	}

	@Override
	public DivisionOperator clone() {
		return new DivisionOperator();
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
		try {
			returnedType(types);
			return true;
		} catch (EvalException ex) {
			return false;
		}
	}

}