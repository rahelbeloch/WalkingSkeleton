package de.hsrm.swt02.logging;

import java.util.logging.Logger;
import java.util.logging.Level;

/** Logging class
 *  Can be used for any class.
 *  The logger has a logging level which can be set to adjust the logger.
 */
public class UseLogger {
    private final static Logger LOGGER = Logger.getLogger(UseLogger.class
    		    .getName());
    
    public void setLoggingLevel(Level level){
    	LOGGER.setLevel(level);
    }
    
    public void logSevere(String logMsg) {
        LOGGER.severe(logMsg);
    }
    
    public void logWaring(String logMsg) {
    	LOGGER.warning(logMsg);
    }
    
    public void logInfo(String logMsg) {
    	LOGGER.info(logMsg);
    }
    
    public void logConfig(String logMsg) {
    	LOGGER.config(logMsg);
    }
    
    public void logFine(String logMsg) {
    	LOGGER.fine(logMsg);
    }
    
    public void logFiner(String logMsg) {
    	LOGGER.finer(logMsg);
    }
    
    public void logFinest(String logMsg) {
    	LOGGER.finest(logMsg);
    }
}
