package de.hsrm.testswt02.messagingtest;

import static org.junit.Assert.*;
import de.hsrm.swt02.businesslogic.SingleModule;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ServerPublisherTest {

    private ServerPublisher publisher;

    @Before
    public void setUp() {
        Injector i = Guice.createInjector(new SingleModule());
        publisher = i.getInstance(ServerPublisher.class);
    }

    @After
    public void tearDown() throws ServerPublisherBrokerException {
        if (publisher.brokerStarted()) {
            publisher.stopBroker();
        }
    }

    @Test
    public void testBrokerStart() throws ServerPublisherBrokerException {
        publisher.startBroker();
        assertTrue(publisher.brokerStarted());
    }

    @Test
    public void testBrokerStop() throws ServerPublisherBrokerException {
        publisher.startBroker();
        if (publisher.brokerStarted()) {
            publisher.stopBroker();
            assertTrue(!publisher.brokerStarted());
        }
    }

    @Test
    public void testPublishing() throws ServerPublisherBrokerException {
        publisher.startBroker();
        if (publisher.brokerStarted()) {
            TestMessagingListener listener = new TestMessagingListener();
            listener.start();
            String testString = "test String";
            publisher.publish(testString, "TEST_TOPIC");
            assertEquals(testString, listener.getReceivedMsg());
            listener.stop();
        }
    }

    @Test
    public void testMultiplePublishing() throws ServerPublisherBrokerException {
        publisher.startBroker();
        if (publisher.brokerStarted()) {
            TestMessagingListener listener = new TestMessagingListener();
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
