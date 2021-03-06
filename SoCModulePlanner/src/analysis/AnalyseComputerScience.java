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

public class AnalyseComputerScience implements IAnalysis {
	
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
	
	private HashMap<String, ArrayList<String>> _modulesToBeTakenPrereq = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> _modulesToBeTaken = new ArrayList<String>();
	private HashSet<String> _modulesFulfillRequirement = new HashSet<String>();
	private boolean _isPrimaries4000Taken = false;
	private int _amountOf4000Taken = 0;
	private int _amountOfPrimariesTaken = 0;
	private int _amountOf4000PrimariesTaken = 0;
	
	private final String FUNDEMENTAL_MODULE_MATH = "MA1301";
	private final String FUNDEMENTAL_MODULE_PHYSICS1 = "PC1221";
	private final String FUNDEMENTAL_MODULE_PHYSICS2 = "PC1222";
	
	public AnalyseComputerScience(TreeMap<String, ModuleInfo> moduleInfo, 
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
		countAmountOf4000Taken();
		analyseModulesFulfillment();
		filterCoreModules();
		filterFocusAreaPrimaries();
		filterScienceGemSs();
		filterPolyExemption();
		filterMathPhysics();
		filterWhitelistPreclusion();
		addWhitelistPrereq();
		forDemo();
		analyseModulesToBeTakenPrereq();
		fulfillCsBreadthDepth();
		//forDemo();
	}
	
