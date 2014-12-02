package de.hsrm.swt02;

import de.hsrm.swt02.logging.LogConfigurator;
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
        final RestServer server;

        // setup log configuration
        LogConfigurator.setup();
        
        // start rest-server instance
        server = new RestServer();
        server.startHTTPServer();
    }
}
