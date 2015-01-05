package de.hsrm.swt02.messaging;

/** Enumeration for message operations.
 *  Every operation has its own protocol representation.
 */
public enum MessageOperation {
    DEFINITION("def"),
    DELETION("del"),
    UPDATE("upd");
    
    private final String protocolInfo;
    
    /** Constructor.
     * 
     * @param protocolInfo describes the protocol string representation for this operation 
     */
    private MessageOperation(String protocolInfo) {
        this.protocolInfo = protocolInfo;
    }
    
    /**
     * @return the protocol representation for this MessageOperation 
     */
    public String getProtocolString() {
        return protocolInfo;
    }
}
