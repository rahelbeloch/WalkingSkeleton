package de.hsrm.swt02.businesslogic.processors;

import java.util.Observable;

import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;

import com.google.inject.Inject;

/**
 * 
 * This class implements the function of StartTrigger. It creates a new item and
 * saves it.
 */
public class StartProcessor extends Observable {

    private Item currentItem;
    private Persistence p;

    /**
     * Constructor of StartProcessor.
     * 
     * @param p is a singleton instance of the persistence
     */
    @Inject
    public StartProcessor(Persistence p) {
        this.p = p;
    }

    /**
     * This method receives an workflow-object and creates an item and calls a
     * method which instantiates its Metadata according to the workflow's
     * initialized steps. The item consists of pairs of step IDs and its
     * MetaState.
     * 
     * @param workflow it's id will be noted within a new item
     */
    public void createItem(Workflow workflow) {

//        final int key = 1000;
        currentItem = new Item();
//        currentItem.setId(workflow.getId() * key + workflow.getSteps().size() + workflow.getItems().size());
//        System.out.println(currentItem.getId());
        currentItem.setWorkflowId(workflow.getId());
        initiateItem(workflow, currentItem);
    }

    /**
     * This method instantiates an item's Metadata. The default MetaState is
     * INACTIVE. Only "Action" step-objects are considered. After successfully
     * creating and initializing the item, add it to workflow. The state of the
     * very first step is set to "OPEN".
     * 
     * @param workflow provides a step list, which will be transformed into
     *            Metadatas
     * @param item is the freshly created item for a workflow
     */
    public void initiateItem(Workflow workflow, Item item) {

        for (Step s : workflow.getSteps()) {
            if (s instanceof Action || s instanceof FinalStep) {
                item.set(s.getId() + "", "step", MetaState.INACTIVE.toString());
            }
        }
        workflow.getItems().add(item);
        if (!item.getForGroup("step").isEmpty()) {
            item.setFirstStepState(MetaState.OPEN.toString());
        }

        p.storeItem(item);
        setChanged();
        item.setState("def");
        notifyObservers(item);
    }
}
