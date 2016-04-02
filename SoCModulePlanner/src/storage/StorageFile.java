package storage;

import java.io.File;

public class StorageFile {
	private static final String DIRECTORY = "data";	
	private static final String MODULE_INFO_PATH = DIRECTORY + "/" + "ModuleInfo.json";
	
	public static final File FOLDER = new File(DIRECTORY);
	public static final File MODULE_INFO_FILE = new File (MODULE_INFO_PATH);
	
	static {
		if (!FOLDER.exists()) {
			FOLDER.mkdirs();
		}
	}
}
