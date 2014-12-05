package de.hsrm.testswt02.messagingtest;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * This class is for testing. It simulates a message listener listening to a
 * TEST_TOPIC.
 */
public class TestMessagingListener implements MessageListener {
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private String receivedMsg = null;

    private static final String BROKERURL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "TEST_TOPIC";
    
    /**
     * Constructor.
     */
    public TestMessagingListener() {

    }
    
    /**
     * Call-back method for messages.
     * Called after incoming messages.
     * @param message incoming message
     */
    public void onMessage(Message message) {
        final TextMessage tm = (TextMessage) message;
        try {
            receivedMsg = tm.getText();
        } catch (JMSException e) {
            receivedMsg = null;
        }
    }

    /**
     * Starts listening.
     */
    public void start() {
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                BROKERURL);
        try {
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Topic topic = session.createTopic(TOPIC_NAME);
            consumer = session.createConsumer(topic);
            consumer.setMessageListener(this);
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Stops listening.
     */
    public void stop() {
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the last received string message.
     * 
     * @return the received string.
     */
    public String getReceivedMsg() {
        return receivedMsg;
    }
    
    public void reset() {
        receivedMsg = null;
    }
}
