package de.hsrm.swt02.restserver.exceptions;

import de.hsrm.swt02.persistence.exceptions.BasicException;

public class ConnectionException extends BasicException {

    /**
     * 
     */
    private static final long serialVersionUID = 1614570197373745612L;
    private int errorcode = 12200;
    
    public ConnectionException() {
        super();
    }

    public ConnectionException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
    
}
