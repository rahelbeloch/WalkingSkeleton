package de.hsrm.swt02.messaging;

/**
 * Exception class for message broker Errors coming from the ServerPublisher.
 */
public class ServerPublisherBrokerException extends MessagingException {
    private static final long serialVersionUID = 2850340582682788238L;
    public static final int ERRORCODE = 12210;
    
    /**
     * Constructor 1.
     */
    public ServerPublisherBrokerException() {
        super();
    }
    
    /**
     * Constructor 2.
     * 
     * @param msg is the message for the exception to set
     */
    public ServerPublisherBrokerException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for errorcode.
     * @return ERRRORCODE for this exception
     */
    public int getErrorCode() {
        return ERRORCODE;
    }
}
