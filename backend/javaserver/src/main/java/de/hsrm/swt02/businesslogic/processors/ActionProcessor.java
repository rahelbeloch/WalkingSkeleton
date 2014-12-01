package de.hsrm.swt02.businesslogic.processors;

import java.util.Observable;

import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.persistence.Persistence;

import com.google.inject.Inject;

/**
 * This class executes "Action" step-objects.
 *
 */
public class ActionProcessor extends Observable implements StepProcessor {

    private Item currentItem;
    private Persistence p;

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
     * saved. First the current step's state of the item will be set on "BUSY".
     * After successfully executing the Action-function the current step's state
     * will be set on "DONE". If the next step isn't an end state, this
     * processor sets the state of the current step's straight neighbor to OPEN.
     * 
     * @param item which is currently edited
     * @param step which is currently executed
     * @param user who currently executes the step
     */
    public void handle(Item item, Step step, User user) {

        currentItem = item;
        currentItem.setStepState(step.getId(), MetaState.BUSY.toString());
        currentItem.setState("upd");
        p.storeItem(currentItem);
        setChanged();
        notifyObservers(currentItem);

        // funktion irrelevant fuer walking skeleton
        currentItem.setStepState(step.getId(), MetaState.DONE.toString());
        currentItem.setState("upd");
        p.storeItem(currentItem);
        setChanged();
        notifyObservers(currentItem);

        for (Step s : step.getNextSteps()) {
            if (!(s instanceof FinalStep)) {
                currentItem.setStepState(s.getId(), MetaState.OPEN.toString());
                currentItem.setState("upd");
                p.storeItem(currentItem);
                setChanged();
                notifyObservers(currentItem);
            } else {
                currentItem.setStepState(s.getId(), MetaState.DONE.toString());
                currentItem.setFinished(true);
                currentItem.setState("upd");
                p.storeItem(currentItem);
                setChanged();
                notifyObservers(currentItem);
            }
        }
    }
}
