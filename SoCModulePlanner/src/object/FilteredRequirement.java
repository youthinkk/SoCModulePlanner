package object;

import java.util.ArrayList;

public class FilteredRequirement {
	private ArrayList<String> _modulesToBeTaken;
	private boolean _isPrimaries4000Taken = false;
	private int _amountOf4000ToBeTaken = 0;
	
	// For Computer Science
	public FilteredRequirement(ArrayList<String> modulesToBeTaken, boolean isPrimaries4000Taken, 
			int amountOf4000ToBeTaken) {
		_modulesToBeTaken = modulesToBeTaken;
		_isPrimaries4000Taken = isPrimaries4000Taken;
		_amountOf4000ToBeTaken = amountOf4000ToBeTaken;
	}
	
	public ArrayList<String> getModulesToBeTaken() {
		return _modulesToBeTaken;
	}
	
	public boolean isPrimaries4000Taken() {
		return _isPrimaries4000Taken;
	}
	
	public int getAmountOf4000ToBeTaken() {
		return _amountOf4000ToBeTaken;
	}
}
