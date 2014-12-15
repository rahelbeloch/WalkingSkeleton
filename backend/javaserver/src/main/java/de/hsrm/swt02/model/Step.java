package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This class represents a Step.
 * 
 * The JsonTypeInfo and JsonSubTypes annotations assure that 
 * the (de)serialization process of derived types works correctly.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "$type")
@JsonSubTypes({@Type(value = StartStep.class, name = "StartStep"), @Type(value = Action.class, name = "Action"), @Type(value = FinalStep.class, name = "FinalStep")})
public class Step extends RootElement {

    @JsonIgnore
    protected List<Step> nextSteps;

    // Used for (de)serialization. Do not change.
    protected List<String> nextStepIds;
    
    // Used for (de)serialization. Do not change.
    protected String username;
    
    // Used for (de)serializsation. Do not change.
    protected String rolename;

    /**
     * Constructor for Step.
     */
    public Step() {
        this.username = "";
    }
    
    /**
     * Init method for cloning process.
     * @param s is the step we want to clone
     */
    public void init(Step s) {
        super.init(s);
        this.username = s.username;
        this.rolename = s.rolename;
        
        this.getNextStepIds().addAll(s.getNextStepIds());
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
     * NextStepIds getter: when there is no nextStepIds yet, a new ArrayList will be returned.
     * @return nextStepIds
     */
    public List<String> getNextStepIds() {
        if (nextStepIds == null) {
            nextStepIds = new ArrayList<String>();
        }
        
        return this.nextStepIds;
    }

    /**
     * Username getter.
     * @return username is the username of the user responsible for the step
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Username setter.
     * @param username is the username of the user responsible for the step
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Rolename getter.
     * @return rolename is the name of the role responsible for the step
     */
    public String getRolename() {
        return this.rolename;
    }
    
    /**
     * Rolename setter.
     * @param rolename is the name of the user responsible for the step
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
    
    /**
     * Deep Copy - Cloning method for Steps.
     * @exception CloneNotSupportedException convention
     * @throws CloneNotSupportedException
     * @return clone is the requested clone
     */
    public Object clone() throws CloneNotSupportedException {
        final Step clone = new Step();
        clone.init(this);

        return clone;
    }
    
    @Override
    public String toString() {
        String ret = "";
        ret += "\tType: " + this.getClass().getName() + "\n";
        ret += "\tId: " + this.id + "\n";
        ret += "\tUsername: " + this.username + "\n";
        ret += "\tNextStepIds: " + this.nextStepIds + "\n";
        
        return ret;
    }
}
