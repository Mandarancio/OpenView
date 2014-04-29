package ovscript;

import java.util.ArrayList;
import java.util.HashMap;

import core.Value;

public class Interpreter implements CodeBlock {
	public static String DEND = "STOP", DPRINT = "PRINT";


	private HashMap<String, Var> variables_ = new HashMap<>();
	private ArrayList<Function> functions_=new ArrayList<>();
	private boolean debug_ = false;
	private boolean __end = false;

	@Override
	public Value runBlock(Block block) {
		Block b = block;
		Value last = new Value();
		while (b != null) {
			last = b.run(this);
			if (__end) {
				return last;
			}
			b = b.next();
		}
		return last;
	}

	@Override
	public void debug(String code) {
		DebugManager.debug(code, this);
	}

	@Override
	public ReturnStruct parse(String[] lines) {
		Block first = null;
		Block last = null;
		String nexts[];
		int i = 0;

		while (i < lines.length) {
			String line = lines[i];

			nexts = new String[lines.length - i];
			System.arraycopy(lines, i, nexts, 0, lines.length - i);
			ReturnStruct rs = Parser.parseLine(this,line, nexts);
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
		return new ReturnStruct(first, lines.length);
	}

	public Block parse(String code) {
		String lines[] = code.split("\n");
		return parse(lines).block;
	}
	
	public void setDebug(boolean f) {
		debug_ = f;
	}

	@Override
	public boolean isDebug() {
		return debug_;
	}

	@Override
	public CodeBlock parent() {
		return null;
	}

	@Override
	public HashMap<String, Var> variableStack() {
		return variables_;
	}

	@Override
	public void putVar(String name, Var v) {
		variables_.put(name, v);
	}
	
	@Override
	public Var getVar(String name) {
		return variables_.get(name);
	}

	@Override
	public void endRun() {
		__end=true;
	}

	@Override
	public HashMap<String, Var> localVariableStack() {
		return variableStack();
	}

	@Override
	public void addFunctionDefinition(Function f) {
		functions_.add(f);
	}
	
	@Override
	public Function getFunctionDefinition(String past, int nargs) {
		for (Function f: functions_){
			if (f.name().equals(past) && f.args()==nargs)
				return f;
		}
		return null;
	}

}
