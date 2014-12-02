package de.hsrm.swt02.persistence.exceptions;

public class UserAlreadyExistsException extends AlreadyExistsException {

    /**
     * 
     */
    private static final long serialVersionUID = -3331198130589019219L;
    private int errorcode = 11221;
    
    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
}
