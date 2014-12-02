package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception dealing with whenever something is requested and it's alread there.
 */
public class AlreadyExistsException extends PersistenceException {
    
    private static final long serialVersionUID = -130962526189571793L;
    public static final int ERRORCODE = 11220;
    
    /**
     * Constructor for the Exception.
     */
    public AlreadyExistsException() {
        super();
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public AlreadyExistsException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return AlreadyExistsException.ERRORCODE;
    }

}
