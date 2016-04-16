package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import analysis.AnalyseComputerScience;
import analysis.IAnalyseMajor;
import common.FocusArea;
import common.ModuleInfo;
import constant.Constant;
import storage.Storage;

public class Logic {
	private TreeMap<String, ModuleInfo> _moduleInfo;
	private HashMap<String, ArrayList<ArrayList<String>>> _modulePrereq;
	private HashMap<String, ArrayList<String>> _modulePreclusion;
	private HashMap<String, boolean[]> _moduleHistory;
	private HashMap<String, FocusArea> _focusArea;
	private HashMap<String, ArrayList<ArrayList<ArrayList<String>>>> _gradRequirement;
	
	private String _plannerCondition;
	private ClipsBuilder _clips;
	
	public Logic() {
		
		_moduleInfo = Storage.getModuleInfo();
		_modulePrereq = Storage.getModulePrereq();
		_modulePreclusion = Storage.getModulePreclusion();
		_moduleHistory = Storage.getModuleHistory();
		_focusArea = Storage.getFocusArea();
		_gradRequirement = Storage.getGradRequirement();
		_plannerCondition = Storage.getPlannerCondition();
		_clips = new ClipsBuilder();
		
		_clips.setCondition(_plannerCondition);
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
		int credits = getCurrentCredits(modulesTaken, isFromPoly);
		IAnalyseMajor analyseMajor = null;
		
		switch (major) {
			case Constant.MAJOR_COMPUTER_SCIENCE:
				analyseMajor = new AnalyseComputerScience(_moduleInfo, _modulePrereq, _modulePreclusion, 
						requirement, focusAreaSet, modulesTaken, modulesWhitelist, isMathTaken, isPhysicsTaken, isFromPoly);
				break;
		}
		
		if (analyseMajor != null) {
			ArrayList<String> modulesToBeTaken = analyseMajor.getModulesToBeTaken().getModulesToBeTaken();

			for (String module: modulesToBeTaken) {
				System.out.println(module);
			}
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
}
