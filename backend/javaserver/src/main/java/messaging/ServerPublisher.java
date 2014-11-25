package messaging;


/**
 * Interface for ServerPublisher
 *
 */
public interface ServerPublisher {

	
	public void publish(String content, String topicName) throws Exception;

	public void startBroker();

	public void stopBroker();
	
	public boolean brokerStarted();
}

