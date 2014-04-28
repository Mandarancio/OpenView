package proceduralScript;

import java.util.HashMap;

import core.Value;
import evaluator.operators.OperatorManager;

public class InterpreterBlock {
    
    private HashMap<String, Var> variables_ = new HashMap<>();
    private OperatorManager operators_ = new OperatorManager();
    
    public void run(Block block) {
        Block b = block;
        while (b != null) {
            
            b.run();
            
            b = b.next();
        }
    }
    
    public Block parse(String code) {
        
        String lines[] = code.split("\n");
        Block first = null;
        Block last = null;
        String nexts[];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            nexts = new String[lines.length - i];
            System.arraycopy(lines, i, nexts, 0, lines.length - i);
            Block b = parseLine(line, nexts);
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
        return first;
    }
    
    private Block parseLine(String l, String nextLines[]) {
        String line = clean(l);
        char c = line.charAt(0);
        char c_past = line.charAt(0);
        String past = "" + c_past;
        for (int i = 1; i < line.length(); i++) {
            c_past = c;
            c = line.charAt(i);
            if (operators_.get("" + c_past + c) != null) {
                
                past = line.substring(0, i - 1);
                
                OperatorBlock ob = new OperatorBlock(operators_.get("" + c_past
                        + c));
                ob.setLeft(parseLine(past, nextLines));
                ob.setRight(parseLine(line.substring(i), nextLines));
                
                return ob;
            } else if (operators_.get("" + c_past) != null) {
                past = line.substring(0, i - 1);
                
                OperatorBlock ob = new OperatorBlock(
                        operators_.get("" + c_past));
                ob.setLeft(parseLine(past, nextLines));
                ob.setRight(parseLine(line.substring(i), nextLines));
                return ob;
            } else if (c_past == '=') {
                past = past.substring(0, past.length() - 1);
                Var v = variables_.get(past);
                if (v == null) {
                    v = new Var(past);
                    variables_.put(past, v);
                }
                return new AssignBlock(v, parseLine(line.substring(i), nextLines));
            } else if (c_past == '(') {
                int pc = 1;
                for (; i < line.length(); i++) {
                    if (line.charAt(i) == ')') {
                        pc--;
                    } else if (line.charAt(i) == '(') {
                        pc++;
                    }
                    if (pc == 0) {
                        break;
                    }
                }
                i++;
                if (i < line.length()) {
                    c = line.charAt(i);
                }
                past = "";
            } else if (c_past == ' ') {
                if (past.equals("if ")) {
                    IFBlock ib = new IFBlock();
                    past = line.substring(i);
                    Block cond = parseLine(past, nextLines);
                    ib.setCondition(cond);
                    Block first = null;
                    Block last = null;
                    for (int j = 1; j < nextLines.length; j++) {
                        String copy[] = new String[nextLines.length - j];
                        System.arraycopy(nextLines, j, copy, 0, copy.length);
                        Block b = parseLine(nextLines[j], copy);
                        if (first == null) {
                            ib.setBody(b);
                            first = b;
                            last = b;
                        } else {
                            last.setNext(b);
                            last = b;
                        }
                        if (b instanceof EndBlock) {
                            return ib;
                        } else if (b instanceof IFBlock) {
                            ib.setElse(b);
                        }
                    }
                    return ib;
                } else if (past.equals("elif ")) {
                     IFBlock ib = new IFBlock();
                    past = line.substring(i);
                    Block cond = parseLine(past, nextLines);
                    ib.setCondition(cond);
                    Block first = null;
                    Block last = null;
                    for (int j = 1; j < nextLines.length; j++) {
                        String copy[] = new String[nextLines.length - j];
                        System.arraycopy(nextLines, j, copy, 0, copy.length);
                        Block b = parseLine(nextLines[j], copy);
                        if (first == null) 
                        {
                            ib.setBody(b);
                            first = b;
                            last = b;
                        } else {
                            last.setNext(b);
                            last = b;
                        }
                        if (b instanceof EndBlock) {
                            return ib;
                        } else if (b instanceof IFBlock) {
                            ib.setElse(b);
                        }
                    }
                    return ib;
                } else if (past.equals("else ")) {
                     IFBlock ib = new IFBlock();
                    Block cond =new Const(new Value(true));
                    ib.setCondition(cond);
                    Block first = null;
                    Block last = null;
                    for (int j = 1; j < nextLines.length; j++) {
                        String copy[] = new String[nextLines.length - j];
                        System.arraycopy(nextLines, j, copy, 0, copy.length);
                        Block b = parseLine(nextLines[j], copy);
                        if (first == null) {
                            ib.setBody(b);
                            first = b;
                            last = b;
                        } else {
                            last.setNext(b);
                            last = b;
                        }
                        if (b instanceof EndBlock) {
                            return ib;
                        }
                    }
                    return ib;
                }
            }
            past += c;
        }
        if (past.equals("end")) {
            return new EndBlock();
        }
        if (variables_.containsKey(past)) {
            return variables_.get(past);
        }
        
        return new Const(Value.parse(past));
    }
    
    private String clean(String line) {
        String l = line;
        if (l.startsWith(" ")) {
            int c = 0;
            while (l.charAt(c) == ' ') {
                c++;
            }
            l = l.substring(c);
        }
        if (l.endsWith(" ")) {
            int c = l.length() - 1;
            while (l.charAt(c) == ' ') {
                c--;
            }
            l = l.substring(0, c);
        }
        if (l.startsWith("(") && l.endsWith(")")) {
            int c = 0;
            for (int i = 1; i < l.length() - 1; i++) {
                if (l.charAt(i) == '(') {
                    c++;
                } else if (l.charAt(i) == ')') {
                    c--;
                }
            }
            if (c == 0) {
                return l.substring(1, l.length() - 1);
            }
            return l;
        } else {
            return l;
        }
    }
    
    public static void main(String[] args) {
        String test = "a=5\n"
                + "if a<3\n"
                + "  a=3\n"
                + "end\n";
 
        
        InterpreterBlock i = new InterpreterBlock();
        Block b = i.parse(test);
        i.run(b);
        System.exit(0);
    }
    
}
