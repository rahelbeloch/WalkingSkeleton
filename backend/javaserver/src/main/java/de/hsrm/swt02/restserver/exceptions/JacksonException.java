package de.hsrm.swt02.restserver.exceptions;

public class JacksonException extends RestserverException {

    /**
     * 
     */
    private static final long serialVersionUID = 5071158186141166613L;
    private int errorcode = 12210;
    
    public JacksonException() {
        super("Jackson parsing-error occured");
    }

    public JacksonException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
    
}
