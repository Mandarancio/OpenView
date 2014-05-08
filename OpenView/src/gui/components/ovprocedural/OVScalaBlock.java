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

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import scala.runtime.AbstractFunction1;
import scala.runtime.BoxedUnit;
import scala.tools.nsc.GenericRunnerSettings;
import scala.tools.nsc.interpreter.IMain;
import scala.tools.nsc.settings.MutableSettings;
import core.Setting;
import core.SlotInterface;
import core.SlotListener;
import core.Value;
import core.ValueType;
import core.support.OrientationEnum;

public class OVScalaBlock extends OVNodeComponent implements SlotListener,
		DocumentListener {

	private static class ErrorHandler extends
			AbstractFunction1<String, BoxedUnit> {
		@Override
		public BoxedUnit apply(String message) {
			System.err.println("Interpreter error: " + message);
			return BoxedUnit.UNIT;
		}
	}

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
	private IMain interpreter_;
	private boolean __lock = false;

	private HashMap<Integer, InNode> slots_ = new HashMap<>();
	private HashMap<Integer, OutNode> emitters_ = new HashMap<>();
	private GenericRunnerSettings settings;

	public OVScalaBlock(OVContainer father) {
		super(father);
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
		initInterpreter();
	}

	private void initInterpreter() {
		// Setup the compiler/interpreter
		settings = new GenericRunnerSettings(new ErrorHandler());

		// In scala this is settings.usejavacp.value = true;
		// It it through this setting that the compiled code is able to
		// reference the
		// `MustConform` interface. The runtime classpath leaks into the
		// compiler classpath, but
		// we're OK with that in this use case.
		((MutableSettings.BooleanSetting) settings.usejavacp()).v_$eq(true);
		interpreter_ = new IMain(settings);

	}

	private void initTextArea() {
		textArea_ = new RSyntaxTextArea(5, 30);
		textArea_.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA);
		textArea_.setCodeFoldingEnabled(true);
		scrollArea_ = new RTextScrollPane(textArea_);
		this.add(scrollArea_, BorderLayout.CENTER);

		textArea_.getDocument().addDocumentListener(this);
	}

	@Override
	public void setMode(EditorMode mode) {

		if (mode != getMode()) {

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

			__lock = true;
			code_ = textArea_.getText();
			run();

		}
	}

	private void readyForRun() {
		interpreter_.reset();
		interpreter_.interpret("import core.Emitter");
		interpreter_.interpret("import core.Slot");
		interpreter_.interpret("import core.Value");
		interpreter_.interpret("import java.util.HashMap");
		interpreter_.bind("emits", "java.util.HashMap[Int,core.Emitter]",
				emitters_, settings.recreateArgs());
		interpreter_.bind("slots", "java.util.HashMap[Int,core.Slot]", slots_,
				settings.recreateArgs());

		interpreter_.interpret("def export(a: Any, line: Int){"
				+ "		emits.get(line).trigger(new Value(a))" + "}");

		interpreter_.interpret("def export(a: Any,type : String, line: Int){"
				+ "		emits.get(line).trigger(new Value(a))" + "}");

		interpreter_.interpret("def import(line :Int){"
				+ " 	return slots.get(line).pullValue()" + "}");

		interpreter_.interpret("def import(type: String, line :Int){"
				+ " 	return slots.get(line).pullValue()" + "}");
	}

	private void run() {
		(new Thread() {
			@Override
			public void run() {
				if (!__lock)
					__lock = true;
				// readyForRun();

				String[] lines = code_.split("\n");
				for (int i = 0; i < lines.length; i++) {
					interpreter_.interpret(lines[i]);
				}
				__lock = false;

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
				in.put(new Integer(i + 1), getImportType(lines[i]));
			}
			if (lines[i].contains(Export)) {
				out.put(new Integer(i + 1), getExportType(lines[i]));
			}
		}

		if (in.size() != slots_.size() || out.size() != emitters_.size()) {
			readyForRun();
		}

		for (Integer l : in.keySet()) {
			if (slots_.containsKey(l)) {
				slots_.get(l).setType(in.get(l));
				Point p = slots_.get(l).getLocation();
				repaint(p.x - 5, p.y - 5, 10, 10);
			} else {
				InNode n = addInput("in " + l.toString(), in.get(l));
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
				emitters_.put(l, n);
			}
		}

		ArrayList<Integer> keys = new ArrayList<>(slots_.keySet());
		for (Integer l : keys) {
			if (!in.containsKey(l)) {
				InNode n = slots_.get(l);
				removeInput(n);
				slots_.remove(l);
			}
		}
		keys = new ArrayList<>(emitters_.keySet());
		for (Integer l : keys) {
			if (!out.containsKey(l)) {
				OutNode n = emitters_.get(l);
				removeOutput(n);
				emitters_.remove(l);
			}
		}

	}
}
