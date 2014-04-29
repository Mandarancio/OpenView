package ovscript;

import core.Value;

public class WHILEBlock extends AbstractBlock {


	private Block body_;
	private Block condition_;
	
	public WHILEBlock(Block c) {
		super("while");
		condition_=c;
	}
	
	@Override
	public Value run(InterpreterBlock i) {
		try{
			while (condition_.run(i).getBoolean()){
				runBlock(body_,i);
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
