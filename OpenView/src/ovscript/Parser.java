package ovscript;

import java.util.ArrayList;

import core.Value;
import evaluator.functions.Function;
import evaluator.functions.FunctionManager;
import evaluator.operators.Operator;
import evaluator.operators.OperatorManager;

public class Parser {

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
			if (c_past == '[') {
				int pc = 1;
				for (; i < line.length(); i++) {
					past += array[i];
					if (array[i] == '\'' || array[i] == '"') {
						char achar = array[i];
						for (; i < line.length(); i++) {
							past += array[i];

							if (array[i] == achar) {
								break;
							}
						}
					}
					if (array[i] == '[') {
						pc++;
					} else if (array[i] == ']') {
						pc--;
						if (pc == 0) {
							break;
						}
					}

				}
				i++;
				if (i < line.length()) {
					c = line.charAt(i);
				}
				continue;
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
			} else if ((c_past == '+' || c_past == '-' || c_past == '*' || c_past == '/')
					&& c == '=') {
				past = past.substring(0, past.length() - 1);
				Var v = block.getVar(past);
				if (v == null) {
					v = new Var(past);
					block.putVar(past, v);
				}
				OperatorBlock op = new OperatorBlock(OperatorManager.get(
						c_past + "").clone());
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
				OperatorBlock op = new OperatorBlock(OperatorManager.get(c_past
						+ ""));
				op.setLeft(v);
				op.setRight(new Const(1));
				return new ReturnStruct(new AssignBlock(v, op), 1);
			} else if (OperatorManager.get("" + c_past + c) != null) {
				Operator o = OperatorManager.get("" + c_past + c);
				if (opSel == null || opSel.priority() >= o.priority()) {
					opSel = o;
					opSelInd = i - 1;
				}

			} else if (OperatorManager.get("" + c_past) != null) {
				Operator o = OperatorManager.get("" + c_past);
				if (opSel == null || opSel.priority() >= o.priority()) {
					opSel = o;
					opSelInd = i - 1;
				}

			} else if (c_past == '=') {
				if (i > 2 && array[i - 2] == '>' || array[i - 2] == '<'
						|| array[i - 2] == '!' || array[i - 2] == '=') {

				} else {

					past = line.substring(0, i - 1);
					past = clean(past);

					Block b = parseLine(block, past, nextLines, currentLine).block;
					Var v = null;
					if (b instanceof Var) {
						v = (Var) b;
					}
					if (v == null) {
						v = new Var(past);
						block.putVar(past, v);
					}
					return new ReturnStruct(new AssignBlock(v, parseLine(block,
							line.substring(i), nextLines, currentLine).block),
							1);
				}
			} else if (c_past == '(') {
				if (checkClsoing(line.substring(i)) && opSel == null) {
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
						String args[] = getArgs(arg);
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
					} else if (past.equals("wait")) {
						Block b = parseLine(block, line.substring(i - 1),
								nextLines, currentLine).block;
						return new ReturnStruct(new Wait(b), 1);
					} else if (past.equals("alert")) {
						Block b = parseLine(block, line.substring(i - 1),
								nextLines, currentLine).block;
						return new ReturnStruct(new Alert(b), 1);
					} else {
						String arg = getArg(line.substring(i));
						int nargs = getArgs(arg).length;
						FunctionDefinition fb = block.getFunctionDefinition(
								past, nargs);
						if (fb != null) {
							String args[] = getArgs(arg);
							;
							Block blocks[] = new Block[nargs];
							for (int j = 0; j < nargs; j++) {
								blocks[j] = parseLine(block, args[j],
										new String[0], currentLine).block;
							}
							return new ReturnStruct(fb.instanciate(blocks), 1);

						} else if (FunctionManager.get(past) != null) {
							Function f = FunctionManager.get(past).clone();
							String args[] = getArgs(arg);
							Block blocks[] = new Block[nargs];
							for (int j = 0; j < nargs; j++) {
								blocks[j] = parseLine(block, args[j],
										new String[0], currentLine).block;
							}
							return new ReturnStruct(
									new FunctionBlock(f, blocks), 1);
						} else if (block.getVar(past) != null) {
							ArrayElement el = new ArrayElement(
									block.getVar(past), parseLine(block, arg,
											nextLines, currentLine).block,
									block);
							el.setLine(currentLine);
							return new ReturnStruct(el, 1);
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

		if (line.contains(":")) {
			String split[] = line.split(":");
			if (split.length == 2) {
				String var = split[0];
				String method = split[1];
				if (block.variableStack().containsKey(var)) {
					if (method.contains("(")) {
						int startIndex = method.indexOf("(");
						String arg = method.substring(startIndex + 1);
						method = method.substring(0, startIndex);
						arg = getArg(arg);
						String args[] = getArgs(arg);
						int nargs = args.length;
						Block blocks[] = new Block[nargs];

						for (int j = 0; j < nargs; j++) {
							blocks[j] = parseLine(block, args[j],
									new String[0], currentLine).block;
						}
						ArrayElement el = new ArrayElement(block.getVar(var),
								method, blocks);
						return new ReturnStruct(el, 1);

					} else {
						ArrayElement el = new ArrayElement(block.getVar(var),
								method);
						el.setLine(currentLine);
						return new ReturnStruct(el, 1);
					}
				}
			}
		}

		if (block.variableStack().containsKey(past)) {
			return new ReturnStruct(block.getVar(past), 1);
		}
		if (past.equals("PI")) {
			return new ReturnStruct(new Const(new Value(Math.PI)), 1);

		} else if (past.equals("E")) {
			return new ReturnStruct(new Const(new Value(Math.E)), 1);
		}
		Value v = Value.parseValue(past);
		return new ReturnStruct(new Const(v), 1);
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
		String args[] = getArgs(l);
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

	public static String getArg(String substring) {
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

	public static String[] getArgs(String arg) {
		ArrayList<String> args = new ArrayList<>();
		char array[] = arg.toCharArray();
		int begin = 0;
		int cp = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == ',') {
				args.add(arg.substring(begin, i));
				begin = i + 1;
			} else if (array[i] == '\'' || array[i] == '"') {
				char c = array[i];
				i++;
				for (; i < array.length; i++) {
					if (array[i] == c) {
						break;
					}
				}
				i++;
			} else if (array[i] == '(') {
				cp = 1;
				i++;
				for (; i < array.length; i++) {
					if (array[i] == '(') {
						cp++;
					} else if (array[i] == ')') {
						cp--;
						if (cp == 0) {
							break;
						}
					}
				}

			}
		}

		args.add(arg.substring(begin, arg.length()));

		return args.toArray(new String[args.size()]);
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
