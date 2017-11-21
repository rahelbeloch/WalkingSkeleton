package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class ExpectedAtLeastOneFinalStepException extends InvalidWorkflowException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -8141116118634015829L;
    public static final int ERRORCODE = 11560;
    
    /**
     * Constructor for the Exception.
     */
    public ExpectedAtLeastOneFinalStepException() {
        super("Every workflow must contain at least one Step of type 'FinalStep'.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public ExpectedAtLeastOneFinalStepException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return ExpectedAtLeastOneFinalStepException.ERRORCODE;
    }

}
