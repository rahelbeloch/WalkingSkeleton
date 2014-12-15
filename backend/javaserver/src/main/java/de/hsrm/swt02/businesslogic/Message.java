package de.hsrm.swt02.businesslogic;

/**
 * This class is used for the LogicResponse. It contains a topic and a value.
 *
 */
public class Message {

    private String topic;
    private String value;

    /**
     * Default-Constructor.
     */
    public Message() {

    }

    /**
     * Constructor of Message.
     * 
     * @param topic indicates its information group
     * @param value is the actual information
     */
    public Message(String topic, String value) {
        this.topic = topic;
        this.value = value;
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
     * Setter for topic.
     * 
     * @param topic
     *            under which the information should be saved
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Getter for value.
     * 
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter for value.
     * 
     * @param value
     *            of the saved information
     */
    public void setValue(String value) {
        this.value = value;
    }

}
