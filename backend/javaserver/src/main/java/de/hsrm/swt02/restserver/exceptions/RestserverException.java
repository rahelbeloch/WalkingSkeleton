package de.hsrm.swt02.restserver.exceptions;

public class RestserverException extends ConnectionException {

    /**
     * 
     */
    private static final long serialVersionUID = 7591430845597579348L;
    private int errorcode = 12200;
    
    public RestserverException() {
        super();
    }

    public RestserverException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
    
}
