package de.hsrm.swt02.persistence.exceptions;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;

/**
 * Exception responsible forr all persistence exceptions.
 */
public class PersistenceException extends LogicException {
    
    private static final long serialVersionUID = -3291139698927930642L;
    public static final int ERRORCODE = 11200;
    
    /**
     * Constructor for the Exception.
     */
    public PersistenceException() {
        super("Es ist ein Fehler in der Datenbank passiert.");
    }

    /**
     * Method dealing with the errormessage.
     * @param msg is the errormessage
     */
    public PersistenceException(String msg) {
        super(msg);
    }
    
    /**
     * ERRORCODE Getter.
     * @return ERRORCODE is the code
     */
    public int getErrorCode() {
        return PersistenceException.ERRORCODE;
    }

}
