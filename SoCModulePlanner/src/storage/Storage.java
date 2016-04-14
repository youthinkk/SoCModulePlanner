package storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import object.FocusArea;
import object.ModuleInfo;

public class Storage extends StorageFile {
	private static ObjectMapper _mapper = new ObjectMapper();
	
	public static TreeMap<String, ModuleInfo> getModuleInfo() {
		TreeMap<String, ModuleInfo> moduleList = new TreeMap<String, ModuleInfo>();
		
		try {
			TypeReference<TreeMap<String, ModuleInfo>> typeRef = 
					new TypeReference<TreeMap<String, ModuleInfo>>() { };
			
			moduleList = _mapper.readValue(MODULE_INFO_FILE, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return moduleList;
		}
		
		return moduleList;
	}
	
	public static HashMap<String, ArrayList<ArrayList<String>>> getModulePrereq() {
		HashMap<String, ArrayList<ArrayList<String>>> prereqList = 
				new HashMap<String, ArrayList<ArrayList<String>>>();
		
		try {
			TypeReference<HashMap<String, ArrayList<ArrayList<String>>>> typeRef = 
					new TypeReference<HashMap<String, ArrayList<ArrayList<String>>>>() { };
					
			prereqList = _mapper.readValue(MODULE_PREREQ_FILE, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return prereqList;
		}
		return prereqList;
	}
	
	public static HashMap<String, ArrayList<String>> getModulePreclusion() {
		HashMap<String, ArrayList<String>> preclusionList = new HashMap<String, ArrayList<String>>();
		
		try {
			TypeReference<HashMap<String, ArrayList<String>>> typeRef =
					new TypeReference<HashMap<String, ArrayList<String>>>() { };
					
			preclusionList = _mapper.readValue(MODULE_PRECLUSION_FILE, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return preclusionList;
		}
		return preclusionList;
	}
	
	public static HashMap<String, boolean[]> getModuleHistory() {
		HashMap<String, boolean[]> historyList = new HashMap<String, boolean[]>();
		
		try {
			TypeReference<HashMap<String, boolean[]>> typeRef =
					new TypeReference<HashMap<String, boolean[]>>() { };
					
			historyList = _mapper.readValue(MODULE_HISTORY_FILE, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return historyList;
		}
		return historyList;
	}
	
	public static HashMap<String, FocusArea> getFocusArea() {
		HashMap<String, FocusArea> focusAreaList = new HashMap<String, FocusArea>();
		
		try {
			TypeReference<HashMap<String, FocusArea>> typeRef =
					new TypeReference<HashMap<String, FocusArea>>() { };
					
			focusAreaList = _mapper.readValue(FOCUS_AREA_FILE, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return focusAreaList;
		}
		return focusAreaList;
	}
	
	public static HashMap<String, ArrayList<ArrayList<ArrayList<String>>>> getGradRequirement() {
		HashMap<String, ArrayList<ArrayList<ArrayList<String>>>> focusAreaList = 
				new HashMap<String, ArrayList<ArrayList<ArrayList<String>>>>();

		try {
			TypeReference<HashMap<String, ArrayList<ArrayList<ArrayList<String>>>>> typeRef =
					new TypeReference<HashMap<String, ArrayList<ArrayList<ArrayList<String>>>>>() { };

					focusAreaList = _mapper.readValue(GRAD_REQUIREMENT_FILE, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			return focusAreaList;
		}
		return focusAreaList;
	}
}
