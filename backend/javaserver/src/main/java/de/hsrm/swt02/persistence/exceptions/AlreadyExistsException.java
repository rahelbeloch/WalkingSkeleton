package de.hsrm.swt02.persistence.exceptions;

public class AlreadyExistsException extends PersistenceException {
    
    private static final long serialVersionUID =9128135084952400723L;
    private int errorcode = 11220;
    
    public AlreadyExistsException() {
        super();
    }

    public AlreadyExistsException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }

}
