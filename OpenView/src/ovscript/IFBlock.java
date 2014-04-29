/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovscript;

import core.Value;

/**
 * 
 * @author martino
 */
public class IFBlock extends AbstractBlock {

	private Block condition_;
	private Block else_;
	private Block body_;

	public IFBlock() {
		super("IF");
	}

	@Override
	public Value run(Interpreter i) {
		Value v = condition_.run(i);
		boolean b = false;
		try {
			b = v.getBoolean();
		} catch (Exception e) {
		}
		if (b) {
			runBlock(body_,i);
		} else {

			if (else_ != null) {
				
				return else_.run(i);
			} 
		}
		return new Value(Void.TYPE);
	}

	
	public void setCondition(Block b) {
		condition_ = b;
	}

	public void setElse(Block b) {
		else_ = b;
	}

	void setBody(Block first) {
		body_ = first;
	}

}