	private void countAmountOf4000Taken() {
		for (String module: _modulesTaken) {
			ModuleInfo info = _moduleInfo.get(module);
			
			if (info.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE) && info.getLevel() >= 4000) {
				_amountOf4000Taken += 1;
			}
			
			// check if the module is one of the focus area primaries
			if (isPrimaries(module, _focusArea.getPrimaries())) {
				_amountOfPrimariesTaken += 1;
				
				// check if the primaries is 4000
				if (!_isPrimaries4000Taken) {
					_isPrimaries4000Taken = _moduleInfo.get(module).getLevel() == 4000;
				}
				
				if (_moduleInfo.get(module).getLevel() == 4000) {
					_amountOf4000PrimariesTaken += 1;
				}
			} 
		}
	}
	
	private void analyseModulesFulfillment() {
		for (String module: _modulesTaken) {
			ArrayList<String> prereq = getPrereq(module);
			_modulesFulfillRequirement.addAll(prereq);
		}
	}
	
	private void filterCoreModules() {
		for (ArrayList<ArrayList<String>> moduleSets: _requirement) {
			if (moduleSets.size() == 1) { // for normal one set
				ArrayList<String> modules = moduleSets.get(0);
				for (String module: modules) {
					if (!_modulesTaken.contains(module) && !_modulesFulfillRequirement.contains(module)) {
						_modulesToBeTaken.add(module);
					} else {
						removeModuleTaken(module);
					}
				}
			} else { 		// for EQUIVALENCE sets
				int oneTakenIndex = 0;
				int whitelistIndex = 0;
				boolean foundTaken = false;
				
				for (int i = 0; i < moduleSets.size(); i++) {
					ArrayList<String> modules = moduleSets.get(i);
					
					for (String module: modules) {
						if (_modulesTaken.contains(module)) {
							oneTakenIndex = !foundTaken ? i : oneTakenIndex;
							foundTaken = true;
						} else if (_modulesWhitelist.contains(module)) {
							whitelistIndex = i;
						}
					}
				}
				
				ArrayList<String> matchedModules = foundTaken ? moduleSets.get(oneTakenIndex) : moduleSets.get(whitelistIndex);
				
				for (String module: matchedModules) {
					if (!_modulesTaken.contains(module) && !_modulesFulfillRequirement.contains(module)) {
						_modulesToBeTaken.add(module);
					} else {
						removeModuleTaken(module);
					}
				}
				
				for (int i = 0; i < moduleSets.size(); i++) {
					if (i == oneTakenIndex) {
						continue;
					}
					
					_modulesFulfillRequirement.addAll(moduleSets.get(i));
				}
			}
		}
	}
	
	private void filterFocusAreaPrimaries() {
		int count = 3;
		int number = _amountOfPrimariesTaken;
		
		while (number > 0 && count > 0) {
			String name = "Primaries" + count;
			if (_modulesToBeTaken.contains(name)) {
				_modulesToBeTaken.remove(name);
			}
			number--;
			count--;
		}
	}
	
	private void filterScienceGemSs() {
		for (String module: _modulesTaken) {
			String type = _moduleInfo.get(module).getType();
			
			if (type.equals("GEM")) {
				if (_modulesToBeTaken.contains(Constant.REQUIREMENT_GEM2)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_GEM2);
					removeModuleTaken(module);
				} else if (_modulesToBeTaken.contains(Constant.REQUIREMENT_GEM1)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_GEM1);
					removeModuleTaken(module);
				}
			} else if (type.equals("Science")) {
				if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SCIENCE3)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SCIENCE3);
					removeModuleTaken(module);
				} else if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SCIENCE2)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SCIENCE2);
					removeModuleTaken(module);
				} else if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SCIENCE1)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SCIENCE1);
					removeModuleTaken(module);
				} 
			} else if (type.equals("SS")) {
				if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SS)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SS);
					removeModuleTaken(module);
				}
			}
		}
	}
	
	private void filterPolyExemption() {
		if (_isFromPoly) {
			if (_modulesToBeTaken.contains(Constant.REQUIREMENT_GEM2)) {
				_modulesToBeTaken.remove(Constant.REQUIREMENT_GEM2);
			} else if (_modulesToBeTaken.contains(Constant.REQUIREMENT_GEM1)) {
				_modulesToBeTaken.remove(Constant.REQUIREMENT_GEM1);
			}
		}
	}
	
	private void filterMathPhysics() {		
		if (_isMathTaken && _modulesToBeTaken.contains(FUNDEMENTAL_MODULE_MATH)) {
			_modulesToBeTaken.remove(FUNDEMENTAL_MODULE_MATH);
			_modulesFulfillRequirement.add(FUNDEMENTAL_MODULE_MATH);
		}
		
		if (_isPhysicsTaken && _modulesToBeTaken.contains(FUNDEMENTAL_MODULE_PHYSICS1)) {
			_modulesToBeTaken.remove(FUNDEMENTAL_MODULE_PHYSICS1);
			_modulesFulfillRequirement.add(FUNDEMENTAL_MODULE_PHYSICS1);
		}
		
		if (_isPhysicsTaken && _modulesToBeTaken.contains(FUNDEMENTAL_MODULE_PHYSICS2)) {
			_modulesToBeTaken.remove(FUNDEMENTAL_MODULE_PHYSICS2);
			_modulesFulfillRequirement.add(FUNDEMENTAL_MODULE_PHYSICS2);
		}
	}
	
	private void filterWhitelistPreclusion() {
		ArrayList<String> removeList = new ArrayList<String>();
		for (String module: _modulesWhitelist) {
			
			// remove MA1301 when A-level math is taken
			if (_isMathTaken && module.equals(FUNDEMENTAL_MODULE_MATH)) {
				removeList.add(module);
				continue;
			}
			
			// remove PC1221 or PC1222 when A-level physics is taken
			if (_isPhysicsTaken && (module.equals(FUNDEMENTAL_MODULE_PHYSICS1) || module.equals(FUNDEMENTAL_MODULE_PHYSICS2))) {
				removeList.add(module);
				continue;
			}
			
			if (_modulesToBeTaken.contains(module)) {
				removeList.add(module);
				continue;
			}
			
			ArrayList<String> preclusions = _modulePreclusion.containsKey(module) ? 
					_modulePreclusion.get(module) : new ArrayList<String>();
			
			for (String preclusion: preclusions) {
				if (_modulesToBeTaken.contains(preclusion) || 
						_modulesTaken.contains(preclusion) || 
						_modulesFulfillRequirement.contains(preclusion)) {
					removeList.add(module);
				}
			}
		}
		
		_modulesWhitelist.removeAll(removeList);
	}
	
	private void addWhitelistPrereq() {
		for (String module: _modulesWhitelist) {
			ArrayList<String> prereqList = getPrereq(module);
			
			//System.out.print("WHITE LIST " + module + ": ");
			for (String prereq: prereqList) {
				_modulesToBeTaken.add(prereq);
				//System.out.print(prereq + " ");
			}
			//System.out.println();
		}
	}
	
	private void analyseModulesToBeTakenPrereq() {
		for(String module: _modulesToBeTaken) {
			ArrayList<ArrayList<String>> prereqSets = _modulePrereq.get(module);
			ArrayList<String> toBeTakenPrereq = new ArrayList<String>();

			if (prereqSets != null) {
				int preferredIndex = 0;
				int bestMatch = 0;

				for (int i = 0; i < prereqSets.size(); i++) {
					ArrayList<String> prereqSet = prereqSets.get(i);
					int currentMatch = 0;

					for (String prereq: prereqSet) {
						if (_modulesToBeTaken.contains(prereq)) {
							//preferredIndex = i;
							currentMatch += 1;
						}
					}
					System.out.println("Current match: " + currentMatch);
					System.out.println("Best match: " + bestMatch);
					if (currentMatch > bestMatch) {
						bestMatch = currentMatch;
						preferredIndex = i;
					}
				}

				ArrayList<String> preferredPrereqSet = prereqSets.get(preferredIndex);
				
				//System.out.println("MODULE: " + module);
				for (String prereq: preferredPrereqSet) {
					//System.out.println("PREREQ: " + prereq);
					if (_modulesToBeTaken.contains(prereq)) {
						toBeTakenPrereq.add(prereq);
					}
				}
				//System.out.println();
			}
			
			_modulesToBeTakenPrereq.put(module, toBeTakenPrereq);
		}
		
		/*Set<Entry<String, ArrayList<String>>> entries = _modulesToBeTakenPrereq.entrySet();
		
		for(Entry<String, ArrayList<String>> entry: entries) {
			String module = entry.getKey();
			ArrayList<String> prereqSet = entry.getValue();
			
			System.out.print(module + ": ");
			for (String prereq: prereqSet) {
				System.out.print(prereq + " ");
			}
			System.out.println();
		}*/
	}
	
	private void fulfillCsBreadthDepth() {
		//_isPrimaries4000Taken;
		//_amountOf4000Taken;
		//_amountOfPrimariesTaken;
		ArrayList<String> primaries = _focusArea.getPrimaries();
		ArrayList<String> electives = _focusArea.getElectives();
		int amountOfCsButNotCoreTaken = 0;
		for (String module: _modulesTaken) {
			ModuleInfo info = _moduleInfo.get(module);
			
			if (info.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE) && 
					!_focusArea.getPrimaries().contains(module) && 
					!primaries.contains(module)) {
				amountOfCsButNotCoreTaken += 1;
			}
			
		}
		
		int amountOf4000ToBeTaken = (_amountOf4000Taken > 3) ? 0 : (3 - _amountOf4000Taken);
		int amountOfPrimariesToBeTaken = (_amountOfPrimariesTaken > 3) ? 0 : (3 - _amountOfPrimariesTaken);
		
		if (!_isPrimaries4000Taken) {
			
		}
	}
	
	private ArrayList<String> getPrereq(String module) {		
		ArrayList<String> prereqList = new ArrayList<String>();
		if (!_modulePrereq.containsKey(module) &&
				!_modulesTaken.contains(module) && 
				!_modulesFulfillRequirement.contains(module) &&
				!_modulesToBeTaken.contains(module)) {
			
			prereqList.add(module);
			return prereqList;
		}
		
		if (!_modulesTaken.contains(module) && 
				!_modulesFulfillRequirement.contains(module) &&
				!_modulesToBeTaken.contains(module)) {
			prereqList.add(module);
		} 
		
		ArrayList<ArrayList<String>> prereqSets = _modulePrereq.get(module);
		
		if (prereqSets == null) return prereqList;
		
		int preferredIndex = 0;
		
		outerloop:
		for (int i = 0; i < prereqSets.size(); i++) {
			ArrayList<String> prereqSet = prereqSets.get(i);
			
			for (String prereq: prereqSet) {
				if (_modulesToBeTaken.contains(prereq) ||
						_modulesTaken.contains(prereq) || 
						_modulesFulfillRequirement.contains(prereq)) {
					preferredIndex = i;
					break outerloop;
				}
			}
		}
		
		ArrayList<String> preferredPrereqSet = prereqSets.get(preferredIndex);
		
		for (String prereq: preferredPrereqSet) {
			if (!_modulesTaken.contains(prereq) && 
					!_modulesFulfillRequirement.contains(prereq) &&
					!_modulesToBeTaken.contains(prereq)) {
				prereqList.addAll(getPrereq(prereq));
			}
		}
		
		return prereqList;
	}
	
	private void removeModuleTaken(String module) {
		_modulesTaken.remove(module);
		_modulesFulfillRequirement.add(module);
	}
	
	private boolean isPrimaries(String module, ArrayList<String> primaries) {
		return primaries.contains(module);
	}
	
	private void forDemo() {
		int count = 3;
		
		while (count > 0) {
			String name = "Primaries" + count;
			if (_modulesToBeTaken.contains(name)) {
				_modulesToBeTaken.remove(name);
			}
			
			name = "Electives" + count;
			if (_modulesToBeTaken.contains(name)) {
				_modulesToBeTaken.remove(name);
			}
			count--;
		}
		
		ArrayList<String> primaries = _focusArea.getPrimaries();
		ArrayList<String> electives = _focusArea.getElectives();
		
		int amountOfCsButNotCoreTaken = 0;
		for (String module: _modulesTaken) {
			ModuleInfo info = _moduleInfo.get(module);
			
			if (info.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE) && 
					!primaries.contains(module)) {
				amountOfCsButNotCoreTaken += 1;
			}	
		}
		
		count = 0;
		for (int i = 0; i < primaries.size() && count < 3; i++) {
			String module = primaries.get(i);
			
			if (!_moduleInfo.containsKey(module)) continue;
			
			if (_modulesTaken.contains(module) && _modulesFulfillRequirement.contains(module)) continue;
			ArrayList<String> prereqSet = getPrereq(module);
			
			for (String prereq: prereqSet) {
				if (!_modulesToBeTaken.contains(prereq)) {
					_modulesToBeTaken.add(prereq);
				}
				
				if (!_moduleInfo.containsKey(prereq)) continue;
				
				if (!_modulesTaken.contains(prereq) && 
						_moduleInfo.get(prereq).getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE)) {
					amountOfCsButNotCoreTaken += 1;
				}
			}
			count++;
		}
		
		int amountOfElectivesToBeTaken = (amountOfCsButNotCoreTaken > 3) ? 0 : (3 - amountOfCsButNotCoreTaken);
		
		count = 0;
		for (int i = 0; i < electives.size() && count < 3; i++) {
			String module = electives.get(i);
			
			if (!_moduleInfo.containsKey(module)) continue;
			
			if (_modulesTaken.contains(module) && _modulesFulfillRequirement.contains(module)) continue;
			ArrayList<String> prereqSet = getPrereq(module);
			
			for (String prereq: prereqSet) {
				if (!_modulesToBeTaken.contains(prereq)) {
					_modulesToBeTaken.add(prereq);
				}
			}
			count++;
		}
		//for (int i)
	}

	public Analysis getResult() {
		int amountOf4000ToBeTaken = (_amountOf4000Taken > 3) ? 0 : (3 - _amountOf4000Taken);
		return new Analysis(_modulesToBeTaken, _modulesToBeTakenPrereq, _isPrimaries4000Taken, amountOf4000ToBeTaken);
	}
}
