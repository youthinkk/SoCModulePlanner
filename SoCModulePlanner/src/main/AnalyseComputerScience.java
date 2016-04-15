package main;

import java.util.ArrayList;
import java.util.TreeMap;

import constant.Constant;
import object.FilteredRequirement;
import object.FocusArea;
import object.ModuleInfo;

public class AnalyseComputerScience implements IAnalyseMajor {
	
	private TreeMap<String, ModuleInfo> _moduleInfo;
	private ArrayList<ArrayList<ArrayList<String>>> _requirement;
	private ArrayList<String> _modulesTaken;
	private ArrayList<String> _modulesWhitelist;
	private FocusArea _focusArea;
	private boolean _isMathTaken;
	private boolean _isPhysicsTaken;
	private boolean _isFromPoly;
	
	private ArrayList<String> _modulesToBeTaken = new ArrayList<String>();
	private boolean _isPrimaries4000Taken = false;
	private int _amountOf4000Taken = 0;
	private int _amountOfPrimariesTaken = 0;
	
	public AnalyseComputerScience(TreeMap<String, ModuleInfo> moduleInfo,
			ArrayList<ArrayList<ArrayList<String>>> requirement, FocusArea focusArea, 
			ArrayList<String> modulesTaken, ArrayList<String> modulesWhitelist, 
			boolean isMathTaken, boolean isPhysicsTaken, boolean isFromPoly) {
		_moduleInfo = moduleInfo;
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
		filterCoreModules();
		filterFocusAreaPrimaries();
		filterScienceGemSs();
		filterPolyExemption();
		filterMathPhysics();
	}
	
	private void countAmountOf4000Taken() {
		for (String module: _modulesTaken) {
			ModuleInfo info = _moduleInfo.get(module);
			
			if (info.getDepartment().equals(Constant.DEPARTMENT_COMPUTER_SCIENCE) && info.getLevel() >= 4000) {
				_amountOf4000Taken += 1;
			}
			
			System.out.println(isPrimaries(module, _focusArea.getPrimaries()));
			// check if the module is one of the focus area primaries
			if (isPrimaries(module, _focusArea.getPrimaries())) {
				_amountOfPrimariesTaken += 1;
				
				// check if the primaries is 4000
				if (!_isPrimaries4000Taken) {
					_isPrimaries4000Taken = _moduleInfo.get(module).getLevel() == 4000;
				}
			} 
		}
	}
	
	private void filterCoreModules() {
		for (ArrayList<ArrayList<String>> moduleSets: _requirement) {
			if (moduleSets.size() == 1) { // for normal one set
				ArrayList<String> modules = moduleSets.get(0);
				for (String module: modules) {
					if (!_modulesTaken.contains(module)) {
						_modulesToBeTaken.add(module);
					} else {
						_modulesTaken.remove(module);
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
					if (!_modulesTaken.contains(module)) {
						_modulesToBeTaken.add(module);
					} else {
						_modulesTaken.remove(module);
					}
				}
			}
		}
	}
	
	private void filterFocusAreaPrimaries() {
		int count = 3;
		
		while (_amountOfPrimariesTaken > 0 && count > 0) {
			String name = "Primaries" + count;
			if (_modulesToBeTaken.contains(name)) {
				_modulesToBeTaken.remove(name);
			}
			_amountOfPrimariesTaken--;
			count--;
		}
	}
	
	private void filterScienceGemSs() {
		for (String module: _modulesTaken) {
			String type = _moduleInfo.get(module).getType();
			
			if (type.equals("GEM")) {
				if (_modulesToBeTaken.contains(Constant.REQUIREMENT_GEM2)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_GEM2);
					_modulesTaken.remove(module);
				} else if (_modulesToBeTaken.contains(Constant.REQUIREMENT_GEM1)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_GEM1);
					_modulesTaken.remove(module);
				}
			} else if (type.equals("Science")) {
				if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SCIENCE3)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SCIENCE3);
					_modulesTaken.remove(module);
				} else if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SCIENCE2)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SCIENCE2);
					_modulesTaken.remove(module);
				} else if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SCIENCE1)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SCIENCE1);
					_modulesTaken.remove(module);
				} 
			} else if (type.equals("SS")) {
				if (_modulesToBeTaken.contains(Constant.REQUIREMENT_SS)) {
					_modulesToBeTaken.remove(Constant.REQUIREMENT_SS);
					_modulesTaken.remove(module);
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
		String mathModule = "MA1301";
		String physicsModule1 = "PC1221";
		String physicsModule2 = "PC1222";
		
		if (_isMathTaken && _modulesToBeTaken.contains(mathModule)) {
			_modulesToBeTaken.remove(mathModule);
		}
		
		if (_isPhysicsTaken && _modulesToBeTaken.contains(physicsModule1)) {
			_modulesToBeTaken.remove(physicsModule1);
		}
		
		if (_isPhysicsTaken && _modulesToBeTaken.contains(physicsModule2)) {
			_modulesToBeTaken.remove(physicsModule2);
		}
	}
	
	private boolean isPrimaries(String module, ArrayList<String> primaries) {
		return primaries.contains(module);
	}

	public FilteredRequirement getModulesToBeTaken() {
		
		return new FilteredRequirement(_modulesToBeTaken, _isPrimaries4000Taken, _amountOf4000Taken);
	}

}
