package de.hsrm.swt02.businesslogic.exceptions;

/**
 * Exception dealing with whenever something is requested and it's alread there.
 */
public class UserHasNoPermissionException extends NoPermissionException {
    
    private static final long serialVersionUID = -2596215157765836553L;
    public static final int ERRORCODE = 11310;
    
    /**
     * Constructor for the Exception.
     */
    public UserHasNoPermissionException() {
        super("given user is not the assigned user");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public UserHasNoPermissionException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return UserHasNoPermissionException.ERRORCODE;
    }
}
