package ovscript;

import core.Value;

public class Var extends AbstractBlock {

    public Value value;

    public Var(String name) {
        super(name);
    }

    public Var(String name, Value val) {
        this(name);
        value = new Value(val.getData());
    }

    @Override
    public Value run(CodeBlock i) {
        return new Value(value.getData());
    }

}
