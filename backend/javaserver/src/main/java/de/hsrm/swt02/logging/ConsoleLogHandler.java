package de.hsrm.swt02.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Custom ConsoleHandler.
 * Logs messages to console.
 * If the logLevel for a message is higher or equals WARNING
 * the message will be published on system.err
 * Otherwise the message will be published on system.out
 */
public class ConsoleLogHandler extends Handler {

    @Override
    public void close() throws SecurityException {
        this.close();
    }

    @Override
    public void flush() {
        this.flush();
    }
    
    @Override
    public void publish(LogRecord rec) {
        final Level currentLevel = rec.getLevel();
        String logMessage;
        String msg;
        final Throwable ex =  rec.getThrown();
       
        if (ex != null) {
            msg = ex.getClass().getName() + " - " + ex.getMessage();
        }
        else {
            msg = rec.getMessage();
        }
        logMessage = currentLevel + " " + calcDate(rec.getMillis()) + " " + msg;
        
        if (currentLevel.intValue() >= Level.WARNING.intValue()) {
            System.err.println(logMessage);
        }
        else {
            System.out.println(logMessage);
        }
    }
    
    /**
     * Returns the current date and time.
     * 
     * @param millisecs time to the current date
     * @return the current date and time
     */
    private String calcDate(long millisecs) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(
                "[MMM dd,yyyy HH:mm]");
        final Date resultDate = new Date(millisecs);
        return dateFormat.format(resultDate);
    }

}
