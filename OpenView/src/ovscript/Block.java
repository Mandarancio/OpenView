package ovscript;

import core.Value;

public interface Block {
	public String name();
	public Value run(Interpreter i);
	public Block next();
	public void setNext(Block b);
	public boolean isBinary();
}
