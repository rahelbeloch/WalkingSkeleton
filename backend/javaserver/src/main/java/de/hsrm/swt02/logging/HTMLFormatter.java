package de.hsrm.swt02.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**HtmlFormatter for our logger file handler
 */
public class HTMLFormatter extends Formatter{

	/**
	 * makes sure every entry of the log file is right
	 */
	@Override
	public String format(LogRecord rec) {	
		StringBuffer buf = new StringBuffer(1000);
		buf.append("<tr>\n");
		
		if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
			buf.append("\t<td style=\"color:red\">");
			buf.append("<b>");
			buf.append(rec.getLevel());
			buf.append("</b>");
		} else {
			buf.append("\t<td>");
			buf.append(rec.getLevel());
		}
		
		buf.append("</td>\n");
		buf.append("\t<td>");
		buf.append(calcDate(rec.getMillis()));
		buf.append("</td>\n");
		buf.append("\t<td>");
		buf.append(formatMessage(rec));
		buf.append("</td>\n");
		buf.append("</td>\n");
		
		return buf.toString();
	}
	
	/**Returns the current date and time.
	 * @param millisecs
	 * @returns the current date and time
	 */
	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}
	
	/**Generates the HTML-header for the log file.
	 * @returns the head for the HTML log file
	 */
	public String getHead(Handler h){
		return "<!DOCTYPE html>\n<head>\n<style"
				+ "type=\"text/css\">\n"
				+ "table { width: 100% }\n"
				+ "th { font:bold 10pt Tahoma; }\n"
				+ "td { font:normal 10pt Tahoma; }\n"
				+ "h1 {font:normal 11pt Tahoma;}\n"
				+ "</style>\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "<h1>" + (new Date()) + "</h1>\n"
				+ "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n"
				+ "<tr align=\"left\">\n"
				+ "\t<th style=\"width:10%\">Loglevel</th>\n"
				+ "\t<th style=\"width:15%\">Time</th>\n"
				+ "\t<th style=\"width:75%\">Log Message</th>\n"
				+ "</tr>\n";
	}
	
	/**Generates the HTML-tail for the log file
	 * @returns the tail for the HTML log file
	 */
	public String getTail(Handler h) {
		return "</table>\n</body>\n</html>";
	}
}
