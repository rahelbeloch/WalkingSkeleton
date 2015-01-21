package de.hsrm.swt02.businesslogic.exceptions;

/**
 * Exception thrown if a role cannot be deleted beacuse it is still reference in a workflow.
 */
public class RoleStillInUseExeption extends NoPermissionException {

    /**
     * 
     */
    private static final long serialVersionUID = 7370057433351582735L;
    public static final int ERRORCODE = 11350;
    
    /**
     * Constructor for the Exception.
     */
    public RoleStillInUseExeption() {
        super("Role cannot be deleted - still in use");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public RoleStillInUseExeption(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return RoleStillInUseExeption.ERRORCODE;
    }
}
