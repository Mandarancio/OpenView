package ovscript;

import core.Value;

public class PrintBlock extends AbstractBlock{

	private Block body_;

	public PrintBlock(Block b) {
		super("print");
		setBody(b);
	}

	@Override
	public Value run(InterpreterBlock i) {
		Value v=body_.run( i);
		System.out.println(v);
		return v;
	}

	public Block getBody() {
		return body_;
	}

	public void setBody(Block body_) {
		this.body_ = body_;
	}
	
}
