package de.hsrm.testswt02.RestserverTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.properties.ConfigProperties;
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
        targetUrl = ConfigProperties.getInstance().getProperties().getProperty("RestServerURI");
    }

    /**
     * This Test checks if an workflow can be successfully updated.
     * Its success is granted if the response equals code 200.
     * @throws StorageFailedException 
     */
    @Test
    public void testUpdate() throws StorageFailedException {
        final int httpstatus = 200;
        final Workflow workflow = new Workflow();
        final Step step1 = new StartStep();
        step1.setId("start");
        final Step step2 = new Action();
        step2.setId("mitte");
        final Step step3 = new FinalStep();
        step3.setId("ende");
        step1.getRoleIds().add("Manager");
        step2.getRoleIds().add("Manager");
        step3.getRoleIds().add("Sachbearbeiter");
        workflow.addStep(step1);
        workflow.addStep(step2);
        workflow.addStep(step3);
        step1.getNextStepIds().add("mitte");
        step2.getNextStepIds().add("ende");
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
                .path("resource/workflows/1")
                .request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .put(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(resp.getStatus(), httpstatus);
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
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        workflowAsString = client.target(targetUrl)
                .path("resource/workflows/1").request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .get(String.class);
        
        try {
            workflow = mapper.readValue(workflowAsString, Workflow.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(workflow.getId().equals("1"));
    }

    /**
     * This Test checks if a client can post a flawless workflow to the server.
     * It's successful if the response code is 200.
     * @throws StorageFailedException 
     */
    @Test
    public void testPost() throws StorageFailedException {
        final int httpstatus = 200;
        final Workflow workflow = new Workflow();
        final Step step1 = new StartStep();
        step1.setId("start");
        final Step step2 = new Action();
        step2.setId("mitte");
        final Step step3 = new FinalStep();
        step3.setId("ende");
        step1.getRoleIds().add("Manager");
        step2.getRoleIds().add("Manager");
        step3.getRoleIds().add("Sachbearbeiter");
        workflow.addStep(step1);
        workflow.addStep(step2);
        workflow.addStep(step3);
        step1.getNextStepIds().add("mitte");
        step2.getNextStepIds().add("ende");
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
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .post(Entity.entity(dataform,
                        MediaType.APPLICATION_FORM_URLENCODED));
        assertEquals(httpstatus, resp.getStatus());
    }

    /**
     * This Tests checks if a nonexistent workflow can be deleted.
     * It's successful if the response code is 500.
     */
    @Test
    public void testDelete() {
        final int url500 = 500;
        final Response resp = client.target(targetUrl).path("resource/workflows/0")
                .request()
                .header("username", headerUsername)
                .header("password", headerPW)
                .header("clientID",headerClientID)
                .delete();
        assertEquals(url500, resp.getStatus());
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