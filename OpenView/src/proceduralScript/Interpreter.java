package proceduralScript;

import core.Value;
import evaluator.operators.AdditionOperator;
import evaluator.operators.SubtractionOperator;

public class Interpreter {

	public void run(Block block) {
		Block b = block;
		while (b != null) {
			b.run();
			b = b.next();
		}
	}

	public static void main(String[] args) {
		Var a = new Var("a", new Value(2));

		Var b = new Var("b", new Value(3));
		a.setNext(b);
		AssignBlock ab = new AssignBlock();
		b.setNext(ab);
		ab.setVar(a);
		OperatorBlock op = new OperatorBlock(new AdditionOperator());
		op.setLeft(a);
		op.setRight(b);
		ab.setRightExp(op);
		OperatorBlock op2 = new OperatorBlock(new SubtractionOperator());
		op2.setLeft(a);
		op2.setRight(new Const(2));
		AssignBlock as = new AssignBlock(a, op2);
		ab.setNext(as);
		Interpreter i = new Interpreter();
		i.run(a);
		System.out.println(a.value.toString());
	}

}
