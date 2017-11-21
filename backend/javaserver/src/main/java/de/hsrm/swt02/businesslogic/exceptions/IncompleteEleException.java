package de.hsrm.swt02.businesslogic.exceptions;

/**
 * Exception for incomplete Elements.
 *
 */
public class IncompleteEleException extends LogicException {
    
    private static final long serialVersionUID = -2169286945881690968L;
    public static final int ERRORCODE = 11400;
    
    /**
     * Constructor for the Exception.
     */
    public IncompleteEleException() {
        super("Element is incomplete");
    }

    /**
     * Method dealing with the error message.
     * @param msg is the errormessage.
     */
    public IncompleteEleException(String msg) {
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
