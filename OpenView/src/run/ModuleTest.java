package run;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class ModuleTest {
	private static URLClassLoader urlCl;

	public static void main(String[] args) {
		String path = System.getProperty("user.home") + "/.openview";
		System.out.println("Dir: " + path);
		File dir = new File(path);
		ArrayList<File> modules = new ArrayList<>();
		if (dir.exists()) {
			File[] list = dir.listFiles();
			for (File f : list) {
				if (f.isFile()) {
					if (f.getName().startsWith("OV_")
							&& f.getName().endsWith(".jar")) {
						System.out.println("Modue : " + f.getName());
						modules.add(f);
						loadModule(f);
					}
				}
			}
		}
		System.exit(0);
	}

	public static void loadModule(File f) {
		try {
			URL[] urls = { f.toURI().toURL() };
			urlCl = new URLClassLoader(urls, ModuleTest.class.getClassLoader());
			Class<?> base = urlCl.loadClass("module.Base");
			Object o = base.newInstance();
			System.out.println(o);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
