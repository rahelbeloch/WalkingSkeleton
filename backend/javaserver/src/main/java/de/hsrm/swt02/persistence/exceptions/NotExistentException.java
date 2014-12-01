package de.hsrm.swt02.persistence.exceptions;

public class NotExistentException extends PersistenceException {
    
    private static final long serialVersionUID = 1477135084952400723L;
    private int errorcode = 11250;
    
    public NotExistentException() {
        super();
    }

    public NotExistentException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
    
}