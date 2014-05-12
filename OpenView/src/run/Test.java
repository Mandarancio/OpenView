package run;

import core.Value;

public class Test {
	public static void main(String[] args) {
//		String array[]={"ciao","ciao"};
		String toParse="[0:1:10]";
		Value v=Value.parseValue(toParse);
		
		System.out.println(v);
		

		System.exit(0);
	}
}
