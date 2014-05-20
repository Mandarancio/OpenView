package run.init;

import java.io.File;

public class FilesUtil {
	private static final String userFolder = ".openview";
	private static final String modulesFolder = "modules";
	public static final String extraJarsFolder = "jars";
	public static final String moduleJarPath = "module.jar";
	private static final String settingsFile = "settings.xml";

	public static String homePath() {
		return System.getProperty("user.home");
	}

	public static String userFolderPath() {
		return homePath() + File.separator + userFolder + File.separator;
	}

	public static String settingsFilePath() {
		return userFolderPath() + settingsFile;
	}

	public static String modulesFolderPath() {
		return userFolderPath() + modulesFolder + File.separator;
	}

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
