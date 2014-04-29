/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovscript;

import java.util.HashMap;

import core.Value;

/**
 * 
 * @author martino
 */
public class IFBlock extends AbstractBlock implements CodeBlock {

	private Block condition_;
	private Block else_;
	private Block body_;
	private CodeBlock parent_;
	private HashMap<String, Var> variables_ = new HashMap<>();

	public IFBlock(CodeBlock parent) {
		super("IF");
		parent_ = parent;
	}

	@Override
	public Value run(CodeBlock i) {
		return new Value(Void.TYPE);
	}

	@Override
	public Block next() {
		Value v = condition_.run(this);
		boolean b = false;
		try {
			b = v.getBoolean();
		} catch (Exception e) {
		}
		if (b) {
			return body_;
		} else {

			if (else_ != null) {

				return else_;
			} else
				return super.next();
		}
	}

	public void setCondition(Block b) {
		condition_ = b;
	}

	public void setElse(Block b) {
		else_ = b;
	}

	void setBody(Block first) {
		body_ = first;
	}

	@Override
	public CodeBlock parent() {
		return parent_;
	}

	@Override
	public ReturnStruct parse(String[] lines) {
		Block first = null;
		Block last = null;
		int i = 0;
		while (i < lines.length) {
			String copy[] = new String[lines.length - i];
			System.arraycopy(lines, i, copy, 0, copy.length);

			ReturnStruct rs = Parser.parseLine(this, lines[i], copy);
			Block b = rs.block;
			i += rs.lines;

			if (b instanceof ELSEBlock) {
				this.setElse(b);

				break;
			} else if (b instanceof ENDBlock) {
				this.setNext(b);
				break;
			} else {
				if (b != null) {
					if (first == null) {
						first = b;
						last = b;
						this.setBody(first);
					} else {
						last.setNext(b);
						last = b;
					}
				}
			}

		}
		return new ReturnStruct(this, i + 1);
	}

	@Override
	public void setNext(Block b) {
		super.setNext(b);
		if (else_ != null) {
			else_.setNext(b);
		}
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
