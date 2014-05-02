package ovscript;

import javax.swing.JOptionPane;

public class DebugManager {
	public static String DEND = "STOP", DPRINT = "PRINT", DBREAK= "BREAK",DWAIT="WAIT";

	public static void debug(String code, CodeBlock block) {
		if (block.isDebug()) {
			if (code.equals(DEND)) {
				block.endRun();
				CodeBlock b=block.parent();
				while (b!=null){
					b.endRun();
					b=b.parent();
				}
			} else if (code.equals(DPRINT)) {
				System.out.println("VARIABLE STATUS OF "+block.getClass().getSimpleName()+" : ");
				for (String s : block.localVariableStack().keySet()){
					System.out.println("  "+s+" : "+block.localVariableStack().get(s).value);
				}
				System.out.println("----\n");
				if (block.parent()!=null){
					block.parent().debug(code);
				}
			}else if (code.equals(DBREAK)){
				 block.endRun();
			}else if (code.equals(DWAIT)){
				JOptionPane.showMessageDialog(null,"Waiting...");
			}
		}
	}

}