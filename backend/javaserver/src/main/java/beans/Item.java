//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.11.19 um 10:44:15 AM CET 
//


package beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Item complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Item">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "id",
    "workflowId",
    "metadata",
    "finished"
})
public class Item {

    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "WorkflowId")
    protected int workflowId;
    @XmlElement(name = "Metadata", required = true)
    protected Map metadata;
    protected boolean finished;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der workflowId-Eigenschaft ab.
     * 
     */
    public int getWorkflowId() {
        return workflowId;
    }

    /**
     * Legt den Wert der workflowId-Eigenschaft fest.
     * 
     */
    public void setWorkflowId(int value) {
        this.workflowId = value;
    }

    /**
     * Ruft den Wert der metadata-Eigenschaft ab.
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
     * Legt den Wert der metadata-Eigenschaft fest.
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
     * Ruft den Wert der finished-Eigenschaft ab.
     * 
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Legt den Wert der finished-Eigenschaft fest.
     * 
     */
    public void setFinished(boolean value) {
        this.finished = value;
    }

}
