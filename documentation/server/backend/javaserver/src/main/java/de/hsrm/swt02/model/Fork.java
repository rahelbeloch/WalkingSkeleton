package de.hsrm.swt02.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents a Fork. Similar to Step Fork is an manifestation of a Step.
 *
 */
public class Fork extends Step {
    
    @JsonIgnore
    private static final long serialVersionUID = 769271551770870735L;
    
    // Used for (de)-serialization. Do not change.
    private String script;

    /**
     * Getter for a Forks Python Script.
     * @return script as String
     */
    public String getScript() {
        return script;
    }

    /**
     * Setter for a Forks Python Script.
     * @param script as String
     */
    public void setScript(String script) {
        this.script = script;
    }

    /**
     * Constructor for Action without parameters.
     */
    public Fork() {
        super();
    }

    /**
     * Constructor for Fork with parameters.
     * @param roleIds ids if the responsible roles for action
     */
    public Fork(ArrayList<String> roleIds) {
        this.roleIds = roleIds;
    }
    
    /**
     * Getter for branch 'true'.
     * @return next step is for TrueBranch
     */
    public Step getTrueBranch() {
        return this.nextSteps.get(0);
    }
    
    /**
     * Getter for branch 'false'.
     * @return next step is for FalseBranch
     */
    public Step getFalseBranch() {
        return this.nextSteps.get(1);
    }
    
    /**
     * Init method.
     * @param fork is the Fork we want to init
     */
    public void init(Fork fork) {
        super.init(fork);
        this.script = fork.getScript();
    }
    
    /**
     * Deep Copy - Cloning method for Forks.
     * @exception CloneNotSupportedException clone convention
     * @throws CloneNotSupportedException
     * @return clone is the clone of the Fork
     */
    public Object clone() throws CloneNotSupportedException {
        final Fork clone = new Fork();
        clone.init(this);
        return clone;
    }
    
    @Override
    public String toString() {
        String ret = "";
        ret = super.toString();
        ret += "\tScript: " + this.script + "\n";
        
        return ret;
    }
}
