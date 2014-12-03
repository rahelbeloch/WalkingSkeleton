package de.hsrm.testswt02.RestserverTesting;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

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
    public final String targetUrl = "http://localhost:8080";

    /**
     * This method sets and starts the REST-Server. Additionally it provides a test client.
     */
    @BeforeClass
    public static void setUp() {
        restServer = new RestServer();
        restServer.startHTTPServer();
        client = ClientBuilder.newClient();
        
    }
    /**
     * This test checks if a new user can be saved.
     * It's successful if the response is 200.
     */
    @Test
    public void testSaveUser() {
        final User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        final Form dataform;
        String userAsString = null;
        testUser.setId(0);
        testUser.setUsername("Tester");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        final Response resp = client
                .target(targetUrl)
                .path("resource/user")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(200, resp.getStatus());
    }
    
    /**
     * This test checks if an already existent user can be added again.
     * Same user must be available in persistence.
     * It should receive an error code 500.
     */
    @Test
    public void testSaveExistentUser() {
        final User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        final Form dataform;
        String userAsString = null;
        testUser.setId(0);
        testUser.setUsername("Tester1");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        final Response resp = client
                .target(targetUrl)
                .path("resource/user")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        
        assertEquals(200, resp.getStatus());
        
        final Response resp2 = client
                .target(targetUrl)
                .path("resource/user")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(500, resp2.getStatus());
    }
    
    /**
     * This test checks if a client can get a nonexistent user.
     * Error 500 should be received.
     */
    @Test
    public void testGetNonExistentUser() {
        final Response resp = client.target(targetUrl).path("resource/user/Testerxy")
                .request().delete();
        assertEquals(500, resp.getStatus());
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
        testUser.setId(0);
        testUser.setUsername("Tester2");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        client.target(targetUrl)
              .path("resource/user")
              .request()
              .post(Entity.entity(dataform,
                      MediaType.APPLICATION_FORM_URLENCODED));
        
        userAsString = client.target(targetUrl)
                .path("resource/user/Tester2").request().get(String.class);
        
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
        final Response resp = client.target(targetUrl).path("resource/user/Testerabc")
                .request().delete();
        assertEquals(500, resp.getStatus());
    }
    
    /**
     * This test checks if a existent user is successfully deleted.
     * It's successful if the response is 200.
     */
    @Test
    public void testDeleteExistentUser() {
        final User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        final Form dataform;
        String userAsString = null;
        testUser.setId(0);
        testUser.setUsername("Tester3");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        client.target(targetUrl)
              .path("resource/user")
              .request()
              .post(Entity.entity(dataform,
                      MediaType.APPLICATION_FORM_URLENCODED));
        final Response resp = client.target(targetUrl).path("resource/user/Tester3")
                .request().delete();
        assertEquals(200, resp.getStatus());
    }
    
    
    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        client.close();
        restServer.stopHTTPServer();
    }
}
