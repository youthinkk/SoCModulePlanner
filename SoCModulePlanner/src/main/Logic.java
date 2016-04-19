package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import analysis.AnalyseComputerScience;
import analysis.AnalysisCS;
import analysis.IAnalysis;
import common.Analysis;
import common.FocusArea;
import common.KeyValue;
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
	
	private String _regex = "(\\d{4})";
	private Pattern _pattern = Pattern.compile(_regex);
	private Matcher _matcher;
	
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
		_clips.watch();
	}
	
	public TreeMap<String, ModuleInfo> getModuleList() {
		return _moduleInfo;
	}
	
	public TreeMap<Integer, ArrayList<KeyValue>> getPlanner(String major, String focusArea, 
			ArrayList<String> modulesTaken, ArrayList<String> modulesWhitelist, 
			boolean isMathTaken, boolean isPhysicsTaken, boolean isFromPoly, int planSemester) {
		TreeMap<Integer, ArrayList<KeyValue>> planner = new TreeMap<Integer, ArrayList<KeyValue>>();
		
		FocusArea focusAreaSet = focusArea != null ? _focusArea.get(focusArea) : null;
		ArrayList<ArrayList<ArrayList<String>>> requirement = _gradRequirement.get(major);
		int credits = getCurrentCredits(modulesTaken, isFromPoly);
		IAnalysis analysis = null;
		
		/*switch (major) {
			case Constant.MAJOR_COMPUTER_SCIENCE:
				analysis = new AnalyseComputerScience(_moduleInfo, _modulePrereq, _modulePreclusion, 
						requirement, focusAreaSet, modulesTaken, modulesWhitelist, isMathTaken, isPhysicsTaken, isFromPoly);
				break;
		}*/
		
		analysis = new AnalysisCS(_moduleInfo, _modulePrereq, _modulePreclusion, 
				requirement, focusAreaSet, modulesTaken, modulesWhitelist, isMathTaken, isPhysicsTaken, isFromPoly);
		
		
		if (analysis != null) {
			Analysis result = analysis.getResult();
			ArrayList<String> modulesToBeTaken = result.getModulesToBeTaken();
			HashMap<String, ArrayList<String>> modulesToBeTakenPrereq = result.getPrereq();
			
			Collections.sort(modulesToBeTaken, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					_matcher = _pattern.matcher(o1);
					boolean isMatch1 = _matcher.find();
					
					_matcher = _pattern.matcher(o2);
					boolean isMatch2 = _matcher.find();
					
					if (isMatch1 && !isMatch2) {
						return -1;
					} else if (!isMatch1 && isMatch2) {
						return 1;
					}
					
					return o1.compareTo(o2);
				}
				
			});
			
			Collections.reverse(modulesToBeTaken);
			
			for (String module: modulesToBeTaken) {
				ModuleInfo info = _moduleInfo.get(module);
				
				if (info != null) {
					_clips.assertModule(module, info.getLevel(), info.getCredits(), 
							"", modulesToBeTakenPrereq.get(module), _moduleHistory.get(module));
				} else {
					_clips.assertModule(module, 1000, 4, 
							"", new ArrayList<String>(), new boolean[] {true, true, false, false});
				}
			}
			
			_clips.assertManagement(planSemester, modulesToBeTaken.size(), credits);
			_clips.run();
			planner = _clips.getPlannedModules(_moduleInfo);
			_clips.showFacts();
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
