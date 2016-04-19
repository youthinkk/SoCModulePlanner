package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import common.Analysis;
import common.FocusArea;
import common.ModuleInfo;
import constant.Constant;

public class AnalysisCS implements IAnalysis {
	private final String FUNDEMENTAL_MODULE_MATH = "MA1301";
	private final String FUNDEMENTAL_MODULE_PHYSICS1 = "PC1221";
	private final String FUNDEMENTAL_MODULE_PHYSICS2 = "PC1222";
	
	private TreeMap<String, ModuleInfo> _moduleInfo;
	private HashMap<String, ArrayList<ArrayList<String>>> _modulePrereq;
	private HashMap<String, ArrayList<String>> _modulePreclusion;
	private ArrayList<ArrayList<ArrayList<String>>> _requirement;
	private ArrayList<String> _modulesTaken;
	private ArrayList<String> _modulesWhitelist;
	private FocusArea _focusArea;
	private boolean _isMathTaken;
	private boolean _isPhysicsTaken;
	private boolean _isFromPoly;
	
	private ArrayList<String> _planModules = new ArrayList<String>();
	
	// Classification of modules that are taken
	private ArrayList<String> _coreModulesTaken = new ArrayList<String>();
	private ArrayList<String> _primariesModulesTaken = new ArrayList<String>();
	private ArrayList<String> _otherCSModulesTaken = new ArrayList<String>();
	private ArrayList<String> _unclassifiedModulesTaken = new ArrayList<String>();
	
	// Classification of planned white list modules
	private ArrayList<String> _primariesModulesWhitelist = new ArrayList<String>();
	private ArrayList<String> _otherCSModulesWhitelist = new ArrayList<String>();
	private ArrayList<String> _unclassifiedModulesWhitelist = new ArrayList<String>();
	
	private HashSet<String> _prereqOfModulesTaken = new HashSet<String>(); // The modules that have been fulfilled of modules taken
	private HashSet<String> _equivalenceOfPlannedOrTakenModules = new HashSet<String>();
	
	// Prerequisite of modules to be planned
	private HashMap<String, ArrayList<String>> _planModulesPrereq = new HashMap<String, ArrayList<String>>();
	
	private int _numberOfScienceTaken = 0;
	
	public AnalysisCS(TreeMap<String, ModuleInfo> moduleInfo, 
			HashMap<String, ArrayList<ArrayList<String>>> modulePrereq, 
			HashMap<String, ArrayList<String>> modulePreclusion,
			ArrayList<ArrayList<ArrayList<String>>> requirement, FocusArea focusArea, 
			ArrayList<String> modulesTaken, ArrayList<String> modulesWhitelist, 
			boolean isMathTaken, boolean isPhysicsTaken, boolean isFromPoly) {
		
		_moduleInfo = moduleInfo;
		_modulePrereq = modulePrereq;
		_modulePreclusion = modulePreclusion;
		_requirement = requirement;
		_modulesTaken = modulesTaken;
		_modulesWhitelist = modulesWhitelist;
		_focusArea = focusArea;
		_isMathTaken = isMathTaken;
		_isPhysicsTaken = isPhysicsTaken;
		_isFromPoly = isFromPoly;
		
		analyse();
	}
	
	private void analyse() {
		classifyModulesTaken();
		analyseFulfillmentOfModulesTaken();
		planCoreModules();
		planWhitelistModules();
		planBreadthDepth();
		maintainCorequisite();
		analysePlanModulePrereq();
	}
	
	private void classifyModulesTaken() {
		classifyCoreTaken();
		classifyOthersTaken();
		printTakenClassification();
	}
	
	private void analyseFulfillmentOfModulesTaken() {
		for (String module: _modulesTaken) {
			_prereqOfModulesTaken.addAll(getAllPrereq(module));
			
			_equivalenceOfPlannedOrTakenModules.add(module);
			if (_modulePreclusion.containsKey(module)) {
				_equivalenceOfPlannedOrTakenModules.addAll(_modulePreclusion.get(module));
			}
		}
		
		for (String prereq: _prereqOfModulesTaken) {
			if (_modulePreclusion.containsKey(prereq)) {
				_equivalenceOfPlannedOrTakenModules.addAll(_modulePreclusion.get(prereq));
			}
		}
		printFulfillmentOfModulesTaken();
	}
	
