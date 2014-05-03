package ovscript;

import core.Value;

public class PrintBlock extends AbstractBlock{

	private Block body_;

	public PrintBlock(Block b) {
		super("print");
		setBody(b);
	}

	@Override
	public Value run(CodeBlock i) throws InterpreterException {
		Value v=body_.run( i);
		System.out.println(v.getString());
		return v;
	}

	public Block getBody() {
		return body_;
	}

	public void setBody(Block body_) {
		this.body_ = body_;
	}
	
}
