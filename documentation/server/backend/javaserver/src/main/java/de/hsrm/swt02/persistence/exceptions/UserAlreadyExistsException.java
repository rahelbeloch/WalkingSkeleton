package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception dealing with whenever an user is requested and it's already there.
 */
public class UserAlreadyExistsException extends AlreadyExistsException {

    private static final long serialVersionUID = -3331198130589019219L;
    public static final int ERRORCODE = 11221;
    
    /**
     * Constructor for the Exception.
     */
    public UserAlreadyExistsException() {
        super("Der Nutzer existiert bereits.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the error message
     */
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
    
    /**
     * ErrorCode Getter.
     * @return ERRORCODE is the code
     */
    public int getErrorCode() {
        return UserAlreadyExistsException.ERRORCODE;
    }
}
