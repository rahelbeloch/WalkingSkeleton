package de.hsrm.swt02.businesslogic;

import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.businesslogic.processors.ActionProcessor;
import de.hsrm.swt02.businesslogic.processors.StartProcessor;
import de.hsrm.swt02.businesslogic.processors.StepProcessor;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;

/**
 * This class handles the processing of Steps. (For now) it provides methods for
 * checking whether an user can execute a step and selecting the right processor
 * for a step.
 *
 */
@Singleton
public class ProcessManagerImp implements ProcessManager {

    private Persistence persistence;
    private UseLogger logger;

    /**
     * Constructor of ProcessManager. Per default is the LogicResponse gesettet.
     * 
     * @param p is a singleton for the persistence
     * @param logger is for logging information
     */
    @Inject
    public ProcessManagerImp(Persistence p, UseLogger logger) {
        this.persistence = p;
        this.logger = logger;
    }

    /**
     * This method checks if the user is authorized to do something (like
     * executing) with a step.
     * 
     * @param step is the step the user wants to edit
     * @param username is the name of the user to check
     * @return true if user is "owner" of step and false if not
     * @throws PersistenceException if an error in persistence occurs
     */
    private boolean checkAuthorization(Step step, String username)
            throws PersistenceException 
    {
        final User userToCheck = persistence.loadUser(username);

        boolean authorized = false;
        for (String rolename: step.getRoles()) {
            if (userToCheck.getRoles().contains(persistence.loadRole(rolename))) {
                authorized = true;
                break;
            }
        }
        return authorized;
    }

    /**
     * This method checks if the inquired user can actually start the workflow.
     * If she/he can a workflow is started.
     * 
     * @param workflow is the which should be started.
     * @param username is the name of the user who wants to start workflow.
     * @return itemID
     * @throws LogicException to catch Persistence and Permission Exceptions
     */
    public String startWorkflow(Workflow workflow, String username)
            throws LogicException {
        final StartStep startStep = (StartStep) workflow.getStepByPos(0);
        String itemID = "";
        final StartProcessor startProcessor = new StartProcessor(persistence);
        if (checkAuthorization(startStep, username)) {
            itemID = startProcessor.createItem(workflow);
            return itemID;
        } else {
            logger.log(Level.WARNING, "Access denied, Authorization failed.");
            throw new UserHasNoPermissionException("user " + username + "has no permission on this start item");
        }
    }

    /**
     * This method selects the appropriate stepprocessor for a step.
     * 
     * @param step is the step which will be executed
     */
    public StepProcessor selectProcessor(Step step) {
        if (step instanceof Action) {
            return new ActionProcessor(persistence);
        }
        return null;
    }

    /**
     * This method executes step operations.
     * 
     * @param step is the step which is to be edited
     * @param item is the item which is currently active
     * @param user is the user who started tzhe operation
     * @throws LogicException if problem occured while editing item
     * @return itemid of edited item
     */
    public String executeStep(Step step, Item item, User user)
            throws LogicException
    {
        final StepProcessor stepProcessor = selectProcessor(step);
        String itemId = "";

        if (checkAuthorization(step, user.getUsername())) {
            itemId = stepProcessor.handle(item, step, user);
            return itemId;
        } else {
            logger.log(Level.WARNING, "Access denied, Authorization failed.");
            throw new UserHasNoPermissionException("user " + user.getUsername() + "has no permission on this item");
        }
    }

}
