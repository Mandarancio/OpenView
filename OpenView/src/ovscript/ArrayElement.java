package ovscript;

import core.Value;

public class ArrayElement extends Var {

	private static final Object Length = "length";
	private static final Object Add = "add";
	private static final Object Remove = "remove";
	private Block index_;
	private CodeBlock parent_;
	private Var var_;
	private boolean methodMode_ = false;
	private String method_ = "";
	private Block[] args_;

	public ArrayElement(Var var, Block index, CodeBlock parent) {
		super(var.name());
		index_ = index;
		parent_ = parent;
		var_ = var;
	}

	public ArrayElement(Var var, String method, Block... args) {
		super(var.name());
		var_ = var;
		methodMode_ = true;
		method_ = method;
		args_ = args;
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
		if (!methodMode_)
			return getValue();
		else {
			return runMethod(i);
		}
	}

	private Value runMethod(CodeBlock i) throws InterpreterException {
		if (method_.equals(Length)) {
			try {
				return new Value(var_.getValue().getArray().size());
			} catch (Exception e) {
				throw new InterpreterException(e.getMessage(), getLine());
			}
		} else if (method_.equals(Add)) {
			try {
				Value v = args_[0].run(i);
				if (args_.length == 2) {
					int ind = args_[1].run(i).getInt();
					if (v.getType().isArray()) {
						for (int j = 0; j < v.getValues().length; j++) {
							var_.getValue().getArray().add(j+ind, v.getValues()[j]);
						}
					} else {
						var_.getValue().getArray().add(ind, v);
					}

				} else {
					if (v.getType().isArray()) {
						for (int j = 0; j < v.getValues().length; j++) {
							var_.getValue().getArray().add(v.getValues()[j]);
						}
					} else
						var_.getValue().getArray().add(v);
				}
				return var_.getValue();
			} catch (Exception e) {
				throw new InterpreterException(e.getMessage(), getLine());

			}
		} else if (method_.equals(Remove)) {
			try {
				Value v = args_[0].run(i);
				int ind = v.getInt();
				var_.getValue().getArray().remove(ind);
				return var_.getValue();
			} catch (Exception e) {
				throw new InterpreterException(e.getMessage(), getLine());

			}
		}
		throw new InterpreterException("No method : " + method_ + " found",
				getLine());
	}

}
