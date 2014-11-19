package restserver;

import java.io.IOException;
import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

/**
 * 
 * @author akoen001
 * 
 * REST-Server, located on localhost:8080
 */
public class RestServer {

	public static final String BASE_URI = "http://0.0.0.0:8080/";
	public static final int WARTEZEIT = 5;
	
	/**
	 * Creates a new http server to run REST on
	 * @return the instance of the new server
	 */
	public static HttpServer startServer() {
        ResourceConfig rc = new ResourceConfig();
        rc.packages("restserver.resource");
        return JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
	
	public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop(WARTEZEIT);
    }
}