	private void planCoreModules() {
		for (ArrayList<ArrayList<String>> requirementSet: _requirement) {
			int bestMatch = 0;
			int preferredIndex = 0;
			int whitelistIndex = 0;
			boolean isTaken = false;
			
			// Find the most suitable set if there are 2 sets or more
			for (int i = 0; i < requirementSet.size(); i++) {
				int currentMatch = 0;
				ArrayList<String> modules = requirementSet.get(i);
				
				for (String module: modules) {
					if (_prereqOfModulesTaken.contains(module) ||
							_coreModulesTaken.contains(module)) {
						currentMatch += 1;
						isTaken = true;
					} else if (_modulesWhitelist.contains(module)) {
						whitelistIndex = i;
					}
				}
				
				if (currentMatch > bestMatch) {
					bestMatch = currentMatch;
					preferredIndex = i;
				}
			}
			
			// Add those modules in preferred set to the plan list
			ArrayList<String> modules = isTaken ? requirementSet.get(preferredIndex) : requirementSet.get(whitelistIndex);
			
			for (String module: modules) {
				if (!isCoreRequirementFulfilled(module)) {
					_planModules.add(module);
					updateEquivalence(module);
				}
			}
		}
		
		// Add number of science to be taken
		int numberToBeTaken = _numberOfScienceTaken > 3 ? 0 : (3 - _numberOfScienceTaken);
		for (int i = 0; i < numberToBeTaken; i++) {
			_planModules.add(Constant.REQUIREMENT_SCIENCE + (i + 1));
		}
		
		// If the user is from polytechnic, remove one GEM
		if (_isFromPoly && _planModules.contains(Constant.REQUIREMENT_GEM)) {
			_planModules.remove(Constant.REQUIREMENT_GEM);
		}
		
		printPlanModules("planCoreModules");
	}
	
	private void planWhitelistModules() {
		outerloop:
		for (String module: _modulesWhitelist) {
			ArrayList<String> preclusions = _modulePreclusion.get(module);
			
			if (preclusions != null) {
				for (String preclusion: preclusions) {
					// If any preclusion is found taken or planned, 
					// then this whitelist module will be excluded
					if (_planModules.contains(preclusion) ||
							_modulesTaken.contains(preclusion)) {
						continue outerloop;
					}
				}
			}
			
			HashSet<String> unfulfilledPrereq = getUnfulfilledPrereq(module);
			
			for (String prereq: unfulfilledPrereq) {
				addPlanWhitelist(prereq);
			}
			
			addPlanWhitelist(module);
		}
	
		printPlanModules("planWhitelistModules");
		printWhitelistClassification();
	}
	
	private void planBreadthDepth() {
		HashSet<String> combinedPrimaries = new HashSet<String>();
		HashSet<String> combinedOtherCSModules = new HashSet<String>();
		ArrayList<String> primaries = _focusArea.getPrimaries();
		ArrayList<String> electives = _focusArea.getElectives();
		
		int primaries4000 = 0;
		int total4000 = 0;
		
		combinedPrimaries.addAll(_primariesModulesTaken);
		combinedPrimaries.addAll(_primariesModulesWhitelist);
		combinedOtherCSModules.addAll(_otherCSModulesTaken);
		combinedOtherCSModules.addAll(_otherCSModulesWhitelist);
		
		for (String module: _planModules) {
			if (primaries.contains(module)) {
				combinedPrimaries.add(module);
			}
		}
		
		// Update planned primaries
		for (String module: combinedPrimaries) {
			ModuleInfo info = _moduleInfo.get(module);
			
			if (info.getLevel() >= 4000) {
				primaries4000 += 1;
				total4000 += 1;
			}
		}
		
		if (primaries4000 == 0) {
			// Plan one 4000 primary
			for (String primary: primaries) {
				ModuleInfo info = _moduleInfo.get(primary);
				if (info == null) continue;
				if (!combinedPrimaries.contains(primary) && 
						info.getLevel() >= 4000) {
					HashSet<String> prereqList = getUnfulfilledPrereq(primary);
					
					_planModules.add(primary);
					_planModules.addAll(prereqList);
					
					// Update combined list
					combinedPrimaries.add(primary);
					updateEquivalence(primary);
					
					for (String prereq: prereqList) {
						updateEquivalence(prereq);
						
						ModuleInfo prereqInfo = _moduleInfo.get(prereq);
						if (prereqInfo == null) continue;
						
						if (primaries.contains(prereq)) {
							combinedPrimaries.add(prereq);
						} else if (prereqInfo.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE)) {
							combinedOtherCSModules.add(prereq);
						}
					}
					
					primaries4000 += 1;
					total4000 += 1;
					break;
				}
			}	
		}
		
