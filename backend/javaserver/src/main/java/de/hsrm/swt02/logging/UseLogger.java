package de.hsrm.swt02.logging;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Logging class Can be used for any class. The logger has a logging level which
 * can be set to adjust the logger.
 */
public class UseLogger {
    private final static Logger LOGGER = Logger.getLogger(UseLogger.class
            .getName());

    /**
     * Sets the logging level for the logger to the given log level.
     * 
     * @param level is the value for the next logging operations
     */
    public void setLoggingLevel(Level level) {
        LOGGER.setLevel(level);
    }

    /**
     * Logs a message with logging level SEVERE.
     * 
     * @param logMsg is the message to log
     */
    public void logSevere(String logMsg) {
        LOGGER.severe(logMsg);
    }

    /**
     * Logs a message with logging level WARNING.
     * 
     * @param logMsg is the message to log
     */
    public void logWarning(String logMsg) {
        LOGGER.warning(logMsg);
    }

    /**
     * Logs a message with logging level INFO.
     * 
     * @param logMsg is the message to log
     */
    public void logInfo(String logMsg) {
        LOGGER.info(logMsg);
    }

    /**
     * Logs a message with logging level CONFIG.
     * 
     * @param logMsg is the message to log
     */
    public void logConfig(String logMsg) {
        LOGGER.config(logMsg);
    }

    /**
     * Logs a message with logging level FINE.
     * 
     * @param logMsg is the message to log
     */
    public void logFine(String logMsg) {
        LOGGER.fine(logMsg);
    }

    /**
     * Logs a message with logging level FINER.
     * 
     * @param logMsg is the message to log
     */
    public void logFiner(String logMsg) {
        LOGGER.finer(logMsg);
    }

    /**
     * Logs a message with logging level FINEST.
     * 
     * @param logMsg is the message to log
     */
    public void logFinest(String logMsg) {
        LOGGER.finest(logMsg);
    }

    /**
     * Logs a message String into the logging file. The logging file is
     * specified in the 'server.config' file. If the specified log level is
     * lower than the current log level, the message wont be logged.
     * 
     * @param logLevel is the urgency level for log message
     * @param logMsg is the message that should be logged
     */
    public void log(Level logLevel, String logMsg) {
        LOGGER.log(logLevel, logMsg);
    }

    /**
     * Logs an exception message into the logging file. The logging file is
     * specified in the 'server.config' file. If the specified log level is
     * lower than the current log level, the message wont be logged.
     * 
     * @param logLevel is the urgency level for log message
     * @param throwable is the source for the log information
     */
    public void log(Level logLevel, Throwable throwable) {
        LOGGER.log(logLevel, "", throwable);
    }
}
