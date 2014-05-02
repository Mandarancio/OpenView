package ovscript;

import java.util.ArrayList;
import java.util.HashMap;

import core.Emitter;
import core.Slot;
import core.Value;

public class FORBlock extends AbstractBlock implements CodeBlock {
	private Block initalization_ = null;
	private Block condition_ = null;
	private Block operation_ = null;

	private Block body_;

	private HashMap<String, Var> variables_ = new HashMap<>();
	private CodeBlock parent_;
	private String[] code_;

	public FORBlock(CodeBlock parent, Block i, Block c, Block o) {
		super("for");
		initalization_ = i;
		condition_ = c;
		operation_ = o;
		parent_ = parent;
	}

	public FORBlock(CodeBlock block, String i, String c, String o) {
		super("for");
		parent_ = block;
		if (i.length() > 0)
			initalization_ = Parser.parseLine(this, i, new String[0]).block;
		condition_ = Parser.parseLine(this, c, new String[0]).block;
		if (o.length() > 0)
			operation_ = Parser.parseLine(this, o, new String[0]).block;
	}

	@Override
	public Value run(CodeBlock i) {
		try {
			__return=false;
			if (body_==null){
				body_=parseForRun();
			}
			if (initalization_ != null)
				initalization_.run(i);
			while (!__end && !__return && condition_.run(i).getBoolean()) {
				runBlock(body_, this);
				if (operation_ != null)
					operation_.run(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Value();
	}

	public Block getBody() {
		return body_;
	}

	public void setBody(Block body_) {
		this.body_ = body_;
	}

	@Override
	public CodeBlock parent() {
		return parent_;
	}
	
	private Block parseForRun(){
		int i = 0;
		Block b = null;
		Block first=null;
        String[] lines=code_;
		while (i < lines.length) {
			String copy[] = new String[lines.length - i];
			System.arraycopy(lines, i, copy, 0, copy.length);
			ReturnStruct rs = Parser.parseLine(this, lines[i], copy);
			if (rs.block != null) {
				if (b == null) {
					b = rs.block;
					first=b;
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
		return first;
	}

	@Override
	public ReturnStruct parse(String[] lines) {
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
            
            if (c == 0) {
                code_ = code.toArray(new String[code.size()]);
                return new ReturnStruct(this, i + 1);
            }
            
            code.add(line);
            i++;
        }
		return new ReturnStruct(this, i + 1);
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
		if (v == null)
			v = parent_.getVar(name);
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
	public Slot getSlot() {
		return parent().getSlot();
	}

	@Override
	public Emitter getEmitter() {
		return parent().getEmitter();
	}
    
}