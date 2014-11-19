package RestserverTesting;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.Test;

import beans.Workflow;

import com.sun.net.httpserver.HttpServer;

import restserver.RestServer;



public class RestserverTest {
	
	@BeforeClass
	public static void setUp() {
		final HttpServer restserver = RestServer.startServer();
		restserver.toString();
	}
	
	@Test
	public void reqeustTest () {
		Client client = ClientBuilder.newClient();
		Workflow workflow = client.target("http://localhost:8080").path("items/workflow/17").request(MediaType.APPLICATION_XML).get(Workflow.class);
		assertEquals(workflow.getId(),17);
	}
	
	@Test
	public void postTest () {
		
	}
	
}
