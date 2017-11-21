package de.hsrm.swt02.businesslogic.exceptions;

/**
 * Exception for incomplete Elements.
 *
 */
public class AdminRoleDeletionException extends NoPermissionException {
    
    private static final long serialVersionUID = -6810531375848540815L;
    public static final int ERRORCODE = 11330;
    
    /**
     * Constructor for the Exception.
     */
    public AdminRoleDeletionException() {
        super("Admin role can't be deleted.");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public AdminRoleDeletionException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return AdminRoleDeletionException.ERRORCODE;
    }

}
