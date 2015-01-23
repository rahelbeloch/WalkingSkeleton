package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.hsrm.swt02.persistence.exceptions.StorageFailedException;

/**
 * This class represents a Workflow. A Workflow is a manifestation of a
 * RootElement
 *
 */
public class Workflow extends RootElement {
   
    @JsonIgnore
    private static final long serialVersionUID = 6064167361250573486L;

    // Used for (de)serialization. Do not change.
    private List<Step> steps;

    // Used for (de)serialization. Do not change.
    private List<Item> items;

    // Used for (de)serializsation. Do not change.
    private boolean active;

    private Form form;

    private String name;
    
    /**
     * Constructor for Workflow without parameters.
     */
    public Workflow() {
       this("No Name Workflow");
    }
    
    /**
     * Constructor for passing a name for this workflow.
     * @param name of the workflow
     */
    public Workflow(String name) {
        super();
        steps = new ArrayList<Step>();
        items = new ArrayList<Item>();
        form = new Form();
        active = true;
        this.name = name;
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
     * 
     * @return items are the items corresponding the workflow
     */
    public List<Item> getItems() {
        return this.items;
    }
    
    /**
     * 
     * @param form the form to set
     */
    public void setForm(Form form) {
        this.form = form;
    }
    
    /**
     * form getter.
     * @return form
     */
    public Form getForm() {
        return form;
    }
    
    /**
     * Active Getter getter.
     * 
     * @return active if the workflow is active or not
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Active setter.
     * 
     * 
     * @param active
     *            if the workflow is active or not
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * This method returns a step object by its position in the step list of a
     * workflow.
     * 
     * @param pos
     *            states the index of the searched step object
     * @return searched step object if found
     */
    public Step getStepByPos(int pos) {

        return steps.get(pos);
    }

    /**
     * This method returns a step object by its id.
     * 
     * @param stepId
     *            states which certain object is looked for
     * @return searched step object if found, else return null
     */
    public Step getStepById(String stepId) {
        for (Step s : steps) {
            if (s.getId().equals(stepId)) {
                return s;
            }
        }
        return null;
    }

    /**
     * This method returns an item object by its position in the workflow item
     * list.
     * 
     * @param pos
     *            states the index of searched item object
     * @return searched item object if found
     */
    public Item getItemByPos(int pos) {

        return items.get(pos);
    }

    /**
     * This method returns an item object by its id.
     * 
     * @param itemId
     *            states which certain object is looked for
     * @return searched item object if found, else return null
     */
    public Item getItemById(String itemId) {

        for (Item ai : items) {
            if (ai.getId().equals(itemId)) {
                return ai;
            }
        }
        return null;
    }

    /**
     * This method adds single steps to a workflow.
     * 
     * @param step
     *            which will be added to the step list of a workflow
     * @throws StorageFailedException 
     */
    public void addStep(Step step) throws StorageFailedException {

        if (step.getRoleIds().size() < 1) {
            throw new StorageFailedException("[logic] a step without roles cannot be added.");
        }
        steps.add(step);

        if (steps.size() > 1) {
            steps.get(steps.size() - 2).getNextSteps().add(step);
        }
    }

    /**
     * This method removes a certain step by its id.
     * 
     * @param stepId
     *            states which step should be removed
     */
    public void removeStep(String stepId) {

        for (Step as : steps) {
            if (as.getId().equals(stepId)) {
                steps.remove(as);
            }
        }
    }

    /**
     * This method adds a new item of a workflow.
     * 
     * @param item
     *            which will be added to the item list of a workflow
     */
    public void addItem(Item item) {
        
        items.add(item);
    }

    /**
     * This method removes a certain item of a workflow.
     * 
     * @param itemId
     *            states which item should be removed
     */
    public void removeItem(String itemId) {

        Item itemToRemove = null;
        for (Item ai : items) {
            if (ai.getId().equals(itemId)) {
                itemToRemove = ai;
            }
        }
        if (itemToRemove != null) {
            this.items.remove(itemToRemove);
        }
    }
    
    /**
     * This method tells if a workflow has unfinished items.
     * @return true if there are unfinished items else false
     */
    public boolean hasUnfinishedItem() {
        if (items != null) {
            for (Item item : items) {
                if (!(item.isFinished())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * This method returns all unfinished items of its workflow.
     * @return list of unfinished (finished = false) items
     */
    public List<Item> unfinishedItems() {
        final List<Item> unfinishedItems = new ArrayList<Item>();
        for (Item item : items) {
            if (!(item.isFinished())) {
                unfinishedItems.add(item);
            }
        }
        return unfinishedItems;
    }

    /**
     * Deep Copy - Cloning method for Workflows.
     * 
     * @exception CloneNotSupportedException convention
     * @throws CloneNotSupportedException
     * @return Object clone
     */
    public Object clone() throws CloneNotSupportedException {
        this.convertReferencesToIdList();

        final Workflow clone = new Workflow();
        clone.setActive(active);
        clone.setId(id);
        for (Step step : this.steps) {
            final Step cloneStep = (Step) step.clone();
            clone.getSteps().add(cloneStep);
        }
        for (Item item : this.items) {
            final Item cloneItem = (Item) item.clone();
            clone.addItem(cloneItem);
        }

        clone.convertIdListToReferences();
        clone.setForm(form);
        return clone;
    }

    /**
     * Incoming order of step ids are converted into references.
     */
    public void convertIdListToReferences() {
        for (Step step : this.getSteps()) {
            for (String id : step.getNextStepIds()) {
                step.getNextSteps().add(this.getStepById(id));
            }
        }
    }

    /**
     * Workflow references are converted to ids.
     * 
     * @param workflow
     */
    public void convertReferencesToIdList() {
        for (Step step : this.getSteps()) {
            step.getNextStepIds().clear();
            for (Step next : step.getNextSteps()) {
                if (!step.getNextStepIds().contains(next.getId())) { 
                    step.getNextStepIds().add(next.getId());
                }
            }
        }
    }

    @Override
    public String toString() {
        String ret = "--- WORKFLOW START ---\n";
        ret += "Id: " + this.id + "\n";
        ret += "Active: " + this.active + "\n";
        for (Step s : this.steps) {
            ret += "\t--- STEP:\n";
            ret += s.toString();
        }

        ret += "\n";

        for (Item i : this.items) {
            ret += "\t--- ITEM:\n";
            ret += i.toString();
        }
        ret += "--- WORKFLOW ENDE ---";

        return ret;
    }

    /**
     * Setter for name.
     * @param name of workflow
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gettter for name.
     * @return name of wf
     */
    public String getName() {
        return this.name;
    }
}
