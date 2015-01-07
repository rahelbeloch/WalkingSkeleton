package de.hsrm.testswt02.RestserverTesting;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.restserver.RestServer;


/**
 * 
 * JUNIT Test Class for the REST servers command functions.
 *
 */
public class RestserverCommandTest {

    public static RestServer restServer;
    public static Client client;
    public static String targetUrl;
    public static String headerUsername = "TestAdmin";
    public static String headerPW = "abc123";
    public static String headerClientID = "admin";

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
     * This test creates a new User with username "Alex" and saves it into persistence,
     * then tries to login with that name.
     */
    @Test
    public void testLogin() {
        final int httpstatus = 200;
        Form dataform;

        dataform = new Form().param("data", "abc");
        final Response resp = client
                .target(targetUrl)
                .path("command/users/login")
                .request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(resp.getStatus(), httpstatus);
    }
    
    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        restServer.stopHTTPServer(true);
    }
    
}
