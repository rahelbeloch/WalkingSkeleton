package messaging;


/**
 * Interface for ServerPublisher
 * @author jvanh001
 *
 */
public interface ServerPublisher {

	
	public void publish(String content, String topicName) throws Exception;

	public void startBroker();

	public void stopBroker();
}

