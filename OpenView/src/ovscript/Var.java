package ovscript;

import core.Value;

public class Var extends AbstractBlock {

    private Value value=new Value();

    public Var(String name) {
        super(name);
    }

    public Var(String name, Value val) {
        this(name);
        value = new Value(val.getData());
    }

    @Override
    public Value run(CodeBlock i) throws InterpreterException {
    	if (value!=null)
    		return new Value(value.getData());
    	else
    		return new Value();
    }
    
    
    public Value getValue() throws InterpreterException{
    	return value;
    }
    
    public void setValue(Value v) throws InterpreterException{
    	value=new Value(v);
    }

}
