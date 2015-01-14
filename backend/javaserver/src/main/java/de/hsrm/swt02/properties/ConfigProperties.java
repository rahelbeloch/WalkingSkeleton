package de.hsrm.swt02.properties;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;

import de.hsrm.swt02.logging.UseLogger;

/**
 * Class responsible for loading the properties initially. can be globally accessed to get Properties. Implements
 * singleton pattern.
 *
 */
public class ConfigProperties {
    
    private static final String CONFIG_NAME = "server.config";
    
    private static ConfigProperties theInstance;
    private Properties properties;
    private UseLogger logger;
    
    /**
     * private constructor.
     */
    private ConfigProperties() {
        logger = new UseLogger();
        BufferedInputStream stream;
        properties = new Properties();
        
        if (findExistingConfig()) {
            try {
             // read configuration file for rest properties
                stream = new BufferedInputStream(new FileInputStream( CONFIG_NAME ));
                properties.load(stream);
                stream.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Can't read file!");
            }
        }
        else {
            try {
                // write default configuration file
                final PrintWriter pWriter = new PrintWriter(new BufferedWriter(new FileWriter( CONFIG_NAME )));
                pWriter.println("BrokerURL = vm://localhost");
                pWriter.println("BrokerConnectionURL = tcp://0.0.0.0:61616");
                pWriter.println("RestServerURI = http://0.0.0.0:18887/");
                pWriter.println("LogFile = serverlog.html");
                pWriter.println("LogLevel = info");
                pWriter.println("ConsoleLogging = true");
                pWriter.flush();
                pWriter.close();
                // and read it
                stream = new BufferedInputStream(new FileInputStream( CONFIG_NAME ));
                properties.load(stream);
                stream.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Configuration file could not be generated!");
            }
        }
    }
    
    /**
     * 
     * takes care of the fact that only one instance of this class exists.
     * 
     * @return theInstance instance of Properties
     */
    public synchronized static ConfigProperties getInstance() {
        if (theInstance == null) {
            theInstance = new ConfigProperties();
        }
        return theInstance;
    }
   
    /**
     * Method to determine whether or not a config file is already existing.
     * @return true if the config file is existing, false if not
     */
    private static boolean findExistingConfig() {
        
        final File dir = new File(".");
        final String configName = ("server.config");
        
        final File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equalsIgnoreCase(configName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 
     * getter for the properties.
     * 
     * @return properties of the configFile
     */
    public Properties getProperties() {
        return this.properties;
    }
}
