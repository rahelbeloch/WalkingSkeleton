package de.hsrm.swt02.businesslogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.LogInException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
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
 * This class implements the logic interface and is used for logic operations.
 */
public class LogicImp implements Logic {

    private Persistence p;
    private ProcessManager pm;
    private LogicResponse logicResponse;

    /**
     * Constructor for LogicImp.
     * 
     * @param p is a singleton instance of the persistence
     * @param pm is a singleton instance of the processmanager
     * @param logger is the logger instance for this application
     * @throws StorageFailedException 
    */
    @Inject
    public LogicImp(Persistence p, ProcessManager pm, UseLogger logger) throws StorageFailedException {
        this.p = p;
        this.pm = pm;
        setLogicResponse(new LogicResponse());

        try {
            initTestdata();
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IncompleteEleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method starts a Workflow via processmanager.
     * 
     * @param workflowID the workflow, which should be started
     * @param user the User, who starts the workflow
     * @exception WorkflowNotExistentException if the requested workflow doesnt exist in the persistence
     * @throws WorkflowNotExistentException
     * @throws StorageFailedException 
     * @throws ItemNotExistentException 
    */
    @Override
    public LogicResponse startWorkflow(String workflowID, String username) throws WorkflowNotExistentException, StorageFailedException, ItemNotExistentException {
        final Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        String itemID = pm.startWorkflow(workflow, username);
        setLogicResponse(new LogicResponse());
        
        logicResponse.add(new Message("ITEMS_FROM_" + workflowID, "item=def="
                + itemID));
        return logicResponse;
    }

    
    /**
     * This method store a workflow and distribute a id.
     * 
     * @param workflow
     * @throws IncompleteEleException 
     * @throws StorageFailedException 
    */
    @Override 
    public LogicResponse addWorkflow(Workflow workflow) throws IncompleteEleException, StorageFailedException {
        if ((workflow.getStepByPos(0) instanceof StartStep) && (workflow.getStepByPos(workflow.getSteps().size()-1) instanceof FinalStep)) {
            final String id = p.storeWorkflow(workflow);
            setLogicResponse(new LogicResponse());
            logicResponse.add(new Message("WORKFLOW_INFO", "workflow=def=" + id));
            return logicResponse;
        }
        else {
            throw new IncompleteEleException();
        }
    }

    /**
     * This method loads a Workflow.
     * 
     * @param workflowID
     *            describe the workflow
     * @return a Workflow, if there is one, who has this workflowID
     * @throws WorkflowNotExistentException
     * @throws StorageFailedException 
    */
    @Override
    public Workflow getWorkflow(String workflowID) throws WorkflowNotExistentException, StorageFailedException {
        return p.loadWorkflow(workflowID);
    }

    /**
     * This method delete a Workflow in Persistence.
     * 
     * @param workflowID
     *            describe the Workflow
     * @throws WorkflowNotExistentException
     */
    @Override
    public LogicResponse deleteWorkflow(String workflowID) throws WorkflowNotExistentException {
        p.deleteWorkflow(workflowID);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=del="
                + workflowID));
        return logicResponse;
    }

    /**
     * This method execute a step in an item.
     * 
     * @param item the Item, which edited
     * @param step the step, which execute
     * @param user, who execute the step in the Item
     * @throws UserNotExistentException
     * @throws ItemNotExistentException
     * @throws StorageFailedException 
     * @throws StepNotExistentException 
     * @throws WorkflowNotExistentException 
     */
    @Override
    public void stepForward(String itemId, String stepId, String username) 
            throws ItemNotExistentException, UserNotExistentException, ItemNotForwardableException, UserHasNoPermissionException,
            StorageFailedException, WorkflowNotExistentException, StepNotExistentException {
        
        pm.executeStep(p.loadStep(itemId, stepId), p.loadItem(itemId), p.loadUser(username));
    }

