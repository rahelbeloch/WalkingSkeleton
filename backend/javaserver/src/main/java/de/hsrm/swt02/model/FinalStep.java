package de.hsrm.swt02.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents a Final Step. A final step is a manifestation of a Step.
 *
 */
public class FinalStep extends Step {
    
    @JsonIgnore
    private static final long serialVersionUID = 5753195185732582026L;

    /**
    * Constructor for FinalStep.
    */
    public FinalStep() {
        super();
    }
    
    public FinalStep(ArrayList<String> roleIds) {
        this.roleIds = roleIds;
    }
    
    /**
     * Init method.
     * @param a is the final step we want to init
     */
    public void init(Action a) {
        super.init(a);
    }
    
    /**
     * Deep Copy - Cloning method for FinalSteps.
     * @exception CloneNotSupportedException convention
     * @throws CloneNotSupportedException
     * @return clone is the requested clone of the FinalStep
     */
    public Object clone() throws CloneNotSupportedException {
        final FinalStep clone = new FinalStep();
        clone.init(this);
        return clone;
    }
}
