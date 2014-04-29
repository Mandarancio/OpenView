package ovscript;

import java.util.HashMap;

import core.Value;
import evaluator.operators.OperatorManager;

public class InterpreterBlock {
	public static String DEND="STOP", DPRINT="PRINT";
	private class ReturnStruct {

		public Block block;
		public int lines;

		public ReturnStruct(Block b, int l) {
			block = b;
			lines = l;
		}
	}

	private HashMap<String, Var> variables_ = new HashMap<>();
	private OperatorManager operators_ = new OperatorManager();
	private boolean debug_=false;
	private boolean __end=false;

	public void run(Block block) {
		Block b = block;
		while (b != null) {
		
			b.run(this);
			if (__end){
				return;
			}
			b = b.next();
		}
	}

	public void debug(String name) {
		if (!debug_)
			return;
		if (name.equals(DPRINT)){
			System.out.println("VARABLE STATUS:");
			for (String s: variables_.keySet()){
				System.out.println( "  "+s + " : "+ variables_.get(s).value);
			}
			System.out.println("_______________");
		}else if (name.equals(DEND)){
			__end=true;
		}
	}

	public Block parse(String code) {

		String lines[] = code.split("\n");
		Block first = null;
		Block last = null;
		String nexts[];
		int i = 0;

		while (i < lines.length) {
			String line = lines[i];

			nexts = new String[lines.length - i];
			System.arraycopy(lines, i, nexts, 0, lines.length - i);
			ReturnStruct rs = parseLine(line, nexts);
			Block b = rs.block;
			if (b != null) {
				if (first == null) {
					first = b;
					last = b;
				} else {
					last.setNext(b);
					last = b;
				}
			}
			i += rs.lines;

		}
		return first;
	}

