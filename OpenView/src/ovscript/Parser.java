package ovscript;

import core.Value;
import evaluator.operators.OperatorManager;

public class Parser {


	private static final OperatorManager operators_ = new OperatorManager();

	

	
	public static ReturnStruct parseLine(CodeBlock block,String l, String nextLines[]) {
		if (l.length() == 0)
			return new ReturnStruct(null, 1);
		String line = Parser.clean(l);
		if (line.length() == 0 || line.startsWith("#"))
			return new ReturnStruct(null, 1);
		else if (line.startsWith("@")) {
			// debug option
			return new ReturnStruct(new DebugCommand(line.substring(1)), 1);
		}
		char c = line.charAt(0);
		char c_past = line.charAt(0);
		String past = "" + c_past;
		if (line.startsWith("if ")) {
			IFBlock ib = new IFBlock(block);
			past = line.substring(2);
			Block cond = parseLine(block,past, nextLines).block;
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return ib.parse(copy);
		} else if (line.startsWith("elif ")) {
			ELSEBlock ib = new ELSEBlock(block);
			past = line.substring(4);
			Block cond = parseLine(block,past, nextLines).block;
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return ib.parse(copy);
		} else if (line.equals("else")) {
			ELSEBlock ib = new ELSEBlock(block);
			Block cond = new Const(new Value(true));
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return ib.parse(copy);
		} else if (line.equals("end")) {
			return new ReturnStruct(new ENDBlock(), 1);
		} else if (line.startsWith("for ")) {
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseFor(block,line, copy);
		} else if (line.startsWith("while ")) {
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseWhile(block,line, copy);
		}
		for (int i = 1; i < line.length(); i++) {
			c_past = c;
			c = line.charAt(i);
			if ((c_past == '+' || c_past == '-' || c_past == '*' || c_past == '/')
					&& c == '=') {
				past = past.substring(0, past.length() - 1);
				Var v = block.getVar(past);
				if (v == null) {
					v = new Var(past);
					block.putVar(past, v);
				}
				OperatorBlock op = new OperatorBlock(operators_
						.get(c_past + "").clone());
				op.setLeft(v);
				op.setRight(parseLine(block,line.substring(i + 1), nextLines).block);
				return new ReturnStruct(new AssignBlock(v, op), 1);
			} else if (c_past == c && (c == '+' || c == '-')) {
				past = past.substring(0, past.length() - 1);
				Var v = block.getVar(past);
				if (v == null) {
					v = new Var(past);
					block.putVar(past, v);
				}
				OperatorBlock op = new OperatorBlock(operators_
						.get(c_past + "").clone());
				op.setLeft(v);
				op.setRight(new Const(1));
				return new ReturnStruct(new AssignBlock(v, op), 1);
			} else if (operators_.get("" + c_past + c) != null) {

				past = line.substring(0, i - 1);

				OperatorBlock ob = new OperatorBlock(operators_.get("" + c_past
						+ c));
				ob.setLeft(parseLine(block,past, nextLines).block);
				ob.setRight(parseLine(block,line.substring(i), nextLines).block);

				return new ReturnStruct(ob, 1);
			} else if (operators_.get("" + c_past) != null) {
				past = line.substring(0, i - 1);

				OperatorBlock ob = new OperatorBlock(
						operators_.get("" + c_past));
				ob.setLeft(parseLine(block,past, nextLines).block);
				ob.setRight(parseLine(block,line.substring(i), nextLines).block);
				return new ReturnStruct(ob, 1);
			} else if (c_past == '=') {
				past = past.substring(0, past.length() - 1);
				Var v = block.getVar(past);
				if (v == null) {
					v = new Var(past);
					block.putVar(past, v);
				}
				return new ReturnStruct(new AssignBlock(v, parseLine(block,
						line.substring(i), nextLines).block), 1);
			} else if (c_past == '(') {
				past = line.substring(0, i - 1);
				if (past.equals("print")) {
					Block b = parseLine(block,line.substring(i - 1), nextLines).block;
					return new ReturnStruct(new PrintBlock(b), 1);
				} else {
					int pc = 1;
					for (; i < line.length(); i++) {
						if (line.charAt(i) == ')') {
							pc--;
						} else if (line.charAt(i) == '(') {
							pc++;
						}
						if (pc == 0) {
							break;
						}
					}
					i++;
					if (i < line.length()) {
						c = line.charAt(i);
					}
					past = "";
				}
			}
			past += c;
		}
		if (block.variableStack().containsKey(past)) {
			return new ReturnStruct(block.getVar(past), 1);
		}

		return new ReturnStruct(new Const(Value.parse(past)), 1);
	}
	
	public static String clean(String line) {
		String l = line;
		if (line.length() == 0)
			return l;
		if (l.startsWith(" ") || l.startsWith("\t")) {
			int c = 0;
			while (c < l.length()
					&& (l.charAt(c) == ' ' || l.charAt(c) == '\t')) {
				c++;
			}
			l = l.substring(c);
		}
		if (l.endsWith(" ") || l.endsWith("\t")) {
			int c = l.length() - 1;
			while (c > 0 && (l.charAt(c) == ' ' || l.charAt(c) == '\t')) {
				c--;
			}
			l = l.substring(0, c);
		}
		if (l.startsWith("(") && l.endsWith(")")) {
			int c = 0;
			for (int i = 1; i < l.length() - 1; i++) {
				if (l.charAt(i) == '(') {
					c++;
				} else if (l.charAt(i) == ')') {
					c--;
				}
			}
			if (c == 0) {
				return l.substring(1, l.length() - 1);
			}
			return l;
		} else {
			return l;
		}
	}
	
	private static ReturnStruct parseFor(CodeBlock block,String line, String[] lines) {
		String l = line.substring(3);
		String args[] = l.split(",");
		if (args.length != 3 && args.length != 2) {
			System.err.println("Something wrong");
			return new ReturnStruct(null, 1);
		}
		Block init = parseLine(block,args[0], lines).block;
		Block cond = parseLine(block,args[1], lines).block;
		Block oper = null;
		if (args.length == 3)
			cond = parseLine(block,args[2], lines).block;

		FORBlock fb = new FORBlock(block,init, cond, oper);

		return fb.parse(lines);
	}

	private static ReturnStruct parseWhile(CodeBlock block,String line, String[] lines) {
		String l = line.substring(5);
		Block cond = parseLine(block,l, lines).block;
		WHILEBlock fb = new WHILEBlock(block,cond);
		return fb.parse(lines);
	}

}
