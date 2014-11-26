package de.hsrm.swt02.model;

public class Action extends Step {
	// Used for (de)serialization. Do not change.
	private String description;
	
	public Action() {
		super();
	}
	
	public Action(int id, String username, String description) {
		this.id = id;
		this.username = username;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
