package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception if we want to add an user to a role and he's already there.
 */
public class RoleHasAlreadyUserException extends AlreadyExistsException {

    private static final long serialVersionUID = -926705890263823593L;
    public static final int ERRORCODE = 1124;
    
    /**
     * Constructor for the Exception.
     */
    public RoleHasAlreadyUserException() {
        super();
    }
    
    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public RoleHasAlreadyUserException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return RoleHasAlreadyUserException.ERRORCODE;
    }

}
