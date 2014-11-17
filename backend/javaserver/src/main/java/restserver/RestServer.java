package restserver;

import java.io.IOException;
import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class RestServer {

	public static final String BASE_URI = "http://0.0.0.0:8080/";
	public static final int WARTEZEIT = 5;
	
	public static HttpServer startServer() {
        ResourceConfig rc = new ResourceConfig();
    	// hier werden Resource-Klassen gesucht
        rc.packages("restserver");
        //rc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,true);
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