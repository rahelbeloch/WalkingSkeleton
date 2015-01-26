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

import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.restserver.RestServer;
import de.hsrm.swt02.restserver.exceptions.JacksonException;
import de.hsrm.swt02.restserver.resource.JsonParser;

/**
 * Unit-Test class for role handling on REST-Server.
 * role operation are tested
 *     - testSaveRole()
 *     - testSaveExistentRole()
 *     - testGetNonExistentRole()
 *     - testGetExistentRole()
 *     - testDeleteNonExistentRole()
 *     - testDeleteExistentRole()
 *
 */
public class RestserverRoleTest {

    public static RestServer restServer;
    public static Client client;
    public static String targetUrl;
    public static String headerUsername = "TestAdmin";
    public static String headerPW = "admin";
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
     * This test checks if a new user can be saved.
     * It's successful if the response is 200.
     */
    @Test
    public void testSaveRole() {
        final int httpstatus = 200;
        final Role testRole = new Role();
        final Form dataform;
        String roleAsString = null;
        testRole.setId("0");
        testRole.setRolename("testrole");
        
        try {
            roleAsString = JsonParser.marshall(testRole);
        } catch (JacksonException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", roleAsString);
        final Response resp = client
                .target(targetUrl)
                .path("resource/roles")
                .request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(httpstatus, resp.getStatus());
    }
    
    /**
     * This test checks if a client can get a nonexistent role.
     * Error 500 should be received.
     */
    @Test
    public void testGetNonExistentRole() {
        final int httpstatus = 500;
        final Response resp = client.target(targetUrl).path("resource/roles/testroleabc")
                .request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .delete();
        assertEquals(httpstatus, resp.getStatus());
    }
    
    /**
     * This test checks if the client can get a new role successfully.
     * It succeeds if the requested role is the same as the sent role.
     */
    @Test
    public void testGetExistentRole() {
        Role testRole = new Role();
        final Form dataform;
        String roleAsString = null;
        testRole.setId("0");
        testRole.setRolename("testrole2");
        
        try {
            roleAsString = JsonParser.marshall(testRole);
        } catch (JacksonException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", roleAsString);
        client.target(targetUrl)
              .path("resource/roles")
              .request()
              .header("username", headerUsername)
              .header("password", headerPW)
              .header("clientID",headerClientID)
              .post(Entity.entity(dataform,
                      MediaType.APPLICATION_FORM_URLENCODED));
        
        roleAsString = client.target(targetUrl)
                .path("resource/roles/testrole2").request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .get(String.class);
        
        try {
            testRole = (Role)JsonParser.unmarshall(roleAsString, testRole);
        } catch (JacksonException e) {
            e.printStackTrace();
        }
        assertEquals(testRole.getRolename(), "testrole2");
    }
    
    /**
     * This test checks if a nonexistent Role can be deleted.
     * Error code 500 should be received.
     */
    @Test
    public void testDeleteNonExistentRole() {
        final int httpstatus = 500;
        final Response resp = client.target(targetUrl).path("resource/roles/abc")
                .request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .delete();
        assertEquals(httpstatus, resp.getStatus());
    }
    
    /**
     * This test checks if a existent role is successfully deleted.
     * It's successful if the response is 200.
     */
    @Test
    public void testDeleteExistentRole() {
        final int httpstatus = 200;
        final Role testRole = new Role();
        final Form dataform;
        String roleAsString = null;
        testRole.setId("0");
        testRole.setRolename("testrole3");
        
        try {
            roleAsString = JsonParser.marshall(testRole);
        } catch (JacksonException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", roleAsString);
        client.target(targetUrl)
              .path("resource/roles")
              .request()
              .header("username", headerUsername)
              .header("password", headerPW)
              .header("clientID",headerClientID)
              .post(Entity.entity(dataform,
                      MediaType.APPLICATION_FORM_URLENCODED));
        final Response resp = client.target(targetUrl).path("resource/roles/testrole3")
                .request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .delete();
        assertEquals(httpstatus, resp.getStatus());
    }
    
    
    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        try {
            ConstructionFactory.getInstance().getPublisher().stopBroker();
        } catch (ServerPublisherBrokerException e) {
            e.printStackTrace();
        }
        restServer.stopHTTPServer(true);
    }
}
