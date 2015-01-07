package de.hsrm.swt02.messaging;

/**
 * This class is used for the LogicResponse. 
 * It contains a topic and a value.
 * A protocol conform can be built by calling build() or buildWithTopicId().
 */
public class Message {

    private static final String SEPARATOR = "=";
    
    private String topic;
    private String value;

    /**
     * Constructor of Message.
     * 
     * @param topic indicates its information group
     * @param value is the actual information
     */
    private Message(String topic, String value) {
        this.topic = topic;
        this.value = value;
    }
    
    /**Builds a message protocol conform message.
     * 
     * @param topic defines which message topic shall be used
     * @param operation defines the rest operation which shall be used
     * @param sourceId is the identifier for the related object (persistence)
     * @return a protocol conform message 
     */
    public static Message build(MessageTopic topic, MessageOperation operation, String sourceId) {
        final String protocolMsg = topic.getProtocolString() + SEPARATOR + operation.getProtocolString() + SEPARATOR + sourceId;
        final String topicName = topic.toString();
        
        return new Message(topicName, protocolMsg);
    }
   
    /**Builds a message protocol conform message.
     * 
     * @param topic defines which message topic shall be used
     * @param topicId is the identifier for the topic
     * @param operation defines the rest operation which shall be used
     * @param sourceId is the identifier for the related object (persistence)
     * @return a protocol conform message
     */
    public static Message buildWithTopicId(MessageTopic topic, String topicId, MessageOperation operation, String sourceId) {
        final String protocolMsg = topic.getProtocolString() + SEPARATOR + operation.getProtocolString() + SEPARATOR + sourceId;
        final String topicName = topic.toString() + topicId;
        
        return new Message(topicName, protocolMsg);
    }

    /**
     * Getter for topic.
     * 
     * @return topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Getter for value.
     * 
     * @return value
     */
    public String getValue() {
        return value;
    }
}
