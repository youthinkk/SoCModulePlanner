package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import object.ModuleInfo;
import storage.Storage;

public class Logic {
	private TreeMap<String, ModuleInfo> _moduleInfo;
	private HashMap<String, ArrayList<ArrayList<String>>> _modulePrereq;
	private HashMap<String, ArrayList<String>> _modulePreclusion;
	private HashMap<String, boolean[]> _moduleHistory;
	
	public Logic() {
		_moduleInfo = Storage.getModuleInfo();
		_modulePrereq = Storage.getModulePrereq();
		_modulePreclusion = Storage.getModulePreclusion();
		_moduleHistory = Storage.getModuleHistory();
	}
	
	public TreeMap<String, ModuleInfo> getModuleList() {
		return _moduleInfo;
	}
}
