package evaluator.operators;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * Static class used to manage all the list of operators
 * 
 * @author martino
 * 
 */
public class OperatorManager {
	/***
	 * List of operators
	 */
	private static ArrayList<Operator> operators_;
	/***
	 * Symbol/{@link Operator} map
	 */
	private static HashMap<String, Operator> operatorsMap_;

	/***
	 * Init standard operators
	 */
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

	/***
	 * Retrive the Symbol/Operator map
	 * 
	 * @return Symbol/Operator map
	 */
	public static HashMap<String, Operator> getOperatorMap() {
		if (operatorsMap_ == null) {
			operatorsMap_ = new HashMap<>();

			for (Operator o : getOperators()) {
				operatorsMap_.put(o.name(), o);
			}

		}
		return operatorsMap_;
	}

	/***
	 * Add extra operator to the list (invalidate the current map)
	 * 
	 * @param o
	 *            {@link Operator} to add
	 */
	public static void addOperator(Operator o) {
		if (o != null) {
			if (operatorsMap_ != null) {
				operatorsMap_.clear();
				operatorsMap_ = null;
			}
			operators_.add(o);
		}
	}

	/***
	 * Add a list of extra operators to the list (invalidate the current map)
	 * 
	 * @param os
	 *            list of extra {@link Operator}
	 */
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

	/***
	 * Get an operator
	 * 
	 * @param key
	 *            {@link Operator} symbol
	 * @return {@link Operator}
	 */
	public static Operator get(String key) {
		return getOperatorMap().get(key);
	}

	/**
	 * Get the complete list of operators
	 * 
	 * @return list of operators
	 */
	public static ArrayList<Operator> getOperators() {
		if (operators_ == null) {
			intiOperators();
		}
		return operators_;
	}
}
