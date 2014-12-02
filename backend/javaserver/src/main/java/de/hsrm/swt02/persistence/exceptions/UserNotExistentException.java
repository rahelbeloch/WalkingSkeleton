package de.hsrm.swt02.persistence.exceptions;

public class UserNotExistentException extends NotExistentException {

    /**
     * 
     */
    private static final long serialVersionUID = -7867137682701372781L;
    private int errorcode = 11251;
    
    public UserNotExistentException() {
        super();
    }

    public UserNotExistentException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }
}
