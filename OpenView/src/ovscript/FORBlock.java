package ovscript;

import core.Value;

public class FORBlock extends AbstractBlock {
	private Block initalization_;
	private Block condition_;
	private Block operation_;
	
	private Block body_;
	
	public FORBlock(Block i, Block c, Block o) {
		super("for");
		initalization_=i;
		condition_=c;
		operation_=o;
	}

	@Override
	public Value run(InterpreterBlock i) {
		try{
			initalization_.run(i);
			while (condition_.run(i).getBoolean()){
				runBlock(body_,i);
				operation_.run(i);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return new Value();
	}

	public Block getBody() {
		return body_;
	}

	public void setBody(Block body_) {
		this.body_ = body_;
	}
	



}
