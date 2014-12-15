package de.hsrm.testswt02.RestserverTesting;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
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
public class RestserverWorkflowTest {

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
        final Step step1 = new StartStep();
        final Step step2 = new Step();
        final Step step3 = new FinalStep();
        step1.setUsername("A");
        step2.setUsername("B");
        step3.setUsername("D");
        workflow.addStep(step1);
        workflow.addStep(step2);
        workflow.addStep(step3);
        workflow.setId("5");
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
                .path("resource/workflows/17")
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
                .path("resource/workflows")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        workflowAsString = client.target(targetUrl)
                .path("resource/workflows/1").request().get(String.class);
        
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
        final Step step1 = new StartStep();
        final Step step2 = new Step();
        final Step step3 = new FinalStep();
        step1.setUsername("A");
        step2.setUsername("B");
        step3.setUsername("C");
        workflow.addStep(step1);
        workflow.addStep(step2);
        workflow.addStep(step3);
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
                .path("resource/workflows")
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
        final Response resp = client.target(targetUrl).path("resource/workflows/0")
                .request().delete();
        assertEquals(500, resp.getStatus());
    }
    
    /**
     * This test creates a User and a Workflow assigned to this User.
     * Expects to get the Workflow back by requesting all workflows of the User
     */
    @Test
    public void testGetWorkflowsByUser() {
        
        final ObjectMapper mapper = new ObjectMapper();
        
        final Workflow workflow = new Workflow();
        final Step step1 = new StartStep();
        final Step step2 = new Step();
        final Step step3 = new FinalStep();
        step1.setUsername("Belex");
        step2.setUsername("Belex");
        step3.setUsername("Belex");
        workflow.addStep(step1);
        workflow.addStep(step2);
        workflow.addStep(step3);
        
        final User BELEX = new User();
        BELEX.setUsername("Belex");
        
        String workflowAsString = null;
        String userAsString = null;
        
        final int HTTPStatus = 200;
        
        //Store Workflow in persistence
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Form dataform = new Form().param("data", workflowAsString);
        Response resp = client
                .target(targetUrl)
                .path("resource/workflows")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        
        //Store User "Belex" in persistence
        try {
            userAsString = mapper.writeValueAsString(BELEX);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        dataform = new Form().param("data", userAsString);
        resp = client
                .target(targetUrl)
                .path("resource/users")
                .request()
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        
        workflowAsString = client.target(targetUrl)
                .path("resource/workflows").request().header("username", "Belex").get(String.class);
        
        //Get all workflows for User "Belex"
        List<Workflow> wList = null;
        Workflow recievedWorkflow = null;
        final JavaType type = mapper.getTypeFactory().
                constructCollectionType(List.class, Workflow.class);
        try {
            wList = mapper.readValue(workflowAsString, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (wList != null) {
            assertEquals(1,wList.size());
        }
        
        if (wList != null) {
            recievedWorkflow = wList.get(0);
        }
        
        assertEquals(workflow.getSteps().get(0).getUsername(),recievedWorkflow.getSteps().get(0).getUsername());
        assertEquals(HTTPStatus, resp.getStatus());
        
    }

    /**
     * After the test execution, stop server and close client.
     */
    @AfterClass
    public static void cleanUp() {
        restServer.stopHTTPServer(true);
    }
}