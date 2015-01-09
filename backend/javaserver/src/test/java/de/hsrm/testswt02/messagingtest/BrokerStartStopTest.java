package de.hsrm.testswt02.messagingtest;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

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
    private final UseLogger logger = new UseLogger();
    
    /**
     * Test setup method.
     * Invoked before every test case.
     */
    @Before
    public void setup() {
        publisher = new ServerPublisherImp(new UseLogger());
        final Properties properties = new Properties();
        BufferedInputStream stream;
        // read configuration file for rest properties
        try {
            stream = new BufferedInputStream(new FileInputStream(
                    "server.config"));
            properties.load(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Configuration file not found!");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can't read file!");
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "Read Access not granted!");
        }
        publisher.applyProperties(properties);
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
