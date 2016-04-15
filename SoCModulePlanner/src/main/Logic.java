package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import constant.Constant;
import object.FocusArea;
import object.ModuleInfo;
import storage.Storage;

public class Logic {
	private TreeMap<String, ModuleInfo> _moduleInfo;
	private HashMap<String, ArrayList<ArrayList<String>>> _modulePrereq;
	private HashMap<String, ArrayList<String>> _modulePreclusion;
	private HashMap<String, boolean[]> _moduleHistory;
	private HashMap<String, FocusArea> _focusArea;
	private HashMap<String, ArrayList<ArrayList<ArrayList<String>>>> _gradRequirement;
	
	public Logic() {
		_moduleInfo = Storage.getModuleInfo();
		_modulePrereq = Storage.getModulePrereq();
		_modulePreclusion = Storage.getModulePreclusion();
		_moduleHistory = Storage.getModuleHistory();
		_focusArea = Storage.getFocusArea();
		_gradRequirement = Storage.getGradRequirement();
	}
	
	public TreeMap<String, ModuleInfo> getModuleList() {
		return _moduleInfo;
	}
	
	public ArrayList<ArrayList<String>> getPlanner(String major, String focusArea, 
			ArrayList<String> modulesTaken, ArrayList<String> modulesWhitelist, 
			boolean isMathTaken, boolean isPhysicsTaken, boolean isFromPoly, int planSemester) {
		ArrayList<ArrayList<String>> planner = new ArrayList<ArrayList<String>>();
		
		FocusArea focusAreaSet = focusArea != null ? _focusArea.get(focusArea) : null;
		ArrayList<ArrayList<ArrayList<String>>> requirement = _gradRequirement.get(major);
		ArrayList<String> modulesToBeTaken = getModulesToBeTaken(requirement, modulesTaken);
		int credits = getCurrentCredits(modulesTaken, isFromPoly);
		
		for (String module: modulesToBeTaken) {
			System.out.println(module);
		}
		
		return planner;
	}
	
	private int getCurrentCredits(ArrayList<String> modulesTaken, boolean isFromPoly) {
		int credits = isFromPoly ? 20 : 0;
		
		for (String module: modulesTaken) { 
			credits += _moduleInfo.get(module).getCredits();
		}
		
		return credits;
	}
	
	/**
	 * Get modules to be taken after filtering from modules that has been taken
	 * @param requirement
	 * @param modulesTaken
	 * @return required modules that have not been taken
	 */
	private ArrayList<String> getModulesToBeTaken(ArrayList<ArrayList<ArrayList<String>>> requirement,
			ArrayList<String> modulesTaken) {
		ArrayList<String> modulesToBeTaken = new ArrayList<String>();
		
		for(ArrayList<ArrayList<String>> moduleSets: requirement) {
			if (moduleSets.size() == 1) { // for normal one set
				ArrayList<String> modules = moduleSets.get(0);
				for (String module: modules) {
					if (!modulesTaken.contains(module)) {
						modulesToBeTaken.add(module);
					}
				}
			} else { 		// for EQUIVALENCE sets
				int oneTakenIndex = 0;
				
				outerloop: 
				for (int i = 0; i < moduleSets.size(); i++) {
					ArrayList<String> modules = moduleSets.get(i);
					
					for (String module: modules) {
						if (modulesTaken.contains(module)) {
							oneTakenIndex = i;
							break outerloop;
						}
					}
				}
				
				ArrayList<String> matchedModules = moduleSets.get(oneTakenIndex);
				
				for (String module: matchedModules) {
					if (!modulesTaken.contains(module)) {
						modulesToBeTaken.add(module);
					}
				}
			}
		}
		
		return modulesToBeTaken;
	}
}
