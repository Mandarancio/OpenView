package ovscript;

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
	
	public void setValue(Value v){
		value_=new Value(v.getData());
	}
	
	public Value getValue(){
		return value_;
	}
	
	@Override
	public Value run(){
		return value_;
	}
}
