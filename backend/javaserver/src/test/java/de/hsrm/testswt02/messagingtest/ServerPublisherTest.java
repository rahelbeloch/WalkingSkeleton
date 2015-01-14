package de.hsrm.testswt02.messagingtest;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.messaging.ServerPublisherImp;

/**
 * Unit test class for ServerPublisher.class.
 * Tested methods:
 *    - publish()
 */
public class ServerPublisherTest {

    private static final int LISTENER_CHECKS = 20;
    private static final int SLEEP_TIME = 500;
    private static final int MULTIPUBLISH_ATTEMPTS = 3;
    private static ServerPublisher publisher;
    private static TestMessagingListener listener;
    
    /**
     * Disables the log4j-system (no need for it).
     * 
     * @throws ServerPublisherBrokerException if the publisher cannot be started
     */
    @BeforeClass
    public static void setup() throws ServerPublisherBrokerException {
        publisher = new ServerPublisherImp(new UseLogger());
        publisher.startBroker();
        listener = new TestMessagingListener();
        listener.start();
    }
    
    /**
     * Cleans up the resources for this test.
     * 
     * @throws ServerPublisherBrokerException if the publisher cannot be stopped
     */
    @AfterClass
    public static void tearDown() throws ServerPublisherBrokerException {
        publisher.stopBroker();
        listener.stop();   
    }
    
    /**
     * Publishing test for one single message post.
     * The message will be posted to a test listener class.
     * @see TestMessagingListener.class
     * @throws ServerPublisherBrokerException if something goes wrong.
     */
    @Test
    public void testPublishing() throws ServerPublisherBrokerException {
        if (publisher.brokerStarted()) {
            final String testString = "test String";
            publisher.publish(testString, "TEST_TOPIC");     
            waitAndCheckListener();
            
            assertEquals(testString, listener.getReceivedMsg());
            listener.reset();
        }
    }
    
    /**
     * Publishing test for multiple message posts.
     * The messages will be posted to a test listener class.
     * @see TestMessagingListener.class
     * @throws ServerPublisherBrokerException if something goes wrong.
     */
    @Test
    public void testMultiplePublishing() throws ServerPublisherBrokerException {
        if (publisher.brokerStarted()) {
            final String testString = "test";
            for (int i = 0; i < MULTIPUBLISH_ATTEMPTS; i++) {
                publisher.publish(testString, "TEST_TOPIC");
                waitAndCheckListener();
                
                assertEquals(testString, listener.getReceivedMsg());
                listener.reset();
            }
        }
    }
    
    /**
     * waits a few times and checks whether the listener received a message.
     */
    private void waitAndCheckListener() {
        for (int i = 0; i < LISTENER_CHECKS; i++) {
            if (listener.getReceivedMsg() != null) {
                break;
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }         
        }
    }
}
