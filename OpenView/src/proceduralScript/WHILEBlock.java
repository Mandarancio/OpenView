package proceduralScript;

import core.Value;

public class WHILEBlock extends AbstractBlock {


	private Block body_;
	private Block condition_;
	
	public WHILEBlock(Block c) {
		super("while");
		condition_=c;
	}
	
	@Override
	public Value run() {
		try{
			while (condition_.run().getBoolean()){
				runBlock(body_);
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
	
	private void runBlock(Block body){
		Block b=body;
		while(b!=null){
			b.run();
			b=b.next();
		}
	}
}
