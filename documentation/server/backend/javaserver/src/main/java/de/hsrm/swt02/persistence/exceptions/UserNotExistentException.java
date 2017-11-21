package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception dealing with whenever a user is requested and it's not there.
 */
public class UserNotExistentException extends NotExistentException {

    private static final long serialVersionUID = -7867137682701372781L;
    public static final int ERRORCODE = 11251;
    
    /**
     * Constructor for the Exception.
     */
    public UserNotExistentException() {
        super("Der Nutzer existiert nicht.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the error message
     */
    public UserNotExistentException(String msg) {
        super(msg);
    }
    
    /**
     * ERRORCODE Getter.
     * @return ERRORCODE is the code
     */
    public int getErrorCode() {
        return UserNotExistentException.ERRORCODE;
    }
}
