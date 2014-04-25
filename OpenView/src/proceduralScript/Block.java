package proceduralScript;

import core.Value;

public interface Block {
	String name();
	Value run();
	Block next();
	void setNext(Block b);
}
