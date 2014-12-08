package de.hsrm.swt02.model;

/**
 * This class represents a Final Step. A final step is a manifestation of a Step.
 *
 */
public class FinalStep extends Step {
    /**
    * Constructor for FinalStep.
    */
    public FinalStep() {
        super();
    }
    
    /**
     * Deep Copy - Cloning method for FinalSteps
     */
    protected Object clone() throws CloneNotSupportedException {
        FinalStep clone = (FinalStep) super.clone();
        return clone;
    }
}
