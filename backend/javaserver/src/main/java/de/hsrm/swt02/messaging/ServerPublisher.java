package de.hsrm.swt02.messaging;

/**
 * Interface for ServerPublisher
 */
public interface ServerPublisher {

    public void publish(String content, String topicName)
            throws ServerPublisherBrokerException;

    public void startBroker() throws ServerPublisherBrokerException;

    public void stopBroker() throws ServerPublisherBrokerException;

    public boolean brokerStarted();
}
