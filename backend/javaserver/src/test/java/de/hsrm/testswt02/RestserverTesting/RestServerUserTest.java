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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.restserver.RestServer;

/**
 * Unit-Test class for user handling on REST-Server.
 * user operation are tested
 *     - testSaveUser()
 *     - testSaveExistentUser()
 *     - testGetNonExistentUser()
 *     - testGetExistentUser()
 *     - testDeleteNonExistentUser()
 *     - testDeleteExistentUser()
 *
 */
public class RestServerUserTest {

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
     * This test checks if a new user can be saved.
     * It's successful if the response is 200.
     */
    @Test
    public void testSaveUser() {
        final int httpstatus = 200;
        final User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        final Form dataform;
        String userAsString = null;
        testUser.setId("0");
        testUser.setUsername("Tester");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        final Response resp = client
                .target(targetUrl)
                .path("resource/users")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(httpstatus, resp.getStatus());
    }
    
    /**
     * This test checks if an already existent user can be added again.
     * Same user must be available in persistence.
     * It should receive an error code 500.
     */
    @Test
    public void testSaveExistentUser() {
        final int httpstatus = 200;
        final int httpstatus2 = 500;
        final User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        final Form dataform;
        String userAsString = null;
        testUser.setId("0");
        testUser.setUsername("Tester1");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        final Response resp = client
                .target(targetUrl)
                .path("resource/users")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        
        assertEquals(httpstatus, resp.getStatus());
        
        final Response resp2 = client
                .target(targetUrl)
                .path("resource/users")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(httpstatus2, resp2.getStatus());
    }
    
    /**
     * This test checks if a client can get a nonexistent user.
     * Error 500 should be received.
     */
    @Test
    public void testGetNonExistentUser() {
        final int httpstatus = 500;
        final Response resp = client.target(targetUrl).path("resource/users")
                .request().header("username","Testerxy").delete();
        assertEquals(httpstatus, resp.getStatus());
    }
    /**
     * This test checks if the client can get a new user successfully.
     * It succeeds if the requested user is the same as the sent user.
     */
    @Test
    public void testGetExistentUser() {
        User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        final Form dataform;
        String userAsString = null;
        testUser.setId("0");
        testUser.setUsername("Tester2");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        client.target(targetUrl)
              .path("resource/users")
              .request()
              .post(Entity.entity(dataform,
                      MediaType.APPLICATION_FORM_URLENCODED));
        
        userAsString = client.target(targetUrl)
                .path("resource/users/Tester2").request().get(String.class);
        
        try {
            testUser = mapper.readValue(userAsString, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(testUser.getUsername(), "Tester2");
    }
    
    /**
     * This test checks if a nonexistent user can be deleted.
     * Error code 500 should be received.
     */
    @Test
    public void testDeleteNonExistentUser() {
        final int httpstatus = 500;
        final Response resp = client.target(targetUrl).path("resource/users")
                .request().header("username", "Testerabc").delete();
        assertEquals(httpstatus, resp.getStatus());
    }
    
    /**
     * This test checks if a existent user is successfully deleted.
     * It's successful if the response is 200.
     */
    @Test
    public void testDeleteExistentUser() {
        final int httpstatus = 200;
        final User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        final Form dataform;
        String userAsString = null;
        testUser.setId("0");
        testUser.setUsername("Tester3");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        client.target(targetUrl)
              .path("resource/users")
              .request()
              .post(Entity.entity(dataform,
                      MediaType.APPLICATION_FORM_URLENCODED));
        final Response resp = client.target(targetUrl).path("resource/users")
                .request().header("username", "Tester3").delete();
        assertEquals(httpstatus, resp.getStatus());
    }
    
    
    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        restServer.stopHTTPServer(true);
    }
}
