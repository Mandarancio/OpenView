package ovscript.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ovscript.Block;
import ovscript.InterpreterBlock;

public class TestClass {
	public static void main(String[] args) throws IOException {
		String path = TestClass.class.getResource("test.ov").getFile();
		System.out.println("run file: "+path);
		BufferedReader br = new BufferedReader(new FileReader(path));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String code = sb.toString();
			
			InterpreterBlock i=new InterpreterBlock();
			Block b=i.parse(code);
			i.setDebug(true);
			i.run(b);
			
		} finally {
			br.close();
		}
	}

}
