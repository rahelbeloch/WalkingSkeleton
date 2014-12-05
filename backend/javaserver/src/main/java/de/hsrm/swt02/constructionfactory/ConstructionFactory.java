package de.hsrm.swt02.constructionfactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;

/**
 * This class provides instances of the Logic interface and Messaging
 * operations.
 *
 */
public class ConstructionFactory {

    private static final Injector INJECTOR = Guice
            .createInjector(new SingleModule());

    private static Logic logic = INJECTOR.getInstance(Logic.class);
    private static ServerPublisher serverPublisher = INJECTOR
            .getInstance(ServerPublisher.class);
    
    public ConstructionFactory() {
        try {
            serverPublisher.startBroker();
        } catch (ServerPublisherBrokerException e) {
            //TODO find a solution for the logging instance-problem
        }
    }

    /**
     * This method returns the Logic instance.
     * 
     * @return logic
     */
    public static Logic getLogic() {
        return logic;
    }

    /**
     * This method returns the instance of ServerPublisher.
     * 
     * @return serverpublisher
     */
    public static ServerPublisher getPublisher() {
        return serverPublisher;
    }

}
