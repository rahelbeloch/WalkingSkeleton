package Models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name"
})

@XmlRootElement(name = "Action")
public class ActionModel extends StepModel{
	
	@XmlElement(name = "Name", required = true)
	protected String name;
	@XmlElement(name = "UserName", required = true)
	protected String userName;

	/**
	 * Getter & Setter
	 */
	
    /**
     * Gets the value of the name property.
     * 
     * @return String
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value String
     *     
     */
    public void setName(String value) {
        this.name = value;
    }
    
    
    /**
     * Gets the value of the userName property.
     * 
     * @return String
     *   
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value String
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }
}
