package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

/**
 * The greater than operator.
 */
public class GreaterThanOperator extends AbstractOperator {
	/**
	 * Default constructor.
	 */
	public GreaterThanOperator() {
		super(">", "Greater than operatror.", 4);
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
		if (operands.length != 2)
			throw new EvalException(this.name()
					+ " Error: wrong number of operands");

		// get operands
		Value l = operands[0];
		Value r = operands[1];

		// if they are identical
		if (l.getType().isNumeric() && r.getType().isNumeric()) {
			if (l.getType() == ValueType.LONG && r.getType() == ValueType.LONG) {
				// if they are numerical
				if (l.getType().isNumeric() && r.getType().isNumeric()) {
					return new Value(new Boolean(l.getLong() > r.getLong()));
				}
			}

			return new Value(new Boolean(l.getDouble() > r.getDouble()));
		}

		if (l.getType() == ValueType.STRING && r.getType() == ValueType.STRING)
			return new Value(new Boolean(l.toString().length() > r.toString()
					.length()));

		if (l.getType() == ValueType.STRING && r.getType().isNumeric())
			return new Value(new Boolean(l.toString().length() > r.getInt()));

		if (l.getType().isNumeric() && r.getType() == ValueType.STRING)
			return new Value(new Boolean(l.getInt() > r.toString().length()));

	

		
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

		return (ValueType.BOOLEAN);

	}

	@Override
	public AbstractOperator clone() {
		return new GreaterThanOperator();
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
		if (types.length == input()) {
			ValueType l = types[0], r = types[1];

			if (r != ValueType.VOID && l == ValueType.VOID)
				return true;

			if (l.isNumeric()
					&& (r == ValueType.STRING || r.isNumeric() || r == ValueType.VOID))
				return true;

			if (l == ValueType.STRING
					&& (r == ValueType.STRING || r.isNumeric() || r == ValueType.VOID))
				return true;

			if (l.isArray()
					&& (r.isNumeric() || r == ValueType.VOID || r.isArray()))
				return true;

			if (l == ValueType.VOID
					&& (r.isNumeric() || r.isArray() || r == ValueType.STRING))
				return true;
		}
		return false;
	}
}