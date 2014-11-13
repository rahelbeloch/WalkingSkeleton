package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "metaList"
})

@XmlRootElement(name = "Item")
public class ItemModel {
	
	@XmlElement(name = "metaList", required = true)
	HashMap<String, String> metaList;
	
	/**
	 * Adds a new Metadata object to the metaList:
	 * if its the very first metadata or the list hasn't been
	 * created: make one, first step is open
	 * else: add one metadata and set the value to inactive
	 * 
	 * @param newMeta Metadata
	 */
	public void addMeta(String stepName){
		if(metaList == null){
			metaList = new HashMap<String, String>();
			metaList.put(stepName, "OPEN");
		}else{
			metaList.put(stepName, "INACTIVE");
		}
	}
	
	/**
	 * Sets a Value of the metaList for the key
	 * stepName to "OPEN"
	 * 
	 * @param stepName String
	 */
	public void setMetaToOpen(String stepName){
		if(metaList.containsKey(stepName)){
			metaList.put(stepName, "OPEN");
		}
	}
	
	/**
	 * Sets a Value of the metaList for the key
	 * stepName to "BUSY"
	 * 
	 * @param stepName String
	 */
	public void setMetaToBusy(String stepName){
		if(metaList.containsKey(stepName)){
			metaList.put(stepName, "BUSY");
		}
	}
	
	/**
	 * Sets a Value of the metaList for the key
	 * stepName to "DONE"
	 * 
	 * @param stepName String
	 */
	public void setMetaToDone(String stepName){
		if(metaList.containsKey(stepName)){
			metaList.put(stepName, "DONE");
		}
	}
	
	/**
	 * Getter & Setter
	 */
	
    /**
     * Gets the value of the metaList property.
     * 
     * @return HashMap<String, String> metaList
     *     
     */
    public HashMap<String, String> getMetaList() {
        if (metaList == null) {
            metaList = new HashMap<String, String>();
        }
        return this.metaList;
    }
    
    /**
     * Sets the value of the property metaList.
     * 
     * @param list HashMap<String, String>
     *     
     */
    public void setMetaList(HashMap<String, String> list) {
        this.metaList = list;
    }
}
