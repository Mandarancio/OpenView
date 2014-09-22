package core.support;

public abstract class Rule {
	abstract public boolean check(Object... args);
	public int orderValue(){
		return -1;
	}
}
