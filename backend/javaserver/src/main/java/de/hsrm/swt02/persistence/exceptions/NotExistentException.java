package de.hsrm.swt02.persistence.exceptions;

public class NotExistentException extends PersistenceException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5881137338615320424L;
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