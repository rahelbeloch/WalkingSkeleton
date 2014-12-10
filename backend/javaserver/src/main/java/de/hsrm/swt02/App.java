package de.hsrm.swt02;

import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.restserver.RestServer;

/**
 * Class for application start. Contains the main-method for the server.
 * Loads the logging configuration. Initiates business logic.
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
        
        //performance optimization
        new Thread(new Runnable() {
            public void run() {
                ConstructionFactory.getInstance();
            }
        }).start();
        
        // start rest-server instance
        server = new RestServer();
        
        //ShutDownHook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (server != null) {
                    server.stopHTTPServer(true);
                }
            }
        });
        
        server.startHTTPServer();
    }
}
