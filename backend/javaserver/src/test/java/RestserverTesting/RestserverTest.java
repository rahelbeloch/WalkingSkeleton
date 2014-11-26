package RestserverTesting;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import restserver.RestServer;

public class RestserverTest {
	
	public static RestServer restServer;
	
	@BeforeClass
	public static void setUp() {
		restServer = new RestServer();
		restServer.startHTTPServer();
	}
	
	@Test
	public void testRequest () {
		Client client = ClientBuilder.newClient();
		Response resp = client.target("http://localhost:8080").path("resource/workflow/17").request().get();
		assertEquals(200, resp.getStatus());
	}
	
	@Test
	public void testDelete() {
		Client client = ClientBuilder.newClient();
		Response resp = client.target("http://localhost:8080").path("resource/workflow/17").request().delete();
		assertEquals(200, resp.getStatus());
	}
	
	@AfterClass
	public static void cleanUp() {
		restServer.stopHTTPServer();
	}
}