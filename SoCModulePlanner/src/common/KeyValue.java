package common;

public class KeyValue {
	private String _key;
	private String _value;
	
	public KeyValue(String key, String value) {
		_key = key;
		_value = value;
	}
	
	public String getKey() {
		return _key;
	}
	
	public void setKey(String key) {
		_key = key;
	}
	
	public String getValue() {
		return _value;
	}
	
	public void setValue(String value) {
		_value = value;
	}
	
	@Override
	public String toString() {
		return _value;
	}
}
