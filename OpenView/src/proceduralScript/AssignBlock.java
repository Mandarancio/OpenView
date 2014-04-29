package proceduralScript;

import core.Value;

public class AssignBlock extends AbstractBlock{
	
	private Var var_;
	private Block rightExp_;
	public AssignBlock() {
		super("assign");
	}
	
	public AssignBlock(Var var, Block right ){
		this();
		var_=var;
		rightExp_=right;
	}


	@Override
	public Value run() {
		Value v=rightExp_.run();
		var_.value=v;

		System.out.println(var_.name()+"="+v);
		return var_.run();
	}



	public Block getRightExp() {
		return rightExp_;
	}



	public void setRightExp(Block rightExp_) {
		this.rightExp_ = rightExp_;
	}



	public Var getVar() {
		return var_;
	}



	public void setVar(Var var_) {
		this.var_ = var_;
	}

	@Override
	public boolean isBinary() {
		return true;
	}
}
