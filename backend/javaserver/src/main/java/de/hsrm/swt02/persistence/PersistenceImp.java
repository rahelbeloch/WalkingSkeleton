package de.hsrm.swt02.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.RoleHasAlreadyUserException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserHasAlreadyRoleException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * @author Dominik
 *
 */
@Singleton
public class PersistenceImp implements Persistence {

    /** The logger. */
    private UseLogger logger;
    private static final int ID_MULTIPLICATOR = 1000;
    
    /*
     * abstraction of a database, that persists the data objects workflow, item,
     * user, step, metaEntry
     */
    private List<Workflow> workflows = new LinkedList<>();
    private List<Item> items = new LinkedList<>();
    private List<User> users = new LinkedList<>();

    private List<Step> steps = new LinkedList<>();
    private List<MetaEntry> metaEntries = new LinkedList<>();
    
    private List<Role> roles = new LinkedList<>();

    /**
     * Constructor for PersistenceImp.
     * @param logger is the logger for logging.
     */
    @Inject
    public PersistenceImp(UseLogger logger) {
        this.logger = logger;
    }

    /**
     * store functions to store workflows, items, and users into persistence.
     * @param workflow is a workflow for storing
     * @throws WorkflowNotExistentException 
     */
    @Override
    public void storeWorkflow(Workflow workflow) {
        if (workflow.getId() <= 0) {
            workflow.setId(workflows.size() + 1);
        }
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == workflow.getId()) {
                workflowToRemove = wf;
                break;
            }
        }
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
            this.logger.log(Level.INFO, "[persistence] removed existing workflow "
                    + workflowToRemove.getId() + ".");
        }
        workflows.add((Workflow) workflow);
        this.logger.log(Level.INFO, "[persistence] successfully stored workflow " + workflow.getId()
                + ".");

        // a workflows steps are resolved and stored one by one
        final List<Step> workflowsSteps = workflow.getSteps();
        for (Step step : workflowsSteps) {
            storeStep(step, workflow);
        }
    }

    /**
     * Method for loeading all workflows into a list of workflows.
     * @return List<Workflow> is the list we want to load
     */
    @Override
    public List<Workflow> loadAllWorkflows() throws WorkflowNotExistentException {
        if (workflows.size() > 0) {
            return workflows;
        }
        else {
            final WorkflowNotExistentException e = new WorkflowNotExistentException("no stored workflows on database");
            this.logger.log(Level.INFO, e);
            throw e;
        }
    }

    /**
     * store functions to store an item.
     * @param item is an item for storing
     */
    @Override
    public void storeItem(Item item) throws WorkflowNotExistentException {
        if (item.getId() <= 0) {
            Workflow motherWorkflow = null;
            for (Workflow wf: workflows) {
                if (item.getWorkflowId() == wf.getId()) {
                    motherWorkflow = wf;
                }
            }
            if (motherWorkflow != null) {
                item.setId(motherWorkflow.getId() * ID_MULTIPLICATOR + motherWorkflow.getSteps().size() + 1);

            }
            else {
                final WorkflowNotExistentException e = new WorkflowNotExistentException("invalid workflow id in item " + item.getId());
                this.logger.log(Level.WARNING, e);
                throw e;
            }
        }
        
        Item itemToRemove = null;
        for (Item i : items) {
            if (i.getId() == item.getId()) {
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
            this.logger.log(Level.INFO, "[persistence] removing exisiting item "
                    + itemToRemove.getId() + ".");
        }
        items.add((Item) item);
        this.logger.log(Level.INFO, "[persistence] successfully stored item " + item.getId() + ".");

        // items include information of type MetaEntry which have to be stored
        // separately
        final List<MetaEntry> itemsMetadata = item.getMetadata();
        for (MetaEntry metaEntry : itemsMetadata) {
            storeMetaEntry(metaEntry);
        }
    }

    /**
     * Method for adding a new user.
     * @param user is the needed user
     * @exception UserAlreadyExistsException if the user to store is already there.
     * @throws UserAlreadyExistsException
     */
    @Override
    public void addUser(User user) throws UserAlreadyExistsException {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                final UserAlreadyExistsException e = new UserAlreadyExistsException(
                        user.getUsername());
                this.logger.log(Level.WARNING, e);
                throw e;
            }
        }
        users.add((User) user);
        this.logger
                .log(Level.INFO, "[persistence] adding user '" + user.getUsername() + "'.");
    }

    /**
     * Method for updating an already existing user.
     * @param user is the user we need
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     */
    @Override
    public void updateUser(User user) throws UserNotExistentException {
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                userToRemove = u;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
            this.logger.log(Level.INFO, "[persistence] removing existing user '"
                    + userToRemove.getUsername() + "'.");
            users.add(user);
            this.logger.log(Level.INFO,
                    "[persistence] successfully stored user '" + user.getUsername() + "'.");
        } else {
            final UserNotExistentException e = new UserNotExistentException(
                    user.getUsername());
            this.logger.log(Level.WARNING, e);
            throw e;
        }
    }

    /**
     * Method for storing a step.
     * @param step is the  step we need to store
     */
    public void storeStep(Step step, Workflow motherWorkflow) {
        if(loadStep(step.getId()) == null) {
            step.setId(motherWorkflow.getId()*ID_MULTIPLICATOR + motherWorkflow.getSteps().indexOf(step)+1);
        }
        Step stepToRemove = null;
        for (Step s : steps) {
            if (s.getId() == step.getId()) {
                stepToRemove = s;
                break;
            }
        }
        if (stepToRemove != null) {
            steps.remove(stepToRemove);
        }
        steps.add((Step) step);
        //TODO: need to distinguish between Action/FirstStep/StartStep?
    }

    /**
     * Method for storing a MetaEntry.
     * @param metaEntry is the MetaEntry we need to store
     */
    public void storeMetaEntry(MetaEntry metaEntry) {
        if (metaEntry.getKey().equals("")) {
            metaEntry.setKey(Integer.toString(metaEntries.size() + 1));
        }
        MetaEntry metaEntryToRemove = null;
        for (MetaEntry me : metaEntries) {
            // assumption that MetaEntries have keys that are unique
            if (me.getKey().equals(metaEntry.getKey())) {
                metaEntryToRemove = me;
                break;
            }
        }
        if (metaEntryToRemove != null) {
            metaEntries.remove(metaEntryToRemove);
        }
        metaEntries.add((MetaEntry) metaEntry);
    }

    /**
     * Method for loading a workflow.
     * @param id is the id of the requested workflow.
     * @return workflow is the requested workflow
     * @exception WorkflowNotExistentException if the requested workflow is not there.
     * @throws WorkflowNotExistentException
     */
    @Override
    public Workflow loadWorkflow(int id) throws WorkflowNotExistentException {
        Workflow workflow = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
                workflow = wf;
            }
        }
        if (workflow != null) {
            return workflow;
        } else {
            final WorkflowNotExistentException e = new WorkflowNotExistentException(
                    "" + id);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        // TODO: return steps of a workflow as well! - can be done as soon as a
        // unique and combined ID for steps and workflows is given
    }

    /**
     * Method for loading an item.
     * @param id is the id of the requested item.
     * @return Item is the requested item.
     * @exception ItemNotExistentException if the requested item is not there.
     * @throws ItemNotExistentException
     */
    @Override
    public Item loadItem(int id) throws ItemNotExistentException {
        Item item = null;
        for (Item i : items) {
            if (i.getId() == id) {
                item = i;
            }
        }
        if (item != null) {
            return item;
        } else {
            final ItemNotExistentException e = new ItemNotExistentException("" + id);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        // TODO: return MetaData of an item as well!
    }

    @Override
    public User loadUser(String name) throws UserNotExistentException {
        User user = null;
        for (User u : users) {
            if (u.getUsername().equals(name)) {
                user = u;
            }
        }
        if (user != null) {
            return user;
        } else {
            final UserNotExistentException e = new UserNotExistentException(name);
            throw e;
        }
    }

    /**
     * Only for the walking skeleton:  method for loading a step.
     * @param id is the id of the step.
     * @return step is the requested step
     */
    public Step loadStep(int id) {
        Step step = null;
        for (Step s : steps) {
            if (s.getId() == id) {
                step = s;
            }
        }
        return step;
    }

    /**
     * Method for loading a requested MetaEntry.
     * @param key is the key string.
     * @return MetaEntry is the requested MetaEntry of the list with the right key string.
     */
    public MetaEntry loadMetaEntry(String key) {
        MetaEntry metaEntry = null;
        for (MetaEntry me : metaEntries) {
            if (me.getKey().equals(key)) {
                metaEntry = me;
            }
        }
        return metaEntry;
    }

    /**
     * delete functions to remove workflows, items, and users from persistence.
     * @param id is the id of the requested workflow
     * @exception WorkflowNotExistentException if the requested workflow is not there.
     * @throws WorkflowNotExistentException
     */
    @Override
    public void deleteWorkflow(int id) throws WorkflowNotExistentException {
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
                // a workflows steps are resolved and deleted one by one
                final List<Step> workflowsSteps = wf.getSteps();
                for (Step step : workflowsSteps) {
                    deleteStep(step.getId());
                }
                workflowToRemove = wf;
                break;
            }
        }
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
            this.logger.log(Level.INFO,
                    "[persistence] removed workflow " + workflowToRemove.getId() + ".");
        } else {
            final WorkflowNotExistentException e = new WorkflowNotExistentException(
                    "" + id);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
    }

    /**
     * Method for function to delete an item.
     * @param id is the id of the requested item.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    @Override
    public void deleteItem(int id) throws ItemNotExistentException {
        Item itemToRemove = null;
        for (Item i : items) {
            if (i.getId() == id) {

                // an items metaData are deleted as well
                final List<MetaEntry> itemsMetaData = i.getMetadata();
                for (MetaEntry metaEntry : itemsMetaData) {
                    deleteMetaEntry(metaEntry.getKey());
                }
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
            this.logger.log(Level.INFO, "[persistence] removed item " + itemToRemove.getId()
                    + ".");
        } else {
            final ItemNotExistentException e = new ItemNotExistentException("" + id);
            this.logger.log(Level.WARNING, e);
            throw e;

        }
    }

    /**
     * Method for the function of deleting an user.
     * @param name is the name of the requested user.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     */
    @Override
    public void deleteUser(String name) throws UserNotExistentException {
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(name)) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
        } else {
            final UserNotExistentException e = new UserNotExistentException(name);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
    }

    /**
     * Method for deleting a step.
     * @param id of the requested step
     */
    public void deleteStep(int id) {
        Step stepToRemove = null;
        for (Step s : steps) {
            if (s.getId() == id) {
                stepToRemove = s;
                break;
            }
        }
        if (stepToRemove != null) {
            steps.remove(stepToRemove);
        }
    }

    /**
     * Method for deleting a MetaEntry.
     * @param key is the key string.
     */
    public void deleteMetaEntry(String key) {
        MetaEntry metaEntryToRemove = null;
        for (MetaEntry me : metaEntries) {
            if (me.getKey().equals(key)) {
                metaEntryToRemove = me;
                break;
            }
        }
        if (metaEntryToRemove != null) {
            metaEntries.remove(metaEntryToRemove);
        }
    }

    
    // Sprint 2 Persistence  
    
    /**
     * Method for storing a role.
     * @param role is the role to store
     */
    public void storeRole(Role role) {
        if (role.getId() <= 0) {
            role.setId(roles.size() + 1);
        }
        Role roleToRemove = null;
        for (Role r: roles) {
            if (r.getId() == role.getId()) {
                roleToRemove = r;
                break;
            }
        }
        if (roleToRemove != null) {
            roles.remove(roleToRemove);
            this.logger.log(Level.INFO, "[persistence] removed existing role "
                    + roleToRemove.getId() + ".");
        }
        roles.add((Role) role);
        this.logger.log(Level.INFO, "[persistence] successfully stored role " + role.getId()
                + ".");
    }

    /**
     * Method for loading all roles into a list of roles.
     * @return List<Workflow> is the list we want to load
     */
    public List<Role> loadAllRoles() {
        return roles;
    }

    /**
     * Method for loading a workflow.
     * @param id is the id of the requested workflow.
     * @return workflow is the requested workflow
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     */
    public Role loadRole(int id) throws RoleNotExistentException {
        Role role = null;
        for (Role r : roles) {
            if (r.getId() == id) {
                role = r;
            }
        }
        if (role != null) {
            return role;
        } else {
            final RoleNotExistentException e = new RoleNotExistentException("" + id);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
    }

    /**
     * @param user is the user we want to add
     * @param role is the role we want to give the user
     * @exception UserHasAlreadyRoleException if we want to assign a role to a user and the user has it already
     * @exception RoleHasAlreadyUserException if we want to assign a user to a role and the role has him already
     * @exception UserNotExistentException if the requested user is not there
     * @exception RoleNotExistentException if the requested role is not there
     * @throws UserHasAlreadyRoleException
     * @throws RoleHasAlreadyRoleException
     * @throws UserNotExistentException
     * @throws RoleNotExistentException  
    */
    @Override
    public void addUserToRole(User user, Role role) throws UserNotExistentException, RoleNotExistentException, RoleHasAlreadyUserException, UserHasAlreadyRoleException {
        Role searchedRole = null;
        User searchedUser = null;
        for (Role r : roles) {
            if (r.getId() == role.getId()) {
                searchedRole = r;
            }
        }
        if (searchedRole == null) {
            final RoleNotExistentException e = new RoleNotExistentException(role.getRolename());
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        
        for (User u : users) {
            if (u.getId() == user.getId()) {
                searchedUser = u;
            }
        }
        if (searchedUser == null) {
            final UserNotExistentException e = new UserNotExistentException(user.getUsername());
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        
        
        for (Role r: user.getRoles()) {
            if (role.getId() == r.getId()) {
                final UserHasAlreadyRoleException e = new UserHasAlreadyRoleException(role.getRolename());
                this.logger.log(Level.WARNING, e);
                throw e;
            }
        }
        
        for (User u: role.getUsers()) {
            if (user.getId() == u.getId()) {
                final RoleHasAlreadyUserException e = new RoleHasAlreadyUserException(user.getUsername());
                this.logger.log(Level.WARNING, e);
                throw e;
            }
        }
        
        user.getRoles().add(role);
        role.getUsers().add(user);
    }
}
