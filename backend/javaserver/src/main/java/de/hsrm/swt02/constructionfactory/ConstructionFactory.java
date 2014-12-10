package de.hsrm.swt02.constructionfactory;

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
     */
    private ConstructionFactory() {
        logic = INJECTOR.getInstance(Logic.class);
        serverPublisher = INJECTOR.getInstance(ServerPublisher.class);
        try {
            serverPublisher.startBroker();
        } catch (ServerPublisherBrokerException e) {
            final UseLogger logger = new UseLogger();
            logger.log(Level.SEVERE, e);
        }
    }
    
    /**
     * Return the instance for this class.
     * @return the instance
     */
    public synchronized static ConstructionFactory getInstance() {
        if (theInstance == null) {
            theInstance = new ConstructionFactory(); 
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
