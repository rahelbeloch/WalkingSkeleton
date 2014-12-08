package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception dealing with whenever something is requested and not there.
 */
public class NotExistentException extends PersistenceException {

    private static final long serialVersionUID = -5881137338615320424L;
    public static final int ERRORCODE = 11250;
    
    /**
     * Constructor for the Exception.
     */
    public NotExistentException() {
        super("Das Element existiert nicht.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage
     */
    public NotExistentException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for ErrorCode.
     * @return ERRORCODE the errorcode
     */
    public int getErrorCode() {
        return NotExistentException.ERRORCODE;
    }
    
}