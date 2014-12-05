package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception for when the requested role is not there.
 */
public class RoleNotExistentException extends NotExistentException {

    private static final long serialVersionUID = 2095701291993027112L;
    public static final int ERRORCODE = 11253;

    /**
     * Constructor for the Exception.
     */
    public RoleNotExistentException() {
        super();
    }

    /**
     * Method for managing the error message.
     * @param msg error message
     */
    public RoleNotExistentException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the Errorcode.
     * @return errorcode self explanatory
     */
    public int getErrorCode() {
        return RoleNotExistentException.ERRORCODE;
    }
}
