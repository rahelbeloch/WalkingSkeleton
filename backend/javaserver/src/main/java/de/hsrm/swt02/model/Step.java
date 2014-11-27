package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

public class Step {
	// Used for (de)serialization. Do not change.
	protected int id;
	
	// Used for (de)serialization. Do not change.
	protected List<Step> nextSteps;
	
	// Used for (de)serialization. Do not change.
	protected String username = "noname";
	
	public Step() {
	
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public List<Step> getNextSteps() {
        if (nextSteps == null) {
            nextSteps = new ArrayList<Step>();
        }
        return this.nextSteps;
    }
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
}
