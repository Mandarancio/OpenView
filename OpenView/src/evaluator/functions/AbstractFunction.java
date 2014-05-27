package evaluator.functions;

/***
 * Abstract implementation of {@link Function} interface
 * 
 * @author martino
 * 
 */
public abstract class AbstractFunction implements Function {
	/***
	 * Name of the function
	 */
	private String name_ = "";
	/***
	 * Symbol of the function
	 */
	private String symbol_ = "";
	/***
	 * Number of inputs
	 */
	private int input_ = 0;
	/***
	 * Description of the function
	 */
	private String description_ = "";

	/**
	 * Complete initialization
	 * 
	 * @param name
	 *            name of the function
	 * @param symbol
	 *            symbol of the function
	 * @param description
	 *            description of the function
	 * @param inputs
	 *            number of arguments
	 */
	public AbstractFunction(String name, String symbol, String description,
			int inputs) {
		name_ = name;
		symbol_ = symbol;
		description_ = description;
		input_ = inputs;
	}

	/***
	 * Partial initialization
	 * 
	 * @param name
	 *            name, symbol and descriptor of the function
	 * @param inputs
	 *            number of inputs
	 */
	public AbstractFunction(String name, int inputs) {
		this(name, name, name, inputs);
	}

	/***
	 * Partial initialization
	 * 
	 * @param name
	 *            name, symbol and descriptor of the function
	 */
	public AbstractFunction(String name) {
		this(name, name, name, 0);
	}

	@Override
	public String name() {
		return name_;
	}

	@Override
	public String symbol() {
		return symbol_;
	}

	@Override
	public int input() {
		return input_;
	}

	@Override
	public String description() {
		return description_;
	}

	@Override
	public Function clone() {
		return null;
	}

}
