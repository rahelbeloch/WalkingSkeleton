package de.hsrm.swt02.businesslogic.processors;

import java.util.Observable;
import java.util.logging.Level;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * This class executes "Action" step-objects.
 *
 */
public class ActionProcessor extends Observable implements StepProcessor {

    private Item currentItem;
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
     * @exception ItemNotForwardableException if the steplist of the responding item can't go any further
     * @exception UserHasNoPermissionException if the given user has no right to operate on the step
     * @throws StorageFailedException 
     * @throws ItemNotExistentException 
     */
    public void handle(Item item, Step step, User user) throws ItemNotForwardableException, UserHasNoPermissionException, ItemNotExistentException, StorageFailedException {
        
        final String username = user.getUsername();
        
        if (!username.equals(step.getUsername())) {
            throw new UserHasNoPermissionException("user " + username + "has no permission on this item");
        }

        currentItem = item;

        if (currentItem.getEntryValue(step.getId() + "", "step").equals(MetaState.OPEN.toString())) {
            currentItem.setStepState(step.getId(), MetaState.BUSY.toString());
        } else if (currentItem.getEntryValue(step.getId() + "", "step").equals(MetaState.BUSY.toString())) {
            currentItem.setStepState(step.getId(), MetaState.DONE.toString());
            for (Step s : step.getNextSteps()) {
                if (!(s instanceof FinalStep)) {
                    currentItem.setStepState(s.getId(), MetaState.OPEN.toString());
                } else {
                    currentItem.setStepState(s.getId(), MetaState.DONE.toString());
                    currentItem.setFinished(true);
                    LOGGER.log(Level.INFO, "[logic] Successfully finished item " + currentItem.getId() + " from workflow " + currentItem.getWorkflowId());
                }
            }
        } else {
            throw new ItemNotForwardableException("no forwarding action on item " + currentItem.getId());
        } 
        
        currentItem.setState("upd");
        
        try {
            p.storeItem(currentItem);
        } catch (WorkflowNotExistentException e) {
            e.printStackTrace();
        }
        
        setChanged();
        notifyObservers(currentItem);
    }
}
