package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class ExpectedOneStartStepException extends InvalidWorkflowException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 7520194686046741678L;
    public static final int ERRORCODE = 11550;
    
    /**
     * Constructor for the Exception.
     */
    public ExpectedOneStartStepException() {
        super("Every workflow must contain exactly one Step of type 'StartStep'.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public ExpectedOneStartStepException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return ExpectedOneStartStepException.ERRORCODE;
    }

}
