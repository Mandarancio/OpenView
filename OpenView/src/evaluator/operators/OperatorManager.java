package evaluator.operators;

import java.util.ArrayList;
import java.util.HashMap;

public class OperatorManager {
	private ArrayList<Operator> operators_ = new ArrayList<>();
	private HashMap<String, Operator> operatorsMap_;

	public OperatorManager() {
		operators_.add(new AdditionOperator());
		operators_.add(new SubtractionOperator());
		operators_.add(new MultiplicationOperator());
		operators_.add(new DivisionOperator());
		operators_.add(new ModulusOperator());

		operators_.add(new EqualOperator());
		operators_.add(new NotEqualOperator());
		operators_.add(new GreaterThanOperator());
		operators_.add(new GreaterThanOrEqualOperator());
		operators_.add(new LessThanOperator());
		operators_.add(new LessThanOrEqualOperator());
		
		operators_.add(new AndOperator());
		operators_.add(new OrOperator());
		operators_.add(new NotOperator());

		operators_.add(new LeftShiftOperator());
		operators_.add(new RightShiftOperator());
		
		operators_.add(new MatrixMultiplicationOperator());

	}

	public HashMap<String, Operator> getOperatorMap() {
		if (operatorsMap_ == null) {
			operatorsMap_ = new HashMap<>();

			for (Operator o : operators_) {
				operatorsMap_.put(o.name(), o);
			}

		}
		return operatorsMap_;
	}

	public Operator get(String key) {
		return getOperatorMap().get(key);
	}

	public ArrayList<Operator> getOperators() {
		return operators_;
	}
}
