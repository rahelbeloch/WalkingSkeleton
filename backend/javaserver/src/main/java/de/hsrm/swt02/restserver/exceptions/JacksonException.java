package de.hsrm.swt02.restserver.exceptions;

/**
 * Exception for JacksonExceptionhandling.
 */
public class JacksonException extends RestException {

    private static final long serialVersionUID = 5071158186141166613L;
    public static final int ERRORCODE = 12110;
    
    /**
     * Constructor.
     */
    public JacksonException() {
        super("Jackson parsing-error occured");
    }

    /**
     * Method dealing with the exception message.
     * @param msg is the exception message
     */
    public JacksonException(String msg) {
        super(msg);
    }
    
    /**
     * getter for the errorcode.
     * @return errorcode is the errorcode
     */
    public int getErrorCode() {
        return JacksonException.ERRORCODE;
    }
    
}
