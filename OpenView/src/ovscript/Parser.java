package ovscript;

import core.Value;
import evaluator.functions.Function;
import evaluator.functions.FunctionManager;
import evaluator.operators.OperatorManager;

public class Parser {

	private static final OperatorManager operators_ = new OperatorManager();
	private static final FunctionManager functions_ = new FunctionManager();

	public static ReturnStruct parseLine(CodeBlock block, String l,
			String nextLines[]) {
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
			Block cond = parseLine(block, past, nextLines).block;
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return ib.parse(copy);
		} else if (line.startsWith("elif ")) {
			ELSEBlock ib = new ELSEBlock(block);
			past = line.substring(4);
			Block cond = parseLine(block, past, nextLines).block;
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
			return parseFor(block, line, copy);
		} else if (line.startsWith("while ")) {
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseWhile(block, line, copy);
		} else if (line.startsWith("var ")) {
			String var = line.substring(4);
			if (var.contains("="))
				return parseLine(block, var, nextLines);
			else {
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
				System.err.println("ERROR");
				return new ReturnStruct(null, 1);
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
					line.substring(7), nextLines).block), 1);
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
				op.setRight(parseLine(block, line.substring(i + 1), nextLines).block);
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
				ob.setLeft(parseLine(block, past, nextLines).block);
				ob.setRight(parseLine(block, line.substring(i), nextLines).block);

				return new ReturnStruct(ob, 1);
			} else if (operators_.get("" + c_past) != null) {
				past = line.substring(0, i - 1);

				OperatorBlock ob = new OperatorBlock(
						operators_.get("" + c_past));
				ob.setLeft(parseLine(block, past, nextLines).block);
				ob.setRight(parseLine(block, line.substring(i), nextLines).block);
				return new ReturnStruct(ob, 1);
			} else if (c_past == '=') {
				past = past.substring(0, past.length()-1);
				past= clean(past);
				Var v = block.getVar(past);
				if (v == null) {
					v = new Var(past);
					block.putVar(past, v);
				}
				return new ReturnStruct(new AssignBlock(v, parseLine(block,
						line.substring(i), nextLines).block), 1);
			} else if (c_past == '(') {
				past = line.substring(0, i - 1);
				if (past.equals("import")){
					String type=line.substring(i).replace(")", "");
					if (type.length()==0)
						type="VOID";
					return new ReturnStruct(new Import(type), 1);
					
				}
				else if (past.equals("export")){
					String args[] = line.substring(i).replace(")", "")
							.split(",");
					int nargs = args.length;
					Block body;
					String type="VOID";
					body=parseLine(block, args[0],nextLines).block;
					if (nargs==2){
						type=args[1];
					}
					return new ReturnStruct(new Export(body,type), 1);
				}
				else if (past.equals("print")) {
					Block b = parseLine(block, line.substring(i - 1), nextLines).block;
					return new ReturnStruct(new PrintBlock(b), 1);
				} else {
					int nargs = line.substring(i - 1).split(",").length;
					FunctionDefinition fb = block.getFunctionDefinition(past,
							nargs);
					if (fb != null) {
						String args[] = line.substring(i).replace(")", "")
								.split(",");
						Block blocks[] = new Block[nargs];
						for (int j = 0; j < nargs; j++) {
							blocks[j] = parseLine(block, args[j], new String[0]).block;
						}
						return new ReturnStruct(fb.instanciate(blocks), 1);

					} else if (functions_.get(past) != null) {
						Function f=functions_.get(past);
						String args[] = line.substring(i).replace(")", "")
								.split(",");
						Block blocks[] = new Block[nargs];
						for (int j = 0; j < nargs; j++) {
							blocks[j] = parseLine(block, args[j], new String[0]).block;
						}
						return new ReturnStruct(new FunctionBlock(f,blocks), 1);
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
			int c = l.length() -1;
			while (c > 0 && (l.charAt(c) == ' ' || l.charAt(c) == '\t')) {
				c--;
			}
			l = l.substring(0, c+1);
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

	private static ReturnStruct parseFor(CodeBlock block, String line,
			String[] lines) {
		String l = line.substring(3);
		String args[] = l.split(",");
		if (args.length != 3 && args.length != 2) {
			System.err.println("Something wrong");
			return new ReturnStruct(null, 1);
		}
		FORBlock fb = new FORBlock(block, args[0], args[1],
				args.length == 3 ? args[2] : "");

		// Block init = parseLine(block,args[0], lines).block;
		// Block cond = parseLine(block,args[1], lines).block;
		// Block oper = null;
		// if (args.length == 3)
		// cond = parseLine(block,args[2], lines).block;

		// FORBlock fb = new FORBlock(block,init, cond, oper);

		return fb.parse(lines);
	}

	private static ReturnStruct parseWhile(CodeBlock block, String line,
			String[] lines) {
		String l = line.substring(5);
		Block cond = parseLine(block, l, lines).block;
		WHILEBlock fb = new WHILEBlock(block, cond);
		return fb.parse(lines);
	}

}