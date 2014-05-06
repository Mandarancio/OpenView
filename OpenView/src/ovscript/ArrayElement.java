package ovscript;

import core.Value;

public class ArrayElement extends Var {

	private Block index_;
	private CodeBlock parent_;
	private Var var_;

	public ArrayElement(Var var, Block index, CodeBlock parent) {
		super(var.name());
		index_ = index;
		parent_ = parent;
		var_ = var;
	}

	@Override
	public Value getValue() throws InterpreterException {
		if (var_.getValue().getType().isArray()) {
			try {
				int ind = index_.run(parent_).getInt();
				return var_.getValue().getArray().get(ind);
			} catch (Exception e) {
				throw new InterpreterException(e.getMessage(), getLine());
			}
		} else
			throw new InterpreterException("variable is not an array!",
					getLine());

	}

	@Override
	public void setValue(Value v) throws InterpreterException {
		if (var_.getValue().getType().isArray()) {
			try {
				int ind = index_.run(parent_).getInt();
				if (var_.getValue().getArray().size() > ind) {
					var_.getValue().getArray().set(ind, new Value(v));					
				} else {
					var_.getValue().getArray().add(v);
				}
			} catch (Exception e) {
				throw new InterpreterException(e.getMessage(), getLine());
			}
		} else
			throw new InterpreterException("variable is not an array!",
					getLine());

	}
	
	@Override
	public Value run(CodeBlock i) throws InterpreterException {
		return getValue();
	}

}
