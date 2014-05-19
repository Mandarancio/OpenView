package run.init;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.swing.JMenu;

import run.constants.Constants;
import core.maker.OVClassFactory;
import core.maker.OVMenuManager;
import core.module.BaseModule;

public class ModuleUtil {
	private static URLClassLoader urlCl;
	private static final String user_path = "/.openview/modules/";
	private static final String module_file = "/module.jar";
	private static final String jars_dir = "/jars/";

	public static ArrayList<File> getModuleList() {
		ArrayList<File> modules = new ArrayList<>();
		String path = System.getProperty("user.home") + user_path;
		File dir = new File(path);
		if (dir.exists()) {
			File[] list = dir.listFiles();
			for (File f : list) {
				if (f.isDirectory()) {
					File module = new File(f.getAbsolutePath() + module_file);
					if (module.exists() && module.isFile()) {
						modules.add(module);
						if (Constants.Debug)
							System.out.println("Modue : " + f.getName());
					}
				}
			}
		}
		return modules;
	}

	public static BaseModule loadModule(File f) {
		try {
			ArrayList<URL> jars = new ArrayList<>();
			jars.addAll(lookForJars(f));
			jars.add(f.toURI().toURL());
			URL[] urls = jars.toArray(new URL[jars.size()]);
			urlCl = new URLClassLoader(urls, ModuleUtil.class.getClassLoader());
			Class<?> base = urlCl.loadClass("module.Base");
			Object o = base.newInstance();
			return (BaseModule) o;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ArrayList<URL> lookForJars(File f) {
		String dir = f.getParent();
		File jarsDir = new File(dir + jars_dir);
		if (jarsDir.exists() && jarsDir.isDirectory()) {
			ArrayList<URL> list = new ArrayList<>();
			File[] files = jarsDir.listFiles();
			for (File file : files) {
				if (file.getName().endsWith(".jar")) {
					try {
						list.add(file.toURI().toURL());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
			return list;
		}
		return new ArrayList<>();
	}

	public static void importModule(BaseModule module) {
		if (Constants.Debug)
			System.out.println("Import moduele: " + module.getModuleName()
					+ " " + module.getVersion());
		for (JMenu m : module.getGuiMenus()) {
			OVMenuManager.addGUIMenu(m);
		}
		for (JMenu m : module.getNodeMenus()) {
			OVMenuManager.addNodeMenu(m);
		}

		for (String k : module.getComponents().keySet()) {
			OVClassFactory.addClass(k, module.getComponents().get(k));
		}
	}
}
