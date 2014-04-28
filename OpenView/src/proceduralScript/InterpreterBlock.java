package proceduralScript;

import java.util.HashMap;

import core.Value;
import evaluator.operators.OperatorManager;

public class InterpreterBlock {
	private HashMap<String, Var> variables_ = new HashMap<>();
	private OperatorManager operators_ = new OperatorManager();

	public void run(Block block) {
		Block b = block;
		while (b != null) {
			b.run();
			b = b.next();
		}
	}

	public Block parse(String code) {
		String lines[] = code.split("\n");
		Block first = null;
		Block last = null;
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			Block b = parseLine(line);
			if (first == null) {
				first = b;
				last = b;
			} else {
				last.setNext(b);
				last = b;
			}
			//

		}
		return first;
	}

	private Block parseLine(String line) {
		System.out.println(line);
		char c = line.charAt(0);
		char c_past = line.charAt(0);
		String past = "" + c_past;
		for (int i = 1; i < line.length(); i++) {
			c_past = c;
			c = line.charAt(i);
			if (operators_.get("" + c_past + c) != null) {
				Block left;
				past=past.substring(0,past.length()-1);

				if (variables_.containsKey(past)) {
					left = variables_.get(past);
				} else
					left = new Const(Value.parse(past));
				OperatorBlock ob = new OperatorBlock(operators_.get("" + c_past
						+ c));
				ob.setLeft(left);
				ob.setRight(parseLine(line.substring(i)));

				return ob;
			} else if (operators_.get("" + c_past) != null) {
				past=past.substring(0,past.length()-1);
				System.out.println("past: "+past);
				Block left;
				if (variables_.containsKey(past)) {
					left = variables_.get(past);
				} else
					left = new Const(Value.parse(past));
				OperatorBlock ob = new OperatorBlock(
						operators_.get("" + c_past));
				ob.setLeft(left);
				ob.setRight(parseLine(line.substring(i)));
				return ob;
			} else if (c_past == '=') {
				past=past.substring(0,past.length()-1);
				Var v = variables_.get(past);
				if (v == null) {
					v = new Var(past);
					variables_.put(past, v);
				}
				return new AssignBlock(v, parseLine(line.substring(i)));
			}else  if(c_past=='('){
				past=past.substring(0,past.length()-1);
				if (past.equals("export")){
					System.out.println("export");
				}else if (past.equals("import")){
					System.out.println("import");
				}
			}

			past += c;
		}
		if (variables_.containsKey(past))
			return variables_.get(past);

		return new Const(Value.parse(past));
	}

	public static void main(String[] args) {
		String test = "a=0\n"
					+ "import(a)\n"
					+ "b=3\n"
					+ "a=b+a/2\n"
					+ "export(a)\n";
	

		//
		//
		// Var a = new Var("a", new Value(2));
		//
		// Var b = new Var("b", new Value(3));
		// a.setNext(b);
		// AssignBlock ab = new AssignBlock();
		// b.setNext(ab);
		// ab.setVar(a);
		// OperatorBlock op = new OperatorBlock(new AdditionOperator());
		// op.setLeft(a);
		// op.setRight(b);
		// ab.setRightExp(op);
		// OperatorBlock op2 = new OperatorBlock(new SubtractionOperator());
		// op2.setLeft(a);
		// op2.setRight(new Const(2));
		// AssignBlock as = new AssignBlock(a, op2);
		// ab.setNext(as);
		InterpreterBlock i = new InterpreterBlock();
		Block b = i.parse(test);
		i.run(b);

		// System.out.println(a.value.toString());

	}

}
