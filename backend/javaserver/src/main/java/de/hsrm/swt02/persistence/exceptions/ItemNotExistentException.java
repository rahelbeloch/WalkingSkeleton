package de.hsrm.swt02.persistence.exceptions;

public class ItemNotExistentException extends NotExistentException {
    
    private static final long serialVersionUID = 979473446452493460L;
    private int errorcode = 11253;
    
    public ItemNotExistentException() {
        super();
    }

    public ItemNotExistentException(String msg) {
        super(msg);
    }

    public int getErrorCode() {
        return this.errorcode;
    }
}
