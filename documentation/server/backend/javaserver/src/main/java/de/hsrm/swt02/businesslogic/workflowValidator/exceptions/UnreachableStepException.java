package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class UnreachableStepException extends InvalidWorkflowException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1230368580081990474L;
    public static final int ERRORCODE = 11540;
    
    /**
     * Constructor for the Exception.
     */
    public UnreachableStepException() {
        super("At least one step ist not correctly connected with the workflow.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public UnreachableStepException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return UnreachableStepException.ERRORCODE;
    }

}
