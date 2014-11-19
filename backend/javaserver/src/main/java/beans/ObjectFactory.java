//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2014.11.19 um 10:44:15 AM CET 
//


package beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the beans package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TestAction_QNAME = new QName("http://www.example.org/Beans", "TestAction");
    private final static QName _TestItem_QNAME = new QName("http://www.example.org/Beans", "TestItem");
    private final static QName _TestUser_QNAME = new QName("http://www.example.org/Beans", "TestUser");
    private final static QName _TestWorkflow_QNAME = new QName("http://www.example.org/Beans", "TestWorkflow");
    private final static QName _TestFinalStep_QNAME = new QName("http://www.example.org/Beans", "TestFinalStep");
    private final static QName _TestMap_QNAME = new QName("http://www.example.org/Beans", "TestMap");
    private final static QName _TestStep_QNAME = new QName("http://www.example.org/Beans", "TestStep");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Step }
     * 
     */
    public Step createStep() {
        return new Step();
    }

    /**
     * Create an instance of {@link Map }
     * 
     */
    public Map createMap() {
        return new Map();
    }

    /**
     * Create an instance of {@link FinalStep }
     * 
     */
    public FinalStep createFinalStep() {
        return new FinalStep();
    }

    /**
     * Create an instance of {@link Workflow }
     * 
     */
    public Workflow createWorkflow() {
        return new Workflow();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Action }
     * 
     */
    public Action createAction() {
        return new Action();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Action }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/Beans", name = "TestAction")
    public JAXBElement<Action> createTestAction(Action value) {
        return new JAXBElement<Action>(_TestAction_QNAME, Action.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/Beans", name = "TestItem")
    public JAXBElement<Item> createTestItem(Item value) {
        return new JAXBElement<Item>(_TestItem_QNAME, Item.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/Beans", name = "TestUser")
    public JAXBElement<User> createTestUser(User value) {
        return new JAXBElement<User>(_TestUser_QNAME, User.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Workflow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/Beans", name = "TestWorkflow")
    public JAXBElement<Workflow> createTestWorkflow(Workflow value) {
        return new JAXBElement<Workflow>(_TestWorkflow_QNAME, Workflow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FinalStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/Beans", name = "TestFinalStep")
    public JAXBElement<FinalStep> createTestFinalStep(FinalStep value) {
        return new JAXBElement<FinalStep>(_TestFinalStep_QNAME, FinalStep.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Map }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/Beans", name = "TestMap")
    public JAXBElement<Map> createTestMap(Map value) {
        return new JAXBElement<Map>(_TestMap_QNAME, Map.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Step }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/Beans", name = "TestStep")
    public JAXBElement<Step> createTestStep(Step value) {
        return new JAXBElement<Step>(_TestStep_QNAME, Step.class, null, value);
    }

}
