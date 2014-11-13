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

@XmlRootElement(name = "User")
public class UserModel {
	
	@XmlElement(name = "name", required = true)
	protected String name;
	
	
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

}
