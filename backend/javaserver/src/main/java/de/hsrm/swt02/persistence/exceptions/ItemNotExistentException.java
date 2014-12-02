package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception dealing with whenever an item is requested and it's not there.
 */
public class ItemNotExistentException extends NotExistentException {
    
    private static final long serialVersionUID = -1921144766461094157L;
    public final static int ERRORCODE = 11253;
    
    /**
     * Constructor for the Exception.
     */
    public ItemNotExistentException() {
        super();
    }

    /**
     * Method dealingn with the error message.
     * @param msg is the error message
     */
    public ItemNotExistentException(String msg) {
        super(msg);
    }

    /**
     * ERRORCODE Getter.
     * @return ERRORCODE is the code
     */
    public int getErrorCode() {
        return ItemNotExistentException.ERRORCODE;
    }
}
