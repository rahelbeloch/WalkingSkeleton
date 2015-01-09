package de.hsrm.swt02.restserver.exceptions;

/**
 * Exception for RestServerExceptionhandling.
 */
public class RestException extends ConnectException {

    private static final long serialVersionUID = 7591430845597579348L;
    public static final int ERRORCODE = 12100;
    
    /**
     * Constructor.
     */
    public RestException() {
        super();
    }

    /**
     * Method dealing the exception message.
     * @param msg exception message
     */
    public RestException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for error code.
     * @return errorcode is the error code
     */
    public int getErrorCode() {
        return RestException.ERRORCODE;
    }
    
}
