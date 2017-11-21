package de.hsrm.swt02.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents a StartStep. A StartStep is a manifestation of a Step.
 */
public class StartStep extends Step {

    @JsonIgnore
    private static final long serialVersionUID = 1L;

    /**
    * Constructor for StartStep without parameters.
    */
    public StartStep() {
        super();
    }

    /**
     * Constructor for StartStep with parameters.
     * @param roleIds are the ids of the roles responsible for the startstep
     */
    public StartStep(ArrayList<String> roleIds) {
        super();
        this.roleIds = roleIds;
    }
    
    /**
     * Deep Copy - Cloning method for Actions.
     * @exception CloneNotSupportedException convention
     * @throws CloneNotSupportedException
     * @return clone is the 
     */
    public Object clone() throws CloneNotSupportedException {
        final StartStep clone = new StartStep();
        clone.init(this);
        return clone;
    }
}
