package de.hsrm.swt02.businesslogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.LogInException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.protocol.Message;
import de.hsrm.swt02.businesslogic.protocol.MessageOperation;
import de.hsrm.swt02.businesslogic.protocol.MessageTopic;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
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
    private UseLogger logger;

    /**
     * Constructor for LogicImp.
     * 
     * @param p is a singleton instance of the persistence
     * @param pm is a singleton instance of the process manager
     * @param logger is the logger instance for this application
     * @throws LogicException if something goes wrong
     */
    @Inject
    public LogicImp(Persistence p, ProcessManager pm, UseLogger logger) 
        throws LogicException {
        this.persistence = p;
        this.processManager = pm;
        this.logger = logger;

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
     * @throws LogicException
     */
    @Override
    public LogicResponse startWorkflow(String workflowID, String username)
            throws LogicException, PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();
        final Workflow workflow;
        final String itemId;

        workflow = persistence.loadWorkflow(workflowID);
        itemId = processManager.startWorkflow(workflow, username);
        logicResponse.add(Message.buildWithTopicId(MessageTopic.ITEMS_FROM_,
                workflowID, MessageOperation.DEFINITION, itemId));
        return logicResponse;
    }

    /**
     * This method stores a workflow and distribute a id. This method is used for saving a new workflow and
     * for editting a workflow. It is also used for updating a workflow. If a completely new workflow should be saved
     * it doesn't have an id (means it's null or ""). If a workflow is to be edited it already has an id. 
     * If a workflow doesn't have any unfinished items (or none at all) it will be overwritten. Otherwise the "original" workflow
     * will be deactivated and a new workflow (with only steps, other attributes are reseted) will be saved.
     * Should a workflow be de-/activated then its state will be setted on the original one which will be saved.
     * 
     * @param workflow is new workflow which should be stored
     * @throws LogicException is thrown if errors occur while storing the
     *             workflow
     */
    @Override
    public LogicResponse addWorkflow(Workflow workflow) throws LogicException {
        Workflow oldWorkflow = null;
        String id, oldWorkflowId;
        boolean finished = true;
        final LogicResponse logicResponse = new LogicResponse();

        if ((workflow.getStepByPos(0) instanceof StartStep)
                && (workflow.getStepByPos(workflow.getSteps().size() - 1) instanceof FinalStep)) 
        {
            if (workflow.getId() == null || workflow.getId().equals("")) {
                id = persistence.storeWorkflow(workflow);
                logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                        MessageOperation.DEFINITION, id));
            } else {
                oldWorkflow = persistence.loadWorkflow(workflow.getId());
                if (oldWorkflow != null) {
                    for (Item item : oldWorkflow.getItems()) {
                        if (!item.isFinished()) {
                            finished = false;
                            break;
                        }
                    }
                    if (finished) {
                        id = persistence.storeWorkflow(workflow);
                        
                        logicResponse.add(Message.build(
                                MessageTopic.WORKFLOW_INFO,
                                MessageOperation.DEFINITION, id));
                    } else {
                        
                        if (oldWorkflow.isActive() != workflow.isActive()) {
                            oldWorkflow.setActive(workflow.isActive());
                            oldWorkflowId = persistence.storeWorkflow(oldWorkflow);
                            logicResponse.add(Message.build(
                                    MessageTopic.WORKFLOW_INFO,
                                    MessageOperation.UPDATE, oldWorkflowId));
                        } else {
                            oldWorkflow.setActive(false);
                            oldWorkflowId = persistence.storeWorkflow(oldWorkflow);
                            workflow.setId("");
                            workflow.setActive(true);
                            workflow.getItems().clear();
                            id = persistence.storeWorkflow(workflow);
                            logicResponse.add(Message.build(
                                    MessageTopic.WORKFLOW_INFO,
                                    MessageOperation.UPDATE, oldWorkflowId));
                            logicResponse.add(Message.build(
                                    MessageTopic.WORKFLOW_INFO,
                                    MessageOperation.DEFINITION, id));
                        }
                    }
                }
            }
            return logicResponse;
        } else {
            throw new IncompleteEleException();
        }
    }

    /**
     * This method loads a Workflow.
     * 
     * @param workflowID describe the workflow
     * @return a Workflow, if there is one, who has this workflowID
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     */
    @Override
    public Workflow getWorkflow(String workflowID) throws PersistenceException {
        return persistence.loadWorkflow(workflowID);
    }

    /**
     * This method delete a Workflow in Persistence.
     * 
     * @param workflowID describe the Workflow
     * @throws PersistenceException
     */
    @Override
    public LogicResponse deleteWorkflow(String workflowID)
            throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();

        persistence.deleteWorkflow(workflowID);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.DELETION, workflowID));
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
    public LogicResponse stepForward(String itemId, String stepId,
            String username) throws LogicException {
        final String updatedItemId;
        final String workflowId;
        final LogicResponse logicResponse = new LogicResponse();
        
        updatedItemId = processManager.executeStep(
                persistence.loadStep(itemId, stepId),
                persistence.loadItem(itemId), persistence.loadUser(username));
        workflowId = persistence.loadItem(itemId).getWorkflowId();
        logicResponse.add(Message.buildWithTopicId(MessageTopic.ITEMS_FROM_,
                workflowId, MessageOperation.DEFINITION, updatedItemId));
        return logicResponse;
    }

    /**
     * This method add a step into an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param step the step, which should be added
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     */
    @Override
    public LogicResponse addStep(String workflowID, Step step)
            throws PersistenceException {
        final Workflow workflow;
        final LogicResponse logicResponse = new LogicResponse();

        workflow = persistence.loadWorkflow(workflowID);
        workflow.addStep(step);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflow.getId()));
        return logicResponse;
    }

    /**
     * This method delete a step from an existing Workflow.
     * 
     * @param workflowID the workflow, which shall edited
     * @param stepID the step, which shall delete
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     */
    @Override
    public LogicResponse deleteStep(String workflowID, String stepID)
            throws PersistenceException {
        final Workflow workflow;
        final LogicResponse logicResponse = new LogicResponse();

        workflow = persistence.loadWorkflow(workflowID);
        workflow.removeStep(stepID);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflow.getId()));
        return logicResponse;
    }

    /**
     * This method store a workflow and distribute a id.
     * 
     * @param user
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
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
            // messagingsublist is cleared and filled with the new list
            user.getMessagingSubs().clear();
            user.getMessagingSubs().addAll(definiteSubs);

        }
        // finally user is added
        persistence.storeUser(user);
        logicResponse.add(Message.build(MessageTopic.USER_INFO,
                MessageOperation.DEFINITION, user.getUsername()));
        return logicResponse;
    }

    /**
     * This method loads a User.
     * 
     * @param username describe the user
     * @return a User, if there is one, who has this username
     * @throws PersistenceException 
     */
    @Override
    public User getUser(String username) throws PersistenceException {
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
            throws UserNotExistentException {
        final LogicResponse logicResponse = new LogicResponse();

        persistence.deleteUser(username);
        logicResponse.add(Message.build(MessageTopic.USER_INFO,
                MessageOperation.DELETION, username));
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
    public List<Workflow> getAllWorkflowsForUser(String username)
            throws LogicException {
        persistence.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
            if (wf.isActive() || wf.getItems().size() > 0) {
                for (Step step : wf.getSteps()) {
                    if (checkAuthorization(step, username)) {
                        Workflow copyOfWf;
                        try {
                            copyOfWf = ((Workflow) wf.clone());
                            copyOfWf.getItems().clear(); // eliminate items
                            workflows.add(copyOfWf);
                            break;
                        } catch (CloneNotSupportedException e) {
                            throw new LogicException(
                                    "Workflow is not cloneable.");
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
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     * @throws CloneNotSupportedException
     */

    public List<Workflow> getAllWorkflowsByUserWithItems(String username)
            throws PersistenceException, CloneNotSupportedException {
        persistence.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
            if (wf.isActive()) {
                for (Step step : wf.getSteps()) {
                    if (checkAuthorization(step, username)) {
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

        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getAllWorkflowsForUser(username);
        final LinkedList<Item> items = new LinkedList<Item>();

        for (Workflow wf : workflows) {
            for (Item item : wf.getItems()) {

                if (checkAuthorization(
                        wf.getStepById(item.getActStep().getKey()), username)) {
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

    public List<String> getStartableWorkflowsForUser(String username)
            throws LogicException {

        final List<String> startableWorkflows = new LinkedList<>();
        for (Workflow workflow : getAllWorkflowsForUser(username)) {
            if (workflow.isActive()) {
                final Step startStep = workflow.getSteps().get(0);
                assert (startStep instanceof StartStep);
                if (checkAuthorization(startStep, username)) {
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
    public List<Workflow> getStartableWorkflows(String username)
            throws LogicException {

        final LinkedList<Workflow> startableWorkflows = new LinkedList<Workflow>();
        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getAllWorkflowsForUser(username);

        for (Workflow wf : workflows) {
            if (checkAuthorization(wf.getStepByPos(0), username)) {
                startableWorkflows.add(wf);
            }
        }
        return startableWorkflows;
    }

    /**
     * Method for getting a list of ids of the items relevant to an user (if
     * he's responsible for a step in the steplist).
     * 
     * @param workflowId is the id of the given workflow
     * @param username desribes the given user
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     * @return all relevant items for an user
     */
    public List<Item> getRelevantItemsForUser(String workflowId, String username)
            throws PersistenceException {
        final LinkedList<Item> relevantItems = new LinkedList<>();
        final Workflow workflow = getWorkflow(workflowId);
        
        for (Item item : workflow.getItems()) {
            final MetaEntry me = item.getActStep();
            if (me != null) {
                final Step step = workflow.getStepById(me.getKey());
                if (checkAuthorization(step, username)) {
                    relevantItems.add(item);
                }
            }
        }
        
        return relevantItems;
    }

    /**
     * Return item by itemId.
     * 
     * @param itemId indicates which item should be returned
     * @return looked for item
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     */
    public Item getItem(String itemId) throws PersistenceException {
        return persistence.loadItem(itemId);
    }

    /**
     * This method return all workflows in persistence.
     * 
     * @return all workflows in persistence
     * @throws PersistenceException 
     */
    @Override
    public List<Workflow> getAllWorkflows() throws PersistenceException {
        final List<Workflow> workflows = new LinkedList<Workflow>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
            workflows.add(wf);
        }

        return workflows;
    }

    @Override
    public List<Workflow> getAllActiveWorkflows()
            throws PersistenceException {
        final List<Workflow> workflows = new LinkedList<Workflow>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
            if (wf.isActive()) {
                workflows.add(wf);
            }
        }

        return workflows;
    }

    /**
     * This method check a User, later it will be extended for password.
     * 
     * @param username the user, to be checked
     * @return if user existing true, else false
     * @throws LogInException
     * @throws n 
     */
    @Override
    public boolean checkLogIn(String username, String password,
            boolean adminRequired) throws LogicException, LogInException {
        User user;
        try {
            user = persistence.loadUser(username);
        } catch (UserNotExistentException e) {
            throw new LogInException();
        }

        if (!user.getPassword().equals(password)) {
            throw new LogInException();
        }

        if (adminRequired) {
            for (Role aktRole : user.getRoles()) {
                if (aktRole.getRolename().equals("admin")) {
                    return true;
                }
            }
            throw new LogInException();
        }
        return true;
    }

    /**
     * 
     * @param step to be operated on
     * @param username that has to be authorized
     * @return boolean
     * @throws PersistenceException to catch UserNotExistent or RoleNotExistent
     *             exceptions
     */
    public boolean checkAuthorization(Step step, String username)
            throws PersistenceException {
        final User userToCheck = persistence.loadUser(username);
        boolean authorized = false;
        for (String rolename : step.getRoleIds()) {
            if (userToCheck.hasRole(persistence.loadRole(rolename))) {
                authorized = true;
                break;
            }
        }
        return authorized;
    }

    // BusinessLogic Sprint 2

    /**
     * This method loads a Role.
     * 
     * @param rolename describe the role
     * @return a role, if there is one, who has this rolename
     * @throws RoleNotExistentException
     * @throws PersistenceException 
     */
    public Role getRole(String rolename) throws PersistenceException {
        return (Role) persistence.loadRole(rolename);
    }

    /**
     * This method returns all roles in persistence.
     * 
     * @return p.loadAllRoles is the list of all Roles
     * @throws RoleNotExistentException
     * @throws PersistenceException 
     */
    public List<Role> getAllRoles() throws PersistenceException {
        return persistence.loadAllRoles();
    }

    /**
     * This method returns all users in persistance.
     * 
     * @return p.loadAll Users is the list of all users
     * @throws UserNotExistentException
     * @throws PersistenceException 
     */
    public List<User> getAllUsers() throws PersistenceException {
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
        logicResponse.add(Message.build(MessageTopic.ROLE_INFO,
                MessageOperation.DEFINITION, role.getRolename()));
        return logicResponse;
    }

    /**
     * This method adds a role to a user.
     * 
     * @param username indicates who will get a role assigned
     * @param role which user should be assigned
     * @return logicResponse about update
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     */
    @Override
    public LogicResponse addRoleToUser(User user, Role role)
            throws PersistenceException 
    {
        final User userToUpdate = persistence.loadUser(user.getUsername());
        final Role roleToAdd = persistence.loadRole(role.getRolename());
        userToUpdate.addRole(roleToAdd);
        persistence.storeUser(userToUpdate);
        
        final LogicResponse logicResponse = new LogicResponse();

        logicResponse.add(Message.build(MessageTopic.USER_INFO,
                MessageOperation.UPDATE, user.getUsername()));
        return logicResponse;
    } 
    
    /**
     * 
     * @param user - user from database
     * @param role - role to be deleted
     * @throws PersistenceException - is thrown if user or role are not existent
     */
    public void deleteRoleFromUser(User user, Role role) throws PersistenceException {
        final User userToUpdate = persistence.loadUser(user.getUsername()); 
        if (userToUpdate.hasRole(role)) {
            userToUpdate.removeRole(role);
            persistence.storeUser(userToUpdate);
            logger.log(Level.INFO, "[Logic] successfully removed role " + role.getRolename() + " from user " + user.getUsername() + ".");
        }
    }
    
    /**
     * This method deletes a role.
     * 
     * @param rolename indicates which should be deleted
     * @return logicResponse about delete
     * @throws WorkflowNotExistentException
     * @throws PersistenceException.
     */
    @Override
    public LogicResponse deleteRole(String rolename)
            throws PersistenceException 
    {
        // check if role is used in any step of an workflow 
        boolean roleInUse = false;
        for (Workflow workflow : this.getAllWorkflows()) {
            for (Step step : workflow.getSteps()) {
                if (step.getRoleIds().contains(persistence.loadRole(rolename).getId())) {
                    roleInUse = true;
                    break;
                }
            }
            if (roleInUse) {
                break;
            }
        }
        
        if (roleInUse) {
            throw new RoleNotExistentException("[Logic] No Deletion allowed - Role " + rolename + "is still in use.");
        }
        
        final Role roleToRemove = persistence.loadRole(rolename);
        persistence.deleteRole(roleToRemove.getRolename());
        
        // delete role from all existent users as well
        for (User user: persistence.loadAllUsers()) {
            this.deleteRoleFromUser(user, roleToRemove);
        }
        
        final LogicResponse logicResponse = new LogicResponse();
        logicResponse.add(Message.build(MessageTopic.ROLE_INFO,
                MessageOperation.DELETION, rolename));
        return logicResponse;
    }

    /**
     * This method deactivates a workflow.
     * 
     * @param workflowId the id of the workflow which should be deactivated
     * @return logicResponse about update
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     */
    public LogicResponse deactivateWorkflow(String workflowId)
            throws PersistenceException {
        final Workflow workflow = persistence.loadWorkflow(workflowId);
        final LogicResponse logicResponse = new LogicResponse();

        workflow.setActive(false);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflowId));
        return logicResponse;
    }

    /**
     * This method activate a workflow.
     * 
     * @param workflowId the id of the workflow which should be deactivate
     * @return logicResponse about update
     * @throws PersistenceException
     */
    public LogicResponse activateWorkflow(String workflowId)
            throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();
        final Workflow workflow = persistence.loadWorkflow(workflowId);

        workflow.setActive(true);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflowId));
        return logicResponse;
    }
    
    @Override
    public LogicResponse addForm(Form form) throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();
        String id;
        
        id = persistence.storeForm(form);
        logicResponse.add(Message.build(MessageTopic.FORM_INFO, MessageOperation.DEFINITION, id));
        return logicResponse;
    }

    @Override
    public LogicResponse deleteForm(String formId) throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();
        String id;
        
        id = persistence.deleteForm(formId);
        logicResponse.add(Message.build(MessageTopic.FORM_INFO, MessageOperation.DELETION, id));
        return logicResponse;
    }

    @Override
    public LogicResponse updateItem(Item item) throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();
        Workflow workflow;
        String workflowId, itemId;
        
        itemId = item.getId();
        workflowId = item.getWorkflowId();
        workflow = persistence.loadWorkflow(workflowId);
        workflow.removeItem(itemId);
        workflow.addItem(item);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.buildWithTopicId(MessageTopic.ITEMS_FROM_, workflowId, MessageOperation.UPDATE, itemId));
        return logicResponse;
    }

    @Override
    public List<Form> getAllForms() throws PersistenceException {
        final List<Form> forms = new LinkedList<Form>();
        
        for (Form form : persistence.loadAllForms()) {
            forms.add(form);
        }
        return forms;
    }

    /**
     * Initialize test datas.
     * 
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
        Role role1, role2, role3;

        user1 = new User();
        user1.setUsername("Alex");
        user2 = new User();
        user2.setUsername("Dominik");
        user3 = new User();
        user3.setUsername("Tilman");
        user4 = new User();
        user4.setUsername("TestAdmin");
        user4.setPassword("abc123");

        role1 = new Role();
        // role1.setId("1");
        role1.setRolename("Manager");
        addRole(role1);
        role2 = new Role();
        role2.setRolename("Sachbearbeiter");
        addRole(role2);
        role3 = new Role();
        role3.setRolename("admin");
        addRole(role3);

        user1.addRole(role1);
        user2.addRole(role2);
        user4.addRole(role3);

        addUser(user1);
        addUser(user2);
        addUser(user3);
        addUser(user4);

        ArrayList<String> user1Roles = new ArrayList<String>();
        user1Roles.add(role1.getRolename());
        ArrayList<String> user2Roles = new ArrayList<String>();
        user2Roles.add(role2.getRolename());

        startStep1 = new StartStep();
        startStep1.getRoleIds().addAll(user1Roles);

        action1 = new Action(new ArrayList<String>(), "Action von "
                + user1.getUsername());
        action2 = new Action(new ArrayList<String>(), "Action von "
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
