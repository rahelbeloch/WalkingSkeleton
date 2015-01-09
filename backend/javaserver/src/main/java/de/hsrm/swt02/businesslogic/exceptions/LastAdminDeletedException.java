package de.hsrm.swt02.businesslogic.exceptions;

/**
 * Exception for incomplete Elements.
 *
 */
public class LastAdminDeletedException extends NoPermissionException {
    
    private static final long serialVersionUID = -8260127818295648965L;
    public static final int ERRORCODE = 11340;
    
    /**
     * Constructor for the Exception.
     */
    public LastAdminDeletedException() {
        super("There needs to be at least one admin.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public LastAdminDeletedException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return IncompleteEleException.ERRORCODE;
    }

}
