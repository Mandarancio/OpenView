package gui.components.ovprocedural;

import gui.components.nodes.InNode;
import gui.components.nodes.OutNode;
import gui.components.ovnode.OVNodeComponent;
import gui.constants.ComponentSettings;
import gui.enums.EditorMode;
import gui.interfaces.OVContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import ovscript.Block;
import ovscript.Interpreter;
import ovsynthax.OVScriptTokenMaker;
import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;
import core.support.OrientationEnum;
import javax.swing.JOptionPane;
import ovscript.InterpreterException;

public class OVProceduralBlock extends OVNodeComponent implements SlotListener,
        DocumentListener {

    /**
     *
     */
    private static final long serialVersionUID = 7990244919879751570L;
    private static final String Trigger = "Trigger";
    private static final String Import = "import(";
    private static final String Export = "export(";

    private String code_ = "";
    private boolean expand_ = true;
    private Dimension oldSize_;
    private RSyntaxTextArea textArea_;
    private RTextScrollPane scrollArea_;
    private Interpreter interpreter_;
    private Block body_;
    private boolean __lock = false;

    private HashMap<Integer, InNode> slots_ = new HashMap<>();
    private HashMap<Integer, OutNode> emitters_ = new HashMap<>();

    public OVProceduralBlock(OVContainer father) {
        super(father);
        interpreter_ = new Interpreter();
        getSetting(ComponentSettings.Name).setValue("Code");
        oldSize_ = new Dimension(getSize());
        expand_ = true;
        resizable_ = true;
        __minY = 4;
        InNode trigger = addInput(Trigger, ValueType.VOID);
        trigger.addListener(this);
        getSetting(ComponentSettings.SizeW).setValue(300);
        getSetting(ComponentSettings.SizeH).setValue(250);
        this.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
        initTextArea();
    }

    private void initTextArea() {
        textArea_ = new RSyntaxTextArea(5, 30);
        textArea_.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea_.setCodeFoldingEnabled(true);
        scrollArea_ = new RTextScrollPane(textArea_);
        this.add(scrollArea_, BorderLayout.CENTER);

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory
                .getDefaultInstance();
        atmf.putMapping("text/OVScript",
                OVScriptTokenMaker.class.getCanonicalName());
        textArea_.setSyntaxEditingStyle("text/OVScript");
        textArea_.getDocument().addDocumentListener(this);
    }

    @Override
    public void setMode(EditorMode mode) {

        if (mode != getMode()) {
            if (mode == EditorMode.DEBUG) {
                interpreter_.setDebug(true);
            } else {
                interpreter_.setDebug(false);
            }

            this.mode_ = mode;
            for (String s : settings_.keySet()) {
                for (Setting stg : settings_.get(s)) {
                    stg.setMode(getMode());
                }
            }
            if (mode_ != EditorMode.NODE && mode_ != EditorMode.DEBUG) {
                setVisible(false);
            } else {
                setVisible(true);
            }
            repaint();
        }
    }

    @Override
    protected void paintOVNode(Graphics2D g) {
        if (expand_) {

            g.setColor(getForeground());

            paintText(getName(), g, new Rectangle(15, 3, getWidth() - 20, 15),
                    OrientationEnum.LEFT);
            // g.drawLine(0, 20, getWidth(), 20);
            g.setFont(getFont().deriveFont(10.0f));
        } else {
            super.paintOVNode(g);
        }
    }

    @Override
    public void valueRecived(SlotInterface s, Value v) {
        if (s.getLabel().equals(Trigger)) {
            if (__lock) {
                return;
            }
            // do something
            if (code_.equals(textArea_.getText()) && body_ != null) {
                __lock = true;
                run();

            } else {
                __lock = true;
                code_ = textArea_.getText();
                parse();

            }
        }
    }

    private void run() {
        (new Thread() {
            @Override
            public void run() {
                try {
                    interpreter_.runBlock(body_);
                    __lock = false;
                } catch (InterpreterException e) {
                    JOptionPane.showMessageDialog(null, "Error at line: " + (e.getLine()+1) + "\n" + e.getMessage());
                }
            }
        }).start();
    }

    private void parse() {
        (new Thread() {
            @Override
            public void run() {
                try {
                    body_ = interpreter_.parse(code_);
                    interpreter_.runBlock(body_);
                    __lock = false;
                } catch (InterpreterException e) {
                    JOptionPane.showMessageDialog(null, "Error at line: " + (e.getLine()+1) + "\n" + e.getMessage());
                }
            }
        }).start();
    }

    public boolean isExpand() {
        return expand_;
    }

    private void expand() {
        expand_ = true;
        scrollArea_.setVisible(true);
        __minY = 4;
        setSize(oldSize_);
        getSetting(ComponentSettings.SizeW).setValue(oldSize_.width);
        getSetting(ComponentSettings.SizeH).setValue(oldSize_.height);
        resizable_ = true;
        repaint();
    }

    private void comprime() {
        if (expand_) {
            oldSize_ = new Dimension(getSize());
        }

        expand_ = false;
        scrollArea_.setVisible(false);
        __minY = 0;

        getSetting(ComponentSettings.SizeW).setValue(60);
        getSetting(ComponentSettings.SizeH).setValue(60);
        resizable_ = false;
        repaint();
    }

    @Override
    public void doubleClick(Point point) {
        if (isExpand() && point.y <= 20) {
            comprime();
        } else if (!isExpand()) {
            expand();
        }
        super.doubleClick(point);

    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
        parseNodes();
    }

    private ValueType getExportType(String string) {
        int ind = string.indexOf(Export);
        String sub = string.substring(ind + Import.length());
        ind = getIndex(sub);
        if (ind > 1) {
            String split[] = sub.substring(0, ind).split(",");
            if (split.length == 2) {
                String type = split[1];
                try {
                    return ValueType.valueOf(type);
                } catch (Exception e) {
                    return ValueType.VOID;
                }
            }
        }
        return ValueType.VOID;
    }

    private ValueType getImportType(String string) {
        int ind = string.indexOf(Import);
        String sub = string.substring(ind + Import.length());
        ind = getIndex(sub);
        if (ind > 1) {
            String type = sub.substring(0, ind);
            try {
                return ValueType.valueOf(type);
            } catch (Exception e) {
                return ValueType.VOID;
            }
        }
        return ValueType.VOID;
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        parseNodes();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        parseNodes();
    }

    private static int getIndex(String line) {
        char array[] = line.toCharArray();
        int p = 1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                p++;
            } else if (array[i] == ')') {
                p--;
                if (p == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void parseNodes() {
        String lines[] = textArea_.getText().split("\n");
        HashMap<Integer, ValueType> in = new HashMap<>();
        HashMap<Integer, ValueType> out = new HashMap<>();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(Import)) {
                in.put(new Integer(i), getImportType(lines[i]));
            }
            if (lines[i].contains(Export)) {
                out.put(new Integer(i), getExportType(lines[i]));
            }
        }

        for (Integer l : in.keySet()) {
            if (slots_.containsKey(l)) {
                slots_.get(l).setType(in.get(l));
                Point p = slots_.get(l).getLocation();
                repaint(p.x - 5, p.y - 5, 10, 10);
            } else {
                InNode n = addInput("in " + l.toString(), in.get(l));
                interpreter_.addSlot(l.intValue(), n);
                slots_.put(l, n);
            }
        }

        for (Integer l : out.keySet()) {
            if (emitters_.containsKey(l)) {
                emitters_.get(l).setType(out.get(l));
                Point p = emitters_.get(l).getLocation();
                repaint(p.x - 5, p.y - 5, 10, 10);
            } else {
                OutNode n = addOutput("out " + l, out.get(l));
                interpreter_.addEmitter(l.intValue(), n);
                emitters_.put(l, n);
            }
        }

        ArrayList<Integer> keys = new ArrayList<>(slots_.keySet());
        for (Integer l : keys) {
            if (!in.containsKey(l)) {
                InNode n = slots_.get(l);
                interpreter_.removeSlot(n);
                removeInput(n);
                slots_.remove(l);
            }
        }
        keys = new ArrayList<>(emitters_.keySet());
        for (Integer l : keys) {
            if (!out.containsKey(l)) {
                OutNode n = emitters_.get(l);
                interpreter_.removeEmitter(n);
                removeOutput(n);
                emitters_.remove(l);
            }
        }
    }
}
