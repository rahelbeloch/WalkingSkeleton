package de.hsrm.swt02.messaging;

import de.hsrm.swt02.restserver.exceptions.ConnectException;

/**
 * Exception class for messaging Errors.
 */
public class MessagingException extends ConnectException {
    /**
     * 
     */
    private static final long serialVersionUID = 2806651554510892036L;
    public static final int ERRORCODE = 12200;
    
    /**
     * Constructor 1.
     */
    public MessagingException() {
        super();
    }
    
    /**
     * Constructor 2.
     * 
     * @param msg is the message for the exception to set
     */
    public MessagingException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for errorcode.
     * @return ERRRORCODE for this exception
     */
    public int getErrorCode() {
        return ERRORCODE;
    }
}
