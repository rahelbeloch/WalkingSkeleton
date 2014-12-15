package de.hsrm.swt02.businesslogic.exceptions;

import de.hsrm.swt02.persistence.exceptions.BasicException;

/**
 * General Exception for LogicExceptionHandling.
 * @author jfick001
 *
 */
public class LogicException extends BasicException {

    private static final long serialVersionUID = -7612098314265342947L;
    public static final int ERRORCODE = 11000;

    /**
     * Constructor for LogicException.
     */
    public LogicException() {
        super();
    }

    /**
     * Method for dealing with the Exception message.
     * @param msg is the mssagestring.
     */
    public LogicException(String msg) {
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
