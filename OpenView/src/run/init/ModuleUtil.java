package run.init;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JMenu;

import run.constants.Constants;
import xeus.jcl.JarClassLoader;
import core.maker.OVClassFactory;
import core.maker.OVMenuManager;
import core.module.BaseModule;
import core.support.EnumManager;
import evaluator.functions.FunctionManager;
import evaluator.operators.OperatorManager;

/***
 * Utility to discover and load extra-modules
 * 
 * @author martino
 * 
 */
public class ModuleUtil {
	private static JarClassLoader loader_ = new JarClassLoader();
	private static ArrayList<BaseModule> modules_ = new ArrayList<>();

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

	public static BaseModule loadModule(File f) {
		try {
			loader_.add(f.toURI().toURL());
			Class<?> base = loader_.loadClass(f.getParentFile().getName()
					.toLowerCase()
					+ ".module.Base");
			BaseModule o = (BaseModule) base.newInstance();
			modules_.add(o);
			o.setPath(f.getParent());
			return o;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void loadExtJars(String dir) {
		File jarsDir = new File(dir + File.separator
				+ FilesUtil.extraJarsFolder);
		if (jarsDir.exists() && jarsDir.isDirectory()) {
			File[] files = jarsDir.listFiles();
			for (File file : files) {
				if (file.getName().endsWith(".jar")) {
					try {
						loader_.add(file.toURI().toURL());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void importModule(BaseModule module) {
		if (Constants.Debug)
			System.out
					.println("Import module: " + module.getModuleName()
							+ "\nVersion: " + module.getVersion()
							+ "\nAuthor: " + module.getAuthor() + "\nMail: "
							+ module.getMailAddress());
		Splash.setStatus("Import module: " + module.getModuleName() + " "
				+ module.getVersion());
		loadExtJars(module.getPath());
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
		FunctionManager.addFunctions(module.getFunctions());
		OperatorManager.addOperatros(module.getOperators());
	}

	public static ArrayList<BaseModule> getModules() {
		return modules_;
	}

	public static String version(String module) {
		for (BaseModule m : modules_) {
			if (m.getModuleName().equals(module))
				return m.getVersion();
		}
		return "";
	}

	public static boolean exist(String module) {
		for (BaseModule m : modules_) {
			if (m.getModuleName().equals(module))
				return true;
		}
		return false;
	}
}
