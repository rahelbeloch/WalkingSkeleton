package de.hsrm.swt02.persistence.exceptions;

public class AlreadyExistsException extends PersistenceException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -130962526189571793L;
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
