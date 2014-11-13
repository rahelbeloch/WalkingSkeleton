package Models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "stepList",
    "itemList"
})

@XmlRootElement(name = "Workflow")
public class WorkflowModel {
	
	@XmlElement(name = "stepList", required = true)
	protected LinkedList<StepModel> stepList;
	@XmlElement(name = "itemList", required = true)
	protected LinkedList<ItemModel> itemList;
	
	/**
	 * Adds a new Step to the stepList:
	 * if its the very first step or the list hasn't been
	 * created: make one
	 * 
	 * @param newStep Step
	 */
	public void addStep(StepModel newStep){
		if(stepList == null){
			stepList = new LinkedList<StepModel>();
		}
		stepList.add(newStep);
	}
	
	/**
	 * Adds a new Item to the itemList:
	 * if its the very first item or the list hasn't been
	 * created: make one
	 * 
	 * @param newItem Item
	 */
	public void addItem(ItemModel newItem){
		if(itemList == null){
			itemList = new LinkedList<ItemModel>();
		}
		itemList.add(newItem);
	}
	
	/**
	 * Getter & Setter
	 */
	
    /**
     * Gets the value of the stepList property.
     * 
     * @return LinkedList<Step>
     *     
     */
    public List<StepModel> getStepList() {
        if (stepList == null) {
            stepList = new LinkedList<StepModel>();
        }
        return this.stepList;
    }
    
    /**
     * Sets the value of the name stepList.
     * 
     * @param list LinkedList<Step>
     *     
     */
    public void setStepList(LinkedList<StepModel> list) {
        this.stepList = list;
    }
    
    /**
     * Gets the value of the stepList property.
     * 
     * @return LinkedList<Item>
     *     
     */
    public List<ItemModel> getItemList() {
        if (itemList == null) {
            itemList = new LinkedList<ItemModel>();
        }
        return this.itemList;
    }
    
    /**
     * Sets the value of the property itemList.
     * 
     * @param list LinkedList<Item>
     *     
     */
    public void setName(LinkedList<ItemModel> list) {
        this.itemList = list;
    }

}
