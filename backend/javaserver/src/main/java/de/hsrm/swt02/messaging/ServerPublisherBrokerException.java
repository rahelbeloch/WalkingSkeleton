package de.hsrm.swt02.messaging;

/**Exception class for message broker Errors 
 * coming from the ServerPublisher
 */
public class ServerPublisherBrokerException extends Exception {
	private static final long serialVersionUID = 2850340582682788238L;
	
	public ServerPublisherBrokerException() {
		super();
	}
	
	public ServerPublisherBrokerException(String msg) {
		super(msg);
	}
}
