package de.hsrm.swt02.restserver;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for communication between REST-Server and Businesslogic.
 *
 */
public class LogicResponse {
	
	private Map<String, String> messages;
	
	/**
	 * Default-Constructor.
	 */
	public LogicResponse() {
		setMessages(new HashMap<String, String>());
	}

	/**
	 * Getter for messages map.
	 * @return messages
	 */
	public Map<String, String> getMessages() {
		return messages;
	}

	/**
	 * Setter for messages map.
	 * @param messages
	 */
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}

}
