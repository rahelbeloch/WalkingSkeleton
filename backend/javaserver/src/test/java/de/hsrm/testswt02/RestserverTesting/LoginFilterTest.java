package de.hsrm.testswt02.RestserverTesting;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.restserver.RestServer;

/**
 * 
 * @author akoen001
 *
 * This Class tests the loginFilter
 *
 */
public class LoginFilterTest {

    public static RestServer restServer;
    public static Client client;
    public static String targetUrl;
    public static String clientUser = "Alex";
    public static String headerUsername = "TestAdmin";
    public static String headerPW = "admin";
    public static String headerClientID = "admin";
    final int okCode = 200;
    final int errorCode = 500;
    
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
     * Tests if unauthorized requests (with existent users) get blocked.
     */
    @Test
    public void testUnauthorizedRequest() {
        final Response resp = client.target(targetUrl)
                .path("resource/workflows").request()
                .header("username", clientUser)
                .header("password", headerPW)
                .header("client_id",headerClientID)
                .get();
        
        assertEquals(resp.getStatus(), errorCode);
    }
    
    /**
     * Tests if authorized requests may pass.
     */
    @Test
    public void testAuthorizedRequest() {
        final Response resp = client.target(targetUrl)
                .path("resource/workflows").request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("client_id",headerClientID)
                .get();
        
        assertEquals(resp.getStatus(), okCode);
    }
    
    /**
     * Tests if made up usernames(e.g. third party invaders) can access data.
     */
    @Test
    public void testNonExistentUserRequest() {
        final Response resp = client.target(targetUrl)
                .path("resource/workflows").request()
                .header("username", "LeakData")
                .header("password", "abc")
                .header("client_id",headerClientID)
                .get();
        
        assertEquals(resp.getStatus(), errorCode);
    }
    
    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        restServer.stopHTTPServer(true);
    }
    
}
