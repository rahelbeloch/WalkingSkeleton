package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Step.
 */
public class Step {
    // Used for (de)serialization. Do not change.
    protected int id;

    // Used for (de)serialization. Do not change.
    protected List<Step> nextSteps;

    // Used for (de)serialization. Do not change.
    protected String username = "noname";

    /**
     * Constructor for Step
     */
    public Step() {

    }

    /**
     * Id getter
     * @return id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Id setter
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * NextSteps getter: when there is no nextSteps yet, a new ArrayList will be returned.
     * @return nextSteps
     */
    public List<Step> getNextSteps() {
        if (nextSteps == null) {
            nextSteps = new ArrayList<Step>();
        }
        return this.nextSteps;
    }

    /**
     * Username getter
     * @return username
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Username setter
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
