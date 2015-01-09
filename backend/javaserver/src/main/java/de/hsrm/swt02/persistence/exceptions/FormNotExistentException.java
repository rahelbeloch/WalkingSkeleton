package de.hsrm.swt02.persistence.exceptions;

/**
 * Exception dealing with whenever a form is requested and it's not there.
 */
public class FormNotExistentException extends NotExistentException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7443517251971663809L;
    public final static int ERRORCODE = 11255;
    
    /**
     * Constructor for the Exception.
     */
    public FormNotExistentException() {
        super("Das Formular exsistiert nicht.");
    }

    /**
     * Method dealingn with the error message.
     * @param msg is the error message
     */
    public FormNotExistentException(String msg) {
        super(msg);
    }

    /**
     * ERRORCODE Getter.
     * @return ERRORCODE is the code
     */
    public int getErrorCode() {
        return FormNotExistentException.ERRORCODE;
    }
}
