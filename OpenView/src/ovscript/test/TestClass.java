package ovscript.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ovscript.Block;
import ovscript.Interpreter;
import ovscript.InterpreterException;

public class TestClass {
	public static int fact(int n) {
		if (n <= 0)
			return 1;
		else
			return n * fact(n - 1);
	}

	public static void main(String[] args) throws IOException {
		String path = TestClass.class.getResource("test.ov").getFile();
		System.out.println("run file: " + path);
		BufferedReader br = new BufferedReader(new FileReader(path));
		String code;
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			code = sb.toString();

			Interpreter i = new Interpreter();
			Block b = i.parse(code);
			i.setDebug(true);
			long st = System.currentTimeMillis();
			i.runBlock(b);
			long et = System.currentTimeMillis();
			System.out.println("time[ovscript]: " + (et - st) + "ms");
			st = System.currentTimeMillis();
			for (int j = 0; j < 1000; j++) {
				fact((int) Math.random() * 10);
			}
			et = System.currentTimeMillis();
			System.out.println("time[java]: " + (et - st) + "ms");

		} catch (InterpreterException e) {
			System.out.println("Error at Line " + e.getLine());
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

}
