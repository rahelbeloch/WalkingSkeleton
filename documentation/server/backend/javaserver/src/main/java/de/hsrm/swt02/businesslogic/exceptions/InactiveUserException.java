package de.hsrm.swt02.businesslogic.exceptions;

/**
 * This excpetion is used if a user is inactive.
 *
 */
public class InactiveUserException extends LogInException {
    
    private static final long serialVersionUID = -4566837481140591450L;
    public static final int ERRORCODE = 11130;
    
    /**
     * Constructor.
     */
    public InactiveUserException() {
        super();
    }
    /**
     * Method for dealing with the Exception message.
     * @param msg is the mssagestring.
     */
    public InactiveUserException(String msg) {
        super(msg);
    }

    /**
     * Getter for the errorcode.
     * @return errorcode is the wanted errorcode
     */
    public int getErrorCode() {
        return InactiveUserException.ERRORCODE;
    }

}
