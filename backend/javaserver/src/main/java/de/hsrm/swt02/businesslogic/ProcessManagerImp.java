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
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;

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
    public boolean checkAuthorization(Step step, String username) throws PersistenceException {
        final User userToCheck = persistence.loadUser(username);
        final Role expectedRole = persistence.loadRole(step.getRolename());
        
        return userToCheck.getRoles().contains(expectedRole); 
    }

    /**
     * This method checks if the inquired user can actually start the workflow.
     * If she/he can a workflow is started.
     * 
     * @param workflow is the which should be started.
     * @param username is the name of the user who wants to start workflow.
     * @return itemID
     * @throws PersistenceException 
     */
    public String startWorkflow(Workflow workflow, String username) throws PersistenceException {
        final StartStep startStep = (StartStep) workflow.getStepByPos(0);
        String itemID = "";
        StartProcessor startProcessor = new StartProcessor(persistence);
        if (checkAuthorization(startStep, username)) {
            itemID = startProcessor.createItem(workflow);
        } else {
            logger.log(Level.WARNING, "Access denied, Authorization failed.");
            // TODO: Exception needed here, to avoid returning an empty itemID?
        }
        return itemID;
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
     * @throws ItemNotForawrdableException
     * @throws UserHasNoPermissionException
     * @throws LogicException 
     */
    public String executeStep(Step step, Item item, User user) throws LogicException {
        StepProcessor stepProcessor = selectProcessor(step);
        String itemId;
        
        itemId = stepProcessor.handle(item, step, user);
        
        return itemId;
    }


}
