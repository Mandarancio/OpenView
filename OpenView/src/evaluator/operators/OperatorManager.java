package evaluator.operators;

import java.util.ArrayList;
import java.util.HashMap;

public class OperatorManager {
	private static ArrayList<Operator> operators_;
	private static HashMap<String, Operator> operatorsMap_;

	public static void intiOperators() {
		operators_ = new ArrayList<>();
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

	public static HashMap<String, Operator> getOperatorMap() {
		if (operatorsMap_ == null) {
			operatorsMap_ = new HashMap<>();

			for (Operator o : getOperators()) {
				operatorsMap_.put(o.name(), o);
			}

		}
		return operatorsMap_;
	}

	public static void addOperator(Operator o) {
		if (o != null) {
			if (operatorsMap_ != null) {
				operatorsMap_.clear();
				operatorsMap_ = null;
			}
			operators_.add(o);
		}
	}

	public static void addOperatros(ArrayList<Operator> os) {
		if (os.size() > 0) {
			if (operatorsMap_ != null) {
				operatorsMap_.clear();
				operatorsMap_ = null;
			}
			for (Operator o : os) {
				operators_.add(o);
			}
		}
	}

	public static Operator get(String key) {
		return getOperatorMap().get(key);
	}

	public static ArrayList<Operator> getOperators() {
		if (operators_ == null) {
			intiOperators();
		}
		return operators_;
	}
}
