package de.hsrm.swt02.businesslogic;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.businesslogic.processors.ActionProcessor;
import de.hsrm.swt02.businesslogic.processors.StartProcessor;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;

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
    private ActionProcessor actionProcessor;
    private StartProcessor startProcessor;

    /**
     * Constructor of ProcessManager. Per default is the LogicResponse gesettet.
     * 
     * @param p is a singleton for the persistence
     * @param logger is for logging information
     */
    @Inject
    public ProcessManagerImp(Persistence p, UseLogger logger) {
        this.persistence = p;
        setLogicResponse(new LogicResponse());
        this.logger = logger;
    }

    /**
     * This method checks if the user is authorized to do something (like
     * executing) with a step.
     * 
     * @param step is the step the user wants to edit
     * @param username is the name of the user to check
     * @return true if user is "owner" of step and false if not
     */
    public boolean checkAuthorization(Step step, String username) {
        User checkUser = null;
        try {
            checkUser = persistence.loadUser(step.getUsername());
        } catch (UserNotExistentException e) {
            logger.log(Level.SEVERE, e);
        }
        return checkUser.getUsername().equals(username);
    }

    /**
     * This method checks if the inquired user can actually start the workflow.
     * If she/he can a workflow is started.
     * 
     * @param workflow is the which should be started.
     * @param username is the name of the user who wants to start workflow.
     * @return itemID
     * @throws StorageFailedException 
     * @throws ItemNotExistentException 
     */
    public String startWorkflow(Workflow workflow, String username) throws ItemNotExistentException, StorageFailedException {
        final StartStep startStep = (StartStep) workflow.getStepByPos(0);
        String itemID = "-1";
        selectProcessor(startStep);
        if (checkAuthorization(startStep, username)) {
            startProcessor.addObserver(this);
            itemID = startProcessor.createItem(workflow);
        } else {
            logger.log(Level.WARNING, "Access denied, Authorization failed.");
        }
        return itemID;
    }

    /**
     * This method selects the appropriate stepprocessor for a step.
     * 
     * @param step is the step which will be executed
     */
    public void selectProcessor(Step step) {
        if (step instanceof Action) {
            actionProcessor = new ActionProcessor(persistence);
        } else if (step instanceof StartStep) {
            startProcessor = new StartProcessor(persistence);
        }
    }

    /**
     * This method executes step operations.
     * 
     * @param step is the step which is to be edited
     * @param item is the item which is currently active
     * @param user is the user who started tzhe operation
     * @exception ItemNotForwardableException if the steplist corresponding to an item can't go any further
     * @exception UserHasNoPermissionException if the given user is not responsible for the step
     * @throws ItemNotForawrdableException
     * @throws UserHasNoPermissionException
     * @throws StorageFailedException 
     * @throws ItemNotExistentException 
     */
    public void executeStep(Step step, Item item, User user) throws ItemNotForwardableException, UserHasNoPermissionException, ItemNotExistentException, StorageFailedException {
        selectProcessor(step);
        if (step instanceof Action) {
            actionProcessor.addObserver(this);
            actionProcessor.handle(item, step, user);
        }
    }

    /**
     * This method is executed if its observables notifies changes.
     * 
     * @param o is the observed object, can be any object of the datamodels
     * @param arg is an object which is received when notified
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
     * @param lr will be the value of logicResponse
     */
    public void setLogicResponse(LogicResponse lr) {
        this.logicResponse = lr;
    }
}
