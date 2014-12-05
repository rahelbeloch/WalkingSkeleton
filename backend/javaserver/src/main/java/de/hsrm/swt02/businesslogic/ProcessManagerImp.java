package de.hsrm.swt02.businesslogic;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.businesslogic.processors.ActionProcessor;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;

/**
 * This class handles the processing of Steps. (For now) it provides methods for
 * checking whether an user can execute a step and selecting the right processor
 * for a step.
 *
 */
@Singleton
public class ProcessManagerImp implements Observer, ProcessManager {

    private Persistence persistence;
    private LogicResponse logicResponse;
    private UseLogger logger;

    /**
     * Constructor of ProcessManager.
     * 
     * @param p
     *            is a singleton for the persistence
     * @param logger for logging information
     */
    @Inject
    public ProcessManagerImp(Persistence p, UseLogger logger) {
        this.persistence = p;
        setLogicResponse(new LogicResponse());
        this.logger = logger;
    }

    /**
     * IMPORTANT: For walking skeleton this method is not used! This method
     * checks if the user who wishes to edit a step is the responsible user who
     * is allowed to execute the step.
     * 
     * @param username
     *            who edits the step
     * @param step
     *            which user wants to edit
     * @return true if user is "owner" of step and false if not
     */
    public boolean checkUser(String username, Step step) {
        User checkUser = null;
        try {
            checkUser = persistence.loadUser(username);
        } catch (UserNotExistentException e) {
            logger.log(Level.SEVERE, e);
        }
        if (step instanceof Action) {
            return checkUser.getUsername().equals(((Action) step).getUsername());
        }
        return false;
    }

    /**
     * This method selects the processor of a step and executes it.
     * 
     * @param step
     *            which is to be edited
     * @param item
     *            which is currently active
     * @param user
     *            who started interaction
     */
    public void selectProcessor(Step step, Item item, User user) {
        if (step instanceof Action) {
            final ActionProcessor actionProcessor = new ActionProcessor(persistence);
            actionProcessor.addObserver(this);
            actionProcessor.handle(item, step, user);
        }
    }

    /**
     * This method is executed if its observables notifies changes.
     * 
     * @param o
     *            is the observed object
     * @param arg
     *            is an object which is received when notified
     */
    public void update(Observable o, Object arg) {
        logicResponse.add(new Message("ITEMS_FROM"
                + ((Item) arg).getWorkflowId(), "item="
                + ((Item) arg).getState() + "=" + ((Item) arg).getId()));
    }

    /**
     * Getter for LogicResponse-object.
     * 
     * @return logicResonse
     */
    public LogicResponse getLogicResponse() {
        return logicResponse;
    }

    /**
     * Setter for logicResonse.
     * 
     * @param lr
     *            will be the value of logicResponse
     */
    public void setLogicResponse(LogicResponse lr) {
        this.logicResponse = lr;
    }
}
