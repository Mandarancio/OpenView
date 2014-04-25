package evaluator.functions;

public abstract class AbstractFunction implements Function {
	private String name_ = "";
	private String symbol_ = "";
	private int input_ = 0;
	private String description_ = "";
	
	public AbstractFunction(String name, String symbol,String description,int inputs){
		name_=name;
		symbol_=symbol;
		description_=description;
		input_=inputs;
	}
	
	public AbstractFunction(String name,int inputs){
		this(name,name,name,inputs);
	}
	
	public AbstractFunction(String name){
		this(name,name,name,0);
	}

	public String name() {
		return name_;
	}

	public String symbol() {
		return symbol_;
	}

	public int input() {
		return input_;
	}

	public String description() {
		return description_;
	}
	
	@Override
	public Function clone() {
		return null;
	}
}
