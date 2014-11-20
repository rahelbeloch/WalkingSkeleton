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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AbstractStep complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AbstractStep">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nextSteps" type="{http://www.example.org/Beans}AbstractStep" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractStep", propOrder = {
    "id",
    "nextSteps"
})
@XmlSeeAlso({
    AbstractAction.class,
    AbstractStartStep.class,
    AbstractFinalStep.class
})
public class AbstractStep {

    @XmlElement(name = "Id")
    protected int id;
    protected List<AbstractStep> nextSteps;

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
     * Gets the value of the nextSteps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nextSteps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNextSteps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractStep }
     * 
     * 
     */
    public List<AbstractStep> getNextSteps() {
        if (nextSteps == null) {
            nextSteps = new ArrayList<AbstractStep>();
        }
        return this.nextSteps;
    }

}
