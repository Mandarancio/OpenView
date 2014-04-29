package ovscript;

public abstract class AbstractBlock implements Block {

	private String name_;
	private Block next_;

	public AbstractBlock(String name) {
		name_ = name;
	}

	@Override
	public String name() {
		return name_;
	}

	@Override
	public Block next() {
		return next_;
	}

	@Override
	public void setNext(Block b) {
		next_ = b;
	}

	public void setName(String name) {
		name_ = name;
	}

	@Override
	public boolean isBinary() {
		return false;
	}
	
	protected void runBlock(Block body,InterpreterBlock i){
		Block b=body;
		while(b!=null){
			b.run(i);
			b=b.next();
		}
	}
}
