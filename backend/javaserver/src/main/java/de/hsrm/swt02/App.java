package de.hsrm.swt02;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.SingleModule;

/**
 * Class for application start. Contains the main-method for the server
 * application. Uses dependency injection.
 */
public class App {
    public static final Injector INJECTOR = Guice
            .createInjector(new SingleModule());

    /**
     * Application startup method.
     * 
     * @param args are the program start parameters
     */
    public static void main(String[] args) {
        // TODO executable main!!!
    }
}
