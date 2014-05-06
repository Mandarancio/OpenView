package evaluator.operators;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public class MatrixMultiplicationOperator extends AbstractOperator {
	public MatrixMultiplicationOperator() {
		super("@", 2, 2);
	}

	@Override
	public Value evaluate(Value... operands) throws Exception {
		if (operands.length != 2)
			throw new EvalException(
					"Number of operands for Matrix multiplication is 2 and not "
							+ operands.length);

		Value left = operands[0];
		Value right = operands[1];

		if (left.getType().isArray() && right.getType().isArray()) {
			Value[] lvec = left.getValues();
			Value[] rvec = right.getValues();
			if (lvec[0].getType().isArray() || rvec[1].getType().isArray()) {
				if (lvec[0].getType().isArray() && !rvec[1].getType().isArray()
						&& lvec[0].getValues().length == rvec.length) {
					Value[] res = new Value[lvec.length];
					for (int i = 0; i < res.length; i++) {
						Value[] row = lvec[i].getValues();
						double v = 0;
						for (int j = 0; j < rvec.length; j++) {
							v += row[j].getDouble() * rvec[j].getDouble();
						}
						res[i] = new Value(v);
					}
					return new Value(res);
				} else if (lvec[0].getType().isArray()
						&& rvec[0].getType().isArray()
						&& lvec[0].getValues().length == rvec[0].getValues().length) {
					Value[][] res=new Value[lvec.length][rvec.length];
					for (int i=0;i<lvec.length;i++){
						Value[] row=lvec[i].getValues();
						
						for (int j=0;j<rvec.length;j++){
							Value[] column=rvec[j].getValues();
							double v=0;
							for (int h=0;h<column.length;h++){
								v+=row[h].getDouble()*column[h].getDouble();
							}
							res[i][j]=new Value(v);
						}
					}
					return new Value(res);
				}
				throw new EvalException(
						"Matrix multiplication not yet supported");
			} else if (lvec.length == rvec.length) {

				double val = 0;
				for (int i = 0; i < lvec.length; i++) {
					val += lvec[i].getDouble() * rvec[i].getDouble();
				}
				return new Value(val);
			}
		}

		throw new EvalException(
				"Matrix multiplication acpept only 2 array inputs");
	}

	@Override
	public ValueType returnedType(ValueType... types) throws Exception {
		// we need two operands
		if (types.length != input())
			throw new EvalException("Error: wrong number of operands");

		// get operands
		ValueType l = types[0];
		ValueType r = types[1];

		if (l == ValueType.VOID && r == ValueType.VOID)
			return ValueType.VOID;

		// If they are both arrays
		if (l.isArray() && r.isArray())
			return (ValueType.DOUBLE);

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
	public boolean isTypeSupported(ValueType... types) {
		try {
			returnedType(types);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
