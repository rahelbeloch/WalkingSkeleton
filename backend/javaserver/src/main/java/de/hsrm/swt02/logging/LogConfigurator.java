package de.hsrm.swt02.logging;

import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**Class for setting up the global logger. 
 * Invoke setup()-Method to set the configuration.
 */
public class LogConfigurator {
	
    static private FileHandler fileHTMLHandler;
    static private Formatter formatterHTML;
    
    static public void setup() throws IOException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
//        Logger rootLogger = Logger.getLogger("");
//        Handler[] handlers = rootLogger.getHandlers();
//        if (handlers[0] instanceof ConsoleHandler) {
//        	rootLogger.removeHandler(handlers[0]);
//        }
        
        //default level for logging
        logger.setLevel(Level.INFO);
        fileHTMLHandler = new FileHandler("log.html");
        
        formatterHTML = new HTMLFormatter();
        fileHTMLHandler.setFormatter(formatterHTML);
        logger.addHandler(fileHTMLHandler);
	}
}
