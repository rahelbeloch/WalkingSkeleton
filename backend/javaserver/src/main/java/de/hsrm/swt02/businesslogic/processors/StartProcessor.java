package de.hsrm.swt02.businesslogic.processors;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;

/**
 * 
 * This class implements the function of StartTrigger. It creates a new item and
 * saves it.
 */
public class StartProcessor {

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
     * @return itemId 
     * @throws LogicException 
     */
    public String createItem(Workflow workflow) throws LogicException {

        currentItem = new Item();
        currentItem.setWorkflowId(workflow.getId());
        
        initiateItem(workflow, currentItem);
        return currentItem.getId();
    }

    /**
     * This method instantiates an item's Metadata. The default MetaState is
     * INACTIVE. Only "Action" step-objects are considered. After successfully
     * creating and initializing the item, add it to workflow. The state of the
     * very first step is set to "OPEN".
     * 
     * @param workflow provides a step list, which will be transformed into Metadatas
     * @param item is the freshly created item for a workflow
     * @throws LogicException
     */
    public void initiateItem(Workflow workflow, Item item) throws LogicException {
        StartStep startStep = null;
        
        for (Step s: workflow.getSteps()) {
            if (s instanceof StartStep) {
                startStep = (StartStep) s;
                break;
            }
        }
        for (Step s : workflow.getSteps()) {
            if (s instanceof Action || s instanceof FinalStep) {
                item.set(s.getId() + "", "step", "");
                item.set("status", s.getId() + "", MetaState.INACTIVE.toString());
            }
        }
        
        if (!item.getForGroup("step").isEmpty()) {
            item.setFirstStepState(startStep.getNextSteps().get(0).getId(), MetaState.OPEN.toString());
        } 
        
        if (workflow.getForm() != null) {
            item.applyForm(workflow.getForm());
        }
        
        workflow.addItem(item);
        p.storeWorkflow(workflow);
        
    }
}
