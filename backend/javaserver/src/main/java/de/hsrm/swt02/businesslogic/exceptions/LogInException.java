package de.hsrm.swt02.businesslogic.exceptions;

/**
 * This is a LogInException, user should not know, if username or password is wrong.
 */
public class LogInException extends LogicException {


    private static final long serialVersionUID = -6360811980857417787L;
    public static final int ERRORCODE = 11100;

    /**
     * Constructor for LogicException.
     */
    public LogInException() {
        super();
    }

    /**
     * Method for dealing with the Exception message.
     * @param msg is the mssagestring.
     */
    public LogInException(String msg) {
        super(msg);
    }

    /**
     * Getter for the errorcode.
     * @return errorcode is the wanted errorcode
     */
    public int getErrorCode() {
        return LogicException.ERRORCODE;
    }
}
