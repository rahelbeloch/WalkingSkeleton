package de.hsrm.swt02.logging;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Class for setting up the global logger. 
 * Invoke setup()-Method to set the configuration.
 */
public class LogConfigurator {
	
    static private FileHandler fileHTMLHandler;
    static private Formatter formatterHTML;
    static private HashMap<String, Level> levelMap = new HashMap<>();
    
    static public void setup() throws IOException {
        Logger logger = Logger.getLogger(UseLogger.class.getName());
        levelMap.put("severe", Level.SEVERE);
        levelMap.put("warning", Level.WARNING);
        levelMap.put("info", Level.INFO);
        levelMap.put("config", Level.CONFIG);
        levelMap.put("fine", Level.FINE);
        levelMap.put("finer", Level.FINER);
        levelMap.put("finest", Level.FINEST);
        Properties properties = new Properties();
        BufferedInputStream stream;
        //read configuration file for logging properties
        try {
            stream = new BufferedInputStream(new FileInputStream("server.config"));
            properties.load(stream);
            stream.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } catch (SecurityException e) {

        }
        //read properties
        Level logLevel = levelMap.get(properties.getProperty("LogLevel"));
        String consoleLogging = properties.getProperty("ConsoleLogging");
        
        //default level for logging
        logger.setLevel(logLevel);
        fileHTMLHandler = new FileHandler("log.html");
        formatterHTML = new HTMLFormatter();
        fileHTMLHandler.setFormatter(formatterHTML);
        logger.addHandler(fileHTMLHandler);
        
        //disable console logging
        if (consoleLogging.equals("false")) {
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            if (handlers[0] instanceof ConsoleHandler) {
      	        rootLogger.removeHandler(handlers[0]);
            }
        }
	}
}