		// Plan other primaries
		if (combinedPrimaries.size() < 3) {
			for (String primary: primaries) {
				ModuleInfo info = _moduleInfo.get(primary);
				if (info == null) continue;
				if (!combinedPrimaries.contains(primary)) {
					HashSet<String> prereqList = getUnfulfilledPrereq(primary);

					_planModules.add(primary);
					_planModules.addAll(prereqList);

					// Update combined list
					combinedPrimaries.add(primary);
					updateEquivalence(primary);

					for (String prereq: prereqList) {
						updateEquivalence(prereq);

						ModuleInfo prereqInfo = _moduleInfo.get(prereq);
						if (prereqInfo == null) continue;

						if (primaries.contains(prereq)) {
							combinedPrimaries.add(prereq);
						} else if (prereqInfo.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE)) {
							combinedOtherCSModules.add(prereq);
						}
					}

					primaries4000 += info.getLevel() >= 4000 ? 1 : 0;
					total4000 += info.getLevel() >= 4000 ? 1 : 0;
				}
				if (combinedPrimaries.size() >= 3) break;
			}	
		}
		
		for (String module: combinedOtherCSModules) {
			ModuleInfo info = _moduleInfo.get(module);
			
			if (info == null) continue;
			
			if (info.getLevel() >= 4000) {
				total4000 += 1;
			}
		}
		
		if (total4000 < 3) {
			for (String elective: electives) {
				ModuleInfo info = _moduleInfo.get(elective);
				
				if (info == null) continue;
				
				if (!combinedOtherCSModules.contains(elective) && 
						info.getLevel() >= 4000) {
					HashSet<String> prereqList = getUnfulfilledPrereq(elective);
					
					_planModules.add(elective);
					_planModules.addAll(prereqList);
					
					// Update combined list
					combinedOtherCSModules.add(elective);
					updateEquivalence(elective);
					
					for (String prereq: prereqList) {
						updateEquivalence(prereq);
						
						ModuleInfo prereqInfo = _moduleInfo.get(prereq);
						if (prereqInfo == null) continue;
						
						if (prereqInfo.getLevel() >= 4000) {
							total4000 += 1;
						}
						
						if (prereqInfo.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE)) {
							combinedOtherCSModules.add(prereq);
						}
					}
					
					total4000 += 1;
					
					if (total4000 >= 3) break;
				}
			}
		}
		
		if (combinedOtherCSModules.size() < 3) {
			for (String elective: electives) {
				ModuleInfo info = _moduleInfo.get(elective);
				
				if (info == null) continue;
				
				if (!combinedOtherCSModules.contains(elective)) {
					HashSet<String> prereqList = getUnfulfilledPrereq(elective);
					
					_planModules.add(elective);
					_planModules.addAll(prereqList);
					
					// Update combined list
					combinedOtherCSModules.add(elective);
					updateEquivalence(elective);
					
					for (String prereq: prereqList) {
						updateEquivalence(prereq);
						
						ModuleInfo prereqInfo = _moduleInfo.get(prereq);
						if (prereqInfo == null) continue;
						
						if (prereqInfo.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE)) {
							combinedOtherCSModules.add(prereq);
						}
					}
				}
				
				if (combinedOtherCSModules.size() >= 3) break;
			}
		}
		
		System.out.println("OTHER CS " + combinedOtherCSModules.size());
		for (String module: combinedOtherCSModules) {
			System.out.println(module);
		}
		
		
		this.printPlanModules("planBreadthDepth");
	}
	
	private void maintainCorequisite() {
		ArrayList<String> addList = new ArrayList<String>();
		
		for (String module: _planModules) {
			String corequisite = getCorequisite(module);
			
			if (!_planModules.contains(corequisite) && !corequisite.isEmpty()) {
				addList.add(corequisite);
			}
		}
		
		_planModules.addAll(addList);
		
		if (_planModules.contains("CS2103") && _planModules.contains("CS2103T")) {
			while (_planModules.contains("CS2103")) {
				_planModules.remove("CS2103");
			}
		}
		
		printPlanModules("maintainCorequisite");
	}
	
	private void analysePlanModulePrereq() {
		for (String module: _planModules) {
			HashSet<String> prereqList = getAllPrereq(module);
			ArrayList<String> prereqInPlanModules = new ArrayList<String>();
			
			for (String prereq: prereqList) {
				if (_planModules.contains(prereq)) {
					prereqInPlanModules.add(prereq);
				}
			}
			
			_planModulesPrereq.put(module, prereqInPlanModules);
		}
		
		printPlanModulePrereq();
	}
	
	private void classifyCoreTaken() {
		for (ArrayList<ArrayList<String>> requirementSet: _requirement) {			
			for (ArrayList<String> modules: requirementSet) {
				for (String module: modules) {
					
					if (_modulesTaken.contains(module)) {
						_coreModulesTaken.add(module);
						continue;
					}
				}
			}
		}
	}
	
	
	private void classifyOthersTaken() {
		ArrayList<String> primaries = _focusArea.getPrimaries();
				
		int gemCount = 0;
		int ssCount = 0;
		int scienceCount = 0;
		
		for (String module: _modulesTaken) {
			ModuleInfo info = _moduleInfo.get(module);
			String type = info.getType();
			String department = info.getDepartment();
			
			if (primaries.contains(module)) {
				_primariesModulesTaken.add(module);
				continue;
			}
			
			if (type.equals(Constant.REQUIREMENT_GEM) && !module.equals("GEK1901") && gemCount < 1) {
				_coreModulesTaken.add(Constant.REQUIREMENT_GEM);
				gemCount++;
				continue;
			} else if (type.equals(Constant.REQUIREMENT_SS) && ssCount < 1) {
				_coreModulesTaken.add(Constant.REQUIREMENT_SS);
				ssCount++;
				continue;
			} else if (type.equals(Constant.REQUIREMENT_SCIENCE) && scienceCount < 3) {
				_coreModulesTaken.add(Constant.REQUIREMENT_SCIENCE);
				scienceCount++;
				_numberOfScienceTaken = scienceCount;
				continue;
			}
			
			if (_coreModulesTaken.contains(module) || _primariesModulesTaken.contains(module)) continue;
			
			if (department.equals(Constant.DEPARTMENT_COMPUTER_SCIENCE)) {
				_otherCSModulesTaken.add(module);
			} else {
				_unclassifiedModulesTaken.add(module);
			}
		}
	}
	
	
	private HashSet<String> getAllPrereq(String module) {
		HashSet<String> prereqList = getAllPrereqHelper(module);
		prereqList.remove(module);
		//printModuleAllPrereq(module, prereqList);
		return prereqList;
	}
	
	
	private HashSet<String> getAllPrereqHelper(String module) {
		HashSet<String> prereqList = new HashSet<String>();
		
		if (!_modulePrereq.containsKey(module)) {
			prereqList.add(module);
			return prereqList;
		}
		
		ArrayList<ArrayList<String>> prereqSets = _modulePrereq.get(module);
		
		for (ArrayList<String> prereqSet: prereqSets) {
			for (String prereq: prereqSet) {
				prereqList.addAll(getAllPrereqHelper(prereq));
			}
		}
		
		prereqList.add(module);
		
		return prereqList;
	}
	
	
	private HashSet<String> getUnfulfilledPrereq(String module) {
		HashSet<String> prereqList = getUnfulfilledPrereqHelper(module);
		prereqList.remove(module);
		printUnfulfilledPrereq(module, prereqList);
		return prereqList;
	}
	
	
	private HashSet<String> getUnfulfilledPrereqHelper(String module) {
		HashSet<String> prereqList = new HashSet<String>();
		
		if (!_modulePrereq.containsKey(module)) {
			if (!isFulfilledAtCurrentState(module)) {
				prereqList.add(module);
			}
			return prereqList;
		}
		
		ArrayList<ArrayList<String>> prereqSets = _modulePrereq.get(module);
		int bestMatch = 0;
		int preferredIndex = 0;
		
		for (int i = 0; i < prereqSets.size(); i++) {
			int currentMatch = 0;
			
			for (ArrayList<String> prereqSet: prereqSets) {
				for (String prereq: prereqSet) {
					if (isFulfilledAtCurrentState(prereq)) {
						currentMatch += 1;
					}
				}
			}
			
			if (currentMatch > bestMatch) {
				bestMatch = currentMatch;
				preferredIndex = i;
			}
		}
		
		ArrayList<String> preferredSet = prereqSets.get(preferredIndex);
		
		for (String prereq: preferredSet) {
			if (!isFulfilledAtCurrentState(prereq)) {
				prereqList.addAll(getUnfulfilledPrereqHelper(prereq));
			}
		}
		
		prereqList.add(module);
		
		return prereqList;
	}
	
	// Infinite loop
	
	private HashSet<String> getAllPreclusion(String module) {
		HashSet<String> preclusionList = getAllPreclusionHelper(module);
		preclusionList.remove(module);
		printModuleAllPreclusion(module, preclusionList);
		return preclusionList;
	}
	
	
	private HashSet<String> getAllPreclusionHelper(String module) {
		HashSet<String> preclusionList = new HashSet<String>();
		
		if (!_modulePreclusion.containsKey(module)) {
			preclusionList.add(module);
			return preclusionList;
		}
		
		ArrayList<String> preclusionSet = _modulePreclusion.get(module);
		
		for (String preclusion: preclusionSet) {
			preclusionList.addAll(getAllPreclusionHelper(preclusion));
		}
		
		preclusionList.add(module);
		
		return preclusionList;
	}
	
	
	private boolean isCoreRequirementFulfilled(String module) {
		// Science module will be analysed at other parts
		if (module.equals(Constant.REQUIREMENT_SCIENCE)) return true;
		
		if (!_coreModulesTaken.contains(module) && !_prereqOfModulesTaken.contains(module)) {
			
			if (module.equals(FUNDEMENTAL_MODULE_MATH) && _isMathTaken) {
				updateEquivalence(FUNDEMENTAL_MODULE_MATH);
				return true;
			}
			
			if (module.equals(FUNDEMENTAL_MODULE_PHYSICS1) && _isPhysicsTaken) {
				updateEquivalence(FUNDEMENTAL_MODULE_PHYSICS1);
				return true;
			}
			
			if (module.equals(FUNDEMENTAL_MODULE_PHYSICS2) && _isPhysicsTaken) {
				updateEquivalence(FUNDEMENTAL_MODULE_PHYSICS2);
				return true;
			}
			
			return false;
		}
		return true;
	}
	
	
	private boolean isFulfilledAtCurrentState(String module) {		
		return _modulesTaken.contains(module) || 
				_planModules.contains(module) ||
				//_prereqOfModulesTaken.contains(module) || 
				_equivalenceOfPlannedOrTakenModules.contains(module);
	}
	
	
	private void updateEquivalence(String module) {
		HashSet<String> prereqList = getAllPrereq(module);
		_equivalenceOfPlannedOrTakenModules.add(module);
		_equivalenceOfPlannedOrTakenModules.addAll(prereqList);
		
		if (_modulePreclusion.containsKey(module)) {
			_equivalenceOfPlannedOrTakenModules.addAll(_modulePreclusion.get(module));
		}
		
		for (String prereq: prereqList) {
			if (_modulePreclusion.containsKey(prereq)) {
				_equivalenceOfPlannedOrTakenModules.addAll(_modulePreclusion.get(prereq));
			}
		}
		
		printEquivalence();
	}
	
	
	private void addPlanWhitelist(String module) {
		_planModules.add(module);
		updateEquivalence(module);
		
		ArrayList<String> primaries = _focusArea.getPrimaries();
		
		if (primaries.contains(module)) {
			_primariesModulesWhitelist.add(module);
			return;
		}
		
		ModuleInfo info = _moduleInfo.get(module);	
		if (info != null) {
			String department = info.getDepartment();
			String type = info.getType();
			
			if (department.equals(Constant.DEPARTMENT_COMPUTER_SCIENCE)) {
				_otherCSModulesWhitelist.add(module);
			} else if (type.equals(Constant.REQUIREMENT_SS)) {
				if (!_planModules.remove(Constant.REQUIREMENT_SS)) {
					_unclassifiedModulesWhitelist.add(module);
				}
			} else if (type.equals(Constant.REQUIREMENT_GEM)) {
				if (!_planModules.remove(Constant.REQUIREMENT_GEM)) {
					_unclassifiedModulesWhitelist.add(module);
				}
			} else if (type.equals(Constant.REQUIREMENT_SCIENCE)){
				boolean isRemoved = false;
				for (int i = 3; i > 0 ; i--) {
					String code = Constant.REQUIREMENT_SCIENCE + i;
					if (_planModules.remove(code)) {
						isRemoved = true;
						break;
					}
				}
				
				if (!isRemoved) {
					_unclassifiedModulesWhitelist.add(module);
				}
			} else {
				_unclassifiedModulesWhitelist.add(module);
			}
		}
	}
	
	
	private String getCorequisite(String module) {
		switch (module) {
			case "CS3201":
				return "CS3202";
			case "CS3202":
				return "CS3201";
			case "CS3281":
				return "CS3282";
			case "CS3282":
				return "CS3281";
			case "CS2101":
				return "CS2103T";
			case "CS2103T":
				return "CS2101";
			default:
				return "";
		}
	}
	
	private void printTakenClassification() {
		System.out.println("\nMODULES TAKEN CLASSIFICATION");
		System.out.print("Core modules: ");
		for (String module: _coreModulesTaken) {
			System.out.print(module + " ");
		}
		System.out.println();
		
		System.out.print("Primaries modules: ");
		for (String module: _primariesModulesTaken) {
			System.out.print(module + " ");
		}
		System.out.println();
		
		System.out.print("Other CS modules: ");
		for (String module: _otherCSModulesTaken) {
			System.out.print(module + " ");
		}
		System.out.println();
		
		System.out.print("Unclassfied modules: ");
		for (String module: _unclassifiedModulesTaken) {
			System.out.print(module + " ");
		}
		System.out.println();
	}
	
	
	private void printWhitelistClassification() {
		System.out.println("\nMODULES WHITELIST CLASSIFICATION");
		
		System.out.print("Primaries modules: ");
		for (String module: _primariesModulesWhitelist) {
			System.out.print(module + " ");
		}
		System.out.println();
		
		System.out.print("Other CS modules: ");
		for (String module: _otherCSModulesWhitelist) {
			System.out.print(module + " ");
		}
		System.out.println();
		
		System.out.print("Unclassfied modules: ");
		for (String module: _unclassifiedModulesWhitelist) {
			System.out.print(module + " ");
		}
		System.out.println();
	}
	
	
	private void printModuleAllPrereq(String module, HashSet<String> prereqList) {
		System.out.println();
		System.out.println("Prerequisites of " +  module);
		
		for (String prereq: prereqList) {
			System.out.print(prereq + " ");
		}
		System.out.println();
	}	
	
	private void printModuleAllPreclusion(String module, HashSet<String> preclusionList) {
		System.out.println();
		System.out.println("Preclusion of " +  module);
		
		for (String preclusion: preclusionList) {
			System.out.print(preclusion + " ");
		}
		System.out.println();
	}
	
	
	private void printFulfillmentOfModulesTaken() {
		System.out.println();
		System.out.println("All fulfillment of modules taken: ");
		for (String module: _prereqOfModulesTaken) {
			System.out.print(module + " ");
		}
		System.out.println();
	}
	
	
	private void printEquivalence() {
		System.out.println();
		System.out.println("All equivalence of modules taken or planned: ");
		for (String module: _equivalenceOfPlannedOrTakenModules) {
			System.out.print(module + " ");
		}
		System.out.println();
	}
	
	
	private void printPlanModules(String func) {
		System.out.println();
		System.out.println("Plan modules after " + func + ": ");
		
		for (String module: _planModules) {
			System.out.print(module + " ");
		}
		System.out.println();
	}
	
	
	private void printUnfulfilledPrereq(String module, HashSet<String> prereqList) {
		System.out.println();
		System.out.println("Unfulfilled prerequisites of " +  module);
		
		for (String prereq: prereqList) {
			System.out.print(prereq + " ");
		}
		System.out.println();
	}
	
	
	private void printPlanModulePrereq() {
		Set<Entry<String, ArrayList<String>>> entries = _planModulesPrereq.entrySet();
		
		System.out.println();
		System.out.println("MODULES PREREQUISITE");
		for(Entry<String, ArrayList<String>> entry: entries) {
			String module = entry.getKey();
			ArrayList<String> prereqSet = entry.getValue();
			
			System.out.print(module + ": ");
			for (String prereq: prereqSet) {
				System.out.print(prereq + " ");
			}
			System.out.println();
		}
	}
	
	public Analysis getResult() {
		Analysis analysis = new Analysis(_planModules, _planModulesPrereq);
		return analysis;
	}
}
