//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.11.20 um 02:28:57 PM CET 
//


package abstractbeans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AbstractItem complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AbstractItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="WorkflowId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Metadata" type="{http://www.example.org/Beans}AbstractMetaEntry" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "AbstractItem", propOrder = {
    "id",
    "workflowId",
    "metadata",
    "finished"
})
public class AbstractItem {

    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "WorkflowId")
    protected int workflowId;
    @XmlElement(name = "Metadata")
    protected List<AbstractMetaEntry> metadata;
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
     * Gets the value of the metadata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractMetaEntry }
     * 
     * 
     */
    public List<AbstractMetaEntry> getMetadata() {
        if (metadata == null) {
            metadata = new ArrayList<AbstractMetaEntry>();
        }
        return this.metadata;
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
