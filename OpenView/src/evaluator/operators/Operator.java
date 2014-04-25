package evaluator.operators;

import core.Value;
import core.ValueType;

public interface Operator {
	public String name();

	public String symbol();

	public String description();

	public int priority();

	public int input();

	public Value evaluate(Value... operands) throws Exception;
	

	public Operator clone();

	/**
	 * @return The returned type
	 */
	public ValueType returnedType(ValueType... types) throws  Exception;

	/**
	 * @return true if the types are supported. If there is one type it return
	 *         true if the type is compatible. if there is more than one type it
	 *         checks if the types are compatible together
	 */
	public boolean isTypeSupported(ValueType... types);
}
