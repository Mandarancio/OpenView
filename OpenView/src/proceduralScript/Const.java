package proceduralScript;

import core.Value;

public class Const extends AbstractBlock{

	private Value value_;
	public Const(Value v) {
		super("Konst");
		value_=v;
	}
	
	public Const(int i){
		this(new Value(i));
	}
	
	
	@Override
	public Value run(){
		return value_;
	}
}
