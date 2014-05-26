package run.init;

import java.io.File;

/***
 * Util to manage the user directory and files
 * 
 * @author martino
 * 
 */
public class FilesUtil {
	/***
	 * Definition of user folder path
	 */
	private static final String userFolder = ".openview";
	/***
	 * Definition of modules folder
	 */
	private static final String modulesFolder = "modules";
	/***
	 * Definition of extra jars folder for module
	 */
	public static final String extraJarsFolder = "jars";
	/***
	 * Definition of the base module file
	 */
	public static final String moduleJarPath = "module.jar";
	/***
	 * Definition of the setting file
	 */
	private static final String settingsFile = "settings.xml";

	/***
	 * Get the home path
	 * 
	 * @return home path
	 */
	public static String homePath() {
		return System.getProperty("user.home");
	}

	/***
	 * Get the user folder path (homePath() + .openView)
	 * 
	 * @return the user folder path
	 */
	public static String userFolderPath() {
		return homePath() + File.separator + userFolder + File.separator;
	}

	/***
	 * get setting file path
	 * 
	 * @return setting file
	 */
	public static String settingsFilePath() {
		return userFolderPath() + settingsFile;
	}

	/***
	 * get modules folder path
	 * 
	 * @return get the modules folder path
	 */
	public static String modulesFolderPath() {
		return userFolderPath() + modulesFolder + File.separator;
	}

	/***
	 * Check if user document are there and if not it will make its
	 */
	public static void checkUserFolder() {
		File f = new File(userFolderPath());
		if (!f.exists() || !f.isDirectory()) {
			f.mkdir();
		}
		f = new File(modulesFolderPath());
		if (!f.exists() || !f.isDirectory()) {
			f.mkdir();
		}
	}
}
