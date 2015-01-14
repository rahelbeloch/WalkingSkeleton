package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final long serialVersionUID = 7421352527667358620L;
    
    @JsonIgnore
    protected List<Step> nextSteps;

    // Used for (de)serialization. Do not change.
    protected List<String> nextStepIds;
    
    // Used for (de)serialization. Do not change.
    protected ArrayList<String> roleIds;

    /**
     * Constructor for Step.
     */
    public Step() {
        this.roleIds = new ArrayList<String>();
    }
    
    /**
     * Init method for cloning process.
     * @param s is the step we want to clone
     */
    public void init(Step s) {
        super.init(s);
        this.roleIds = s.roleIds;
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
    public ArrayList<String> getRoleIds() {
        if (roleIds == null) {
            this.roleIds = new ArrayList<String>();
        }
        return this.roleIds;
    }
    
    /**
     * RoleIds setter.
     * @param roleIds list of roleIds to set
     */
    public void setRoleIds(ArrayList<String> roleIds) {
        this.roleIds = roleIds;
    }
    
    /**
     * Username setter.
     * @param role the role to add
     */
    public void addRole(String rolename) {
        this.roleIds.add(rolename);
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
        super.clone();

        return clone;
    }
    
    @Override
    public String toString() {
        String ret = "";
        ret += "\tType: " + this.getClass().getSimpleName() + "\n";
        ret += "\tId: " + this.id + "\n";
        ret += "\tRollen: " + Arrays.toString(this.getRoleIds().toArray()) + "\n";
        ret += "\tNextStepIds: " + this.nextStepIds + "\n";
        
        return ret;
    }
}