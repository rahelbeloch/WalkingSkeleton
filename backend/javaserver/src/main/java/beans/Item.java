//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.19 at 09:06:19 AM CET 
//


package beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Item complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Item">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WorkflowId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Metadata" type="{http://www.example.org/Beans}Map"/>
 *         &lt;element name="finished" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Item", propOrder = {
    "workflowId",
    "metadata",
    "finished"
})
public class Item {

    @XmlElement(name = "WorkflowId")
    protected int workflowId;
    @XmlElement(name = "Metadata", required = true)
    protected Map metadata;
    protected boolean finished;

    /**
     * Gets the value of the workflowId property.
     * 
     */
    public int getWorkflowId() {
        return workflowId;
    }

    /**
     * Sets the value of the workflowId property.
     * 
     */
    public void setWorkflowId(int value) {
        this.workflowId = value;
    }

    /**
     * Gets the value of the metadata property.
     * 
     * @return
     *     possible object is
     *     {@link Map }
     *     
     */
    public Map getMetadata() {
        return metadata;
    }

    /**
     * Sets the value of the metadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link Map }
     *     
     */
    public void setMetadata(Map value) {
        this.metadata = value;
    }

    /**
     * Gets the value of the finished property.
     * 
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets the value of the finished property.
     * 
     */
    public void setFinished(boolean value) {
        this.finished = value;
    }

}
