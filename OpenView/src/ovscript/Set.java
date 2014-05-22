package ovscript;

import gui.components.OVComponent;
import gui.components.nodes.InNode;
import run.window.ObjectManager;
import core.Value;

public class Set extends AbstractBlock {

	private String objectName_;
	private String nodeName_;
	private Block block_;

	private Set() {
		super("Set");
	}

	public Set(String arg, Block block) {
		this();
		objectName_ = arg.split(":")[0];
		nodeName_ = arg.split(":")[1];
		block_ = block;
	}

	@Override
	public Value run(CodeBlock i) throws InterpreterException {

		OVComponent c = ObjectManager.getComponent(objectName_);
		if (c != null) {
			InNode n = c.getInNode(nodeName_);
			if (n != null) {
				Value v = block_.run(i);
				n.trigger(v);
			}
		}

		throw new InterpreterException(
				"no object or node found for set function!", getLine());
	}

}
