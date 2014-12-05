package de.hsrm.testswt02.RestserverTesting;

import static org.junit.Assert.assertEquals;

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
 * 
 * JUNIT Test Class for the REST servers command functions
 *
 */
public class RestserverCommandTest {

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
     * This test creates a new User with username "Alex" and saves it into persistence,
     * then tries to login with that name
     */
    @Test
    public void testUpdate() {
        
        final User testUser = new User();
        final ObjectMapper mapper = new ObjectMapper();
        Form dataform;
        String userAsString = null;
        testUser.setId(0);
        testUser.setUsername("Alex");
        
        try {
            userAsString = mapper.writeValueAsString(testUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        Response resp = client
                .target(targetUrl)
                .path("resource/user")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        
        dataform = new Form().param("username", "Alex");
        dataform.param("password", "test");
        resp = client
                .target(targetUrl)
                .path("command/user/login")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(resp.getStatus(), 200);
    }
    
    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        client.close();
        restServer.stopHTTPServer(true);
    }
    
}
