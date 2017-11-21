package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;

/**
 * Exception for invalid Workflows.
 *
 */
public class InvalidWorkflowException extends LogicException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 58097128894333109L;
    public static final int ERRORCODE = 11500;
    
    /**
     * Constructor for the Exception.
     */
    public InvalidWorkflowException() {
        super("Workflow is not valid.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public InvalidWorkflowException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return InvalidWorkflowException.ERRORCODE;
    }

}
