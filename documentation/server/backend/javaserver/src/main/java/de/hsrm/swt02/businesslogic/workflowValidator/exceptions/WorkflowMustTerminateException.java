package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class WorkflowMustTerminateException extends InvalidWorkflowException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3343859633762614235L;
    public static final int ERRORCODE = 11520;
    
    /**
     * Constructor for the Exception.
     */
    public WorkflowMustTerminateException() {
        super("Every branch of the workflow must conclude with a Final Step.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public WorkflowMustTerminateException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return WorkflowMustTerminateException.ERRORCODE;
    }

}
