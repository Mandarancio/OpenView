package ovscript;

import core.Value;

public interface Block {
	public String name();
	public Value run(CodeBlock i) throws InterpreterException;
	public Block next();
	public void setNext(Block b);
	public boolean isBinary();
	public int getLine();
	public int getChar();
	
	public void setLine(int lineind);
	public void setChar(int charind);
}
