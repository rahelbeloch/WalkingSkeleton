package de.hsrm.swt02;

import java.io.IOException;
import java.util.logging.Level;

import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.restserver.RestServer;

/**
 * Class for application start. Contains the main-method for the server.
 * Starts the Rest-server.
 */
public class App {
    /**
     * Application startup method.
     * Configures logging.
     * Starts the Rest-Server.
     * 
     * @param args are the program start parameters
     */
    public static void main(String[] args) {
        final UseLogger logger;
        final RestServer server;
        
        // initialize logging
        logger = new UseLogger();
        // setup log configuration
        try {
            LogConfigurator.setup();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e);
        }
        // start rest-server instance
        server = new RestServer();
        server.startHTTPServer();
        logger.log(Level.INFO, "Rest-Server started.");
    }
}
