package ovscript;

import java.util.HashMap;

import core.Value;

public class FORBlock extends AbstractBlock implements CodeBlock {
	private Block initalization_;
	private Block condition_;
	private Block operation_;
	
	private Block body_;
	
	private HashMap<String, Var> variables_=new HashMap<>();
	private CodeBlock parent_;
	
	public FORBlock(CodeBlock parent,Block i, Block c, Block o) {
		super("for");
		initalization_=i;
		condition_=c;
		operation_=o;
		parent_=parent;
	}

	@Override
	public Value run(CodeBlock i) {
		try{
			if (initalization_!=null)
			initalization_.run(i);
			while (condition_.run(i).getBoolean()){
				runBlock(body_,this);
				if (operation_!=null)
					operation_.run(this);
			}
		}catch (Exception e){
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
			ReturnStruct rs = Parser.parseLine(this,lines[i], copy);
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

	@Override
	public HashMap<String, Var> variableStack() {
		HashMap<String, Var> vars=new HashMap<>(variables_);
		vars.putAll(parent_.variableStack());
		return vars;
	}

	@Override
	public void debug(String code) {
		parent_.debug(code);
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
		Var v=variables_.get(name);
		if (v==null)
			v=parent_.getVar(name);
		return v;
	}
	



}
