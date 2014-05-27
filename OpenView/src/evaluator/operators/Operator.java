package evaluator.operators;

import core.Value;
import core.ValueType;

/***
 * Operator Interface
 * 
 * @author martino
 * 
 */
public interface Operator {
	/***
	 * Get operator name (ex: Sum)
	 * 
	 * @return operator name
	 */
	public String name();

	/***
	 * Get operator symbol (ex: +)
	 * 
	 * @return operator symbol
	 */
	public String symbol();

	/***
	 * Get operator description
	 * 
	 * @return operator description
	 */
	public String description();

	/***
	 * Get operator priority (needed for parsing)
	 * 
	 * @return operator priority
	 */
	public int priority();

	/***
	 * Get the max (an normally also min.) number of inputs
	 * 
	 * @return number of inputs
	 */
	public int input();

	/***
	 * Compute the operation
	 * 
	 * @param operands
	 *            list of operands as {@link Value}
	 * @return {@link Value} computed
	 * @throws Exception
	 *             if types or number of operands are incompatible
	 */
	public Value evaluate(Value... operands) throws Exception;

	public Operator clone();

	/**
	 * Check what is the return type on current configuration
	 * 
	 * @return The returned type
	 */
	public ValueType returnedType(ValueType... types) throws Exception;

	/**
	 * Check if connected types are supported
	 * 
	 * @return true if the types are supported. If there is one type it return
	 *         true if the type is compatible. if there is more than one type it
	 *         checks if the types are compatible together
	 */
	public boolean isTypeSupported(ValueType... types);
}
