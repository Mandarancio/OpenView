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

	public static ArrayList<File> getModuleList() {
		ArrayList<File> modules = new ArrayList<>();
		String path = System.getProperty("user.home") + "/.openview";
		File dir = new File(path);
		if (dir.exists()) {
			File[] list = dir.listFiles();
			for (File f : list) {
				if (f.isFile()) {
					if (f.getName().startsWith("OV")
							&& f.getName().endsWith(".jar")) {
						if (Constants.Debug)
							System.out.println("Modue : " + f.getName());
						modules.add(f);
					}
				}
			}
		}
		return modules;
	}

	public static BaseModule loadModule(File f) {
		try {
			URL[] urls = { f.toURI().toURL() };
			urlCl = new URLClassLoader(urls, ModuleUtil.class.getClassLoader());
			Class<?> base = urlCl.loadClass("module.Base");
			Object o = base.newInstance();
			return (BaseModule) o;
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
		return null;
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
