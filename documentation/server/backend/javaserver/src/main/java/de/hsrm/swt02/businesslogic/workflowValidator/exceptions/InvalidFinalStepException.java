package de.hsrm.swt02.businesslogic.workflowValidator.exceptions;


/**
 * Exception for invalid Workflows.
 *
 */
public class InvalidFinalStepException extends InvalidWorkflowException {

    /**
     * 
     */
    private static final long serialVersionUID = 1253470141735230175L;
    public static final int ERRORCODE = 11530;
    
    /**
     * Constructor for the Exception.
     */
    public InvalidFinalStepException() {
        super("Final Steps must not have next steps.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public InvalidFinalStepException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return InvalidFinalStepException.ERRORCODE;
    }

}
