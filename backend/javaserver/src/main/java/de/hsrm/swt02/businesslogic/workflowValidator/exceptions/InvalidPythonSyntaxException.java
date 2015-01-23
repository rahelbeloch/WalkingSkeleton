package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class InvalidPythonSyntaxException extends InvalidWorkflowException {
    
	/**
     * 
     */
	private static final long serialVersionUID = -7558990513253418543L;
    public static final int ERRORCODE = 11580;
    
    /**
     * Constructor for the Exception.
     */
    public InvalidPythonSyntaxException() {
        super("Python syntax is incorrect.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public InvalidPythonSyntaxException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return InvalidPythonSyntaxException.ERRORCODE;
    }

}
