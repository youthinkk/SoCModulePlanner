package storage;

import java.io.File;

public class StorageFile {
	private static final String DIRECTORY = "data";	
	private static final String MODULE_INFO_PATH = DIRECTORY + "/ModuleInfo.json";
	private static final String MODULE_PREREQ_PATH = DIRECTORY + "/ModulePrereq.json";
	private static final String MODULE_PRECLUSION_PATH = DIRECTORY + "/ModulePreclusion.json";
	private static final String MODULE_HISTORY_PATH = DIRECTORY + "/ModuleHistory.json";
	
	public static final File FOLDER = new File(DIRECTORY);
	public static final File MODULE_INFO_FILE = new File (MODULE_INFO_PATH);
	public static final File MODULE_PREREQ_FILE = new File(MODULE_PREREQ_PATH);
	public static final File MODULE_PRECLUSION_FILE = new File(MODULE_PRECLUSION_PATH);
	public static final File MODULE_HISTORY_FILE = new File(MODULE_HISTORY_PATH);
}
