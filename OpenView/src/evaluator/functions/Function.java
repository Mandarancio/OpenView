package evaluator.functions;

import core.Value;
import core.ValueType;
import evaluator.EvalException;

public interface Function {
	public String name();

	public String symbol();

	public int input();

	public String description();

	public Function clone();

	public Value evaluate(String propertyKey, Value... arguments)
			throws EvalException;

	/**
	 * @return The returned type
	 */
	public ValueType returnedType(ValueType... types) throws EvalException;

	/**
	 * @return true if the types are supported. If there is one type it return
	 *         true if the type is compatible. if there is more than one type it
	 *         checks if the types are compatible together
	 */
	public boolean isTypeSupported(ValueType... types);
}
