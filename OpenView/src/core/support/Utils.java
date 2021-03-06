package core.support;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/***
 * some utils for parse and code complex object
 * 
 * @author martino
 * 
 */
public class Utils {
	/***
	 * parse a color string (as RRR,GGG,BBB,AAA)
	 * 
	 * @param str
	 *            string to parse
	 * @return Color parsed (if not well defined return black)
	 */
	public static Color parseColor(String str) {
		String split[] = str.split(",");
		if (split != null && split.length == 4) {
			int r = Integer.parseInt(split[0]);
			int g = Integer.parseInt(split[1]);
			int b = Integer.parseInt(split[2]);
			int a = Integer.parseInt(split[3]);
			return new Color(r, g, b, a);
		}
		return Color.black;
	}

	/***
	 * code color to string
	 * 
	 * @param color
	 *            to code
	 * @return color coded to string (RRR,GGG,BBB,AAA)
	 */
	public static String codeColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int a = color.getAlpha();
		return Integer.toString(r) + "," + Integer.toString(g) + ","
				+ Integer.toString(b) + "," + Integer.toString(a);
	}

	/***
	 * Format x,y
	 * 
	 * @param p
	 *            point to code
	 * @return string
	 */
	public static String codePoint(Point p) {
		return p.x + "," + p.y;
	}

	/***
	 * Format x,y
	 * 
	 * @param s
	 *            string to parse
	 * @return point
	 */
	public static Point parsePoint(String s) {
		String split[] = s.split(",");
		if (split.length == 2) {
			int x = Integer.parseInt(split[0]);
			int y = Integer.parseInt(split[1]);
			return new Point(x, y);
		}
		return new Point();
	}

	/***
	 * Format w,h
	 * 
	 * @param d
	 *            size to code
	 * @return string
	 */
	public static String codeDimension(Dimension d) {
		return d.width + "," + d.height;
	}

	/***
	 * Parse string (w,h) to diemnsion
	 * 
	 * @param d
	 *            dimension string
	 * @return the parsed dimension
	 */
	public static Dimension parseDimension(String d) {
		String split[] = d.split(",");
		if (split.length == 2) {
			int x = Integer.parseInt(split[0]);
			int y = Integer.parseInt(split[1]);
			return new Dimension(x, y);
		}
		return new Dimension();
	}

}
