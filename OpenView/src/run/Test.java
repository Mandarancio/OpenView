package run;

import core.Value;

public class Test {
	public static void main(String[] args) {
		String array[]={"ciao","ciao"};
		Value v=new Value(array);
		
		System.out.println(v.getData());
		System.exit(0);
	}
}
