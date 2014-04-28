package proceduralScript;

import core.Value;
import evaluator.operators.Operator;

public class OperatorBlock extends AbstractBlock{
	private Operator operator_;
	private Block left_,right_;
	
	public OperatorBlock(Operator o) {
		super("operator");
		operator_=o;
	}

	@Override
	public Value run() {
		Value[] operands=new Value[operator_.input()];
		int c=0;
		if (left_!=null){
			operands[c]=left_.run();
			c++;
		}
		if (right_!=null){
			operands[c]=right_.run();
		}
		try {
			return operator_.evaluate(operands);
		} catch (Exception e) {
			e.printStackTrace();
			return new Value(Void.TYPE);
		}
	}
	
	public void setLeft(Block b){
		left_=b;
	}
	
	public void setRight(Block b){
		right_=b;
	}
	
	public void setOperator(Operator o){
		operator_=o;
	}
	@Override
	public boolean isBinary() {
		if (operator_!=null)
			return operator_.input()==2;
		return super.isBinary();
	}
	
}
