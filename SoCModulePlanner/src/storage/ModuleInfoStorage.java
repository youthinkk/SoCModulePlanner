package storage;

import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import object.ModuleInfo;

public class ModuleInfoStorage extends StorageFile {
	private static ObjectMapper _mapper = new ObjectMapper();
	
	public static void writeFile(TreeMap<String, ModuleInfo> moduleList) {
		try {
			if (!MODULE_INFO_FILE.exists()) {
				createFile();
			}
			
			_mapper.writerWithDefaultPrettyPrinter().writeValue(MODULE_INFO_FILE, moduleList);
		} catch (Exception e) {
			// do nothing
		}
	}
	
	public static TreeMap<String, ModuleInfo> readFile() {
		TreeMap<String, ModuleInfo> moduleList = new TreeMap<String, ModuleInfo>();
		
		try {
			if (!MODULE_INFO_FILE.exists()) {
				createFile();
			}
			
			TypeReference<TreeMap<String, ModuleInfo>> typeRef = new TypeReference<TreeMap<String, ModuleInfo>>() { };
			
			moduleList = _mapper.readValue(MODULE_INFO_FILE, typeRef);
		} catch (Exception e) {
			return moduleList;
		}
		
		return moduleList;
	}
	
	private static void createFile() {
		try {
			TreeMap<String, ModuleInfo> moduleList = new TreeMap<String, ModuleInfo>();
			
			// write empty module list
			_mapper.writeValue(MODULE_INFO_FILE, moduleList);
		} catch (Exception e) {
			// do nothing
		}
	}
}
