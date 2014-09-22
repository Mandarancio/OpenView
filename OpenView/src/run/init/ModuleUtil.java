package run.init;

import evaluator.functions.FunctionManager;
import evaluator.operators.OperatorManager;
import gui.settings.viewers.ViewerManager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import javax.swing.JMenu;

import run.constants.Constants;
import core.maker.OVClassFactory;
import core.maker.OVMenuManager;
import core.module.BaseModule;
import core.support.EnumManager;
import core.support.Rule;

/***
 * Utility to discover and load extra-modules
 * 
 * @author martino
 * 
 */
public class ModuleUtil {

	/***
	 * List of the loaded module descriptors
	 */
	private static ArrayList<BaseModule> modules_ = new ArrayList<>();

	/**
	 * Look in the user module directory for the list of modules
	 * 
	 * @return list of modules
	 */
	public static ArrayList<File> getModuleList() {
		ArrayList<File> modules = new ArrayList<>();
		String path = FilesUtil.modulesFolderPath();
		File dir = new File(path);
		if (dir.exists()) {
			File[] list = dir.listFiles();
			for (File f : list) {
				if (f.isDirectory()) {
					File module = new File(f.getAbsolutePath() + File.separator
							+ FilesUtil.moduleJarPath);
					if (module.exists() && module.isFile()) {

						modules.add(module);
						if (Constants.Debug)
							System.out.println("Module found : " + f.getName());
						Splash.setStatus("Module found : " + f.getName());

					}
				}
			}
		}
		return modules;
	}

	/***
	 * Load the module descriptor
	 * 
	 * @param f
	 *            module file to load
	 * @return module descriptor
	 */
	public static void loadModule(File f) {
		try {
			String name=new File(f.getParent()).getName().toLowerCase();
			System.out.println(name);
			ClassLoader loader=URLClassLoader.newInstance(new URL[]{f.toURI().toURL()});
			BaseModule o=(BaseModule) loader.loadClass(name+".module.Base").newInstance();
			System.out.println(o);
			// Class<?> base = loader_.loadClass(f.getParentFile().getName()
			// .toLowerCase()
			// + ".module.Base");
			// BaseModule o = (BaseModule) base.newInstance();
			// modules_.add(o);
			// o.setPath(f.getParent());
			// return o;
			modules_.add(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Load any needed extra jars of a module
	 * 
	 * @param dir
	 *            directory of the module
	 */
	public static void loadExtJars(String dir) {
		File jarsDir = new File(dir + File.separator
				+ FilesUtil.extraJarsFolder);
		if (jarsDir.exists() && jarsDir.isDirectory()) {
			try {
				for (File f: jarsDir.listFiles()){
					if (f.getName().endsWith(".jar")){
						URLClassLoader.newInstance(new URL[]{f.toURI().toURL()});

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * Import a module and his extra-jars
	 * 
	 * @param module
	 *            module descriptor to load
	 */
	public static void importModule(BaseModule module) {
		if (Constants.Debug)
			System.out
					.println("Import module: " + module.getModuleName()
							+ "\nVersion: " + module.getVersion()
							+ "\nAuthor: " + module.getAuthor() + "\nMail: "
							+ module.getMailAddress());
		Splash.setStatus("Import module: " + module.getModuleName() + " "
				+ module.getVersion());
		for (JMenu m : module.getGuiMenus()) {
			OVMenuManager.addGUIMenu(m);
		}
		for (JMenu m : module.getNodeMenus()) {
			OVMenuManager.addNodeMenu(m);
		}

		for (String k : module.getComponents().keySet()) {
			OVClassFactory.addClass(k, module.getComponents().get(k));
		}

		for (Class<? extends Enum<?>> c : module.getEnums()) {
			EnumManager.addEnum(c);
		}

		for (Rule r : module.getViewer().keySet()) {
			ViewerManager.addViewer(r, module.getViewer().get(r));
		}

		FunctionManager.addFunctions(module.getFunctions());
		OperatorManager.addOperatros(module.getOperators());
	}

	/***
	 * Get the list of loaded descriptors
	 * 
	 * @return list of module descriptors
	 */
	public static ArrayList<BaseModule> getModules() {
		return modules_;
	}

	/***
	 * get the version of a module
	 * 
	 * @param module
	 *            name of the module
	 * @return the version (if any)
	 */
	public static String version(String module) {
		for (BaseModule m : modules_) {
			if (m.getModuleName().equals(module))
				return m.getVersion();
		}
		return "";
	}

	/***
	 * Look if a module exist in the list of loaded module descriptors
	 * 
	 * @param module
	 *            name of the module
	 * @return
	 */
	public static boolean exist(String module) {
		for (BaseModule m : modules_) {
			if (m.getModuleName().equals(module))
				return true;
		}
		return false;
	}
}
