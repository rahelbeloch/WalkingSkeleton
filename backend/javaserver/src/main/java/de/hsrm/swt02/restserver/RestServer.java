package de.hsrm.swt02.restserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

import de.hsrm.swt02.moduledi.JerseyDIBinder;

/**
 * REST-Server
 * 
 * @author akoen001
 */
public class RestServer {

    private static final int WAITING_TIME = 5;
    private HttpServer server;
    private String baseURI;

    /**
     * Constructor
     */
    public RestServer() {
        Properties properties = new Properties();
        BufferedInputStream stream;
        // read configuration file for rest properties
        try {
            stream = new BufferedInputStream(new FileInputStream(
                    "server.config"));
            properties.load(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            // TODO LOGGING
        } catch (IOException e) {
            // TODO LOGGING
        } catch (SecurityException e) {
            // TODO LOGGING
        }
        baseURI = properties.getProperty("RestServerURI");
    }

    /**
     * Starts http server to run REST on
     */
    public void startHTTPServer() {
        ResourceConfig rc = new ResourceConfig();
        rc.packages("de.hsrm.swt02.restserver.resource");
        rc.register(new JerseyDIBinder());
        server = JdkHttpServerFactory.createHttpServer(URI.create(baseURI), rc);
    }

    /**
     * Stops http server The server waits a few seconds before it stops. Giving
     * all open requests enough time to run through.
     */
    public void stopHTTPServer() {
        server.stop(WAITING_TIME);
    }

    /**
     * @returns the URI for the http server
     */
    public String getBaseURI() {
        return baseURI;
    }

    public static void main(String[] args) {
        RestServer server = new RestServer();
        server.startHTTPServer();
    }
}