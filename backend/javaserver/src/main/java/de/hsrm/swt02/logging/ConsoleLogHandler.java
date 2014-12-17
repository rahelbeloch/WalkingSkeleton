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
    
    private static final boolean SHOW_TRACE = true;

    @Override
    public void close() {

    }

    @Override
    public void flush() {
        System.out.flush();
        System.err.flush();
    }
    
    @Override
    public void publish(LogRecord rec) {
        final Level currentLevel = rec.getLevel();
        final String timeStamp = calcDate(rec.getMillis());
        final Throwable ex =  rec.getThrown();
        String logMessage;
        String msg;
        
        // exception?
        if (ex != null) {
            msg = ex.getClass().getName() + " - " + ex.getMessage();
        }
        // normal message
        else {
            msg = rec.getMessage();
        }
        
        logMessage = currentLevel + " " + timeStamp + " " + msg;
        
        // console output
        if (currentLevel.intValue() >= Level.WARNING.intValue()) {
            if (SHOW_TRACE && ex != null) {
                System.err.print(currentLevel + " " + timeStamp + " ");
                ex.printStackTrace();
            }
            else {
                System.err.println(logMessage);
            }
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
