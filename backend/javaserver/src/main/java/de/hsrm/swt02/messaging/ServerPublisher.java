package de.hsrm.swt02.messaging;

import de.hsrm.swt02.businesslogic.LogicResponse;

/**
 * Interface for ServerPublisher.
 */
public interface ServerPublisher {

    /**
     * Publishes a String-content on a specified topic.
     * 
     * @param content content to publish
     * @param topicName topic where the content will be published
     * @throws ServerPublisherBrokerException if publishing goes wrong
     */
    void publish(String content, String topicName)
            throws ServerPublisherBrokerException;

    /**
     * Starts the messaging broker.
     * 
     * @throws ServerPublisherBrokerException if broker start does not work
     */
    void startBroker() throws ServerPublisherBrokerException;
    
    void publishEvent(LogicResponse resp);

    /**
     * Stops the messaging broker.
     * 
     * @throws ServerPublisherBrokerException if broker sto does not work
     */
    void stopBroker() throws ServerPublisherBrokerException;

    /**
     * Check weather the message broker is running.
     * 
     * @return true if the embedded message-broker is running
     */
    boolean brokerStarted();
}
