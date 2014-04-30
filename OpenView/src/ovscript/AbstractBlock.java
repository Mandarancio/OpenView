package ovscript;

import core.Value;

public abstract class AbstractBlock implements Block {

	private String name_;
	private Block next_;
	protected boolean __end=false;
	protected boolean __return=false;
	
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
	
	protected Value runBlock(Block body,CodeBlock i){
		Block b=body;
		Value last=null;
		while(b!=null && !__end && !__return){
			last=b.run(i);
			if (b instanceof ReturnBlock){
				__return=true;
				System.out.println("RETURN!");
			}else
				b=b.next();
		}
		return last;
	}

	public void endBlock() {
		__end=true;
	}
	
	public boolean returnStatus(){
		return __return;
	}
}
