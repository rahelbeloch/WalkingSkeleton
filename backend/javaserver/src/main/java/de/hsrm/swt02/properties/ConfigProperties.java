package de.hsrm.swt02.properties;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import de.hsrm.swt02.logging.UseLogger;

/**
 * Class responsible for loading the properties initially. can be globally accessed to get Properties. Implements
 * singleton pattern.
 *
 */
public class ConfigProperties {
    
    private static final String CONFIG_PATH = "server.config";
    
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
        
        final File f = new File(CONFIG_PATH);
        
        if (f.exists()) {
            try {
             // read configuration file for rest properties
                stream = new BufferedInputStream(new FileInputStream( f ));
                properties.load(stream);
                stream.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Can't read file!");
                logger.log(Level.WARNING, e);
                loadDefaultProperties();
            }
        }
        else {
            loadDefaultProperties();
        }
    }
    
    /** Method loads the default Properties and writes them to a configuration file.
     *  The path for this configuration file is specified by the CONFIG_PATH constant. 
     */
    private void loadDefaultProperties() {
        try {
            properties.setProperty("BrokerURL", "vm://localhost");
            properties.setProperty("BrokerConnectionURL", "tcp://0.0.0.0:61616");
            properties.setProperty("RestServerURI", "http://0.0.0.0:18887/");
            properties.setProperty("LogFile", "serverlog.html");
            properties.setProperty("LogLevel", "info");
            properties.setProperty("ConsoleLogging", "true");
            properties.setProperty("StoragePath", "dataModel.ser");
            
            // write default configuration file
            properties.store(new FileOutputStream(CONFIG_PATH), "");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Configuration file could not be generated!");
            logger.log(Level.WARNING, e);
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
     * 
     * getter for the properties.
     * 
     * @return properties of the configFile
     */
    public Properties getProperties() {
        return this.properties;
    }
}
