package main;

import java.util.TreeMap;

import object.ModuleInfo;
import storage.ModuleInfoStorage;

public class Logic {
	private TreeMap<String, ModuleInfo> _moduleList;
	
	public Logic() {
		_moduleList = ModuleInfoStorage.readFile();
	}
	
	public TreeMap<String, ModuleInfo> getModuleList() {
		return _moduleList;
	}
}
