package de.hsrm.swt02.businesslogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.LogInException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
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
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * This class implements the logic interface and is used for logic operations.
 */
public class LogicImp implements Logic {

    private Persistence persistence;
    private ProcessManager processManager;

    /**
     * Constructor for LogicImp.
     * 
     * @param p is a singleton instance of the persistence
     * @param pm is a singleton instance of the processmanager
     * @param logger is the logger instance for this application
     * @throws LogicException 
    */
    @Inject
    public LogicImp(Persistence p, ProcessManager pm, UseLogger logger) throws LogicException {
        this.persistence = p;
        this.processManager = pm;

        try {
            initTestdata();
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IncompleteEleException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method starts a Workflow via processmanager.
     * 
     * @param workflowID the workflow, which should be started
     * @param user the User, who starts the workflow
     * @exception PersistenceException is thrown if errors occur while persisting objects
    */
    @Override
    public LogicResponse startWorkflow(String workflowID, String username) throws PersistenceException {
        final Workflow workflow = (Workflow) persistence.loadWorkflow(workflowID);
        final String itemId = processManager.startWorkflow(workflow, username);
        final LogicResponse logicResponse = new LogicResponse();
        
        logicResponse.add(new Message("ITEMS_FROM_" + workflowID, "item=def="
                + itemId));
        return logicResponse;
    }

    
    /**
     * This method store a workflow and distribute a id.
     * 
     * @param workflow is new workflow which should be stored
     * @throws LogicException is thrown if errors occur while storing the workflow
    */
    @Override 
    public LogicResponse addWorkflow(Workflow workflow) throws LogicException {
        if ((workflow.getStepByPos(0) instanceof StartStep) && (workflow.getStepByPos(workflow.getSteps().size() - 1) instanceof FinalStep)) {
            final String id = persistence.storeWorkflow(workflow);
            final LogicResponse logicResponse = new LogicResponse();
            
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
     * @throws PersistenceException is thrown if errors occur while persisting objects
     */
    @Override
    public Workflow getWorkflow(String workflowID) throws PersistenceException {
        return persistence.loadWorkflow(workflowID);
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
        persistence.deleteWorkflow(workflowID);
        final LogicResponse logicResponse = new LogicResponse();
        
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
     * @throws LogicException
     */
    @Override
    public LogicResponse stepForward(String itemId, String stepId, String username) 
            throws LogicException 
    {
        
        final String updatedItemId = processManager.executeStep(persistence.loadStep(itemId, stepId), persistence.loadItem(itemId), persistence.loadUser(username));
        final String workflowId = persistence.loadItem(itemId).getWorkflowId();
        final LogicResponse logicResponse = new LogicResponse();
        
        logicResponse.add(new Message("ITEMS_FROM_" + workflowId, "item=def="
                + updatedItemId));
        return logicResponse;
    }

    /**
     * This method add a step into an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param step the step, which should be added
     * @throws PersistenceException is thrown if errors occur while persisting objects
     */
    @Override
    public LogicResponse addStep(String workflowID, Step step)
            throws PersistenceException 
    {
        final Workflow workflow = (Workflow) persistence.loadWorkflow(workflowID);
        final LogicResponse logicResponse = new LogicResponse();
        workflow.addStep(step);
        persistence.storeWorkflow(workflow);
        
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflow.getId()));
        return logicResponse;
    }

    /**
     * This method delete a step from an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param stepID the step, which shall delete
     * @throws PersistenceException is thrown if errors occur while persisting objects
     */
    @Override
    public LogicResponse deleteStep(String workflowID, String stepID)
            throws PersistenceException 
    {
        final Workflow workflow = (Workflow) persistence.loadWorkflow(workflowID);
        final LogicResponse logicResponse = new LogicResponse();
        workflow.removeStep(stepID);
        persistence.storeWorkflow(workflow);
        
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflow.getId()));
        return logicResponse;
    }

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user
     * @throws PersistenceException is thrown if errors occur while persisting objects
     */
    @Override
    public LogicResponse addUser(User user) throws PersistenceException {
        // check if there are duplicate messagingsubs
        final List<String> subs = user.getMessagingSubs();
        final ArrayList<String> definiteSubs = new ArrayList<String>();
        final LogicResponse logicResponse = new LogicResponse();
        
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
        persistence.storeUser(user);
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
        return (User) persistence.loadUser(username);
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
        final LogicResponse logicResponse = new LogicResponse();
        
        persistence.deleteUser(username);
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
     */
    @Override
    public List<Workflow> getAllWorkflowsByUser(String username)
            throws LogicException 
    {
        persistence.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
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
     * @throws PersistenceException is thrown if errors occur while persisting objects
     * @throws CloneNotSupportedException 
     */
    public List<Workflow> getAllWorkflowsByUserWithItems(String username)
            throws PersistenceException, CloneNotSupportedException 
    {
        persistence.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
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

    /**
     * Method for giving a List of items of a user which are all open.
     * 
     * @param username describes the given user
     * @throws LogicException
     * @return List<Item> is the list of items we want to get
     */
    @Override
    public List<Item> getOpenItemsByUser(String username) throws LogicException {

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
    * @throws LogicException 
    * @return List<Workflow> is the requested list of workflows
    */

    public List<String> getStartableWorkflowsByUser(String username) throws LogicException {

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
     * @return startable workflows by an user
     * @throws LogicException
     */
    @Override
    public List<Workflow> getStartableWorkflows(String username) throws LogicException {

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
     * @throws PersistenceException is thrown if errors occur while persisting objects
     * @return all relevant items for an user 
     */    
    public List<Item> getRelevantItemsByUser(String workflowId, String username)
            throws PersistenceException 
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

    /**
     * Return item by itemId.
     * @param itemId indicates which item should be returned
     * @return looked for item
     * @throws PersistenceException is thrown if errors occur while persisting objects
     */
    public Item getItem(String itemId) throws PersistenceException {
        return persistence.loadItem(itemId);
    }

    /**
     * This method return all workflows in persistence.
     * 
     * @return all workflows in persistence
     */
    @Override
    public List<Workflow> getAllWorkflows() throws WorkflowNotExistentException {
        final List<Workflow> workflows = new LinkedList<Workflow>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
            workflows.add(wf);
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
            persistence.loadUser(username);
        } catch (UserNotExistentException e) {
            throw new LogInException();
        }
        return true;
    }

    // BusinessLogic Sprint 2
    
    /**
     * This method loads a User.
     * 
     * @param rolename describe the role
     * @return a role, if there is one, who has this rolename
     * @exception RoleNotExistentException if the requested role doesnt exist
     * @throws RoleNotExistentException
     */
    public Role getRole(String rolename) throws RoleNotExistentException {
        return (Role) persistence.loadRole(rolename);
    }

    /**
     * This method returns all roles in persistence.
     * 
     * @return p.loadAllRoles is the list of all Roles
     * @exception RoleNotExistentException
     *                if the requested role is not there
     * @throws RoleNotExistentException
     */
    public List<Role> getAllRoles() throws RoleNotExistentException {
        return persistence.loadAllRoles();
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
        return persistence.loadAllUsers();
    }

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user
     * @throws PersistenceException 
     */
    @Override
    public LogicResponse addRole(Role role) throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();
        
        persistence.storeRole(role);
        logicResponse.add(new Message("ROLE_INFO", "role=def=" + role.getRolename()));
        return logicResponse;
    }

    /**
     * This method adds a role to a user.
     * @param username indicates who will get a role assigned
     * @param role which user should be assigned
     * @return logicResponse about update
     * @throws PersistenceException is thrown if errors occur while persisting objects
     */
    public LogicResponse addRoleToUser(String username, Role role)
            throws PersistenceException
    {
        final User user = persistence.loadUser(username);
        final LogicResponse logicResponse = new LogicResponse();
        
        persistence.addRoleToUser(user, role);
        logicResponse.add(new Message("USER_INFO", "user=upd=" + username));
        return logicResponse;
    }

    /**
     * This method deletes a role.
     * @param rolename indicates which should be deleted
     * @return logicResponse about delete
     * @throws RoleNotExistentException .
     */
    public LogicResponse deleteRole(String rolename)
            throws RoleNotExistentException 
    {
        final LogicResponse logicResponse = new LogicResponse();
        
        persistence.deleteRole(rolename);
        logicResponse.add(new Message("ROLE_INFO", "role=del=" + rolename));
        return logicResponse;
    }

    /**
     * This method deactivate a workflow.
     * 
     * @param workflowId
     *            the id of the workflow which should be deactivate
     * @return logicResponse about update
     * @throws PersistenceException is thrown if errors occur while persisting objects
     */
    public LogicResponse deactivateWorkflow(String workflowId)
            throws PersistenceException 
    {
        final Workflow workflow = persistence.loadWorkflow(workflowId);
        final LogicResponse logicResponse = new LogicResponse();
        
        workflow.setActive(false);
        persistence.storeWorkflow(workflow);
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflowId));
        return logicResponse;
    }

    /**
     * This method activate a workflow.
     * 
     * @param workflowId
     *            the id of the workflow which should be deactivate
     * @return logicResponse about update
     * @throws PersistenceException 
     */
    public LogicResponse activateWorkflow(String workflowId)
            throws PersistenceException 
    {
        final LogicResponse logicResponse = new LogicResponse();
        final Workflow workflow = persistence.loadWorkflow(workflowId);
        
        workflow.setActive(true);
        persistence.storeWorkflow(workflow);
        logicResponse.add(new Message("WORKFLOW_INFO", "workflow=upd=" + workflowId));
        return logicResponse;
    }

    /**
     * Initialize test datas.
     * @throws LogicException 
     * 
     * @throws UserAlreadyExistsException .
     */
    private void initTestdata() throws LogicException {
        Workflow workflow1;
        User user1, user2, user3, user4;
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
        user4 = new User();
        user4.setUsername("TestAdmin");
        
        role1 = new Role();
        role1.setId("1");
        role1.setRolename("Rolle 1");
        user3.getRoles().add(role1);

        addUser(user1);
        addUser(user2);
        addUser(user3);
        addUser(user4);
        
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
