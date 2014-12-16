package de.hsrm.swt02.businesslogic;

import java.util.Observer;

import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;

/**
 * Interface for ProcessManager. (Due to Dependency Injection)
 *
 */
public interface ProcessManager extends Observer {

    /**
     * This method checks if the user is authorized to do something (like
     * executing) with a step.
     * 
     * @param step which user wants to edit
     * @param username who edits the step
     * @return true if user is "owner" of step and false if not
     */
    boolean checkAuthorization(Step step, String username);

    /**
     * This method starts a workflow.
     * 
     * @param workflow which will be started
     * @param username indicates who wants to start a workflow
     * @throws PersistenceException 
     */
    String startWorkflow(Workflow workflow, String username) throws ItemNotExistentException, StorageFailedException, PersistenceException;

    /**
     * This method selects the appropriate stepprocessor for a step.
     * 
     * @param step which will be executed
     */
    void selectProcessor(Step step);

    /**
     * This method executes the step operation.
     * 
     * @param step which is to be edited
     * @param item which is currently active
     * @param user who started interaction
     * @exception ItemNotForwardableException if the steplist corresponding to an item can't go any further
     * @exception UserHasNoPermissionException if the given user is not responsible for the step
     * @throws ItemNotForawrdableException
     * @throws UserHasNoPermissionException
     * @throws LogicException 
     */
    void executeStep(Step step, Item item, User user) throws ItemNotForwardableException, UserHasNoPermissionException, ItemNotExistentException, StorageFailedException, LogicException;

    /**
     * This method gets a LogicResponse object.
     * 
     * @return LogicResponse is the LogicResponse object
     */
    LogicResponse getLogicResponse();

    /**
     * This method sets a LogicResponse object.
     * 
     * @param lr is the new logicResponse
     */
    void setLogicResponse(LogicResponse lr);
}
