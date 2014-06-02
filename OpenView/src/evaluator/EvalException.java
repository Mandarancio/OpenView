package evaluator;

/***
 * Specific exception for the evaluator
 * 
 * @author martino
 * 
 */
public class EvalException extends Exception {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 4940726390937613272L;

	/***
	 * Generic evaluation exception
	 */
	public EvalException() {
		super("Generic evaluation exception!");
	}

	/***
	 * Costumized evaluation exception
	 * 
	 * @param msg
	 *            message of the exception
	 */
	public EvalException(String msg) {
		super(msg);
	}

}
