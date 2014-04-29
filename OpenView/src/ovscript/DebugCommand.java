package ovscript;

import core.Value;

public class DebugCommand extends AbstractBlock{

	public DebugCommand(String name) {
		super(name);
	}

	@Override
	public Value run(CodeBlock i) {
		i.debug(name());
		return new Value();
	}

}
