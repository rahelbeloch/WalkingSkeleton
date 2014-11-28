package de.hsrm.swt02.model;

public class User extends RootElement {
	// Used for (de)serialization. Do not change.
	private String username;
	
	public User() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}