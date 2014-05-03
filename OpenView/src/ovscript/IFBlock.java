/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovscript;

import java.util.ArrayList;
import java.util.HashMap;

import core.Emitter;
import core.Slot;
import core.Value;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martino
 */
public class IFBlock extends AbstractBlock implements CodeBlock {

    private Block condition_;
    private Block else_;
    private CodeBlock parent_;
    private HashMap<String, Var> variables_ = new HashMap<>();
    private String[] code_;

    public IFBlock(CodeBlock parent) {
        super("IF");
        parent_ = parent;
    }

    @Override
    public Value run(CodeBlock i) {
        return new Value(Void.TYPE);
    }

    @Override
    public Block next() {
        boolean b = false;
        try {
            Value v = condition_.run(this);
            b = v.getBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (b) {
            try {
                return parseForRun();
            } catch (InterpreterException ex) {
                return super.next();
            }
        } else {

            if (else_ != null) {

                return else_;
            } else {
                return super.next();
            }
        }
    }

    public void setCondition(Block b) {
        condition_ = b;
    }

    public void setElse(Block b) {
        else_ = b;
    }

    @Override
    public CodeBlock parent() {
        return parent_;
    }

    private Block parseForRun() throws InterpreterException {
        int i = 0;
        Block first = null;
        Block last = null;

        while (i < code_.length) {
            String copy[] = new String[code_.length - i];
            System.arraycopy(code_, i, copy, 0, copy.length);

            ReturnStruct rs = Parser.parseLine(this, code_[i], copy, getLine() + i);
            Block b = rs.block;
            i += rs.lines;

            if (b instanceof ELSEBlock) {
                this.setElse(b);
            }

            if (b != null) {
                if (first == null) {
                    first = b;
                    last = b;
                } else {
                    last.setNext(b);
                    last = b;
                }
            }

        }
        last.setNext(super.next());
        return first;
    }

    @Override
    public ReturnStruct parse(String[] lines) throws InterpreterException {
        int i = 0;
        int c = 1;
        ArrayList<String> code = new ArrayList<String>();
        while (i < lines.length) {
            String line = Parser.clean(lines[i]);
            if (line.startsWith("if ") || line.startsWith("for ") || line.startsWith("while ") || line.startsWith("function ")) {
                c++;
            } else if (line.equals("end")) {
                c--;
            }

            if (c == 1 && line.startsWith("elif ") || line.startsWith("else")) {
                String copy[] = new String[lines.length - i];
                System.arraycopy(lines, i, copy, 0, copy.length);
                ReturnStruct rs = Parser.parseLine(this, line, copy, getLine() + i);
                i += rs.lines;
                this.setElse(rs.block);
                this.code_ = code.toArray(new String[code.size()]);
                return new ReturnStruct(this, i + 1);
            } else if (c == 0) {
                this.code_ = code.toArray(new String[code.size()]);
                return new ReturnStruct(this, i);
            } else {
                i++;
            }
            code.add(line);

        }
        throw new InterpreterException("No end for if statement found!", i);
    }

    @Override
    public void setNext(Block b) {
        super.setNext(b);
        if (else_ != null) {
            else_.setNext(b);
        }
    }

    public Block superNext() {
        return super.next();
    }

    @Override
    public Value runBlock(Block b) {
        return run(this);
    }

    @Override
    public HashMap<String, Var> variableStack() {
        HashMap<String, Var> vars = new HashMap<>(variables_);
        vars.putAll(parent_.variableStack());
        return vars;
    }

    @Override
    public void debug(String code) {
        DebugManager.debug(code, this);
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
        Var v = variables_.get(name);
        if (v == null) {
            v = parent_.getVar(name);
        }
        return v;
    }

    @Override
    public void endRun() {
        __end = true;
    }

    @Override
    public HashMap<String, Var> localVariableStack() {
        return variables_;
    }

    @Override
    public void addFunctionDefinition(FunctionDefinition f) {
        System.err
                .println("something wrong! you can not define a function in a "
                        + getClass().getSimpleName());
    }

    @Override
    public FunctionDefinition getFunctionDefinition(String past, int nargs) {
        return parent_.getFunctionDefinition(past, nargs);
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
