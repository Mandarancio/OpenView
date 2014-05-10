package ovscript;

import java.util.ArrayList;
import java.util.HashMap;

import core.Emitter;
import core.Slot;
import core.Value;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Interpreter implements CodeBlock {

    public static String DEND = "STOP", DPRINT = "PRINT";

    private HashMap<String, Var> variables_ = new HashMap<>();
    private ArrayList<FunctionDefinition> functions_ = new ArrayList<>();
    private boolean debug_ = false;
    private boolean __end = false;

    private HashMap<Integer, Slot> slots_ = new HashMap<>();
    private HashMap<Integer, Emitter> emitters_ = new HashMap<>();

    @Override
    public Value runBlock(final Block block) throws InterpreterException {
        (new Thread() {

            @Override
            public void run() {
                Block b = block;
                Value last = new Value();
                while (b != null) {
                    try {
                        last = b.run(Interpreter.this);
                        if (__end) {
                            return;
                        }
                        b = b.next();
                    } catch (InterpreterException ex) {
                        Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }).start();
        return new Value();
    }

    @Override
    public void debug(String code, int line) {
        DebugManager.debug(code, this, line);
    }

    @Override
    public ReturnStruct parse(String[] lines) throws InterpreterException {
        Block first = null;
        Block last = null;
        String nexts[];
        int i = 0;
        while (i < lines.length) {
            String line = lines[i];

            nexts = new String[lines.length - i];
            System.arraycopy(lines, i, nexts, 0, lines.length - i);
            ReturnStruct rs = Parser.parseLine(this, line, nexts, i);
            Block b = rs.block;
            if (b != null) {
                if (first == null) {
                    first = b;
                    last = b;
                } else {
                    last.setNext(b);
                    last = b;
                }
            }
            i += rs.lines;

        }
        return new ReturnStruct(first, lines.length);
    }

    public Block parse(String code) throws InterpreterException {
        String lines[] = code.split("\n");
        return parse(lines).block;
    }

    public void setDebug(boolean f) {
        debug_ = f;
    }

    @Override
    public boolean isDebug() {
        return debug_;
    }

    @Override
    public CodeBlock parent() {
        return null;
    }

    @Override
    public HashMap<String, Var> variableStack() {
        return variables_;
    }

    @Override
    public void putVar(String name, Var v) {
        variables_.put(name, v);
    }

    @Override
    public Var getVar(String name) {
        return variables_.get(name);
    }

    @Override
    public void endRun() {
        __end = true;
    }

    @Override
    public HashMap<String, Var> localVariableStack() {
        return variableStack();
    }

    @Override
    public void addFunctionDefinition(FunctionDefinition f) {
        functions_.add(f);
    }

    @Override
    public FunctionDefinition getFunctionDefinition(String past, int nargs) {
        for (FunctionDefinition f : functions_) {
            if (f.name().equals(past) && f.args() == nargs) {
                return f;
            }
        }
        return null;
    }

    @Override
    public Slot getSlot(int line) {
        return slots_.get(new Integer(line));
    }

    @Override
    public Emitter getEmitter(int line) {
        System.out.println("Emitter[" + line + "] in " + emitters_.keySet());
        return emitters_.get(new Integer(line));

    }

    public void addSlot(int l, Slot s) {
        slots_.put(new Integer(l), s);
    }

    public void addEmitter(int l, Emitter e) {
        emitters_.put(new Integer(l), e);
    }

    public void removeSlot(Slot s) {
        slots_.remove(s);
    }

    public void removeEmitter(Emitter e) {
        emitters_.remove(e);
    }
}
