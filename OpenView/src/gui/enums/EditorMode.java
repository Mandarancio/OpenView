package gui.enums;

public enum EditorMode {
	GUI, NODE, RUN, DEBUG;

	public boolean isExec() {
		return this == RUN || this == DEBUG;
	}
}
