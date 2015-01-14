package de.hsrm.swt02.restserver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Level;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.properties.ConfigProperties;
import de.hsrm.swt02.restserver.resource.CheckLoginFilter;

/**
 * This class defines the REST-Server.
 * 
 */
public class RestServer {

    private static final int WAITING_TIME = 5; //seconds
    private static final int ADDITIONAL_TIME = 500; //millisecs
    private UseLogger logger = new UseLogger();
    private HttpServer server;
    private String baseURI;

    /** 
     * Constructor.
     */
    public RestServer() {

        baseURI = ConfigProperties.getInstance().getProperties().getProperty("RestServerURI");
        
    }

    /**
     * Starts HTTP-server to run REST on.
     */
    public void startHTTPServer() {
        final ResourceConfig rc = new ResourceConfig();
        rc.packages("de.hsrm.swt02.restserver.resource");
        rc.register(RestExceptionListener.class);
        rc.register(CheckLoginFilter.class);
        logger.log(Level.INFO, "Starting HTTP-Server...");
        server = JdkHttpServerFactory.createHttpServer(URI.create(baseURI), rc);
        logger.log(Level.INFO, "HTTP-Server started (type 'stop' for proper shutdown)");
    }

    /**
     * Stops HTTP-server. The server waits a few seconds before it stops. Giving
     * all open requests enough time to run through.
     * The HTTP-Server can be forced to stop immediately.
     * 
     * @param forceServerStop forces the HTTP-Server to stop without waiting
     */
    public void stopHTTPServer(boolean forceServerStop) {
        if (server == null) {
            return;
        }
        
        final int waitingTime = forceServerStop ? 0 : WAITING_TIME;
        server.stop(waitingTime);        
        
        // wait for the server stop (make it sync.)
        try {
            Thread.sleep(waitingTime + ADDITIONAL_TIME);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Sleep-Interrupt during stop of HTTP-Server");
        }
        logger.log(Level.INFO, "HTTP-Server stopped...");
    }

    /**
     * This method returns the URI for the HTTP-server.
     * 
     * @return the URI for the HTTP-server
     */
    public String getBaseURI() {
        return baseURI;
    }
}