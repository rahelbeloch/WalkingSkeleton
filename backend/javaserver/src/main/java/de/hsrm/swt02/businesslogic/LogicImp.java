package de.hsrm.swt02.businesslogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;

import de.hsrm.swt02.businesslogic.exceptions.AdminRoleDeletionException;
import de.hsrm.swt02.businesslogic.exceptions.IncompleteEleException;
import de.hsrm.swt02.businesslogic.exceptions.LastAdminDeletedException;
import de.hsrm.swt02.businesslogic.exceptions.LogInException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.NoPermissionException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.businesslogic.protocol.Message;
import de.hsrm.swt02.businesslogic.protocol.MessageOperation;
import de.hsrm.swt02.businesslogic.protocol.MessageTopic;
import de.hsrm.swt02.businesslogic.workflowValidator.WorkflowValidator;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.DataType;
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
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;

/**
 * This class implements the logic interface and is used for logic operations.
 */
public class LogicImp implements Logic {

    private Persistence persistence;
    private ProcessManager processManager;
    private UseLogger logger;
    private String adminRolename = "admin";

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
            throws LogicException 
    {
        this.persistence = p;
        this.processManager = pm;
        this.logger = logger;
        loadData();
    }

    @Override
    public LogicResponse startWorkflow(String workflowID, String username)
            throws LogicException, PersistenceException 
    {
        final LogicResponse logicResponse = new LogicResponse();
        final Workflow workflow;
        final String itemId;

        workflow = persistence.loadWorkflow(workflowID);
        itemId = processManager.startWorkflow(workflow, username);
        logicResponse.add(Message.buildWithTopicId(MessageTopic.ITEMS_FROM_,
                workflowID, MessageOperation.DEFINITION, itemId));
        return logicResponse;
    }

    @Override
    public LogicResponse addWorkflow(Workflow workflow) throws LogicException {
        Workflow oldWorkflow = null;
        String id, oldWorkflowId;
        boolean finished = true;
        final LogicResponse logicResponse = new LogicResponse();
        final WorkflowValidator validator = new WorkflowValidator(workflow, persistence);

        if (validator.isValid()) {
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
                            oldWorkflowId = persistence
                                    .storeWorkflow(oldWorkflow);
                            logicResponse.add(Message.build(
                                    MessageTopic.WORKFLOW_INFO,
                                    MessageOperation.UPDATE, oldWorkflowId));
                        } else {
                            oldWorkflow.setActive(false);
                            oldWorkflowId = persistence
                                    .storeWorkflow(oldWorkflow);
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
            throw new IncompleteEleException("Given Workflow is not valid or incomplete.");
        }
    }

    @Override
    public Workflow getWorkflow(String workflowID) throws PersistenceException {
        return persistence.loadWorkflow(workflowID);
    }

