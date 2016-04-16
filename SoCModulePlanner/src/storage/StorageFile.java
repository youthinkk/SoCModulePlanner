package storage;

import java.io.File;

public class StorageFile {
	private static final String DATA_DIRECTORY = "data";	
	private static final String MODULE_INFO_PATH = DATA_DIRECTORY + "/ModuleInfo.json";
	private static final String MODULE_PREREQ_PATH = DATA_DIRECTORY + "/ModulePrereq.json";
	private static final String MODULE_PRECLUSION_PATH = DATA_DIRECTORY + "/ModulePreclusion.json";
	private static final String MODULE_HISTORY_PATH = DATA_DIRECTORY + "/ModuleHistory.json";
	private static final String FOCUS_AREA_PATH = DATA_DIRECTORY + "/FocusArea.json";
	private static final String GRAD_REQUIREMENT_PATH = DATA_DIRECTORY + "/GradRequirement.json";
	
	private static final String CLIPS_DIRECTORY = "clips";
	private static final String PLANNER_CONDITION_PATH = CLIPS_DIRECTORY + "/PlannerCondition.clp";
	
	public static final File FOLDER = new File(DATA_DIRECTORY);
	public static final File MODULE_INFO_FILE = new File (MODULE_INFO_PATH);
	public static final File MODULE_PREREQ_FILE = new File(MODULE_PREREQ_PATH);
	public static final File MODULE_PRECLUSION_FILE = new File(MODULE_PRECLUSION_PATH);
	public static final File MODULE_HISTORY_FILE = new File(MODULE_HISTORY_PATH);
	public static final File FOCUS_AREA_FILE = new File(FOCUS_AREA_PATH);
	public static final File GRAD_REQUIREMENT_FILE = new File(GRAD_REQUIREMENT_PATH);
	public static final File PLANNER_CONDITION_FILE = new File(PLANNER_CONDITION_PATH);
}
