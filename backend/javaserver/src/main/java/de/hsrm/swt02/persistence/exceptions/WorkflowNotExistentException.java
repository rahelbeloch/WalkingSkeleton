package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception for when the requested workflow is not there.
 */
public class WorkflowNotExistentException extends NotExistentException {

    private static final long serialVersionUID = 6105983074536990369L;
    public static final int ERRORCODE = 11252;

    /**
     * Constructor for the Exception.
     */
    public WorkflowNotExistentException() {
        super();
    }

    /**
     * Method for managing the error message.
     * @param msg error message
     */
    public WorkflowNotExistentException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the Errorcode.
     * @return errorcode self explanatory
     */
    public int getErrorCode() {
        return WorkflowNotExistentException.ERRORCODE;
    }
}
