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

@XmlRootElement(name = "Step")
public class StepModel {
	
	/**
	 * Constructor for the class
	 * Step doesnt have anything because other variations 
	 * of steps besides of action is not needed
	 */
	public StepModel(){
	}

}
