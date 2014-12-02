package de.hsrm.swt02.businesslogic;

import java.util.List;

import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
/**
 * This interface is used for the business logic.
 *
 */
public interface Logic {

    /*
     * workflow functions
     */
     /**
     * This method starts a Workflow.
     * 
     * @param workflowID the workflow, which should be started
     * @param user the User, who starts the workflow
     * @throws WorkflowNotExistentException 
     */
    LogicResponse startWorkflow(int workflowID, String username)
            throws WorkflowNotExistentException;

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param workflow which should be added
     */
    LogicResponse addWorkflow(Workflow workflow); // later a workflows name will
                                                // be available
    /**
     * This method return all workflows in persistence
     * @return
     */
    List<Workflow> getAllWorkflow();
  

    /**
     * This method loads a Workflow.
     * 
     * @param workflowID describe the workflow
     * @return a Workflow, if there is one, who has this workflowID
     * @throws WorkflowNotExistentException 
     */
    Workflow getWorkflow(int workflowID)
            throws WorkflowNotExistentException;

    /**
     * This method delete a Workflow in Persistence.
     * 
     * @param workflowID describe the Workflow
     * @throws WorkflowNotExistentException 
     */
    LogicResponse deleteWorkflow(int workflowID)
            throws WorkflowNotExistentException;

    /*
     * item
     */
    /**
     * This method execute a step in an item.
     * 
     * @param item the Item, which edited
     * @param step the step, which execute
     * @param user who execute the step in the Item
     */
    void stepInProgress(int itemId, int stepId, String username) throws ItemNotExistentException, UserNotExistentException;
    
    /**
     * This method finish a step in an item.
     * 
     * @param item the Item, which edited
     * @param step the step, which execute
     * @param user who execute the step in the Item
     */
    void stepFinished(int itemId, int stepId, String username) throws ItemNotExistentException, UserNotExistentException;


    /*
     * step functions
     */
    /**
     * This method add a step into an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param step the step, which shall added
     * @throws WorkflowNotExistentException 
     */
    LogicResponse addStep(int workflowID, Step stepId)
            throws WorkflowNotExistentException;

    /**
     * This method delete a step from an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param stepID the step, which shall delete
     * @throws WorkflowNotExistentException 
     */
    LogicResponse deleteStep(int workflowID, int stepID)
            throws WorkflowNotExistentException;

    /*
     * user functions
     */
    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user which should be added
     * @throws UserAlreadyExistsException 
     */
    LogicResponse addUser(User user) throws UserAlreadyExistsException;

    /**
     * This method loads a User.
     * 
     * @param username describe the user
     * @return a User, if there is one, who has this username
     * @throws UserNotExistentException 
     */
    User getUser(String username) throws UserNotExistentException; // not
                                                                          // attached
                                                                          // yet
    // public boolean checkLogIn(String username); // later with password
    // checking

    /**
     * This method delete a User.
     * 
     * @param username describe the user
     * @throws UserNotExistentException 
     */
    LogicResponse deleteUser(String username) throws UserNotExistentException;

    /**
     * This method returns all workflows, in which the user is involved.
     * 
     * @param user whose workflows' is looked for
     * @return a LinkedList of workflows
     */
    List<Workflow> getWorkflowsByUser(String username);

    /**
     * This method returns all actual Items for a User.
     * 
     * @param user whose items' is looked for
     * @return a LinkedList, with actual Items
     */
    List<Item> getOpenItemsByUser(String username);

    /**
     * This method returns all Workflows, which can be startes by this user.
     * 
     * @param user who can execute workflows
     * @return a LinkedList of workflows which can be executed
     */
    List<Workflow> getStartableWorkflows(String username);
    
    /**
     * This method gets a LogicResponse object.
     * @return LogicResponse object
     */
    LogicResponse getProcessLogicResponse();
}
