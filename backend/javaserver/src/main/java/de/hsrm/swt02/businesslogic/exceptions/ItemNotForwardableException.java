package de.hsrm.swt02.businesslogic.exceptions;

/**
 * Exception dealing with whenever something is requested and it's alread there.
 */
public class ItemNotForwardableException extends NoPermissionException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4662908169144576997L;
    public static final int ERRORCODE = 11320;
    
    /**
     * Constructor for the Exception.
     */
    public ItemNotForwardableException() {
        super("item is not forwardable");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public ItemNotForwardableException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for the errorcode.
     * @return ERRORCODE is the errorcode
     */
    public int getErrorCode() {
        return ItemNotForwardableException.ERRORCODE;
    }
}
