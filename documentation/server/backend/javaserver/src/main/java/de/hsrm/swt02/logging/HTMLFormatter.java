package de.hsrm.swt02.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * HtmlFormatter for our logger file handler.
 */
public class HTMLFormatter extends Formatter {

    /**
     * Formats the given log entry to HTML format.
     */
    @Override
    public String format(LogRecord rec) {
        final StringBuffer buffer = new StringBuffer(1000);
        final String exceptionName;
        final String exceptionMessage;

        buffer.append("<tr>\n");

        // log level
        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
            buffer.append("\t<td style=\"color:red\">");
            buffer.append("<b>");
            buffer.append(rec.getLevel());
            buffer.append("</b>");
        } else {
            buffer.append("\t<td>");
            buffer.append(rec.getLevel());
        }
        buffer.append("</td>\n");

        // exception message
        if (rec.getThrown() != null) {
            buffer.append("\t<td style=\"color:red\">");
            buffer.append(calcDate(rec.getMillis()));
            buffer.append("</td>\n");
            exceptionName = rec.getThrown().getClass().getSimpleName();
            exceptionMessage = rec.getThrown().getMessage();
            buffer.append("\t<td style=\"color:red\">");
            buffer.append(" EXCEPTION: " + exceptionName + " - "
                    + exceptionMessage);
            // normal log message
        } else {
            buffer.append("\t<td>");
            buffer.append(calcDate(rec.getMillis()));
            buffer.append("</td>\n");
            buffer.append("\t<td>");
            buffer.append(formatMessage(rec));
        }
        buffer.append("</td>\n");
        buffer.append("</td>\n");

        return buffer.toString();
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

    /**
     * Generates the HTML-header for the log file.
     * 
     * @param h handler for this class (not used here)
     * @return the head for the HTML log file
     */
    public String getHead(Handler h) {
        return "<!DOCTYPE html>\n<head>\n"
                + "</head>\n" + "<body>\n" + "<h2>" + (new Date()) + "</h2>\n"
                + "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n"
                + "<tr align=\"left\">\n"
                + "\t<th style=\"width:15%\">Loglevel</th>\n"
                + "\t<th style=\"width:25%\">Time</th>\n"
                + "\t<th style=\"width:60%\">Log Message</th>\n" + "</tr>\n";
    }

    /**
     * Generates the HTML-tail for the log file.
     * 
     * @param h handler for this class (not used here)
     * @return the tail for the HTML log file
     */
    public String getTail(Handler h) {
        return "</table>\n</body>\n</html>";
    }
}
