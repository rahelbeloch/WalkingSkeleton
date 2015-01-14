package de.hsrm.swt02.businesslogic;

import java.util.List;

import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.NoPermissionException;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;


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
     * @exception LogicException if an error in businesslogic occurs
     */
    LogicResponse startWorkflow(String workflowID, String username) throws LogicException;

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param workflow is the workflow which should be added
     * @return logicResponse of adding a workflow
     * @exception LogicException if an error in businesslogic occurs
     */
    LogicResponse addWorkflow(Workflow workflow) throws LogicException; // later a workflows name will be given a name
                                                  

    /**
     * This method return all workflows in persistence.
     * 
     * @return list of all workflows.
     * @throws PersistenceException if an error in persistence occurs
     */
    List<Workflow> getAllWorkflows() throws PersistenceException;


     /**
     * This method return all workflows in persistence that are not marked inactive.
     * 
     * @return all active workflows in persistence
     * @throws PersistenceException if an error in persistence occurs
     */
    List<Workflow> getAllActiveWorkflows() throws PersistenceException;
    
    /**
     * This method loads a Workflow.
     * 
     * @param workflowID is the id of the given worklow
     * @return a Workflow, if there is one, who has this workflowID
     * @throws PersistenceException if an error in persistence occurs
     */
    Workflow getWorkflow(String workflowID) throws PersistenceException;

    /**
     * This method delete a Workflow in Persistence.
     * 
     * @param workflowID
     *            describe the Workflow
     * @return logicResponse of deleting a workflow
     * @throws PersistenceException if an error in persistence occurs
     */
    LogicResponse deleteWorkflow(String workflowID)
            throws PersistenceException;

    /*
     * item
     */
    /**
     * This method execute a step in an item.
     * 
     * @param itemId is the itemId of the given item
     * @param stepId the stepId of the responsible item step
     * @param username is the name of the user who executes the step in the Item
     * @exception LogicException if an error in businesslogic occurs 
     * @return the logicResponse of stepForward
     */
    LogicResponse stepForward(String itemId, String stepId, String username) 
            throws LogicException;

    /**
     * This method deactivates a workflow.
     * 
     * @param workflowID the id of the workflow which should be deactivate
     * @throws PersistenceException if an error in persistence occurs
     * @return the LogicResponse of the deactivated workflow
     */
    LogicResponse deactivateWorkflow(String workflowID) throws PersistenceException;

    /**
     * This method activate a workflow.
     * 
     * @return the LogicResponse of an activated workflow
     * @param workflowID the id of the workflow which should be deactivate
     * @throws PersistenceException if an error in persistence occurs
     * @throws PersistenceException 
     **/
    LogicResponse activateWorkflow(String workflowID) throws PersistenceException;
   

    /**
     * This method add a step into an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param stepId is the Id the step, which shall added
     * @return logicResponse of adding a step
     * @throws PersistenceException if an error in persistence occurs
     */
    LogicResponse addStep(String workflowID, Step stepId) throws PersistenceException;

    /**
     * This method delete a step from an existing Workflow.
     * 
     * @param workflowID is the id of the workflow, which shall edited
     * @param stepID is the id of the step, which shall delete
     * @return logicResponse of deleting a step
     * @throws PersistenceException if an error in persistence occurs
     * @throws PersistenceException   
     */
    LogicResponse deleteStep(String workflowID, String stepID) throws PersistenceException;

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user is the given user which should be added
     * @return logicResponse of adding a user
     * @throws PersistenceException if an error in persistence occurs
     */
    LogicResponse addUser(User user) throws PersistenceException;

    /**
     * This method loads a User.
     * 
     * @param username describe the user
     * @return a User, if there is one, who has this username
     * @throws PersistenceException if an error in persistence occurs 
     * @throws PersistenceException  
     */
    User getUser(String username) throws PersistenceException; // not there yet

    /**
     * This method checks a User.
     * 
     * @param username of the user, to be checked
     * @param password of the user, to be checked
     * @param adminRequired flag whether or not admin check is necessary
     * @return if user correct true, else false
     * @exception LogicException if an error in businesslogic occurs
     */
    boolean checkLogIn(String username, String password, boolean adminRequired) throws LogicException;

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
     * @exception LogicException if an error in businesslogic occurs
     * @return a LinkedList of workflows
     */
    List<Workflow> getAllWorkflowsForUser(String username) throws LogicException;

    /**
     * Method for getting a list of the ids of workflows startable by a given user (if the user is responsible for the Startstep of the steplist).
     * 
     * @param username is the name and describes the given user
     * @exception LogicException if an error in businesslogic occurs
     * @return List<Integer> list of Ids
     */
    List<String> getStartableWorkflowsForUser(String username) throws LogicException;
    
    /**
     * Method for getting a list of ids of the items relevant to an user (if he's responsible for a step in the steplist).
     * @param workflowId is the id of the given workflow
     * @param username desribes the given user
     * @throws PersistenceException if an error in persistence occurs
     * @throws PersistenceException  
     * @return List<Integer> list of stepIds
     */
    List<Item> getRelevantItemsForUser(String workflowId, String username) throws PersistenceException;

    /**
     * method to load a specific item from persistence.
     * 
     * @param itemID - a items unique string id 
     * @param username - userId
     * @return Item - requested item from persistence
     * @throws LogicException if an error in persistence or a permission error occurs
     */
    Item getItem(String itemID, String username) throws LogicException;
    
    
    // Business Logic Sprint 2
    
    /**
     * Method for getting a list of all the existing roles in the persistance.
     * 
     * @return list of all roles
     * @throws PersistenceException if an error in persistence occurs
     */
    List<Role> getAllRoles() throws PersistenceException;

    /**
     * Method for returning a list of all the existing users in the persistance.
     * 
     * @return list of all users
     * @throws PersistenceException if an error in persistence occurs
     * @throws PersistenceException  
     */
    List<User> getAllUsers() throws PersistenceException;

    /**
     * Method for adding a new role in the persistance.
     * 
     * @param role is the role we want to add
     * @return LogicResponse object
     * @throws PersistenceException if persistence errors occur
     */
    LogicResponse addRole(Role role) throws PersistenceException;

    /**
     * 
     * @param user - the user to which the role shall be added
     * @param role - role to be added
     * @return LogicResponse object
     * @throws PersistenceException if persistence errors occur
     */
    LogicResponse addRoleToUser(User user, Role role) throws PersistenceException;
    
    /**
     * Method for deleting an existing role from the persistance. The users who
     * have this role will lose it too.
     * 
     * @param rolename of the role
     * @return LogicResponce object
     * @throws LogicException 
     */
    LogicResponse deleteRole(String rolename) throws LogicException;
    
    /**
     * Method for giving a List of items of a user which are all open.
     * 
     * @param username describes the given user
     * @exception LogicException if an error in businesslogic occurs
     * @throws PersistenceException  
     * @return List<Item> is the list of items we want to get
     */
    List<Item> getOpenItemsByUser(String username) throws LogicException;

    /**
    * Method for getting a list of startable workflows by a given user.
    * 
    * @param username describes the user 
    * @return List<Workflow> is the requested list of workflows
    * @exception LogicException if an error in businesslogic occurs
    */
    List<Workflow> getStartableWorkflows(String username) throws LogicException;

    /**
     * method to load a specific role from persistence.
     * 
     * @param rolename a roles unique string id
     * @return Role - requested Role from persistence
     * @throws PersistenceException if an error in persistence occurs
     */
    Role getRole(String rolename) throws PersistenceException;
    
    /**
     * Method for adding a form to persistence.
     * @param form which should be added
     * @return logicResponse which contains definition notification
     * @throws PersistenceException if an error in persistence occurs
     */
    LogicResponse addForm(Form form) throws PersistenceException;
    
    /**
     * Method for deleting a form in persistence. Only possible if form isn't currently used.
     * @param formId indicates which form should be deleted
     * @return logicResponse which contains deletion notification
     * @throws PersistenceException if an error in persistence occurs
     */
    LogicResponse deleteForm(String formId) throws PersistenceException;
    
    /**
     * method to load a specific form from persistence.
     * 
     * @param formId a forms unique string id
     * @return Form - requested Form from persistence
     * @throws PersistenceException if an error in persistence occurs
     */
    Form getForm(String formId) throws PersistenceException;
    
    /**
     * Method for updating an item. Suitable item from a workflow in persistence will be overwritten.
     * @param item contains changes, will be used to overwrite item in workflow
     * @return logicResposne which contains update notification
     * @throws PersistenceException if an error in persistence occurs
     */
    LogicResponse updateItem(Item item) throws PersistenceException;
    
    /**
     * Method for getting all available forms in persistence.
     * @return list of of all forms in persistence
     * @throws PersistenceException 
     */
    List<Form> getAllForms() throws PersistenceException;

    /**
     * Method for saving all relevant data from server. Calls the method persistence.save().
     */
    void saveData();
    
    /**
     * Method to load the data model from filesystem. 
     * File is read, in case of reading errors or missing file some default test data 
     * is load. Method calls load() in persistence.
     */
    void loadData();
}