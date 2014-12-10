package de.hsrm.swt02.model;

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
     * @param username name of user responsible for action
     * @param description short description of action
     */
    public Action(String username, String description) {
        this.username = username;
        this.description = description;
    }
    
    public void init(Action s) {
        super.init(s);
        this.description = s.getDescription();
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
     * Deep Copy - Cloning method for Actions
     */
    protected Object clone() throws CloneNotSupportedException {
        Action clone = new Action();
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
