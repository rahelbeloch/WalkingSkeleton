package de.hsrm.swt02.restserver.exceptions;

public class BasicException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1725732891907643731L;
    private int errorcode = 10000;
    
    public BasicException() {
        super();
    }

    public BasicException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }

}
