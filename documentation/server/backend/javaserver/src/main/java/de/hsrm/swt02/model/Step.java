package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
@JsonSubTypes({@Type(value = StartStep.class, name = "StartStep"), @Type(value = Action.class, name = "Action"), 
    @Type(value = FinalStep.class, name = "FinalStep"), @Type(value = Fork.class, name = "Fork")})
public class Step extends RootElement {

    @JsonIgnore
    private static final long serialVersionUID = 7421352527667358620L;
    
    @JsonIgnore
    protected List<Step> nextSteps;

    // Used for (de)serialization. Do not change.
    protected List<String> nextStepIds;
    
    // Used for (de)serialization. Do not change.
    protected ArrayList<String> roleIds;

    // Used for graphical display in admin client
    protected double top;

    // Used for graphical display in admin client
    protected double left;
    
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
        this.left = s.getLeft();
        this.top = s.getTop();
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
     * @param rolename the role to add
     */
    public void addRole(String rolename) {
        this.roleIds.add(rolename);
    }
    
    /**
     * This method check if the role list of step contains a role e. g. from a user.
     * @param roleList role collection of e. g. a user
     * @return true if there is a match else false
     */
    public boolean containsRole(Collection<Role> roleList) {
        if (roleList != null) {
            for (String roleids : roleIds) {
                for (Role role : roleList) {
                    if (role.getRolename().equals(roleids)) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    /**
     * Top Getter.
     * @return top is the value to display on y axis
     */
    public double getTop() {
        return top;
    }

    /**
     * Top Setter.
     * @param top is the value to display on y axis
     */
    public void setTop(double top) {
        this.top = top;
    }
    
    /**
     * Left Getter.
     * @return left is the value to display on x axis
     */
    public double getLeft() {
        return left;
    }

    /**
     * Left Setter.
     * @param left is the value to display on x axis
     */
    public void setLeft(double left) {
        this.left = left;
    }
}