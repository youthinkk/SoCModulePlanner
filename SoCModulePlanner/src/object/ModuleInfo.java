package object;

public class ModuleInfo {
	private String _code;
	private String _name;
	private int _modularCredits;
	
	public ModuleInfo() { }
	
	public ModuleInfo(String code, String name, int modularCredits) {
		_code = code;
		_name = name;
		_modularCredits = modularCredits;
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
	
	public int getModularCredits() {
		return _modularCredits;
	}
	
	public void setModularCredits(int modularCredits) {
		_modularCredits = modularCredits;
	}
}
