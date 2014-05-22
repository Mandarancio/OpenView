package ovscript;

import run.window.ObjectManager;
import gui.components.OVComponent;
import gui.components.nodes.OutNode;
import core.Value;
import core.ValueType;

public class Import extends AbstractBlock {
	private ValueType type_;
	private String objectName_;
	private String nodeName_;

	private Import() {
		super("import");
		setType(ValueType.VOID);
	}

	public Import(String... args) {
		this();
		if (args.length > 0) {
			String arg = args[0];
			objectName_ = arg.split(":")[0];
			nodeName_ = arg.split(":")[1];
			if (args.length == 2) {
				arg = args[1];
				try {
					type_ = ValueType.valueOf(arg);
				} catch (Exception e) {
					e.printStackTrace();
					type_ = ValueType.NONE;
				}
			}
		}

	}

	@Override
	public Value run(CodeBlock i) throws InterpreterException {
		OVComponent c = ObjectManager.getComponent(objectName_);
		if (c != null) {
			OutNode n = c.getOutNode(nodeName_);
			if (n != null)
				return n.readValue();
		}

		throw new InterpreterException("no slot connected to import function!",
				getLine());
	}

	public ValueType getType() {
		return type_;
	}

	public void setType(ValueType type_) {
		this.type_ = type_;
	}

}
