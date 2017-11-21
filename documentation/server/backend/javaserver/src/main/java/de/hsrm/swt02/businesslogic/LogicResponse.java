package de.hsrm.swt02.businesslogic;

import java.util.LinkedList;
import java.util.List;

import de.hsrm.swt02.businesslogic.protocol.Message;

/**
 * This class is used for communication. LogicResponses are used for publishing messages to the message broker.
 *
 */
public class LogicResponse {

    private List<Message> messages;

    /**
     * Constructor for LogicResponse.
     */
    public LogicResponse() {
        setMessages(new LinkedList<Message>());
    }

    /**
     * This method adds new message object m to messages-list.
     * @param m new object which will be added
     */
    public void add(Message m) {
        messages.add(m);
    }
    
    /**
     * Getter for messages.
     * 
     * @return messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Setter for messages.
     * 
     * @param messages which saves Message objects
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
