package de.hsrm.swt02.businesslogic;

import java.util.List;

import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.LogInException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.RoleAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StepNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserHasAlreadyRoleException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * This interface is used for the business logic.
 */
public interface Logic {

    /**
     * This method starts a Workflow.
     * 
     * @param workflowID is the workflow, which should be started
     * @param username is the User, who starts the workflow
     * @return logicResponse of starting a workflow
     * @exception WorkflowNotExistentException if the given workflow doesnt exist in the persistence
     * @throws WorkflowNotExistentException
     * @throws ItemNotExistentException 
     */
    LogicResponse startWorkflow(String workflowID, String username) throws WorkflowNotExistentException, StorageFailedException, ItemNotExistentException;

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param workflow is the workflow which should be added
     * @return logicResponse of adding a workflow
     */
    LogicResponse addWorkflow(Workflow workflow) throws IncompleteEleException, StorageFailedException; // later a workflows name will be given a name
                                                  

    /**
     * This method return all workflows in persistence.
     * 
     * @return list of all workflows.
     * @throws WorkflowNotExistentException .
     */
    List<Workflow> getAllWorkflows() throws WorkflowNotExistentException;

    /**
     * This method loads a Workflow.
     * 
     * @param workflowID is the id of the given worklow
     * @return a Workflow, if there is one, who has this workflowID
     * @exception WorkflowNotExistentException if the given workflow doesnt exist in the persistence
     * @throws WorkflowNotExistentException
     */
    Workflow getWorkflow(String workflowID) throws WorkflowNotExistentException, StorageFailedException;

    /**
     * This method delete a Workflow in Persistence.
     * 
     * @param workflowID
     *            describe the Workflow
     * @return logicResponse of deleting a workflow
     * @throws WorkflowNotExistentException .
     */
    LogicResponse deleteWorkflow(String workflowID)
            throws WorkflowNotExistentException;

    /*
     * item
     */
    /**
     * This method execute a step in an item.
     * 
     * @param itemId is the itemId of the given item
     * @param stepId the stepId of the responsible item step
     * @param username is the name of the user who executes the step in the Item
     * @throws ItemNotExistentException
     * @exception ItemNotExistentException if requested item doesn't exist
     * @exception ItemNotForwardableException if the responsive steplist of the item can't go any further
     * @exception UserHasNoPermissionException if the given user has no right to advance the steplist
     * @throws UserNotExistentException
     * @throws ItemNotForwardableException
     * @throws UserHasNoPermissionException
     * @exception UserNotExistentException if requested user doesn't exist
     * @throws StepNotExistentException 
     * @throws WorkflowNotExistentException 
     */

    void stepForward(String itemId, String stepId, String username) 
            throws ItemNotExistentException, UserNotExistentException, ItemNotForwardableException, UserHasNoPermissionException,
                   StorageFailedException, WorkflowNotExistentException, StepNotExistentException;


    // /**
    // * This method finish a step in an item.
    // *
    // * @param itemId the Item, which edited
    // * @param stepId the step, which execute
    // * @param username who execute the step in the Item
    // * @exception ItemNotExistentException if requested item doesn't exist
    // * @exception UserNotExistentException if requested user doesn't exist
    // * @throws UserNotExistentException
    // * @throws ItemNotExistentException
    // */
    // void stepFinished(int itemId, int stepId, String username) throws ItemNotExistentException, UserNotExistentException;

    /**
     * This method deactivates a workflow.
     * 
     * @param workflowID the id of the workflow which should be deactivate
     * @exception WorkflowNotExistentException if the given workflow doesnt exist in the persistence
     * @throws WorkflowNotExistentException 
     */
    LogicResponse deactivateWorkflow(String workflowID) throws WorkflowNotExistentException, StorageFailedException;

    /**
     * This method activate a workflow.
     * @param workflowID the id of the workflow which should be deactivate
     * @throws WorkflowNotExistentException 
     **/
    LogicResponse activateWorkflow(String workflowID) throws WorkflowNotExistentException, StorageFailedException;
   

    /**
     * This method add a step into an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param stepId is the Id the step, which shall added
     * @return logicResponse of adding a step
     * @exception WorkflowNotExistentException if the given worklow doesnt exist in the persistence
     * @throws WorkflowNotExistentException 
     */
    LogicResponse addStep(String workflowID, Step stepId) throws WorkflowNotExistentException, StorageFailedException;

    /**
     * This method delete a step from an existing Workflow.
     * 
     * @param workflowID is the id of the workflow, which shall edited
     * @param stepID is the id of the step, which shall delete
     * @return logicResponse of deleting a step
     * @exception WorkflowNotExistentException if the given workflow doesnt exist in the persistence
     * @throws WorkflowNotExistentException 
     */
    LogicResponse deleteStep(String workflowID, String stepID) throws WorkflowNotExistentException, StorageFailedException;

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user is the given user which should be added
     * @return logicResponse of adding a user
     * @exception UserAlreadyExistsException if the given user already exists in the persistence
     * @throws UserAlreadyExistsException 
     */
    LogicResponse addUser(User user) throws UserAlreadyExistsException, StorageFailedException;

    /**
     * This method loads a User.
     * 
     * @param username describe the user
     * @return a User, if there is one, who has this username
     * @exception UserNotExistentException if the given user doesnt exist in the persistence
     * @throws UserNotExistentException 
     */
    User getUser(String username) throws UserNotExistentException; // not there yet

    /**
     * This method checks a User.
     * 
     * @param username of the user, to be checked
     * @return if user correct true, else false
     * @exception UserNotExistentException if the given user doesnt exist in the persistence
     * @throws UserNotExistentException
     */
    boolean checkLogIn(String username) throws LogInException; // later with passwordcheck

