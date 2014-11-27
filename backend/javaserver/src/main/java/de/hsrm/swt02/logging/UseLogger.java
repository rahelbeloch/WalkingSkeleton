package de.hsrm.swt02.logging;

import java.util.logging.Logger;
import java.util.logging.Level;

/** Logging class
 *  Can be used for any class.
 *  The logger has a logging level which can be set to adjust the logger.
 */
public class UseLogger {
    private final static Logger LOGGER = Logger.getLogger(UseLogger.class.getName());
    
    /**
     * can be used to set the accepted Log level
     * @param level: must be java.util.logging.Level.SEVERE, WARNING, INFRO, CONFIG, FINE, FINER or FINEST
     */
    public void setLoggingLevel(Level level){
    	LOGGER.setLevel(level);
    }
    
    /**
     * can be used to add an entry to the log, level: SEVERE
     * @param logMsg - message
     */
    public void logSevere(String logMsg) {
        LOGGER.severe(logMsg);
    }
    
    /**
     * can be used to add an entry to the log, level: WARNING
     * @param logMsg - message
     */
    public void logWarning(String logMsg) {
    	LOGGER.warning(logMsg);
    }
    
    /**
     * can be used to add an entry to the log, level: INFO
     * @param logMsg - message
     */
    public void logInfo(String logMsg) {
    	LOGGER.info(logMsg);
    }
    
    /**
     * can be used to add an entry to the log, level: CONFIG
     * @param logMsg - message
     */
    public void logConfig(String logMsg) {
    	LOGGER.config(logMsg);
    }
    
    /**
     * can be used to add an entry to the log, level: FINE
     * @param logMsg - message
     */
    public void logFine(String logMsg) {
    	LOGGER.fine(logMsg);
    }
    
    /**
     * can be used to add an entry to the log, level: FINER
     * @param logMsg - message
     */
    public void logFiner(String logMsg) {
    	LOGGER.finer(logMsg);
    }
    
    /**
     * can be used to add an entry to the log, level: FINEST
     * @param logMsg
     */
    public void logFinest(String logMsg) {
    	LOGGER.finest(logMsg);
    }
}
