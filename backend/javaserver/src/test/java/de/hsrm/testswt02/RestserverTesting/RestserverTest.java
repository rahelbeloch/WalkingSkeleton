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

import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.restserver.RestServer;

/**
 * Unit-test class for REST-Server and Resources.
 * CRUD options are tested in the following methods:
 *    - testUpdate()
 *    - testGet()
 *    - testPost()
 *    - testDelete()
 */
public class RestserverTest {

    public static RestServer restServer;
    public static Client client;
    public static String targetUrl;

    /**
     * This method sets and starts the REST-Server. Additionally it provides a test client.
     */
    @BeforeClass
    public static void setUp() {
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
        } catch (IOException e) {
        } catch (SecurityException e) {
        }
        targetUrl = properties.getProperty("RestServerURI");
    }

    /**
     * This Test checks if an workflow can be successfully updated.
     * Its success is granted if the response equals code 200.
     */
    @Test
    public void testUpdate() {
        final Workflow workflow = new Workflow();
        workflow.setId(17);
        final ObjectMapper mapper = new ObjectMapper();
        String workflowAsString = null;
        
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final Form dataform = new Form().param("data", workflowAsString);
        final Response resp = client
                .target(targetUrl)
                .path("resource/workflow/17")
                .request()
                .put(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(resp.getStatus(), 200);
    }

    /**
     * This test checks if a the client can get a flawless workflow from the server.
     */
    @Test
    public void testGet() {
        Workflow workflow = new Workflow();
        final ObjectMapper mapper = new ObjectMapper();
        String workflowAsString = null;
        
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final Form dataform = new Form().param("data", workflowAsString);
        client.target(targetUrl)
                .path("resource/workflow")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        workflowAsString = client.target(targetUrl)
                .path("resource/workflow/1").request().get(String.class);
        
        try {
            workflow = mapper.readValue(workflowAsString, Workflow.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(workflow.getId(), 1);
    }

    /**
     * This Test checks if a client can post a flawless workflow to the server.
     * It's successful if the response code is 200.
     */
    @Test
    public void testPost() {
        final Workflow workflow = new Workflow();
        final ObjectMapper mapper = new ObjectMapper();
        String workflowAsString = null;
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final Form dataform = new Form().param("data", workflowAsString);
        final Response resp = client
                .target(targetUrl)
                .path("resource/workflow")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(200, resp.getStatus());
    }

    /**
     * This Tests checks if a nonexistent workflow can be deleted.
     * It's successful if the response code is 500.
     */
    @Test
    public void testDelete() {
        final Response resp = client.target(targetUrl).path("resource/workflow/0")
                .request().delete();
        assertEquals(500, resp.getStatus());
    }

    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        restServer.stopHTTPServer(true);
    }
}