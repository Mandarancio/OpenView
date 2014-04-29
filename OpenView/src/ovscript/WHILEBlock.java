package ovscript;

import java.util.HashMap;

import core.Value;

public class WHILEBlock extends AbstractBlock implements CodeBlock {

	private Block body_;
	private Block condition_;

	private CodeBlock parent_;
	private HashMap<String, Var> variables_ = new HashMap<>();

	public WHILEBlock(CodeBlock parent, Block c) {
		super("while");
		condition_ = c;
		parent_ = parent;
	}

	@Override
	public Value run(CodeBlock i) {
		try {
			while (!__end && condition_.run(i).getBoolean()) {
				runBlock(body_, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Value();
	}

	public Block getBody() {
		return body_;
	}

	public void setBody(Block body_) {
		this.body_ = body_;
	}

	@Override
	public CodeBlock parent() {
		return parent_;
	}

	@Override
	public ReturnStruct parse(String[] lines) {
		int i = 0;
		Block b = null;
		while (i < lines.length) {
			String copy[] = new String[lines.length - i];
			System.arraycopy(lines, i, copy, 0, copy.length);
			ReturnStruct rs = Parser.parseLine(this, lines[i], copy);
			if (rs.block != null) {
				if (b == null) {
					b = rs.block;
					this.setBody(b);
				} else {
					b.setNext(rs.block);
					b = rs.block;
				}
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

	@Override
	public HashMap<String, Var> variableStack() {
		HashMap<String, Var> vars = new HashMap<>(variables_);
		vars.putAll(parent_.variableStack());
		return vars;
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
		Var v = variables_.get(name);
		if (v == null)
			v = parent_.getVar(name);
		return v;
	}

	@Override
	public void endRun() {
		__end = true;
	}

	@Override
	public HashMap<String, Var> localVariableStack() {
		return variables_;
	}

	@Override
	public void addFunctionDefinition(FunctionBlock f) {
		System.err
				.println("something wrong! you can not define a function in a "
						+ getClass().getSimpleName());
	}

	@Override
	public FunctionBlock getFunctionDefinition(String past, int nargs) {
		return parent_.getFunctionDefinition(past, nargs);
	}

}
