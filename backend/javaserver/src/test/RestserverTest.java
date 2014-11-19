package de.hsrm.mi.gruppe02.javaserver;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.net.httpserver.HttpServer;

import de.hsrm.mi.gruppe02.javaserver.beans.Workflow;
import restserver.RestServer;



public class RestserverTest {
	
	@BeforeClass
	public static void setUp() {
		RestServer server = new RestServer();
		final HttpServer restserver = server.startServer();
	}
	
	@Test
	public void serverTest () {
		Client client = ClientBuilder.newClient();
		Workflow workflow = client.target("http://localhost:8080").path("items/workflow/17").request(MediaType.APPLICATION_XML).get(Workflow.class);
		assertEquals(workflow.getId(),17);
	}
	
}
