package ovscript;

import java.util.HashMap;

import core.Emitter;
import core.Slot;
import core.Value;


public interface CodeBlock {
	public CodeBlock parent();
	public ReturnStruct parse(String[] lines) throws InterpreterException;
	public Value runBlock(Block b) throws InterpreterException;
	public HashMap<String, Var> variableStack();
	public HashMap<String, Var> localVariableStack();
	public void debug(String code);
	public boolean isDebug();
	public void putVar(String name, Var v);
	public Var getVar(String name);
	public void endRun();
	public void addFunctionDefinition(FunctionDefinition f);
	public FunctionDefinition getFunctionDefinition(String past, int nargs);
	public Slot getSlot(int line);
	public Emitter getEmitter(int line);
}
