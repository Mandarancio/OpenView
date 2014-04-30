package ovscript;

import java.util.HashMap;

import core.Value;


public interface CodeBlock {
	public CodeBlock parent();
	public ReturnStruct parse(String[] lines);
	public Value runBlock(Block b);
	public HashMap<String, Var> variableStack();
	public HashMap<String, Var> localVariableStack();
	public void debug(String code);
	public boolean isDebug();
	public void putVar(String name, Var v);
	public Var getVar(String name);
	public void endRun();
	public void addFunctionDefinition(FunctionDefinition f);
	public FunctionDefinition getFunctionDefinition(String past, int nargs);

}
