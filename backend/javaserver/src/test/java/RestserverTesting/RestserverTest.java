package RestserverTesting;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.net.httpserver.HttpServer;
import restserver.RestServer;

public class RestserverTest {
	
	static HttpServer restserver;
	
	@BeforeClass
	public static void setUp() {
		restserver = RestServer.startServer();
	}
	
	@Test
	public void reqeustTest () {
		Client client = ClientBuilder.newClient();
		Response resp = client.target("http://localhost:8080").path("resource/abstractworkflow/17").request(MediaType.APPLICATION_XML).get();
		assertEquals(resp.getStatus(),200);
	}
	
	@Test
	public void deleteTest() {
		Client client = ClientBuilder.newClient();
		Response resp = client.target("http://localhost:8080").path("resource/abstractworkflow/17").request().delete();
		assertEquals(resp.getStatus(),200);
	}
	
	@AfterClass
	public static void cleanUp() {
		restserver.stop(5);
	}
}