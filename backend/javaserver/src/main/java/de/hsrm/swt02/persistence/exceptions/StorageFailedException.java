package de.hsrm.swt02.persistence.exceptions;

public class StorageFailedException extends PersistenceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1037333767813860667L;
    public static final int ERRORCODE = 11270;
    
    /**
     * Constructor for the Exception.
     */
    public StorageFailedException() {
        super("Element konnte nicht persistiert werden.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage
     */
    public StorageFailedException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for ErrorCode.
     * @return ERRORCODE the errorcode
     */
    public int getErrorCode() {
        return StorageFailedException.ERRORCODE;
    }
    
}