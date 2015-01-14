package de.hsrm.testswt02.RestserverTesting;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.restserver.RestServer;

/**
 * 
 * @author akoen001
 *
 */
public class LoginFilterTest {

    public static RestServer restServer;
    public static Client client;
    public static String targetUrl;
    
    /**
     * This method sets and starts the REST-Server. Additionally it provides a test client.
     */
    @BeforeClass
    public static void setUp() {
        LogConfigurator.setup();
        restServer = new RestServer();
        restServer.startHTTPServer();
        client = ClientBuilder.newClient();
        final Properties properties = new Properties();
        BufferedInputStream stream;
        // read configuration file for rest properties
        try {
            stream = new BufferedInputStream(new FileInputStream(
                    "server.config"));
            properties.load(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        targetUrl = properties.getProperty("RestServerURI");
    }
    
    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        restServer.stopHTTPServer(true);
    }
    
}
