package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

public class Workflow extends RootElement {
    // Used for (de)serialization. Do not change.
    private List<Step> steps;

    // Used for (de)serialization. Do not change.
    private List<Item> items;

    public Workflow() {
        super();
        steps = new ArrayList<Step>();
        items = new ArrayList<Item>();
    }

    public Workflow(int id) {
        super();
        this.id = id;
        steps = new ArrayList<Step>();
        items = new ArrayList<Item>();
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    public List<Item> getItems() {
        return this.items;
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

        for (Step as : steps) {
            if (as.getId() == stepId) {
                return as;
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
     * This method connects steps with its straight neighbor. The last step has
     * no neighbor.
     */
    public void connectSteps() {

        for (int i = 0; i < steps.size() - 1; i++) {
            steps.get(i).getNextSteps().add(steps.get(i + 1));
        }
    }
}