    /**
     * This method add a step into an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param step the step, which shall added
     * @throws WorkflowNotExistentException
     * @throws StorageFailedException 
     */
    @Override
    public LogicResponse addStep(String workflowID, Step step)
            throws WorkflowNotExistentException, StorageFailedException 
    {
        final Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        workflow.addStep(step);
        p.storeWorkflow(workflow);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflow.getId()));
        return logicResponse;
    }

    /**
     * This method delete a step from an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param stepID the step, which shall delete
     * @throws WorkflowNotExistentException
     * @throws StorageFailedException 
     */
    @Override
    public LogicResponse deleteStep(String workflowID, String stepID)
            throws WorkflowNotExistentException, StorageFailedException 
    {
        final Workflow workflow = (Workflow) p.loadWorkflow(workflowID);
        workflow.removeStep(stepID);
        p.storeWorkflow(workflow);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflow.getId()));
        return logicResponse;
    }

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user
     * @throws UserAlreadyExistsException
     * @throws StorageFailedException 
     */
    @Override
    public LogicResponse addUser(User user) throws UserAlreadyExistsException, StorageFailedException {
        // check if there are duplicate messagingsubs
        final List<String> subs = user.getMessagingSubs();
        final ArrayList<String> definiteSubs = new ArrayList<String>();
        
        if (subs != null && subs.size() > 0) {
            definiteSubs.add(subs.get(0));
            for (String sub : subs) {
                if (!definiteSubs.contains(sub)) {
                    definiteSubs.add(sub);
                }

            }
            //messagingsublist is cleared and filled with the new list
            user.getMessagingSubs().clear();
            user.getMessagingSubs().addAll(definiteSubs);
            
        }
        //finally user is added
        p.addUser(user);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("USER_INFO", "user=def=" + user.getUsername()));
        return logicResponse;
    }

    /**
     * This method loads a User.
     * 
     * @param username describe the user
     * @return a User, if there is one, who has this username
     * @throws UserNotExistentException
     */
    @Override
    public User getUser(String username) throws UserNotExistentException {
        return (User) p.loadUser(username);
    }

    /**
     * This method delete a User.
     * 
     * @param username describe the user
     * @throws UserNotExistentException
     */
    @Override
    public LogicResponse deleteUser(String username)
            throws UserNotExistentException 
    {
        p.deleteUser(username);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("USER_INFO", "user=del=" + username));
        return logicResponse;
    }

    /**
     * This method returns all workflows, in which the user is involved. Mind
     * that this method won't return the items only a list of workflows.
     * 
     * @param username describes the user
     * @return a LinkedList of workflows
     * @throws LogicException
     * @throws WorkflowNotExistentException 
     */
    @Override
    public List<Workflow> getAllWorkflowsByUser(String username)
            throws LogicException, UserNotExistentException, WorkflowNotExistentException 
    {
        p.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : p.loadAllWorkflows()) {
            if (wf.isActive()) {
                for (Step step : wf.getSteps()) {
                    if (step.getUsername().equals(username)) {
                        Workflow copyOfWf;
                        try {
                            copyOfWf = ((Workflow) wf.clone());
                            copyOfWf.clearItems(); // eliminate items
                            workflows.add(copyOfWf);
                            break;
                        } catch (CloneNotSupportedException e) {
                            throw new LogicException("Workflow is not cloneable.");
                        }
                    }
                }
            }
        }
        return workflows;
    }

    /**
     * Aditional method to get all workflows for a specfic user WITH items. This
     * method should not be used from outside the logicImplementation.
     * 
     * @param username the username
     * @return a list of workflows
     * @throws WorkflowNotExistentException if a workflow doesnt exists.
     * @throws UserNotExistentException if the user doesnt exists.
     * @throws CloneNotSupportedException 
     */
    public List<Workflow> getAllWorkflowsByUserWithItems(String username)
            throws WorkflowNotExistentException, UserNotExistentException, CloneNotSupportedException 
    {
        p.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : p.loadAllWorkflows()) {
            if (wf.isActive()) {
                for (Step step : wf.getSteps()) {
                    if (step.getUsername().equals(username)) {
                        final Workflow copyOfWf = (Workflow) wf.clone();
                        workflows.add(copyOfWf);
                        break;
                    }
                }
            }
        }
        return workflows;
    }

    // /**
    // * This method returns all actual Items for a User.
    // *
    // * @param user
    // * @return a LinkedList, with actual Items
    // * @throws WorkflowNotExistentException
    // */
    // public List<Item> getOpenItemsByUser(String username) throws
    // WorkflowNotExistentException, UserNotExistentException {
    //
    // final LinkedList<Workflow> workflows = (LinkedList<Workflow>)
    // getWorkflowsByUser(username);
    // final LinkedList<Item> items = new LinkedList<Item>();
    //
    //
    // for (Workflow wf : workflows) {
    // for (Item item : wf.getItems()) {
    //
    // if ((wf.getStepById(Integer
    // .parseInt(item.getActStep().getKey())).getUsername())
    // .equals(username))
    // {
    // items.add(item);
    // }
    // }
    // }
    // return items;
    // }

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
    @Override
    public List<Item> getOpenItemsByUser(String username) throws LogicException, WorkflowNotExistentException, UserNotExistentException {

        final LinkedList<Workflow> workflows = (LinkedList<Workflow>)getAllWorkflowsByUser(username);
        final LinkedList<Item> items = new LinkedList<Item>();

        
        for (Workflow wf : workflows) {
            for (Item item : wf.getItems()) {


                if ((wf.getStepById(item.getActStep().getKey()).getUsername())
                        .equals(username)) 
                {
                    items.add(item);
                }
            }
        }
        return items;
    }
    
    /**
    * Method for getting a workflow by the username.
    * 
    * @param username describes the given user
    * @exception WorkflowNotExistentException if the requested workflow doesnt exist in the persistence
    * @exception UserNotExistentException if the requested user doesnt exist in the persistence
    * @throws LogicException if there is an Exception in the businesslogic
    * @throws WorkflowNotExistentException
    * @throws UserNotExistentException
    * @throws LogicException
    * @return List<Workflow> is the requested list of workflows
    */

    public List<String> getStartableWorkflowsByUser(String username) throws LogicException, UserNotExistentException, WorkflowNotExistentException {

        final List<String> startableWorkflows = new LinkedList<>();
        for (Workflow workflow : getAllWorkflowsByUser(username)) {
            if (workflow.isActive()) {
                final Step startStep = workflow.getSteps().get(0);
                assert (startStep instanceof StartStep);
                if (startStep.getUsername().equals(username)) {
                    startableWorkflows.add(workflow.getId());
                }
            }
        }
        return startableWorkflows;
    }
    
    /**
     * This method returns all Workflows, which can be startes by this user.
     * 
     * @param user
     * @return
     * @throws WorkflowNotExistentException 
     */
    @Override
    public List<Workflow> getStartableWorkflows(String username) throws LogicException, WorkflowNotExistentException, UserNotExistentException {

        final LinkedList<Workflow> startableWorkflows = new LinkedList<Workflow>();
        final LinkedList<Workflow> workflows = (LinkedList<Workflow>)getAllWorkflowsByUser(username);

        for (Workflow wf : workflows) {
            if (wf.getStepByPos(0).getUsername().equals(username)) {
                startableWorkflows.add(wf);
            }
        }
        return startableWorkflows;
    }
    
    /**
     * Method for getting a list of ids of the items relevant to an user (if he's responsible for a step in the steplist).
     * @param workflowId is the id of the given workflow
     * @param username desribes the given user
     * @exception WorkflowNotExistentException if the given workflow doesnt exist i9n the persistence
     * @exception UserNotExistentException if the given user doesnt exist in the persistence
     * @throws WorkflowNotExistentException
     * @throws UserNotExistentException
     * @return List<Integer> list of stepIds
     * @throws StorageFailedException 
     */    
    public List<Item> getRelevantItemsByUser(String workflowId, String username)
            throws UserNotExistentException, WorkflowNotExistentException, StorageFailedException 
    {
        final LinkedList<Item> relevantItems = new LinkedList<>();
        final Workflow workflow = getWorkflow(workflowId);
        for (Item item : workflow.getItems()) {
            final MetaEntry me = item.getActStep();
            if (me != null) {
                final String itemUsername = workflow.getStepById(me.getKey()).getUsername();
                if (username.equals(itemUsername)) {
                    relevantItems.add(item);
                }
            } 
        }
        return relevantItems;
    }

    public Item getItem(String itemID) throws ItemNotExistentException, StorageFailedException{
        return p.loadItem(itemID);
    }
    // /**
    // * This method returns all Workflows, which can be startes by this user.
    // *
    // * @param user
    // * @return
    // * @throws WorkflowNotExistentException
    // */
    // public List<Workflow> getStartableWorkflows(String username) throws
    // WorkflowNotExistentException, UserNotExistentException {
    //
    // final LinkedList<Workflow> startableWorkflows = new
    // LinkedList<Workflow>();
    // final LinkedList<Workflow> workflows = (LinkedList<Workflow>)
    // getWorkflowsByUser(username);
    //
    // for (Workflow wf : workflows) {
    // if (wf.getStepByPos(0).getUsername().equals(username)) {
    // startableWorkflows.add(wf);
    // }
    // }
    // return startableWorkflows;
    // }

    /**
     * This method gets a LogicResponse object from the processmanager instance.
     * 
     * @return processmanager's logicResponse object
     */
    public LogicResponse getProcessLogicResponse() {
        return pm.getLogicResponse();
    }

    /**
     * Setter for LogicResponse object from the processmanager instance.
     * 
     * @param lr new LogicResponse object for processmanager logicResponse
     */
    public void setProcessLogicResponse(LogicResponse lr) {
        pm.setLogicResponse(lr);
    }

    /**
     * Getter for logicResponse.
     * 
     * @return logicResponse
     */
    public LogicResponse getLogicResponse() {
        return logicResponse;
    }

    /**
     * Setter for logicResponse.
     * 
     * @param lr
     *            is new value for logicResponse
     */
    public void setLogicResponse(LogicResponse lr) {
        this.logicResponse = lr;
    }

    /**
     * This method return all workflows in persistence.
     * 
     * @return
     */
    @Override
    public List<Workflow> getAllWorkflows() throws WorkflowNotExistentException {
        final List<Workflow> workflows = new LinkedList<Workflow>();
        for (Workflow wf : p.loadAllWorkflows()) {
            if (wf.isActive()) {
                workflows.add(wf);
            }
        }
                
        return workflows;
    }

    /**
     * This method check a User, later it will be extended for password.
     * 
     * @param username
     *            the user, to be checked
     * @return if user existing true, else false
     * @throws LogInException 
     */
    @Override
    public boolean checkLogIn(String username) throws LogInException {
        try {
            p.loadUser(username);
        } catch (UserNotExistentException e) {
            throw new LogInException();
        }
        return true;
    }

    // BusinessLogic Sprint 2

    /**
     * This method returns all roles in persistence.
     * 
     * @return p.loadAllRoles is the list of all Roles
     * @exception RoleNotExistentException
     *                if the requested role is not there
     * @throws RoleNotExistentException
     */
    public List<Role> getAllRoles() throws RoleNotExistentException {
        return p.loadAllRoles();
    }

    /**
     * This method returns all users in persistance.
     * 
     * @return p.loadAll Users is the list of all users
     * @exception UserNotExistentException
     *                if the requested user is not there
     * @throws UserNotExistentException
     */
    public List<User> getAllUsers() throws UserNotExistentException {
        return p.loadAllUsers();
    }

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user
     * @throws UserAlreadyExistsException
     */
    @Override
    public LogicResponse addRole(Role role) throws RoleAlreadyExistsException {
        p.storeRole(role);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("ROLE_INFO", "role=def=" + role.getRolename()));
        return logicResponse;
    }

    /**
     * T.
     * @param username the user which get a role
     * @param role .
     * @return
     * @throws UserNotExistentException .
     * @throws RoleNotExistentException .
     * @throws UserHasAlreadyRoleException .
     * @return .
     */
    public LogicResponse addRoleToUser(String username, Role role)
            throws UserNotExistentException, RoleNotExistentException,
            UserHasAlreadyRoleException 
    {
        final User user = p.loadUser(username);
        p.addRoleToUser(user, role);
        setLogicResponse(new LogicResponse());
//        logicResponse.add(new Message("USER_INFO", "user" + username
//                + "_add_role" + role.getRolename()));
        logicResponse.add(new Message("USER_INFO", "user=upd=" + username));
  
        return logicResponse;
    }

    /**
     * 
     * @param rolename .
     * @return .
     * @throws RoleNotExistentException .
     */
    public LogicResponse deleteRole(String rolename)
            throws RoleNotExistentException 
    {
        p.deleteRole(rolename);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("ROLE_INFO", "role=del=" + rolename));
        return logicResponse;
    }

    /**
     * This method deactivate a workflow.
     * 
     * @param workflowID
     *            the id of the workflow which should be deactivate
     * @return logicResponse about update
     * @throws StorageFailedException 
     * @throws WorkflowNotExistentException .
     */
    public LogicResponse deactivateWorkflow(String workflowID)
            throws WorkflowNotExistentException, StorageFailedException 
    {
        Workflow workflow = p.loadWorkflow(workflowID);
        workflow.setActive(false);
        p.storeWorkflow(workflow);
        //TODO are there any active items?
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflowID));
        return logicResponse;
    }

    /**
     * This method activate a workflow.
     * 
     * @param workflowID
     *            the id of the workflow which should be deactivate
     * @return logicResponse about update
     * @throws StorageFailedException 
     * @throws WorkflowNotExistentException .
     */
    public LogicResponse activateWorkflow(String workflowID)
            throws WorkflowNotExistentException, StorageFailedException 
    {
        p.loadWorkflow(workflowID).setActive(true);
        setLogicResponse(new LogicResponse());
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflowID));
        return logicResponse;
    }

    /**
     * Initialize test datas.
     * @throws IncompleteEleException 
     * @throws StorageFailedException 
     * 
     * @throws UserAlreadyExistsException .
     */
    private void initTestdata() throws UserAlreadyExistsException, IncompleteEleException, StorageFailedException {
        Workflow workflow1;
        User user1, user2, user3;
        StartStep startStep1;
        Action action1, action2;
        FinalStep finalStep;
        Role role1;

        user1 = new User();
        user1.setUsername("Alex");
        user2 = new User();
        user2.setUsername("Dominik");
        user3 = new User();
        user3.setUsername("Tilman");
        
        role1 = new Role();
        role1.setId("1");
        role1.setRolename("Rolle 1");
        user3.getRoles().add(role1);

        addUser(user1);
        addUser(user2);
        addUser(user3);

        startStep1 = new StartStep(user1.getUsername());

        action1 = new Action(user1.getUsername(), "Action von "
                + user1.getUsername());
        action2 = new Action(user2.getUsername(), "Action von "
                + user2.getUsername());

        finalStep = new FinalStep();

        workflow1 = new Workflow();
        workflow1.addStep(startStep1);
        workflow1.addStep(action1);
        workflow1.addStep(action2);
        workflow1.addStep(finalStep);

        addWorkflow(workflow1);
    }
}
