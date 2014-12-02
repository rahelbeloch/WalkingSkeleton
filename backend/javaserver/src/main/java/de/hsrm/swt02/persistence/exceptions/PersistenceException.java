package de.hsrm.swt02.persistence.exceptions;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;

public class PersistenceException extends LogicException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3291139698927930642L;
    private int errorcode = 11200;
    
    public PersistenceException() {
        super();
    }

    public PersistenceException(String msg) {
        super(msg);
    }
    
    public int getErrorCode() {
        return this.errorcode;
    }

}
