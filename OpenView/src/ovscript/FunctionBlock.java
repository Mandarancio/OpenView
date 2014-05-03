/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ovscript;

import core.Value;
import evaluator.functions.Function;

/**
 * 
 * @author martino
 */
public class FunctionBlock extends AbstractBlock {

	private Function function_;
	private Block[] args_;

	public FunctionBlock(Function f) {
		super("function");
		function_ = f;
	}
	
	public FunctionBlock(Function f,Block... args) {
		this(f);
		setArguments(args);
	}

	@Override
	public Value run(CodeBlock b) throws InterpreterException {
		if (args_ != null || getArgsNumber()==0) {
			Value[] vals = new Value[getArgsNumber()];
			for (int i = 0; i < vals.length; i++) {
				vals[i] = args_[i].run(b);
			}
			try {
				Value v = function_.evaluate(vals);
				return v;
			} catch (Exception e) {
				throw new InterpreterException(e.getMessage(), getLine());
			}
		}
		return new Value();
	}

	public int getArgsNumber() {
		return function_.input();
	}

	public void setArguments(Block... args) {
		if (args.length == getArgsNumber()) {
			args_ = args;
		}
	}

}
