package ovscript;

public class ReturnStruct {

	public Block block;
	public int lines;

	public ReturnStruct(Block b, int l) {
		block = b;
		lines = l;
	}
	
	public ReturnStruct(Block b, int l, int line_ind, int char_ind){
		this(b,l);
		b.setLine(line_ind);
		b.setChar(char_ind);
	}
}
