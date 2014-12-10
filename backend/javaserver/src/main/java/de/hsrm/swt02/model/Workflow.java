package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Workflow. A Workflow is a manifestation of a RootElement
 *
 */
public class Workflow extends RootElement implements Cloneable {
    // Used for (de)serialization. Do not change.
    private List<Step> steps;

    // Used for (de)serialization. Do not change.
    private List<Item> items;
    
    // Used for (de)serializsation. Do not change.
    private boolean active;

    /**
     * Constructor for Workflow without parameters.
     */
    public Workflow() {
        super();
        steps = new ArrayList<Step>();
        items = new ArrayList<Item>();
        active = true;
    }
    

    /**
     * Steps getter. There is no setter because steps is an ArrayList.
     * @return steps is the list of steps of a workflow
     */
    public List<Step> getSteps() {
        return this.steps;
    }

    /**
     * Items getter.
     * @return items are the items corresponding the workflow
     */
    public List<Item> getItems() {
        return this.items;
    }
    
    /**
     * sets items to an empty list
     * @param i
     */
    public void clearItems() {
    	this.items.clear();
    }
    
    /**
     * Active Getter getter.
     * @return active if the workflow is active or not
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Active setter.
     * @param active if the workflow is active or not
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * This method returns a step object by its position in the step list of a
     * workflow.
     * 
     * @param pos states the index of the searched step object
     * @return searched step object if found
     */
    public Step getStepByPos(int pos) {

        return steps.get(pos);
    }
    
    /**
     * This method returns a step object by its id.
     * 
     * @param stepId states which certain object is looked for
     * @return searched step object if found, else return null
     */
    public Step getStepById(int stepId) {
        for (Step s : steps) {
            if (s.getId() == stepId) {
                return s;
            }
        }
        return null;
    }

    /**
     * This method returns an item object by its position in the workflow item
     * list.
     * 
     * @param pos states the index of searched item object
     * @return searched item object if found
     */
    public Item getItemByPos(int pos) {

        return items.get(pos);
    }

    /**
     * This method returns an item object by its id.
     * 
     * @param itemId states which certain object is looked for
     * @return searched item object if found, else return null
     */
    public Item getItemById(int itemId) {

        for (Item ai : items) {
            if (ai.getId() == itemId) {
                return ai;
            }
        }
        return null;
    }

    /**
     * This method adds single steps to a workflow.
     * 
     * @param step which will be added to the step list of a workflow
     */
    public void addStep(Step step) {

        steps.add(step);
        
        if (steps.size() >= 2) {
            steps.get(steps.size() - 2).getNextSteps().add(step);
        }
    }

    /**
     * This method removes a certain step by its id.
     * 
     * @param stepId states which step should be removed
     */
    public void removeStep(int stepId) {

        for (Step as : steps) {
            if (as.getId() == stepId) {
                steps.remove(as);
            }
        }
    }

    /**
     * This method adds a new item of a workflow.
     * 
     * @param item which will be added to the item lsit of a workflow
     */
    public void addItem(Item item) {

        items.add(item);
    }

    /**
     * This method removes a certain item of a workflow.
     * 
     * @param itemId states which item should be removed
     */
    public void removeItem(int itemId) {

        for (Item ai : items) {
            if (ai.getId() == itemId) {
                items.remove(ai);
            }
        }
    }
    
    /**
	 * Deep Copy - Cloning method for Workflows
	 */
    public Object clone() throws CloneNotSupportedException {
        this.convertReferencesToIdList();
        
		Workflow clone = new Workflow();
		clone.setActive(active);
		clone.setId(id);
		for(Step step: this.steps) {
			Step cloneStep = (Step)step.clone();
			clone.addStep(cloneStep);
		}
		for(Item item: this.items) {
			Item cloneItem = (Item)item.clone();
			clone.addItem(cloneItem);
		}
		
		clone.convertIdListToReferences();
		return clone;
	}
    
    /** 
    * This method connects steps with its straight neighbor. The last step has 
    * no neighbor. 
    * 
    * TODO: Works only with linear workflows! Is only used to init test data.
    */ 
    public void connectSteps() { 
            
        
        
        
    } 
    
    /**
     * Incoming order of step ids are converted into references.
     * 
     * @param workflow which is operated on
     */
    public void convertIdListToReferences() {
        for (Step step : this.getSteps()) {
            for (int id : step.getNextStepIds()) {
                step.getNextSteps().add(this.getStepById(id));
            }
        }
    }
    
    /**
     * Workflow references are converted to ids.
     * @param workflow
     */
    public void convertReferencesToIdList() {
        for (Step step : this.getSteps()) {
            step.getNextStepIds().clear();
            for (Step next : step.getNextSteps()) {
                step.getNextStepIds().add(next.getId());
            }
        }
    }
    
    @Override
    public String toString() {
        String ret = "--- WORKFLOW START ---\n";
        ret += "Id: "+ this.id + "\n";
        ret += "Active: "+ this.active + "\n";
        for(Step s : this.steps) {
            ret += "\t--- STEP:\n";
            ret += s.toString();
        }
        
        ret += "\n";
        
        for(Item i : this.items) {
            ret += "\t--- ITEM:\n";
            ret += i.toString();
        }
        ret += "--- WORKFLOW ENDE ---";
        
        return ret;
    }
}
