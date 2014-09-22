package evaluator.functions.array;

import java.util.ArrayList;

import core.Value;
import core.ValueType;
import evaluator.EvalException;
import evaluator.functions.AbstractFunction;
import evaluator.functions.Function;

public class ArrayFind extends AbstractFunction {
	 public ArrayFind() {
	        super("find", "find", "", 3);
	    }

	    @Override
	    public Value evaluate(Value... arguments) throws Exception {
	        if (arguments.length !=2 || arguments.length !=3) {
	            throw new EvalException("add support only two or three arguments!");
	        }
	        Value arr = new Value(arguments[0]);
	        Value val = arguments[1];
	        
	        if (arr.getType() == ValueType.ARRAY) {
	        	if (arguments.length==3  && arguments[2].getType().isNumeric()){
	        		ArrayList<Value> array=arr.getArray();
	        		double value=val.getDouble();
	        		double err=arguments[2].getDouble();
	        		for (int i=0;i<array.size();i++){
	        			if (array.get(i).getType().isNumeric()){
	        				double v=array.get(i).getDouble();
	        				if (Math.abs(v-value)<Math.abs(err)){
	        					return new Value(i);
	        				}
	        			}
	        		}
	        	}
	        	else{
	        		ArrayList<Value> array=arr.getArray();
	        		
	        		for (int i=0;i<array.size();i++){
	        			if (array.get(i).getData().equals(val.getData()))
	        				return new Value(i);
	        		}
	        	}
	        	return new Value(-1);
	        }
	        throw new EvalException("add support only array and values!");

	    }

	    @Override
	    public ValueType returnedType(ValueType... types) throws Exception {
	        return ValueType.INTEGER;
	    }

	    @Override
	    public boolean isTypeSupported(ValueType... types) {
	    	if (types.length==2){
	    		if (types[0] == ValueType.ARRAY ) {
	    			return true;
	    		}
	        }else if (types.length==3){
	        	if (types[0]==ValueType.ARRAY && types[1].isNumeric() && types[2].isNumeric())
	        		return true;
	        }
	        return false;
	    }

	    @Override
	    public Function clone() {
	        return new ArrayAdd();
	    }
}
