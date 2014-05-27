package evaluator.operators;

/***
 * Abstract implementation of {@link Operator} interface
 * 
 * @author martino
 * 
 */
public abstract class AbstractOperator implements Operator {
	/***
	 * Name and Symbol of the operator
	 */
	protected String name = "";
	/***
	 * Description of the operator
	 */
	protected String description = "";
	/***
	 * Number of inputs
	 */
	protected int input = 2;
	/***
	 * Priority of the operator
	 */
	protected int priority = 1;

	/***
	 * Simple constructor
	 * 
	 * @param name
	 *            name of the operator
	 * @param priority
	 *            priority of the operator
	 */
	public AbstractOperator(String name, int priority) {
		this(name, "", priority, 2);
	}

	/***
	 * All info constructor
	 * 
	 * @param name
	 *            name of the operator
	 * @param description
	 *            description of the operator
	 * @param priority
	 *            priority of the operator
	 * @param input
	 *            number of inputs
	 */
	public AbstractOperator(String name, String description, int priority,
			int input) {
		this.name = name;
		this.priority = priority;
		this.input = input;
		this.description = description;
	}

	/***
	 * All info constructor
	 * 
	 * @param name
	 *            name of the operator
	 * @param priority
	 *            priority of the operator
	 * @param input
	 *            number of inputs
	 */
	public AbstractOperator(String name, int priority, int input) {
		this(name, "", priority, input);
	}

	/***
	 * All info constructor
	 * 
	 * @param name
	 *            name of the operator
	 * @param description
	 *            description of the operator
	 * @param priority
	 *            priority of the operator
	 */
	public AbstractOperator(String name, String description, int priority) {
		this(name, description, priority, 2);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int priority() {
		return priority;
	}

	@Override
	public AbstractOperator clone() {
		return null;
	}

	@Override
	public int input() {
		return input;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public String symbol() {
		return name;
	}

}
