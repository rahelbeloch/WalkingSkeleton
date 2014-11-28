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

import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.restserver.RestServer;

public class RestserverTest {
	
	public static RestServer restServer;
	public static Client client;
	public final String TARGET_URL = "http://localhost:8080";
	
	@BeforeClass
	public static void setUp() {
		restServer = new RestServer();
		restServer.startHTTPServer();
		client = ClientBuilder.newClient();
	}
	
	@Test
	public void testUpdate () {
		Workflow workflow = new Workflow();
		workflow.setId(17);
		ObjectMapper mapper = new ObjectMapper();
		String workflowAsString = null;
		try {
			workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Form dataform = new Form()
		.param("data", workflowAsString);
		Response resp = client.target(TARGET_URL).path("resource/workflow/17").request()
				.put(Entity.entity(dataform, MediaType.APPLICATION_FORM_URLENCODED));
		assertEquals(resp.getStatus(), 200);
	}
	
	@Test
	public void testGet () {
		ObjectMapper mapper = new ObjectMapper();
		String workflowAsString = client.target(TARGET_URL).path("resource/workflow/17").request().get(String.class);
		Workflow workflow = null;
		try {
			workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(workflow.getId(), 17);
	}
	
	@Test
	public void testPost() {
		Workflow workflow = new Workflow();
		workflow.setId(17);
		ObjectMapper mapper = new ObjectMapper();
		String workflowAsString = null;
		try {
			workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Form dataform = new Form()
		.param("data", workflowAsString);
		Response resp = client.target(TARGET_URL).path("resource/workflow").request()
				.post(Entity.entity(dataform, MediaType.APPLICATION_FORM_URLENCODED));
		assertEquals(200, resp.getStatus());
	}
	
	@Test
	public void testDelete() {
		Response resp = client.target(TARGET_URL).path("resource/workflow/17").request().delete();
		assertEquals(200, resp.getStatus());
	}
	
	@AfterClass
	public static void cleanUp() {
		client.close();
		restServer.stopHTTPServer();
	}
}