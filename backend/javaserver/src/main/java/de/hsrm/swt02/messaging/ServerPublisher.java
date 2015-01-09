package de.hsrm.swt02.messaging;

import java.util.Properties;

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
    
    /**Publishes multiple Messages from one event(response object).
     * The method retrieves all messages from the given logicRsponse object 
     * and publishes them on their specific topic.
     * @param resp is the container for all messages that shall be published
     */
    void publishEvent(LogicResponse resp);
    
    /**
     * applies the config file information to the publisher.
     * @param properties that have been read out of the config file
     */
    void applyProperties(Properties properties);

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
