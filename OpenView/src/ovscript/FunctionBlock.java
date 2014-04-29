package ovscript;

import java.util.HashMap;

import core.Value;

public class FunctionBlock extends AbstractBlock implements CodeBlock {

	private CodeBlock parent_;
	private HashMap<String, Var> variables_ = new HashMap<>();
	private HashMap<String, Block> args_ = new HashMap<>();

	private Block body_;
	private String[] code_;
	private boolean definition_ = true;
	private CodeBlock defPar_;

	public FunctionBlock(CodeBlock defPar, String name, String[] args) {
		super(name);
		defPar_ = defPar;
		for (String arg : args) {
			variables_.put(arg, new Var(arg));
			args_.put(arg, null);
		}
		definition_ = true;
	}

	@Override
	public Value run(CodeBlock i) {
		if (!definition_) {
			for (String s: args_.keySet()){
				getVar(s).value=args_.get(s).run(this);
			}
			
			parent_ = i;
			return runBlock(body_, this);
		} else
			return new Value();
	}

	@Override
	public CodeBlock parent() {
		return parent_;
	}

	@Override
	public ReturnStruct parse(String[] lines) {
		int i = 0;
		code_ = lines;
		Block b = null;
		while (i < lines.length) {
			String line=Parser.clean(lines[i]);
			String copy[] = new String[lines.length - i];
			System.arraycopy(lines, i, copy, 0, copy.length);
			ReturnStruct rs = Parser.parseLine(this, line, copy);
			if (i == 0) {
				b = rs.block;
				this.setBody(b);
			} else {
				b.setNext(rs.block);
				b = rs.block;
			}
			i += rs.lines;
			if (b instanceof ENDBlock) {
				this.setNext(b);
				break;
			}
		}
		return new ReturnStruct(this, i + 1);
	}

	@Override
	public Value runBlock(Block b) {
		return run(this);
	}

	public FunctionBlock instanciate(Block... vars) {
		if (vars.length == args_.size()) {
			FunctionBlock fb = new FunctionBlock(defPar_, name(), args_
					.keySet().toArray(new String[args_.size()]));
			fb.init(vars);
			fb.parse(code_);
			return fb;
		}
		return null;
	}

	public void init(Block... vars) {
		if (vars.length == args_.size()) {
			definition_ = false;
			String args[]=args_.keySet().toArray(new String[args_.size()]);
			for (int i = 0; i < vars.length; i++) {
				args_.put(args[i],vars[i]);
			}
		}

	}

	@Override
	public HashMap<String, Var> variableStack() {
		return variables_;
	}

	@Override
	public void debug(String code) {
		DebugManager.debug(code, this);
	}

	@Override
	public boolean isDebug() {
		return parent_.isDebug();
	}

	@Override
	public void putVar(String name, Var v) {
		variables_.put(name, v);
	}

	@Override
	public Var getVar(String name) {
		return variables_.get(name);
	}

	public Block getBody() {
		return body_;
	}

	public void setBody(Block body_) {
		this.body_ = body_;
	}

	@Override
	public void endRun() {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, Var> localVariableStack() {
		return variables_;
	}

	@Override
	protected Value runBlock(Block body, CodeBlock i) {
		Block b = body;
		Value last = new Value();
		while (b != null && !__end) {
			if (b instanceof ReturnBlock)
				last = b.run(i);
			else
				b.run(i);
			b = b.next();
		}
		return last;
	}

	@Override
	public void addFunctionDefinition(FunctionBlock f) {
		System.err
				.println("something wrong! you can not define a function in a "
						+ getClass().getSimpleName());
	}

	@Override
	public FunctionBlock getFunctionDefinition(String past, int nargs) {
		return defPar_.getFunctionDefinition(past, nargs);
	}

	public int args() {
		return args_.size();
	}
}
