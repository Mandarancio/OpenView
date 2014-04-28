package proceduralScript;

import core.Value;

public interface Block {
	public String name();
	public Value run();
	public Block next();
	public void setNext(Block b);
	public boolean isBinary();
}
