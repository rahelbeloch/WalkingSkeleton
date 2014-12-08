package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception if we want to add a role to an user and he's already there.
 */
public class UserHasAlreadyRoleException extends AlreadyExistsException {

    private static final long serialVersionUID = -2109634252673983754L;
    public static final int ERRORCODE = 11263;
    
    /**
     * Constructor for the Exception.
     */
    public UserHasAlreadyRoleException() {
        super("Der Nutzer hat diese Rolle schon.");
    }
    
    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public UserHasAlreadyRoleException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return UserHasAlreadyRoleException.ERRORCODE;
    }

}
