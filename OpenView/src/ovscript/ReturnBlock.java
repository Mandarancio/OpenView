package ovscript;

import core.Value;

public class ReturnBlock extends AbstractBlock{

	private Block body_;

	public ReturnBlock( Block b) {
		super("return");
		body_=b;
	}

	@Override
	public Value run(CodeBlock i) throws InterpreterException {
		return body_.run(i);
	}

}
