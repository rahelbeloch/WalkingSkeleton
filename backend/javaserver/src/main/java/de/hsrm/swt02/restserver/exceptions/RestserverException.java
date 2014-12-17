package de.hsrm.swt02.restserver.exceptions;

/**
 * Exception for RestServerExceptionhandling.
 */
public class RestserverException extends ConnectionException {

    private static final long serialVersionUID = 7591430845597579348L;
    public static final int ERRORCODE = 12200;
    
    /**
     * Constructor.
     */
    public RestserverException() {
        super();
    }

    /**
     * Method dealing the exception message.
     * @param msg exception message
     */
    public RestserverException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for error code.
     * @return errorcode is the error code
     */
    public int getErrorCode() {
        return RestserverException.ERRORCODE;
    }
    
}
