package de.hsrm.testswt02.messagingtest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.messaging.ServerPublisherImp;

/**
 * Unit test class for ServerPublisher.class.
 * Tested methods:
 *    - startBroker()
 *    - stopBroker()
 */
public class BrokerStartStopTest {
    
    private ServerPublisher publisher;
    
    /**
     * Test setup method.
     * Invoked before every test case.
     */
    @Before
    public void setup() {
        publisher = new ServerPublisherImp(new UseLogger());
    }
    
    /**
     * Testing for broker start.
     * 
     * @throws ServerPublisherBrokerException if something goes wrong.
     */
    @Test
    public void testBrokerStart() throws ServerPublisherBrokerException {
        publisher.startBroker();
        assertTrue(publisher.brokerStarted());
        if (publisher.brokerStarted()) {
            publisher.stopBroker();
        }
    }
    
    /**
     * Testing for broker stop.
     * 
     * @throws ServerPublisherBrokerException if someting goes wrong
     */
    @Test
    public void testBrokerStop() throws ServerPublisherBrokerException {
        publisher.startBroker();
        assertTrue(publisher.brokerStarted());
        publisher.stopBroker();
        assertTrue(!publisher.brokerStarted());     
    }
}
