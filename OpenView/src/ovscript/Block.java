package ovscript;

import core.Value;

public interface Block {
	public String name();
	public Value run(InterpreterBlock i);
	public Block next();
	public void setNext(Block b);
	public boolean isBinary();
}