	private ReturnStruct parseLine(String l, String nextLines[]) {
		if (l.length() == 0 )
			return new ReturnStruct(null, 1);
		String line = clean(l);
		if (line.length() == 0 || line.startsWith("#"))
			return new ReturnStruct(null, 1);
		else if (line.startsWith("@")){
			//debug option
			return new ReturnStruct(new DebugCommand(line.substring(1)), 1);
		}
		char c = line.charAt(0);
		char c_past = line.charAt(0);
		String past = "" + c_past;
		if (line.startsWith("if ")) {
			IFBlock ib = new IFBlock();
			past = line.substring(2);
			Block cond = parseLine(past, nextLines).block;
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseIF(ib, copy);
		} else if (line.startsWith("elif ")) {
			ELSEBlock ib = new ELSEBlock();
			past = line.substring(4);
			Block cond = parseLine(past, nextLines).block;
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseIF(ib, copy);
		} else if (line.equals("else")) {
			ELSEBlock ib = new ELSEBlock();
			Block cond = new Const(new Value(true));
			ib.setCondition(cond);
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseIF(ib, copy);
		} else if (line.equals("end")) {
			return new ReturnStruct(new ENDBlock(), 1);
		} else if (line.startsWith("for ")) {
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseFor(line, copy);
		} else if (line.startsWith("while ")) {
			String copy[] = new String[nextLines.length - 1];
			System.arraycopy(nextLines, 1, copy, 0, copy.length);
			return parseWhile(line, copy);
		}
		for (int i = 1; i < line.length(); i++) {
			c_past = c;
			c = line.charAt(i);
			if ((c_past == '+' || c_past == '-' || c_past == '*' || c_past == '/')
					&& c == '=') {
				past = past.substring(0, past.length() - 1);
				Var v = variables_.get(past);
				if (v == null) {
					v = new Var(past);
					variables_.put(past, v);
				}
				OperatorBlock op = new OperatorBlock(operators_
						.get(c_past + "").clone());
				op.setLeft(v);
				op.setRight(parseLine(line.substring(i + 1), nextLines).block);
				return new ReturnStruct(new AssignBlock(v, op), 1);
			} else if (c_past == c && (c == '+' || c == '-')) {
				past = past.substring(0, past.length() - 1);
				Var v = variables_.get(past);
				if (v == null) {
					v = new Var(past);
					variables_.put(past, v);
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
				ob.setLeft(parseLine(past, nextLines).block);
				ob.setRight(parseLine(line.substring(i), nextLines).block);

				return new ReturnStruct(ob, 1);
			} else if (operators_.get("" + c_past) != null) {
				past = line.substring(0, i - 1);

				OperatorBlock ob = new OperatorBlock(
						operators_.get("" + c_past));
				ob.setLeft(parseLine(past, nextLines).block);
				ob.setRight(parseLine(line.substring(i), nextLines).block);
				return new ReturnStruct(ob, 1);
			} else if (c_past == '=') {
				past = past.substring(0, past.length() - 1);
				Var v = variables_.get(past);
				if (v == null) {
					v = new Var(past);
					variables_.put(past, v);
				}
				return new ReturnStruct(new AssignBlock(v, parseLine(
						line.substring(i), nextLines).block), 1);
			} else if (c_past == '(') {
				past = line.substring(0, i - 1);
				if (past.equals("print")) {
					Block b=parseLine(line.substring(i-1),nextLines).block;
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
		if (variables_.containsKey(past)) {
			return new ReturnStruct(variables_.get(past), 1);
		}

		return new ReturnStruct(new Const(Value.parse(past)), 1);
	}

	private ReturnStruct parseFor(String line, String[] lines) {
		String l = line.substring(3);
		String args[] = l.split(",");
		if (args.length != 3 && args.length!=2) {
			System.err.println("Something wrong");
			return new ReturnStruct(null, 1);
		}
		Block init = parseLine(args[0], lines).block;
		Block cond = parseLine(args[1], lines).block;
		Block oper = null;
		if (args.length==3)
			cond=parseLine(args[2], lines).block;

		FORBlock fb = new FORBlock(init, cond, oper);
		int i = 0;
		Block b=null;
		while (i < lines.length) {
			String copy[] = new String[lines.length - i];
			System.arraycopy(lines, i, copy, 0, copy.length);
			ReturnStruct rs = parseLine(lines[i], copy);
			if (i == 0){
				b=rs.block;
				fb.setBody(b);
			}
			else {
				b.setNext(rs.block);
				b=rs.block;
			}
			i += rs.lines;
			if (b instanceof ENDBlock) {
				fb.setNext(b);
				break;
			}
		}
		return new ReturnStruct(fb, i + 1);
	}

	private ReturnStruct parseWhile(String line, String[] lines) {
		String l = line.substring(5);
		Block cond = parseLine(l, lines).block;
		WHILEBlock fb = new WHILEBlock(cond);
		int i = 0;
		Block b=null;
		while (i < lines.length) {
			String copy[] = new String[lines.length - i];
			System.arraycopy(lines, i, copy, 0, copy.length);
			ReturnStruct rs = parseLine(lines[i], copy);
			if (i == 0){
				b=rs.block;
				fb.setBody(b);
			}else{
				b.setNext(rs.block);
				b=rs.block;
			}
			i += rs.lines;
			if (b instanceof ENDBlock) {
				fb.setNext(b);
				break;
			}
		}
		return new ReturnStruct(fb, i + 1);
	}

	private ReturnStruct parseIF(IFBlock ifB, String[] lines) {
		int i = 0;
		Block first = null;
		Block last = null;
		while (i < lines.length) {
			String copy[] = new String[lines.length - i];
			System.arraycopy(lines, i, copy, 0, copy.length);

			ReturnStruct rs = parseLine(lines[i], copy);
			Block b = rs.block;
			i += rs.lines;

			if (b instanceof ELSEBlock) {
				ifB.setElse(b);

				break;
			} else if (b instanceof ENDBlock) {
				ifB.setNext(b);
				break;
			} else {
				if (first == null) {
					first = b;
					last = b;
					ifB.setBody(first);
				} else {
					last.setNext(b);
					last = b;
				}
			}

		}
		return new ReturnStruct(ifB, i + 1);
	}

	private String clean(String line) {
		String l = line;
		if (line.length()==0)
			return l;
		if (l.startsWith(" ") || l.startsWith("\t")) {
			int c = 0;
			while (c<l.length() && (l.charAt(c) == ' ' || l.charAt(c)=='\t')) {
				c++;
			}
			l = l.substring(c);
		}
		if (l.endsWith(" ") || l.endsWith("\t")) {
			int c = l.length() - 1;
			while (c>0 && (l.charAt(c) == ' '|| l.charAt(c)=='\t')) {
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

	public void setDebug(boolean f) {
		debug_=f;
	}
	
	public boolean isDebug(){
		return debug_;
	}
}
