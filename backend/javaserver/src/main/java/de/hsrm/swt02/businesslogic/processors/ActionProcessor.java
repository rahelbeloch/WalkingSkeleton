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
 * This class executes "Action" step-objects.
 *
 */
public class ActionProcessor implements StepProcessor {

    private Persistence p;
    public static final UseLogger LOGGER = new UseLogger();

    /**
     * Constructor of ActionProcessor.
     * 
     * @param p is a singleton instance of the persistence
     */
    @Inject
    public ActionProcessor(Persistence p) {
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
        final Step currentStep = workflow.getStepById(step.getId());

        if (item.getEntryValue(step.getId() + "", "step").equals(MetaState.OPEN.toString())) {
            currentStep.setOpener(user.getUsername());
            currentItem.setStepState(step.getId(), MetaState.BUSY.toString());
        } else if (item.getEntryValue(step.getId() + "", "step").equals(MetaState.BUSY.toString())) {
            if (currentStep.getOpener().equals(user.getUsername())) {
                currentItem.setStepState(step.getId(), MetaState.DONE.toString());
                for (Step s : currentStep.getNextSteps()) {
                    if (!(s instanceof FinalStep)) {
                        currentItem.setStepState(s.getId(), MetaState.OPEN.toString());
                    } else {
                        currentItem.setStepState(s.getId(), MetaState.DONE.toString());
                        currentItem.setFinished(true);
                        LOGGER.log(Level.INFO, "[logic] Successfully finished item " + currentItem.getId() + " from workflow " + currentItem.getWorkflowId());
                    }
                }
            } else {
                throw new UserHasNoPermissionException("Access denied. Current Operator is " + currentStep.getOpener());
            }
            
        } else {
            throw new ItemNotForwardableException("no forwarding action on item " + currentItem.getId());
        } 
        
        try {
            p.storeWorkflow(workflow);
        } catch (WorkflowNotExistentException e) {
            e.printStackTrace();
        }
        return currentItem.getId();
    }
}