    @Override
    public LogicResponse deleteWorkflow(String workflowID)
            throws PersistenceException 
    {
        final LogicResponse logicResponse = new LogicResponse();

        persistence.deleteWorkflow(workflowID);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.DELETION, workflowID));
        return logicResponse;
    }

    @Override
    public LogicResponse stepForward(String itemId, String stepId,
            String username) throws LogicException 
    {
        final String updatedItemId;
        final String workflowId;
        final LogicResponse logicResponse = new LogicResponse();

        updatedItemId = processManager.executeStep(
                persistence.loadStep(itemId, stepId),
                persistence.loadItem(itemId), persistence.loadUser(username));
        workflowId = persistence.loadItem(itemId).getWorkflowId();
        logicResponse.add(Message.buildWithTopicId(MessageTopic.ITEMS_FROM_,
                workflowId, MessageOperation.UPDATE, updatedItemId));
        return logicResponse;
    }

    @Override
    public LogicResponse addStep(String workflowID, Step step)
            throws PersistenceException 
    {
        final Workflow workflow;
        final LogicResponse logicResponse = new LogicResponse();

        workflow = persistence.loadWorkflow(workflowID);
        workflow.addStep(step);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflow.getId()));
        return logicResponse;
    }

    @Override
    public LogicResponse deleteStep(String workflowID, String stepID)
            throws PersistenceException 
    {
        final Workflow workflow;
        final LogicResponse logicResponse = new LogicResponse();

        workflow = persistence.loadWorkflow(workflowID);
        workflow.removeStep(stepID);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflow.getId()));
        return logicResponse;
    }

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

    @Override
    public User getUser(String username) throws PersistenceException {
        return (User) persistence.loadUser(username);
    }

    @Override
    public LogicResponse deleteUser(String username)
            throws UserNotExistentException 
    {
        final LogicResponse logicResponse = new LogicResponse();

        persistence.deleteUser(username);
        logicResponse.add(Message.build(MessageTopic.USER_INFO,
                MessageOperation.DELETION, username));
        return logicResponse;
    }

    @Override
    public List<Workflow> getAllWorkflowsForUser(String username)
            throws LogicException
    {
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
     * Additional method to get all workflows for a specific user WITH items. This
     * method should not be used from outside the logicImplementation.
     * 
     * @param username the username
     * @return a list of workflows
     * @throws PersistenceException is thrown if errors occur while persisting
     *             objects
     * @throws CloneNotSupportedException is thrown if the clone method is not
     *             implemented
     * @throws NoPermissionException 
     */
    public List<Workflow> getAllWorkflowsByUserWithItems(String username)
            throws PersistenceException, CloneNotSupportedException, NoPermissionException
    {
        persistence.loadUser(username);
        final LinkedList<Workflow> workflows = new LinkedList<>();
        
        for (Workflow wf : persistence.loadAllWorkflows()) {
            if (wf.isActive()) {
                for (Item i : wf.getItems()) {
                    final Item loadedItem = persistence.loadItem(i.getId());
                    if (checkAuthorization(loadedItem, username)) {
                        final Workflow copyOfWf = (Workflow) wf.clone();
                        workflows.add(copyOfWf);
                        break;
                    }
                }                
            }
        }
        return workflows;
    }

    @Override
    public List<Item> getOpenItemsByUser(String username) throws LogicException {

        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getAllWorkflowsForUser(username);
        final LinkedList<Item> items = new LinkedList<Item>();

        for (Workflow wf : workflows) {
            for (Item item : wf.getItems()) {

                if (checkAuthorization(item, username)) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    @Override
    public List<String> getStartableWorkflowsForUser(String username)
            throws LogicException
    {
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

    @Override
    public List<Workflow> getStartableWorkflows(String username)
            throws LogicException
    {
        final LinkedList<Workflow> startableWorkflows = new LinkedList<Workflow>();
        final LinkedList<Workflow> workflows = (LinkedList<Workflow>) getAllWorkflowsForUser(username);

        for (Workflow wf : workflows) {
            if (checkAuthorization(wf.getStepByPos(0), username)) {
                startableWorkflows.add(wf);
            }
        }
        return startableWorkflows;
    }

    @Override
    public List<Item> getRelevantItemsForUser(String workflowId, String username)
            throws PersistenceException
    {
        final LinkedList<Item> relevantItems = new LinkedList<>();
        final Workflow workflow = getWorkflow(workflowId);

        for (Item item : workflow.getItems()) {
            final MetaEntry me = item.getActStep();
            if (me != null) {
                final Step step = workflow.getStepById(me.getGroup());
                if (checkAuthorization(step, username)) {
                    relevantItems.add(item);
                }
            }
        }
        return relevantItems;
    }

    @Override
    public Item getItem(String itemId, String username) throws LogicException {
        final Item item = persistence.loadItem(itemId);
        
        if (!checkAuthorization(item, username)) {
            throw new NoPermissionException("[logic] user " + username + " has no permission on item " + itemId);
        }
        return persistence.loadItem(itemId);
    }

    @Override
    public List<Workflow> getAllWorkflows() throws PersistenceException {
        final List<Workflow> workflows = new LinkedList<Workflow>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
            workflows.add(wf);
        }

        return workflows;
    }

    @Override
    public List<Workflow> getAllActiveWorkflows() throws PersistenceException {
        final List<Workflow> workflows = new LinkedList<Workflow>();
        for (Workflow wf : persistence.loadAllWorkflows()) {
            if (wf.isActive()) {
                workflows.add(wf);
            }
        }

        return workflows;
    }

    @Override
    public boolean checkLogIn(String username, String password,
            boolean adminRequired) throws LogicException, LogInException
    {
        User user;
        try {
            user = persistence.loadUser(username);
        } catch (UserNotExistentException e) {
            throw new LogInException();
        }
        
        if (!user.isActive()) {
            throw new LogInException();
        }

        // if (!user.getPassword().equals(password)) {
        // throw new LogInException();
        // }

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
            throws PersistenceException
    {
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
    
    /**
     * Method for checking if a logged in user is authorized to get an Item.
     * @param item the requested item
     * @param username the user who requests the item
     * @return true if authorized else false
     * @throws PersistenceException 
     * @throws NoPermissionException 
     */
    public boolean checkAuthorization(Item item, String username) throws PersistenceException, NoPermissionException {
        final Workflow workflowToCheck = persistence.loadWorkflow(item.getWorkflowId());
        if (item.getActStep() == null) {
            return false;
        } else {
            final Step actStep = workflowToCheck.getStepById((item.getActStep().getGroup()));
            return checkAuthorization(actStep, username);
        }
    }

    @Override
    public Role getRole(String rolename) throws PersistenceException {
        return (Role) persistence.loadRole(rolename);
    }

    @Override
    public List<Role> getAllRoles() throws PersistenceException {
        return persistence.loadAllRoles();
    }

    @Override
    public List<User> getAllUsers() throws PersistenceException {
        return persistence.loadAllUsers();
    }

    @Override
    public LogicResponse addRole(Role role) throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();

        persistence.storeRole(role);
        logicResponse.add(Message.build(MessageTopic.ROLE_INFO,
                MessageOperation.DEFINITION, role.getRolename()));
        return logicResponse;
    }

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
     * deletes a role from a user.
     * 
     * @param user - user from database
     * @param role - role to be deleted
     * @throws LogicException if there is a needed Exception
     */
    public void deleteRoleFromUser(User user, Role role) throws LogicException {
        boolean atLeastOneAdmin = true;
        final Role adminRole = persistence.loadRole(adminRolename);

        if (role.getRolename().equals(adminRolename)) {
            for (User userToCheck : persistence.loadAllUsers()) {
                if (userToCheck.hasRole(adminRole)) {
                    atLeastOneAdmin = false;
                    // TODO: und der letzte??
                }
            }
        }
        if (!atLeastOneAdmin) {
            throw new LastAdminDeletedException(
                    "[Logic] No Deletion allowed - Role " + adminRolename
                            + " needs to have one assigned User.");
        }

        final User userToUpdate = persistence.loadUser(user.getUsername());
        if (userToUpdate.hasRole(role)) {
            userToUpdate.removeRole(role);
            persistence.storeUser(userToUpdate);
            logger.log(Level.INFO,
                    "[Logic] successfully removed role " + role.getRolename()
                            + " from user " + user.getUsername() + ".");
        }
    }

    @Override
    public LogicResponse deleteRole(String rolename) throws LogicException {
        boolean roleInUse = false;

        if (rolename.equals(adminRolename)) {
            throw new AdminRoleDeletionException(
                    "[Logic] No Deletion allowed - Role " + rolename
                            + " is the role assigned to admins.");
        }

        // check if role is used in any step of an workflow
        for (Workflow workflow : this.getAllWorkflows()) {
            for (Step step : workflow.getSteps()) {
                if (step.getRoleIds().contains(
                        persistence.loadRole(rolename).getId()))
                {
                    roleInUse = true;
                    break;
                }
            }
            if (roleInUse) {
                break;
            }
        }

        if (roleInUse) {
            // role is still active - can not be deleted, it is in use --> write
            // a new exception
            throw new NoPermissionException(
                    "[Logic] No Deletion allowed - Role " + rolename
                            + " is still in use.");
        }

        final Role roleToRemove = persistence.loadRole(rolename);

        // delete role from all existent users as well
        for (User user : persistence.loadAllUsers()) {
            this.deleteRoleFromUser(user, roleToRemove);
        }

        persistence.deleteRole(roleToRemove.getRolename());

        final LogicResponse logicResponse = new LogicResponse();
        logicResponse.add(Message.build(MessageTopic.ROLE_INFO,
                MessageOperation.DELETION, rolename));
        return logicResponse;
    }

    @Override
    public LogicResponse deactivateWorkflow(String workflowId)
            throws PersistenceException
    {
        final Workflow workflow = persistence.loadWorkflow(workflowId);
        final LogicResponse logicResponse = new LogicResponse();

        workflow.setActive(false);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflowId));
        return logicResponse;
    }

    @Override
    public LogicResponse activateWorkflow(String workflowId)
            throws PersistenceException
    {
        final LogicResponse logicResponse = new LogicResponse();
        final Workflow workflow = persistence.loadWorkflow(workflowId);

        workflow.setActive(true);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.build(MessageTopic.WORKFLOW_INFO,
                MessageOperation.UPDATE, workflowId));
        return logicResponse;
    }

    // Form Operations

    @Override
    public LogicResponse addForm(Form form) throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();

        persistence.storeForm(form);
        logicResponse.add(Message.build(MessageTopic.FORM_INFO,
                MessageOperation.DEFINITION, form.getId()));
        return logicResponse;
    }
    
    @Override
    public LogicResponse deleteForm(String formId) throws PersistenceException {
        final LogicResponse logicResponse = new LogicResponse();

        persistence.deleteForm(formId);
        logicResponse.add(Message.build(MessageTopic.FORM_INFO,
                MessageOperation.DELETION, formId));
        return logicResponse;
    }

    @Override
    public Form getForm(String formId) throws PersistenceException {
        return persistence.loadForm(formId);
    }

    @Override
    public LogicResponse updateItem(Item item, String username) throws LogicException {
        final LogicResponse logicResponse = new LogicResponse();
        Workflow workflow;
        String workflowId, itemId, currentOpener;
        Item itemToUpdate;
        
        itemId = item.getId();
        workflowId = item.getWorkflowId();
        workflow = persistence.loadWorkflow(workflowId);
        itemToUpdate = workflow.getItemById(itemId);
        currentOpener = itemToUpdate.getEntryOpener(itemToUpdate.getActStep().getGroup());

        if (currentOpener == null) {
            itemToUpdate.setEntryOpener(itemToUpdate.getActStep().getGroup(), username);
        } else {
            currentOpener = itemToUpdate.getEntryOpener(itemToUpdate.getActStep().getGroup());
            if (!currentOpener.equals(username)) {
                throw new UserHasNoPermissionException("Access denied. Current Operator is " + currentOpener);
            }
        }
        
        for (MetaEntry me : item.getMetadata()) {  
            if (me.getGroup().equals(workflow.getForm().getId()) && !(me.getValue().equals(""))) {
                final String datatype = workflow.getForm().getDataType(me.getKey());
                if (datatype != null) {
                    if (DataType.fromValue(datatype.toUpperCase()).equals(DataType.STRING)) {
                        if (!me.getValue().matches("[a-zA-Z]+")) {
                            throw new StorageFailedException("[logic] invalid form entry --- used unconform String representation");
                        }
                    } else if (DataType.fromValue(datatype.toUpperCase()).equals(DataType.INT)) {
                        if (!me.getValue().matches("[0-9]+")) {
                            throw new StorageFailedException("[logic] invalid form entry --- used unconform Integer representation");
                        } 
                    } else if (DataType.fromValue(datatype.toUpperCase()).equals(DataType.DOUBLE)) {
                        if (!me.getValue().matches("-?\\d+(\\.\\d+)?")) {
                            throw new StorageFailedException("[logic] invalid form entry --- used unconform Double representation");
                        }
                    }
                }
            }
        }
        
        workflow.removeItem(itemId);
        workflow.addItem(item);
        persistence.storeWorkflow(workflow);
        logicResponse.add(Message.buildWithTopicId(MessageTopic.ITEMS_FROM_,
                workflowId, MessageOperation.UPDATE, itemId));
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
    
    @Override
    public void loadData() {
        persistence.load();
    }

    @Override
    public void saveData() {
        persistence.save();
    }

    @Override
    public void setStoragePath(String storagePath) {
        persistence.setStoragePath(storagePath);
    }
}