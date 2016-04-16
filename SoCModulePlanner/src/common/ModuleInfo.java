package common;

public class ModuleInfo {
	private String _code;
	private String _name;
	private String _department;
	private String _type;
	private int _level;
	private int _credits;
	
	public ModuleInfo() { }
	
	public ModuleInfo(String code, String name, String department, String type, int level, int credits) {
		_code = code;
		_name = name;
		_department = department;
		_type = type;
		_level = level;
		_credits = credits;
	}
	
	public String getCode() {
		return _code;
	}
	
	public void setCode(String code) {
		_code = code;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getDepartment() {
		return _department;
	}
	
	public void setDepartment(String department) {
		_department = department;
	}
	
	public String getType() {
		return _type;
	}
	
	public void setType(String type) {
		_type = type;
	}
	
	public int getLevel() {
		return _level;
	}
	
	public void setLevel(int level) {
		_level = level;
	}
	
	public int getCredits() {
		return _credits;
	}
	
	public void setCredits(int credits) {
		_credits = credits;
	}
}
