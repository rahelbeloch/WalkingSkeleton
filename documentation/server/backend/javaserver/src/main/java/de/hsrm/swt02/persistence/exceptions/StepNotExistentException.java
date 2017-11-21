package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception for StepNotExistentExceptionhandling.
 */
public class StepNotExistentException extends NotExistentException {

    private static final long serialVersionUID = -8607483435331612341L;
    public static final int ERRORCODE = 11254;

    /**
     * Constructor for the Exception.
     */
    public StepNotExistentException() {
        super("Der Workflow existiert nicht.");
    }

    /**
     * Method for managing the error message.
     * @param msg error message
     */
    public StepNotExistentException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the Errorcode.
     * @return errorcode self explanatory
     */
    public int getErrorCode() {
        return StepNotExistentException.ERRORCODE;
    }
}
