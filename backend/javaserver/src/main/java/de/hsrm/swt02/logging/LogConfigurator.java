package de.hsrm.swt02.logging;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for setting up the global logger. Invoke setup()-Method to set the
 * configuration.
 */
public class LogConfigurator {

    private static final int LOGFILE_SIZE = 1000000;
    private static final int LOGFILE_AMOUNT = 1;
    private static FileHandler fileHTMLHandler;
    private static Formatter formatterHTML;
    private static Map<String, Level> levelMap;
    
    static {
        final Map<String, Level> unmodifiableMap = new HashMap<String, Level>();
        unmodifiableMap.put("severe", Level.SEVERE);
        unmodifiableMap.put("warning", Level.WARNING);
        unmodifiableMap.put("info", Level.INFO);
        unmodifiableMap.put("config", Level.CONFIG);
        unmodifiableMap.put("fine", Level.FINE);
        unmodifiableMap.put("finer", Level.FINER);
        unmodifiableMap.put("finest", Level.FINEST);
        levelMap = Collections.unmodifiableMap(unmodifiableMap);
    }

    /**
     * Setup method to load the logger configuration from the 'server.config'
     * file. Keep in mind that the configuration is a global setting for the
     * UseLogger.class. That means you have to call this method just once to
     * setup your configuration.
     * 
     * @throws IOException if server.config file can't be found
     */
    public static void setup() {
        final Logger logger = Logger.getLogger(UseLogger.class.getName());
        final Properties properties = new Properties();
        BufferedInputStream stream;
        // properties
        Level logLevel = levelMap.get(properties.getProperty("LogLevel"));
        String consoleLogging = properties.getProperty("ConsoleLogging");
        String logFile = properties.getProperty("LogFile");

        // read configuration file for logging properties
        try {
            stream = new BufferedInputStream(new FileInputStream(
                    "server.config"));
            properties.load(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING,
                    "Couldn't find the 'server.config' for logger setup.");
        } catch (IOException e) {
            logger.log(Level.WARNING,
                    "Couldn't read the 'server.config' for logger setup.");
        } catch (SecurityException e) {
            logger.log(Level.WARNING,
                    "Couldn't read the 'server.config' for logger setup (PERMISSION DENIED).");
        }
        logLevel = levelMap.get(properties.getProperty("LogLevel"));
        consoleLogging = properties.getProperty("ConsoleLogging");
        logFile = properties.getProperty("LogFile");

        // default level for logging
        logger.setLevel(logLevel);
        // set handler for a specific log file
        try {
            fileHTMLHandler = new FileHandler(logFile, LOGFILE_SIZE, LOGFILE_AMOUNT);
        } catch (SecurityException e) {
            logger.log(Level.WARNING,
                    "Couldn't create a file handler for the logging file.");
        } catch (IOException e) {
            logger.log(Level.WARNING,
                    "Couldn't create a file handler for the logging file.");
        }
        // set HTML-Formatter for a logging handler
        formatterHTML = new HTMLFormatter();
        fileHTMLHandler.setFormatter(formatterHTML);
        logger.addHandler(fileHTMLHandler);

        // disable console logging and remove the default
        final Logger rootLogger = Logger.getLogger("");
        final Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        // add consoleHandler if necessary
        if (consoleLogging.equals("true")) {
            final ConsoleLogHandler ourConsoleHandler = new ConsoleLogHandler();
            rootLogger.addHandler(ourConsoleHandler);
        }
    }
}
