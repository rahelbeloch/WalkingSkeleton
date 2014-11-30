package de.hsrm.swt02.persistence;

public class ItemNotExistentException extends Exception {
    
    private static final long serialVersionUID = 979473446452493460L;

    public ItemNotExistentException() {
        super();
    }

    public ItemNotExistentException(String msg) {
        super(msg);
    }

}
