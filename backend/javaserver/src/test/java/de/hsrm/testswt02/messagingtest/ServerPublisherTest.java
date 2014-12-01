package de.hsrm.testswt02.messagingtest;

import static org.junit.Assert.*;
import de.hsrm.swt02.SingleModule;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Unit test class for ServerPublisher.class.
 * Tested methods:
 *    - startBroker()
 *    - stopBroker()
 *    - publish()
 */
public class ServerPublisherTest {

    private ServerPublisher publisher;
    
    /**
     * Disables the log4j-system (no need for it).
     */
    @BeforeClass
    public static void disableLog() {
        Logger.getRootLogger().setLevel(Level.OFF);
    }
    
    /**
     * Method for test setup.
     * Executed before each test.
     */
    @Before
    public void setUp() {
        final Injector i = Guice.createInjector(new SingleModule());
        publisher = i.getInstance(ServerPublisher.class);
    }
    
    /**
     * Method for test tear down.
     * Executed after each test.
     * @throws ServerPublisherBrokerException if something goes wrong.
     */
    @After
    public void tearDown() throws ServerPublisherBrokerException {
        if (publisher.brokerStarted()) {
            publisher.stopBroker();
        }
    }
    
    /**
     * Tests whether the broker starts properly.
     * @throws ServerPublisherBrokerException if something goes wrong.
     */
    @Test
    public void testBrokerStart() throws ServerPublisherBrokerException {
        publisher.startBroker();
        assertTrue(publisher.brokerStarted());
    }

    /**
     * Tests whether the broker stops properly.
     * @throws ServerPublisherBrokerException if something goes wrong.
     */
    @Test
    public void testBrokerStop() throws ServerPublisherBrokerException {
        publisher.startBroker();
        if (publisher.brokerStarted()) {
            publisher.stopBroker();
            assertTrue(!publisher.brokerStarted());
        }
    }
    
    /**
     * Publishing test for one single message post.
     * The message will be posted to a test listener class.
     * @see TestMessagingListener.class
     * @throws ServerPublisherBrokerException if something goes wrong.
     */
    @Test
    public void testPublishing() throws ServerPublisherBrokerException {
        publisher.startBroker();
        if (publisher.brokerStarted()) {
            final TestMessagingListener listener = new TestMessagingListener();
            listener.start();
            final String testString = "test String";
            publisher.publish(testString, "TEST_TOPIC");
            assertEquals(testString, listener.getReceivedMsg());
            listener.stop();
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
        publisher.startBroker();
        if (publisher.brokerStarted()) {
            final TestMessagingListener listener = new TestMessagingListener();
            listener.start();
            String testString = "test String1";
            publisher.publish(testString, "TEST_TOPIC");
            assertEquals(testString, listener.getReceivedMsg());
            testString = "test String2";
            publisher.publish(testString, "TEST_TOPIC");
            assertEquals(testString, listener.getReceivedMsg());
            testString = "test String3";
            publisher.publish(testString, "TEST_TOPIC");
            assertEquals(testString, listener.getReceivedMsg());
            listener.stop();
        }
    }
}