    /**
     * This method deletes a User.
     * 
     * @param username describes the user
     * @exception UserNotExistentException if the given user doesnt exist in the persistence
     * @throws UserNotExistentException
     * @return logicResponse of deleting a user
     */
    LogicResponse deleteUser(String username) throws UserNotExistentException;

    /**
     * This method returns all workflows, in which the user is involved, no
     * matter if there is an open or busy item for him Mind that this method
     * won't return the items only a list of workflows.
     * 
     * @param username is the username of the user whose workflows' is looked for
     * @exception WorkflowNotExistentException if the requested workflow doesnt exist in the persistence
     * @exception UserNotExistentException if the given user doesnt exist in the persistence
     * @exception LogicException if there is an exception in the businesslogic
     * @throws WorkflowNotExistentException
     * @throws UserNotExistentException
     * @throws LogicException
     * @return a LinkedList of workflows
     */
    List<Workflow> getAllWorkflowsByUser(String username) throws WorkflowNotExistentException, UserNotExistentException, LogicException;

    /**
     * Method for getting a list of the ids of workflows startable by a given user (if the user is responsible for the Startstep of the steplist).
     * 
     * @param username is the name and describes the given user
     * @exception UserNotExistentException if the requested user doesnt exist in the persistence
     * @exception WorkflowNotExistentException if the given workflow doesnt exist in the persistence
     * @exception LogicException if there is an exception in the logic
     * @throws UserNotExistentException
     * @throws WorkflowNotExistentException
     * @throws LogicException
     * @throws UserNotExistentException
     * @return List<Integer> list of Ids
     */
    List<String> getStartableWorkflowsByUser(String username) throws UserNotExistentException, WorkflowNotExistentException, LogicException;

    /**
     * Method for getting a list of ids of the items relevant to an user (if he's responsible for a step in the steplist).
     * @param workflowId is the id of the given workflow
     * @param username desribes the given user
     * @exception WorkflowNotExistentException if the given workflow doesnt exist i9n the persistence
     * @exception UserNotExistentException if the given user doesnt exist in the persistence
     * @throws WorkflowNotExistentException
     * @throws UserNotExistentException
     * @return List<Integer> list of stepIds
     */
    List<Item> getRelevantItemsByUser(String workflowId, String username) throws WorkflowNotExistentException, UserNotExistentException, StorageFailedException;

    /**
     * This method gets a LogicResponse object.
     * 
     * @return LogicResponse object
     */
    LogicResponse getProcessLogicResponse();

    /**
     * Setter for logicResponse.
     * 
     * @param lr is new value for logicResponse
     */
    void setProcessLogicResponse(LogicResponse lr);

    /**
     * Method gives an Item.
     * @param itemID 
     * @return an Item
     */
    Item getItem(String itemID) throws ItemNotExistentException, StorageFailedException;
    
    
    // Business Logic Sprint 2

    /**
     * Method for getting a list of all the existing roles in the persistance.
     * 
     * @return list of all roles
     * @exception RoleNotExistentException if the requested Role is not there
     * @throws RoleNotExistentException
     */
    List<Role> getAllRoles() throws RoleNotExistentException;

    /**
     * Method for returning a list of all the existing users in the persistance.
     * 
     * @return list of all users
     * @exception UserNotExistentException if the requested user is not there
     * @throws UserNotExistentException
     */
    List<User> getAllUsers() throws UserNotExistentException;

    /**
     * Method for adding a new role in the persistance.
     * 
     * @param role is the role we want to add
     * @return LogicResponse object
     * @exception RoleAlreadyExistsException if the role we want to add is already there
     * @throws RoleAlreadyExistsException
     */
    LogicResponse addRole(Role role) throws RoleAlreadyExistsException;

    /**
     * Method for adding an existing role to the rolelist of an existing user.
     * 
     * @param username is the username of the user
     * @param role is the role we want to add
     * @return LogicResponse object
     * @exception UserNotExistentException if the user is not in the persistance
     * @exception RoleNotExistentException if the role is not in the persistance
     * @exception UserHasAlreadyRoleException if the user has already the role in his rolelist
     * @throws UserNotExistentException
     * @throws RoleNotExistentException
     * @throws UserHasAlreadyRoleException
     */
    LogicResponse addRoleToUser(String username, Role role)
            throws UserNotExistentException, RoleNotExistentException,
            UserHasAlreadyRoleException;

    /**
     * Method for deleting an existing role from the persistance. The users who
     * have this role will lose it too.
     * 
     * @param rolename of the role
     * @return LogicResponce object
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     */
    LogicResponse deleteRole(String rolename) throws RoleNotExistentException;

    /**
     * Method for giving a List of items of a user which are all open.
     * 
     * @param username describes the given user
     * @exception WorkflowNotExistentException if the given workflow doesnt exist in the persistence
     * @exception UserNotExistentException if the given user doesnt exist in the persistence
     * @throws WorkflowNotExistentException
     * @throws UserNotExistentException
     * @return List<Item> is the list of items we want to get
     */
    List<Item> getOpenItemsByUser(String username) throws WorkflowNotExistentException, UserNotExistentException, LogicException;

    /**
    * Method for getting a list of startable workflows by a given user.
    * 
    * @param username describes the user
    * @exception WorkflowNotExistentException if the given workflow doesnt exist in the persistence
    * @exception UserNotExistentException if the given user doesnt exist in the persistence
    * @throws WorkflowNotExistentException
    * @throws UserNotExistentException
    * @return List<Workflow> is the requested list of workflows
     * @throws LogicException 
    */
    List<Workflow> getStartableWorkflows(String username) throws WorkflowNotExistentException, UserNotExistentException, LogicException;

}
