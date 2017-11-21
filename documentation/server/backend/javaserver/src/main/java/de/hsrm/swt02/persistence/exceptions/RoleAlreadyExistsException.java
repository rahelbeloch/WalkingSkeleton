package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception dealing with whenever an user is requested and it's already there.
 */
public class RoleAlreadyExistsException extends AlreadyExistsException {

    private static final long serialVersionUID = -360183821720195333L;
    public static final int ERRORCODE = 11222;
    
    /**
     * Constructor for the Exception.
     */
    public RoleAlreadyExistsException() {
        super();
    }

    /**
     * Method dealing with the error message.
     * @param msg is the error message
     */
    public RoleAlreadyExistsException(String msg) {
        super(msg);
    }
    
    /**
     * ErrorCode Getter.
     * @return ERRORCODE is the code
     */
    public int getErrorCode() {
        return RoleAlreadyExistsException.ERRORCODE;
    }
}
