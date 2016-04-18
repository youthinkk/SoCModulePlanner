package common;

import java.util.ArrayList;
import java.util.HashMap;

public class FilteredRequirement {
	private ArrayList<String> _modulesToBeTaken;
	private HashMap<String, ArrayList<String>> _prereq;
	private boolean _isPrimaries4000Taken = false;
	private int _amountOf4000ToBeTaken = 0;
	
	// For Computer Science
	public FilteredRequirement(ArrayList<String> modulesToBeTaken, 
			HashMap<String, ArrayList<String>> prereq,
			boolean isPrimaries4000Taken, 
			int amountOf4000ToBeTaken) {
		_modulesToBeTaken = modulesToBeTaken;
		_prereq = prereq;
		_isPrimaries4000Taken = isPrimaries4000Taken;
		_amountOf4000ToBeTaken = amountOf4000ToBeTaken;
	}
	
	public ArrayList<String> getModulesToBeTaken() {
		return _modulesToBeTaken;
	}
	
	public HashMap<String, ArrayList<String>> getPrereq() {
		return _prereq;
	}
	
	public boolean isPrimaries4000Taken() {
		return _isPrimaries4000Taken;
	}
	
	public int getAmountOf4000ToBeTaken() {
		return _amountOf4000ToBeTaken;
	}
}
