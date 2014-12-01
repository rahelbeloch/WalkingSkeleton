package de.hsrm.swt02.model;

/**
 * This class represents a StartStep. A StartStep is a manifestation of a Step.
 */
public class StartStep extends Step {
	
	/**
	 * Constructor for StartStep without parameters
	 */
    public StartStep() {
        super();
    }

    /**
     * Constructor for StartStep with parameters
     * @param username
     */
    public StartStep(String username) {
        super();
        this.username = username;
    }
}
