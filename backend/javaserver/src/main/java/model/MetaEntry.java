package model;

public class MetaEntry {
	// Used for (de)serialization. Do not change.
	private String key;
	
	// Used for (de)serialization. Do not change.
	private String value;
	
	// Used for (de)serialization. Do not change.
	private String group;
	
	public MetaEntry() {
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
