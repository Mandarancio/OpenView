package core.support;

import java.awt.Color;

public class Utils {
	//Format: RRR,GGG,BBB,AAA
	public static Color parseColor(String str){
		String split[]=str.split(",");
		if (split!=null && split.length==4){
			int r=Integer.parseInt(split[0]);
			int g=Integer.parseInt(split[1]);
			int b=Integer.parseInt(split[2]);
			int a=Integer.parseInt(split[3]);
			return new Color(r,g,b,a);
		}
		return Color.black;
	}
	
	//Format: RRR,GGG,BBB,AAA
	public static String codeColor(Color color){
		int r=color.getRed();
		int g=color.getGreen();
		int b=color.getBlue();
		int a=color.getAlpha();
		return Integer.toString(r)+","+Integer.toString(g)+","+Integer.toString(b)+","+Integer.toString(a);
	}

}
