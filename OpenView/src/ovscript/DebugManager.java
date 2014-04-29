package ovscript;

public class DebugManager {
	public static String DEND = "STOP", DPRINT = "PRINT";

	public static void debug(String code, CodeBlock block) {
		if (block.isDebug()) {
			if (code.equals(DEND)) {
				block.endRun();
			} else if (code.equals(DPRINT)) {
				System.out.println("VARIABLE STATUS OF "+block.getClass().getSimpleName()+" : ");
				for (String s : block.localVariableStack().keySet()){
					System.out.println("  "+s+" : "+block.localVariableStack().get(s).value);
				}
				System.out.println("----\n");
				if (block.parent()!=null){
					block.parent().debug(code);
				}
			}
		}
	}

}
