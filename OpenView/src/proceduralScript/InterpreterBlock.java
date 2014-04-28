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
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Block b = parseLine(line);
            if (first == null) {
                first = b;
                last = b;
            } else {
                last.setNext(b);
                last = b;
            }
            //

        }
        return first;
    }
    
    private Block parseLine(String l) {
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
                ob.setLeft(parseLine(past));
                ob.setRight(parseLine(line.substring(i)));
                
                return ob;
            } else if (operators_.get("" + c_past) != null) {
                past = line.substring(0, i - 1);
                
                OperatorBlock ob = new OperatorBlock(
                        operators_.get("" + c_past));
                ob.setLeft(parseLine(past));
                ob.setRight(parseLine(line.substring(i)));
                return ob;
            } else if (c_past == '=') {
                past = past.substring(0, past.length() - 1);
                Var v = variables_.get(past);
                if (v == null) {
                    v = new Var(past);
                    variables_.put(past, v);
                }
                return new AssignBlock(v, parseLine(line.substring(i)));
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
            } else if (c_past==' '){
                if (past.equals("if ")){
                    System.out.println("IF");
                }
                else if (past.equals("elif ")){
                    System.out.println("ELIF");
                }
                else if (past.equals("else ")){
                    System.out.println("ELSE");
                }
            }
            past += c;
        }
        if (past.equals("end")){
            System.out.println("END");
        }
        if (variables_.containsKey(past)) {
            return variables_.get(past);
        }
        
        return new Const(Value.parse(past));
    }
    
    private String clean(String line) {
        String l=line;
        if (l.startsWith(" ")){
            int c=0;
            while (l.charAt(c)==' '){
                c++;
            }
            l=l.substring(c);
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
                + "b=3\n"
                + "a=(b+a)/2\n"
                + "c=3\n"
                + "a=(a*c)-b\n"
                + "if a>3 :\n"
                + "  a=3\n"
                + "elif a<0 :\n"
                + "  a=0\n"
                + "end\n";
       
        InterpreterBlock i = new InterpreterBlock();
        Block b = i.parse(test);
//        i.run(b);
        System.exit(0);
    }
    
    
}
