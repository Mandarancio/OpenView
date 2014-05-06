package run;

import core.Value;
import core.ValueType;

public class Test {
	public static void main(String[] args) {
//		String array[]={"ciao","ciao"};
		String toParse="[1.0, 2.0]";
		Value v=new Value(toParse,ValueType.ARRAY);
		
		
		System.out.println(v);
		
		
		System.exit(0);
	}
}
