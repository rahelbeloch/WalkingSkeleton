package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class ExpectedAtLeastOneActionException extends InvalidWorkflowException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7564748501309710929L;
    public static final int ERRORCODE = 11570;
    
    /**
     * Constructor for the Exception.
     */
    public ExpectedAtLeastOneActionException() {
        super("Every workflow must contain at least one Step of type 'Action'.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public ExpectedAtLeastOneActionException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return ExpectedAtLeastOneActionException.ERRORCODE;
    }

}
