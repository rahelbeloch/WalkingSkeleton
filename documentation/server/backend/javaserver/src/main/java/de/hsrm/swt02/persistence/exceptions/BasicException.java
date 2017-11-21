package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception for BasicExceptionhandling.
 */
public class BasicException extends Exception {
    
    private static final long serialVersionUID = -1725732891907643731L;
    public static final int ERRORCODE = 10000;
    
    /**
     * Constructor.
     */
    public BasicException() {
        super();
    }

    /**
     * Method dealing with the exception message.
     * @param msg exception message
     */
    public BasicException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return errorcode is the errorcode
     */
    public int getErrorCode() {
        return BasicException.ERRORCODE;
    }

}
