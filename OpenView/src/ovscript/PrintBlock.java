package ovscript;

import core.Value;

public class PrintBlock extends AbstractBlock{

	private Block body_;

	public PrintBlock(Block b) {
		super("print");
		setBody(b);
	}

	@Override
	public Value run() {
		Value v=body_.run();
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
