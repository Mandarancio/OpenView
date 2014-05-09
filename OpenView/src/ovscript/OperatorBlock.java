package ovscript;

import core.Value;
import evaluator.operators.Operator;

import java.util.ArrayList;

public class OperatorBlock extends AbstractBlock {

    private Operator operator_;
    private Block left_, right_;

    public OperatorBlock(Operator o) {
        super("operator");
        operator_ = o;
    }

    @Override
    public Value run(CodeBlock i) throws InterpreterException {
        ArrayList<Value> operands = new   ArrayList<Value>();
        if (left_ != null) {
            operands.add(left_.run(i));
        }

        if (right_ != null) {
            operands.add( right_.run(i));
        }
 
        try {
            return operator_.evaluate(operands.toArray(new Value[operands.size()]));
        } catch (Exception e) {
            e.printStackTrace();
            throw new InterpreterException(e.getMessage(), getLine());
        }
    }

    public void setLeft(Block b) {
        left_ = b;
    }

    public void setRight(Block b) {
        right_ = b;
    }

    public void setOperator(Operator o) {
        operator_ = o;
    }

    @Override
    public boolean isBinary() {
        if (operator_ != null) {
            return operator_.input() == 2;
        }
        return super.isBinary();
    }

}
