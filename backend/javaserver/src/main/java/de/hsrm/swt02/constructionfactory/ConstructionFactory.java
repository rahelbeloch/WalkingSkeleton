package de.hsrm.swt02.constructionfactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;

/**
 * This class provides instances of the Logic interface and Messaging
 * operations.
 * Uses a Singleton pattern.
 */
public class ConstructionFactory {

    private static final Injector INJECTOR = Guice
            .createInjector(new SingleModule());
    
    private static Logic logic; 
    private static ServerPublisher serverPublisher;
    private static ConstructionFactory theInstance;

    /**
     * Constructor.
     * @param properties that have been read out of the config file
     */
    private ConstructionFactory(Properties properties) {
        logic = INJECTOR.getInstance(Logic.class);
        logic.setPropConfig(properties);
        logic.loadData();
        
        serverPublisher = INJECTOR.getInstance(ServerPublisher.class);
        serverPublisher.applyProperties(properties);
        try {
            serverPublisher.startBroker();
        } catch (ServerPublisherBrokerException e) {
            final UseLogger logger = new UseLogger();
            logger.log(Level.SEVERE, e);
        }
    }
    
    /**
     * Return the instance for this class.
     * @param properties that have been read out of the config file
     * @return the instance
     */
    public synchronized static ConstructionFactory getInstance(Properties properties) {
        if (theInstance == null) {
            theInstance = new ConstructionFactory(properties);
        }
        return theInstance;
    }
    
    /**
     * Return the instance for this class(for testing).
     * @return the instance
     */
    public synchronized static ConstructionFactory getInstance() {
        
        if (theInstance == null) {
            final UseLogger logger = new UseLogger();
            final Properties properties = new Properties();
            BufferedInputStream stream;
            // read configuration file for rest properties
            try {
                stream = new BufferedInputStream(new FileInputStream(
                        "server.config"));
                properties.load(stream);
                stream.close();
            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE, "Configuration file not found!");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Can't read file!");
            } catch (SecurityException e) {
                logger.log(Level.SEVERE, "Read Access not granted!");
            }
            theInstance = new ConstructionFactory(properties);
        }
        return theInstance;
    }

    /**
     * This method returns the Logic instance.
     * 
     * @return logic the instance responsible for server logic
     */
    public Logic getLogic() {
        return logic;
    }

    /**
     * This method returns the instance of ServerPublisher.
     * 
     * @return serverPublisher the instance responsible for messaging purpose
     */
    public ServerPublisher getPublisher() {
        return serverPublisher;
    }

}
