package de.hsrm.swt02.businesslogic.processors;

import java.util.logging.Level;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * This class executes "Fork" step-objects.
 *
 */
public class ForkProcessor implements StepProcessor {

    private Persistence p;
    public static final UseLogger LOGGER = new UseLogger();

    /**
     * Constructor of ForkProcessor.
     * 
     * @param p is a singleton instance of the persistence
     */
    @Inject
    public ForkProcessor(Persistence p) {
        this.p = p;
    }

    /**
     * This method is initiated by an User. The responsible User sends the item
     * and the step, which they wish to edit. The sent item will be modified and
     * saved. The current step's state of the item will be set on "BUSY" only if
     * it was setted to "OPEN".
     * 
     * @param item which is currently edited
     * @param step which is currently executed
     * @param user who currently executes the step
     * @throws LogicException if there are problems while working on an item
     * @return itemId of item which was edited
     */
    public String handle(Item item, Step step, User user) throws LogicException {
        final Workflow workflow = p.loadWorkflow(item.getWorkflowId());      
        final Item currentItem = workflow.getItemById(item.getId());
        final String stepId = step.getId();
        final Step currentStep = workflow.getStepById(stepId);
        final String itemId = currentItem.getId();
        
        if (currentItem.getEntryValue("status", stepId + "").equals(MetaState.OPEN.toString())) {
            
            currentItem.setEntryOpener(stepId, user.getUsername());
            currentItem.setStepState(stepId, MetaState.BUSY.toString());
        } else if (currentItem.getEntryValue("status", stepId + "").equals(MetaState.BUSY.toString())) {
            
            if (currentItem.getEntryOpener(stepId).equals(user.getUsername())) {
                currentItem.setStepState(stepId, MetaState.DONE.toString());
                for (Step s : currentStep.getNextSteps()) {
                    if (!(s instanceof FinalStep)) {
                        currentItem.setStepState(s.getId(), MetaState.OPEN.toString());
                    } else {
                        currentItem.setStepState(s.getId(), MetaState.DONE.toString());
                        currentItem.setFinished(true);
                        LOGGER.log(Level.INFO, "[logic] Successfully finished item " + itemId + " from workflow " + currentItem.getWorkflowId());
                    }
                }
            } else {
                throw new UserHasNoPermissionException("Access denied. Current Operator is " + currentItem.getEntryOpener(stepId));
            }
            
        } else {
            throw new ItemNotForwardableException("no forwarding action on item " + itemId);
        } 
        
        try {
            p.storeWorkflow(workflow);
        } catch (WorkflowNotExistentException e) {
            e.printStackTrace();
        }
        return itemId;
    }
}
