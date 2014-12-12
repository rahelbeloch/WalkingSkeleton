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
