package de.hsrm.swt02.messaging;

/** Enumeration for message topics.
 *  Every topic has its own protocol representation.
 */
public enum MessageTopic {
    
    WORKFLOW_INFO("workflow"),
    USER_INFO("user"),
    ROLE_INFO("role"),
    ITEMS_FROM_("item");
    
    private final String protocolInfo;
    
    /** Constructor.
     * 
     * @param protocolInfo describes the topic representation in the protocol string
     */
    private MessageTopic(String protocolInfo) {
        this.protocolInfo = protocolInfo;
    }
    
    /**
     * @return the protocol representation for this MessateTopic 
     */
    public String getProtocolString() {
        return protocolInfo;
    }
}
