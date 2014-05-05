package ovscript;

import core.Value;
import evaluator.functions.Function;
import evaluator.functions.FunctionManager;
import evaluator.operators.Operator;
import evaluator.operators.OperatorManager;

public class Parser {

	private static final OperatorManager operators_ = new OperatorManager();
	private static final FunctionManager functions_ = new FunctionManager();

	public static ReturnStruct parseLine(CodeBlock block, String l,
			String nextLines[], int currentLine) throws InterpreterException {
		if (l.length() == 0) {
			return new ReturnStruct(null, 1);
		}
		String line = Parser.clean(l);
		if (line.length() == 0 || line.startsWith("#")) {
			return new ReturnStruct(null, 1);
		} else if (line.startsWith("@")) {
			// debug option
			return new ReturnStruct(new DebugCommand(line.substring(1)), 1);
		}
		char c = line.charAt(0);
		char c_past = line.charAt(0);
		String past = "" + c_past;
		int opSelInd = -1;
		Operator opSel = null;
		char array[] = line.toCharArray();
		if (line.startsWith("if ")) {
			IFBlock ib = new IFBlock(block);
			ib.setLine(currentLine);

			past = line.substring(2);

			Block cond = parseLine(block, past, nextLines, currentLine).block;
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return ib.parse(copy);
		} else if (line.startsWith("elif ")) {
			ELSEBlock ib = new ELSEBlock(block);
			ib.setLine(currentLine);
			past = line.substring(4);
			Block cond = parseLine(block, past, nextLines, currentLine).block;
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return ib.parse(copy);
		} else if (line.equals("else")) {
			ELSEBlock ib = new ELSEBlock(block);
			ib.setLine(currentLine);

			Block cond = new Const(new Value(true));
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return ib.parse(copy);
		} else if (line.equals("end")) {
			return new ReturnStruct(new ENDBlock(), 1, currentLine, 0);
		} else if (line.startsWith("for ")) {
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseFor(block, line, copy, currentLine);
		} else if (line.startsWith("while ")) {
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseWhile(block, line, copy, currentLine);
		} else if (line.startsWith("var ")) {
			String var = line.substring(4);
			if (var.contains("=")) {
				return parseLine(block, var, nextLines, currentLine);
			} else {
				Var v = block.getVar(var);
				if (v == null) {
					v = new Var(var);
					block.putVar(var, v);
				}
				return new ReturnStruct(v, 1);
			}
		} else if (line.startsWith("function ")) {
			String def[] = line.replace("function ", "").split("\\(");
			if (def.length != 2) {
				throw new InterpreterException(
						"function definition not well formatted.", currentLine);
			} else {
				String name = def[0];
				String args = def[1].replace(")", "");
				FunctionDefinition fb = new FunctionDefinition(block, name,
						args.split(","));
				String copy[] = new String[nextLines.length - 1];
				System.arraycopy(nextLines, 1, copy, 0, copy.length);
				block.addFunctionDefinition(fb);
				return fb.parse(copy);
			}

		} else if (line.startsWith("return ")) {
			return new ReturnStruct(new ReturnBlock(parseLine(block,
					line.substring(7), nextLines, currentLine).block), 1);
		}
		for (int i = 1; i < line.length(); i++) {
			c_past = c;
			c = array[i];
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
				op.setRight(parseLine(block, line.substring(i + 1), nextLines,
						currentLine).block);
				return new ReturnStruct(new AssignBlock(v, op), 1);
			} else if (c_past == c && (c == '+' || c == '-')) {
				past = past.substring(0, past.length() - 1);
				Var v = block.getVar(past);
				if (v == null) {
					v = new Var(past);
					block.putVar(past, v);
				}
				OperatorBlock op = new OperatorBlock(
						operators_.get(c_past + ""));
				op.setLeft(v);
				op.setRight(new Const(1));
				return new ReturnStruct(new AssignBlock(v, op), 1);
			} else if (operators_.get("" + c_past + c) != null) {
				Operator o = operators_.get("" + c_past + c);
				if (opSel == null || opSel.priority() >= o.priority()) {
					opSel = o;
					opSelInd = i - 1;
				}

			} else if (operators_.get("" + c_past) != null) {
				Operator o = operators_.get("" + c_past);
				if (opSel == null || opSel.priority() >= o.priority()) {
					opSel = o;
					opSelInd = i - 1;
				}

			} else if (c_past == '=') {

				if (i > 2 && array[i - 2] == '>' || array[i - 2] == '<'
						|| array[i - 2] == '!') {
				} else {
					past = past.substring(0, past.length() - 1);
					past = clean(past);
					Var v = block.getVar(past);
					if (v == null) {
						v = new Var(past);
						block.putVar(past, v);
					}
					return new ReturnStruct(new AssignBlock(v, parseLine(block,
							line.substring(i), nextLines, currentLine).block),
							1);
				}
			} else if (c_past == '(') {
				if (checkClsoing(line.substring(i))) {
					past = line.substring(0, i - 1);
					if (past.equals("import")) {
						String type = getArg(line.substring(i));
						if (type.length() == 0) {
							type = "VOID";
						}
						return new ReturnStruct(new Import(type), 1,
								currentLine, i);

					} else if (past.equals("export")) {
						String arg = getArg(line.substring(i));
						String args[] = arg.split(",");
						int nargs = args.length;
						Block body;
						String type = "VOID";
						body = parseLine(block, args[0], nextLines, currentLine).block;
						if (nargs == 2) {
							type = args[1];
						}
						return new ReturnStruct(new Export(body, type), 1,
								currentLine, i);
					} else if (past.equals("print")) {
						Block b = parseLine(block, line.substring(i - 1),
								nextLines, currentLine).block;
						return new ReturnStruct(new PrintBlock(b), 1);
					} else if (past.equals("alert")) {
						Block b = parseLine(block, line.substring(i - 1),
								nextLines, currentLine).block;
						return new ReturnStruct(new Alert(b), 1);
					} else {
						String arg = getArg(line.substring(i));
						int nargs = arg.split(",").length;
						FunctionDefinition fb = block.getFunctionDefinition(
								past, nargs);
						if (fb != null) {
							String args[] = arg.split(",");
							Block blocks[] = new Block[nargs];
							for (int j = 0; j < nargs; j++) {
								blocks[j] = parseLine(block, args[j],
										new String[0], currentLine).block;
							}
							return new ReturnStruct(fb.instanciate(blocks), 1);

						} else if (functions_.get(past) != null) {
							Function f = functions_.get(past);
							String args[] = arg.split(",");
							Block blocks[] = new Block[nargs];
							for (int j = 0; j < nargs; j++) {
								blocks[j] = parseLine(block, args[j],
										new String[0], currentLine).block;
							}
							return new ReturnStruct(
									new FunctionBlock(f, blocks), 1);
						}
					}
				}
				int pc = 1;
				for (; i < line.length(); i++) {
					if (array[i] == ')') {
						pc--;
					} else if (array[i] == '(') {
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

			} else if (c_past == '\'' || c_past == '"') {
				char achar = c_past;
				for (; i < line.length(); i++) {
					past += array[i];

					if (array[i] == achar) {
						break;
					}
				}
				i++;
				if (i < line.length()) {
					c = line.charAt(i);
				}
				continue;
			}
			past += c;
		}

		if (opSel != null) {
			String left = line.substring(0, opSelInd);
			int i = opSelInd + opSel.name().length();
			String right = line.substring(i);

			OperatorBlock ob = new OperatorBlock(opSel);
			if (opSel.input() == 2) {
				ob.setLeft(parseLine(block, left, nextLines, currentLine).block);
			}
			ob.setRight(parseLine(block, right, nextLines, currentLine).block);
			return new ReturnStruct(ob, 1);
		}

		if (block.variableStack().containsKey(past)) {
			return new ReturnStruct(block.getVar(past), 1);
		}

		return new ReturnStruct(new Const(Value.parse(past)), 1);
	}

	public static String clean(String line) {
		String l = line;
		if (line.length() == 0) {
			return l;
		}
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
			l = l.substring(0, c + 1);
		}
		if (l.startsWith("(") && l.endsWith(")")
				&& checkClsoing(l.substring(1))) {
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

	private static ReturnStruct parseFor(CodeBlock block, String line,
			String[] lines, int currentLine) throws InterpreterException {
		String l = line.substring(3);
		String args[] = l.split(",");
		if (args.length != 3 && args.length != 2) {
			throw new InterpreterException(
					"For has this syntax for init,cond,oper", currentLine);
		}
		FORBlock fb = new FORBlock(block, args[0], args[1],
				args.length == 3 ? args[2] : "", currentLine);

		// Block init = parseLine(block,args[0], lines).block;
		// Block cond = parseLine(block,args[1], lines).block;
		// Block oper = null;
		// if (args.length == 3)
		// cond = parseLine(block,args[2], lines).block;
		// FORBlock fb = new FORBlock(block,init, cond, oper);
		return fb.parse(lines);
	}

	private static ReturnStruct parseWhile(CodeBlock block, String line,
			String[] lines, int currentLine) throws InterpreterException {
		String l = line.substring(5);
		Block cond = parseLine(block, l, lines, currentLine).block;
		WHILEBlock fb = new WHILEBlock(block, cond);
		fb.setLine(currentLine);
		return fb.parse(lines);
	}

	private static String getArg(String substring) {
		int c = 1;
		char array[] = substring.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (array[i] == '(') {
				c++;
			} else if (array[i] == ')') {
				c--;
				if (c == 0) {
					return substring.substring(0, i);
				}
			}
		}
		return substring;
	}

	private static boolean checkClsoing(String substring) {
		if (substring.endsWith(")")) {
			int c = 1;
			char array[] = substring.toCharArray();
			for (int i = 0; i < array.length; i++) {
				if (array[i] == '(') {
					c++;
				} else if (array[i] == ')') {
					c--;
					if (c == 0 && i != array.length - 1) {
						return false;
					}
				}
			}
			if (c == 0) {
				return true;
			}
		}
		return false;
	}

}
