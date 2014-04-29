package ovscript;

import java.util.HashMap;

import core.Value;

public class FunctionBlock extends AbstractBlock implements CodeBlock{

	private CodeBlock parent_;
	private HashMap<String, Var> variables_=new HashMap<>();
	private Block body_;
	
	public FunctionBlock(String name) {
		super(name);
	}

	@Override
	public Value run(CodeBlock i) {
		parent_=i;
		return runBlock(body_,this);
	}

	@Override
	public CodeBlock parent() {
		return parent_;
	}

	@Override
	public ReturnStruct parse(String[] lines) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value runBlock(Block b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Var> variableStack() {
		return variables_;
	}

	@Override
	public void debug(String code) {
		DebugManager.debug(code,this);
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
	
}
