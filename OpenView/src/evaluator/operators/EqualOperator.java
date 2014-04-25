package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

/**
 * The equal operator.
 */
public class EqualOperator extends AbstractOperator {
	/**
	 * Default constructor.
	 */
	public EqualOperator() {
		super("==", "Equal operator", 3);
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
		// we need two operands
		if (operands.length != input())
			throw new EvalException("Error: wrong number of operands");

		// get operands
		Value l = operands[0];
		Value r = operands[1];

		// if they are identical
		if (l.getType().isNumeric() && r.getType().isNumeric()) {
			// perform OR
			return new Value(new Boolean(l.getDouble() == r.getDouble()));
		}

		// if they are identical
		if (l.getType() == r.getType()) {
			// perform OR
			return new Value(new Boolean(l.getData().equals(r.getData())));
		}

		return new Value(new Boolean(false));
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
		return (ValueType.BOOLEAN);
	}

	@Override
	public AbstractOperator clone() {
		return new EqualOperator();
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
		return true;
	}
}