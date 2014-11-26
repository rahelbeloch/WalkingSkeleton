package restserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

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

	private static String baseURI;
	public static final int WARTEZEIT = 5;
	
	/**
	 * Creates a new http server to run REST on
	 * @return the instance of the new server
	 */
	public static HttpServer startServer() {
		Properties properties = new Properties();
		BufferedInputStream stream;
		//read configuration file for rest properties
		try {
			stream = new BufferedInputStream(new FileInputStream("server.config"));
			properties.load(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			//TODO LOGGING
		} catch (IOException e) {
			//TODO LOGGING
		} catch (SecurityException e) {
			//TODO LOGGING
		} 
		baseURI = properties.getProperty("RestServerURI");
		
        ResourceConfig rc = new ResourceConfig();
        rc.packages("restserver.resource");
        return JdkHttpServerFactory.createHttpServer(URI.create(baseURI), rc);
    }
	
	public String getBaseURI() {
		return baseURI;
	}
	
	public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", baseURI));
        System.in.read();
        server.stop(WARTEZEIT);
    }
}