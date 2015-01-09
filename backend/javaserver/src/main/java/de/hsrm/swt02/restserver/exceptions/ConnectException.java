package de.hsrm.swt02.restserver.exceptions;

import de.hsrm.swt02.persistence.exceptions.BasicException;

/**
 * Exception for ConnectionExceptionhandling.
 */
public class ConnectException extends BasicException {

    private static final long serialVersionUID = 1614570197373745612L;
    public static final int ERRORCODE = 12000;
    
    /**
     * Constructor.
     */
    public ConnectException() {
        super();
    }

    /**
     * Method dealing with the exception message.
     * @param msg the exception message
     */
    public ConnectException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for errorcode.
     * @return errorcode is the errorcode
     */
    public int getErrorCode() {
        return ConnectException.ERRORCODE;
    }
    
}
