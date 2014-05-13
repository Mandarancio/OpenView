package ovscript;

import java.util.HashMap;

import core.Emitter;
import core.Slot;
import core.Value;

public class FunctionDefinition extends AbstractBlock implements CodeBlock {

    private CodeBlock parent_;
    private HashMap<String, Var> variables_ = new HashMap<>();
    private HashMap<String, Block> argsBlock_ = new HashMap<>();

    private Block body_;
    private String[] code_;
    private boolean definition_ = true;
    private CodeBlock defPar_;
    private String[] args_;

    public FunctionDefinition(CodeBlock defPar, String name, String[] args) {
        super(name);
        defPar_ = defPar;
        for (int i = 0; i < args.length; i++) {
            if (args[i].length() > 0) {
                variables_.put(args[i], new Var(args[i]));
                argsBlock_.put(args[i], null);
            }
        }
        args_ = args;
        definition_ = true;
    }

    @Override
    public Value run(CodeBlock i) throws InterpreterException {
        if (!definition_) {
            for (String s : argsBlock_.keySet()) {
                if (argsBlock_.get(s) != null) {
                    getVar(s).setValue(argsBlock_.get(s).run(this));
                }
            }

            parent_ = i;
            return runBlock(body_, this);
        } else {
            return new Value();
        }
    }

    @Override
    public CodeBlock parent() {
        return parent_;
    }

    @Override
    public ReturnStruct parse(String[] lines) throws InterpreterException {
        int i = 0;
        code_ = lines;
        Block b = null;
        while (i < lines.length) {
            String line = Parser.clean(lines[i]);
            String copy[] = new String[lines.length - i];
            System.arraycopy(lines, i, copy, 0, copy.length);
            ReturnStruct rs = Parser.parseLine(this, line, copy, getLine() + i);
            if (rs.block != null) {
                rs.block.setLine(i + 1 + getLine());
                if (b == null) {
                    b = rs.block;
                    this.setBody(b);
                } else {
                    b.setNext(rs.block);
                    b = rs.block;
                }
            }
            i += rs.lines;
            if (b instanceof ENDBlock) {
                this.setNext(b);
                break;
            }
        }
        return new ReturnStruct(this, i + 1);
    }

    @Override
    public Value runBlock(Block b) throws InterpreterException {
        return run(this);
    }

    public FunctionDefinition instanciate(Block... vars)
            throws InterpreterException {
        if (vars.length == argsBlock_.size()) {

            FunctionDefinition fb = new FunctionDefinition(defPar_, name(),
                    args_);
            fb.init(vars);
            fb.parse(code_);
            return fb;
        }
        return null;
    }

    public void init(Block... vars) {
        if (vars.length == argsBlock_.size()) {
            definition_ = false;
            String args[] = args_;
            for (int i = 0; i < vars.length; i++) {
                argsBlock_.put(args[i], vars[i]);
            }
        }

    }

    @Override
    public HashMap<String, Var> variableStack() {
        return variables_;
    }

    @Override
    public void debug(String code, int line) {
        DebugManager.debug(code, this, line);
    }

    @Override
    public boolean isDebug() {
        return parent_.isDebug();
    }

    @Override
    public void putVar(String name, Var v) {
        variables_.put(name, v);
    }

    @Override
    public Var getVar(String name) {
        return variables_.get(name);
    }

    public Block getBody() {
        return body_;
    }

    public void setBody(Block body_) {
        this.body_ = body_;
    }

    @Override
    public void endRun() {
        // TODO Auto-generated method stub

    }

    @Override
    public HashMap<String, Var> localVariableStack() {
        return variables_;
    }

    @Override
    protected Value runBlock(Block body, CodeBlock i)
            throws InterpreterException {
        Block b = body;
        Value v = new Value();
        __return = false;
        while (b != null && !__end && !__return) {
            if (b instanceof ReturnBlock) {
                return b.run(i);
            } else {
                v = b.run(i);
            }
            if (b instanceof AbstractBlock
                    && ((AbstractBlock) b).returnStatus()) {
                return v;
            }
            b = b.next();
        }
        v = null;
        return new Value();
    }

    @Override
    public void addFunctionDefinition(FunctionDefinition f) {
        System.err
                .println("something wrong! you can not define a function in a "
                        + getClass().getSimpleName());
    }

    @Override
    public FunctionDefinition getFunctionDefinition(String past, int nargs) {
        return defPar_.getFunctionDefinition(past, nargs);
    }

    public int args() {
        return argsBlock_.size();
    }

    @Override
    public Slot getSlot(int line) {
        return parent().getSlot(line);
    }

    @Override
    public Emitter getEmitter(int line) {
        return parent().getEmitter(line);
    }

}
