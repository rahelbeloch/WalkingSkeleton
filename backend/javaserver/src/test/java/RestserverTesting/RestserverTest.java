package RestserverTesting;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;

import abstractbeans.AbstractWorkflow;

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
		AbstractWorkflow workflow = client.target("http://localhost:8080").path("items/workflow/17").request(MediaType.APPLICATION_XML).get(AbstractWorkflow.class);
		assertEquals(workflow.getId(),17);
	}
	
	@Test
	public void deleteTest() {
		Client client = ClientBuilder.newClient();
		Response resp = client.target("http://localhost:8080").path("delete/workflow/17").request().delete();
		assertEquals(resp.getStatus(),200);
	}
}