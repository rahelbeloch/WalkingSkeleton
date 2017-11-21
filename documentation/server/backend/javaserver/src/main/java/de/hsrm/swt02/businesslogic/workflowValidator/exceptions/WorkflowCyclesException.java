package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class WorkflowCyclesException extends InvalidWorkflowException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4216904831941280754L;
    public static final int ERRORCODE = 11510;
    
    /**
     * Constructor for the Exception.
     */
    public WorkflowCyclesException() {
        super("Cycles are not allowed.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public WorkflowCyclesException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return WorkflowCyclesException.ERRORCODE;
    }

}
