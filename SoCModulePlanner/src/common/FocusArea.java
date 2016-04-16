package common;

import java.util.ArrayList;

public class FocusArea {
	private String _name;
	private ArrayList<String> _primaries;
	private ArrayList<String> _electives;
	
	public FocusArea() { }
	
	public FocusArea(String name, ArrayList<String> primaries, ArrayList<String> electives) {
		_name = name;
		_primaries = primaries;
		_electives = electives;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public ArrayList<String> getPrimaries() {
		return _primaries;
	}
	
	public void setPrimaries(ArrayList<String> primaries) {
		_primaries = primaries;
	}
	
	public ArrayList<String> getElectives() {
		return _electives;
	}
	
	public void setElectives(ArrayList<String> electives) {
		_electives = electives;
	}
}
