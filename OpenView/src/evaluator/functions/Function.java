package evaluator.functions;

import core.Value;
import core.ValueType;

/***
 * Function interface
 * 
 * @author martino
 * 
 */
public interface Function {
	/***
	 * Name of the {@link Function} (ex: Cosine)
	 * 
	 * @return function name
	 */
	public String name();

	/***
	 * Symbol of the {@link Function} (ex: cos)
	 * 
	 * @return function symbol
	 */
	public String symbol();

	/***
	 * Number of arguments of the {@link Function}
	 * 
	 * @return arguments number
	 */
	public int input();

	/***
	 * Function description
	 * 
	 * @return
	 */
	public String description();

	/***
	 * Clone the {@link Function}
	 * 
	 * @return clone
	 */
	public Function clone();

	/***
	 * Evaluate the {@link Function}
	 * 
	 * @param arguments
	 *            list of arguments as {@link Value}
	 * @return computed {@link Value}
	 * @throws Exception
	 *             throw an exception if number or type of arguments are
	 *             incompatible
	 */
	public Value evaluate(Value... arguments) throws Exception;

	/**
	 * Compute the return {@link ValueType} of the current configuration
	 * 
	 * @return The returned type
	 */
	public ValueType returnedType(ValueType... types) throws Exception;

	/**
	 * Check if the current inputs {@link ValueType} are compatibles
	 * 
	 * @return true if the types are supported. If there is one type it return
	 *         true if the type is compatible. if there is more than one type it
	 *         checks if the types are compatible together
	 */
	public boolean isTypeSupported(ValueType... types);
}
