package proceduralScript;

import java.util.HashMap;

import core.Value;
import evaluator.operators.OperatorManager;

public class InterpreterBlock {

    private class ReturnStruct {

        public Block block;
        public int lines;

        public ReturnStruct(Block b, int l) {
            block = b;
            lines = l;
        }
    }

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
        int i = 0;
        while (i < lines.length) {
            String line = lines[i];
            nexts = new String[lines.length - i];
            System.arraycopy(lines, i, nexts, 0, lines.length - i);
            ReturnStruct rs = parseLine(line, nexts);
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
        return first;
    }

    private ReturnStruct parseLine(String l, String nextLines[]) {
        if (l.length()==0)
            return new ReturnStruct(null, 1);
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
                ob.setLeft(parseLine(past, nextLines).block);
                ob.setRight(parseLine(line.substring(i), nextLines).block);

                return new ReturnStruct(ob, 1);
            } else if (operators_.get("" + c_past) != null) {
                past = line.substring(0, i - 1);

                OperatorBlock ob = new OperatorBlock(
                        operators_.get("" + c_past));
                ob.setLeft(parseLine(past, nextLines).block);
                ob.setRight(parseLine(line.substring(i), nextLines).block);
                return new ReturnStruct(ob, 1);
            } else if (c_past == '=') {
                past = past.substring(0, past.length() - 1);
                Var v = variables_.get(past);
                if (v == null) {
                    v = new Var(past);
                    variables_.put(past, v);
                }
                return new ReturnStruct(new AssignBlock(v, parseLine(line.substring(i), nextLines).block), 1);
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
                    Block cond = parseLine(past, nextLines).block;
                    ib.setCondition(cond);
                    return parseIF(ib, nextLines);
                } else if (past.equals("elif ")) {
                    ELSEBlock ib = new ELSEBlock();
                    past = line.substring(i);
                    Block cond = parseLine(past, nextLines).block;
                    ib.setCondition(cond);
                    return parseIF(ib, nextLines);
                }
            }
            past += c;
        }
        if (past.equals("end")) {
            return new ReturnStruct(new ENDBlock(), 1);
        } else if (past.equals("else")) {
            ELSEBlock ib = new ELSEBlock();
            Block cond = new Const(new Value(true));
            ib.setCondition(cond);
            return parseIF(ib, nextLines);

        }
        if (variables_.containsKey(past)) {
            return new ReturnStruct(variables_.get(past), 1);
        }

        return new ReturnStruct(new Const(Value.parse(past)), 1);
    }

    private ReturnStruct parseIF(IFBlock ifB, String[] lines) {

        int i = 1;
        Block first = null;
        Block last = null;
        while (i < lines.length) {
            String copy[] = new String[lines.length - i];
            System.arraycopy(lines, i, copy, 0, copy.length);
            ReturnStruct rs = parseLine(lines[i], copy);
            Block b = rs.block;

            if (b instanceof ELSEBlock) {
                ifB.setElse(b);
                break;
            } else if (b instanceof ENDBlock) {
                ifB.setNext(b);
                break;
            } else {
                if (first == null) {
                    first = b;
                    last = b;
                    ifB.setBody(first);
                } else {
                    last.setNext(b);
                    last = b;
                }
            }

            i += rs.lines;
        }

        return new ReturnStruct(ifB, i + 1);
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
        String test = "a=-2\n"
                + "if a>3\n"
                + "  a=3\n"
                + "elif a>0\n"
                + "  a=1\n"
                + "else\n"
                + "  a=0\n"
                + "end\n";

        InterpreterBlock i = new InterpreterBlock();
        Block b = i.parse(test);
        i.run(b);
        System.exit(0);
    }

}
