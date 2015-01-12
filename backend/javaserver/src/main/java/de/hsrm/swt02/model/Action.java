package de.hsrm.swt02.model;

import java.util.ArrayList;

/**
 * This class represents an Action. An Action is an manifestation of a Step.
 *
 */
public class Action extends Step {
    // Used for (de)-serialization. Do not change.
    private String description;

    /**
     * Constructor for Action without parameters.
     */
    public Action() {
        super();
    }

    /**
     * Constructor for Action with parameters.
     * @param roleIds ids if the responsible roles for action
     * @param description short description of action
     */
    public Action(ArrayList<String> roleIds, String description) {
        this.roleIds = roleIds;
        this.description = description;
    }
    
    /**
     * Init method.
     * @param a is the action we want to init
     */
    public void init(Action a) {
        super.init(a);
        this.description = a.getDescription();
    }

    /**
     * Description getter.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description setter.
     * @param description short description of action
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Deep Copy - Cloning method for Actions.
     * @exception CloneNotSupportedException clone convention
     * @throws CloneNotSupportedException
     * @return clone is the clone of the action
     */
    public Object clone() throws CloneNotSupportedException {
        final Action clone = new Action();
        clone.init(this);
        return clone;
    }
    
    @Override
    public String toString() {
        String ret = "";
        ret = super.toString();
        ret += "\tBeschreibung: " + this.description + "\n";
        
        return ret;
    }
}
