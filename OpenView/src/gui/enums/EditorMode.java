package gui.enums;

/***
 * possible edit mode
 * 
 * @author martino
 * 
 */
public enum EditorMode {
	GUI, NODE, RUN, DEBUG;

	/***
	 * check if is a run mode
	 * 
	 * @return
	 */
	public boolean isExec() {
		return this == RUN || this == DEBUG;
	}
}
