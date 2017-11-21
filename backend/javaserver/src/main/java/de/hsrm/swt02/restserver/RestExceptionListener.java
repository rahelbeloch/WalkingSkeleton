package de.hsrm.swt02.restserver;

import java.util.logging.Level;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import de.hsrm.swt02.logging.UseLogger;

/**
 * Listener for REST exceptions. Used to log exceptions that
 * are catched by the jersey server.
 */
public class RestExceptionListener implements ApplicationEventListener {

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return new ExceptionRequestEventListener();
    }

    /**
     * Internal class to log exceptions that are thrown while a request is
     * processed. 
     */
    private static class ExceptionRequestEventListener implements
            RequestEventListener 
    {
        
        public static final UseLogger LOGGER = new UseLogger();

        @Override
        public void onEvent(RequestEvent event) {
            switch (event.getType()) {
                case ON_EXCEPTION:
                    final Throwable exception = event.getException();
                    LOGGER.log(Level.WARNING, exception);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onEvent(ApplicationEvent arg0) {
        // Nothing to do
    }
}