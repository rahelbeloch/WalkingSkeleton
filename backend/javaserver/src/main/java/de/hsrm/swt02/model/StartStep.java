package de.hsrm.swt02.model;

/**
 * This class represents a StartStep. A StartStep is a manifestation of a Step.
 */
public class StartStep extends Step {

    /**
    * Constructor for StartStep without parameters.
    */
    public StartStep() {
        super();
    }

    /**
     * Constructor for StartStep with parameters.
     * @param username is the username of the user responsible for the startstep
     */
    public StartStep(String username) {
        super();
        this.username = username;
    }
    
    /**
     * Deep Copy - Cloning method for Actions
     */
    protected Object clone() throws CloneNotSupportedException {
        StartStep clone = new StartStep();
        clone.init(this);
        return clone;
    }
}
