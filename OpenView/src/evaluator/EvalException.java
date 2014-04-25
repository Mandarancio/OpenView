package evaluator;

public class EvalException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4940726390937613272L;

	public EvalException() {
		super("Generic evaluation exception!");
	}

	public EvalException(String msg) {
		super(msg);

	}

}
