package de.hsrm.swt02.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StepNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
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

    private List<Role> roles = new LinkedList<>();
    /**
     * Constructor for PersistenceImp.
     * @param logger is the logger for logging.
     */
    @Inject
    public PersistenceImp(UseLogger logger) {
        this.logger = logger;
    }
    
    
    // Workflow Operations
    
    @Override
    public String storeWorkflow(Workflow workflow) throws StorageFailedException {
        Workflow workflowToRemove = null;
        if (workflow.getId() == null) { // TODO: declare default value for empty ids
            workflow.setId(workflows.size() + 1 + "");
        } else {
            for (Workflow wf: workflows) {
                if (wf.getId().equals(workflow.getId())) {
                    workflowToRemove = wf;
                    break;
                }
            }
        }
        if (workflowToRemove != null) {
            try {
                this.deleteWorkflow(workflowToRemove.getId());
                this.logger.log(Level.INFO, "[persistence] overwriting workflow " + workflowToRemove.getId() + "...");
            } catch (WorkflowNotExistentException e) {
                throw new StorageFailedException("storage of workflow" + workflow.getId() + "failed.");
            }
        }
        try {
            final Workflow workflowToStore = (Workflow)workflow.clone();
            workflows.add(workflowToStore);
        } catch (CloneNotSupportedException e) {
            throw new StorageFailedException("storage of workflow" + workflow.getId() + "failed."); 
        }
        this.logger.log(Level.INFO, "[persistence] successfully stored workflow " + workflow.getId() + ".");
        return workflow.getId();
    }
    
    @Override
    public void deleteWorkflow(String id) throws WorkflowNotExistentException {
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId().equals(id)) {
                workflowToRemove = wf;
                break;
            }
        }
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
            this.logger.log(Level.INFO,
                    "[persistence] successfully removed workflow " + workflowToRemove.getId() + ".");
        } else {
            throw new WorkflowNotExistentException("database has no workflow " + id);
        }
        assert (workflows.contains(workflowToRemove) == false);
    }

    @Override
    public Workflow loadWorkflow(String id) throws PersistenceException {
        Workflow workflowToReturn = null;
        for (Workflow wf : workflows) {
            if (wf.getId().equals(id)) {
                try {
                    workflowToReturn = (Workflow)wf.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of workflow" + id + "failed."); 
                }
            }
        }
        if (workflowToReturn != null) {
            return workflowToReturn;
        } else {
            throw new WorkflowNotExistentException("database has no workflow " + id);
        }
    }

    
    // Item Operations
    
    @Override
    public String storeItem(Item item) throws PersistenceException {
        Workflow parentWorkflow = null;
        Item itemToRemove = null;
        
        if (item.getId() == null) {
            for (Workflow wf: workflows) {
                if (item.getWorkflowId().equals(wf.getId())) {
                    parentWorkflow = wf;
                    break;
                }
            }
            if (parentWorkflow != null) {
                item.setId(String.valueOf(Integer.parseInt(parentWorkflow.getId()) * ID_MULTIPLICATOR + parentWorkflow.getItems().size() + ""));
            } else {
                throw new WorkflowNotExistentException("invalid parent workflow id in item " + item.getId());
            }
        }
        
        for (Item i : items) {
            if (i.getId().equals(item.getId())) {
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != null) {
            this.deleteItem(itemToRemove.getId());
            this.logger.log(Level.INFO, "[persistence] overwriting item " + itemToRemove.getId() + "...");
        }
        
        Item itemToStore;
        try {
            itemToStore = (Item)item.clone();
        } catch (CloneNotSupportedException e) {
            throw new StorageFailedException("storage of item" + item.getId() + "failed."); 
        }
        items.add(itemToStore);
        this.logger.log(Level.INFO, "[persistence] successfully stored item " + item.getId() + ".");
        return item.getId();
    }
    
    @Override
    public void deleteItem(String id) throws ItemNotExistentException {
        Item itemToRemove = null;
        for (Item item : items) {
            if (item.getId().equals(id)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
            this.logger.log(Level.INFO,"[persistence] successfully removed workflow " + itemToRemove.getId() + ".");
        } else {
            throw new ItemNotExistentException("database has no item " + id);
        }
    }

    @Override
    public Item loadItem(String id) throws PersistenceException {
        Item itemToReturn = null;
        for (Item item : items) {
            if (item.getId().equals(id)) {
                try {
                    itemToReturn = (Item)item.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of item" + id + "failed."); 
                }
            }
        }
        if (itemToReturn != null) {
            return itemToReturn;
        } else {
            throw new ItemNotExistentException("database has no item " + id);
        }
    }

    // User Operations
    
    @Override
    public void addUser(User user) throws PersistenceException {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                throw new UserAlreadyExistsException("username " + user.getUsername() + " is already assigned.");
            }
        }
        User userToStore;
        try {
            userToStore = (User) user.clone();
        } catch (CloneNotSupportedException e) {
            throw new StorageFailedException("storage of user '" + user.getUsername() + "' failed."); 
        }
        users.add(userToStore);
        this.logger.log(Level.INFO, "[persistence] successfully added user '" + userToStore.getUsername() + "'.");
    }

    @Override
    public void updateUser(User user) throws PersistenceException {        
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                userToRemove = u;
            }
        }
        if (userToRemove != null) {
            this.deleteUser(userToRemove.getUsername());
            this.logger.log(Level.INFO, "[persistence] overwriting user '" + userToRemove.getUsername() + "'...");
        }
        
//            // check if there are duplicate messagingsubs
//            final List<String> subs = user.getMessagingSubs();
//            final List<String> definiteSubs = null;
//            for (String sub :subs) {
//                if (!definiteSubs.contains(sub)) {
//                    definiteSubs.add(sub);
//                }
//            }
            //messagingsublist is cleared and filled with the new list
//            user.getMessagingSubs().clear();
//            user.getMessagingSubs().addAll(definiteSubs);
            //finally user is added

        try {
            this.addUser(user);
        } catch (UserAlreadyExistsException e) {
            throw new StorageFailedException("update user failure, duplicate users");
        }
    }

    @Override
    public User loadUser(String username) throws UserNotExistentException {
        User userToReturn = null;
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                userToReturn = u;
            }
        }
        if (userToReturn != null) {
            return userToReturn;
        } else {
            throw new UserNotExistentException("database has no user '" + username + "'.");
        }
    }

    @Override
    public void deleteUser(String username) throws UserNotExistentException {
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
            this.logger.log(Level.INFO,"[persistence] successfully removed user " + userToRemove.getId() + ".");
        } else {
            throw new UserNotExistentException("database has no user '" + username + "'.");
        }
    }


    @Override
    public List<Workflow> loadAllWorkflows() throws WorkflowNotExistentException {
        if (workflows.size() > 0) {
            return workflows;
        }
        else {
            throw new WorkflowNotExistentException("no stored workflows on database");
        }
    }
    
    
    // temp load Step - to be completed
    @Override
    public Step loadStep(String itemId, String stepId) throws PersistenceException {
        final Item item = loadItem(itemId);
        final String workflowId = item.getWorkflowId();
        final Workflow workflow = loadWorkflow(workflowId);
        Step step = null;
        
        for (Step s : workflow.getSteps()) {
            if (s.getId().equals(stepId)) {
                step = s;
            }
        }
        
        if (step != null) {
            return step;
        } else {
            throw new StepNotExistentException("Step " + stepId + " is not existent.");
        }
    }
    
    
    
    // Sprint 2 Persistence  
    
    
    
    
    /**
     * Method for storing a role.
     * @param role is the role to store
     * @return roleId is the Id of the stored role
     */
    public String storeRole(Role role) {
        if (Integer.parseInt(role.getId()) <= 0) {
            role.setId(roles.size() + 1 + "");
        }
        Role roleToRemove = null;
        for (Role r : roles) {
            if (r.getId().equals(role.getId())) {
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
        return role.getId();
    }

    /**
     * Method for loading all roles into a list of roles.
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     * @return List<Workflow> is the list we want to load
     */
    public List<Role> loadAllRoles() throws RoleNotExistentException {
        if (roles.size() > 0) {
            return roles;
        }
        else {
            final RoleNotExistentException e = new RoleNotExistentException("no stored roles in database");
            this.logger.log(Level.INFO, e);
            throw e;
        }
    }
    
    /**
     * Method for loeading all users into a list of users.
     * @exception UserNotExistentException if the requested user is not there
     * @throws UserNotExistentException
     * @return List<User> is the list we want to load
     */
    @Override
    public List<User> loadAllUsers() throws UserNotExistentException {
        if (users.size() > 0) {
            return users;
        }
        else {
            final UserNotExistentException e = new UserNotExistentException("no stored users in database");
            this.logger.log(Level.INFO, e);
            throw e;
        }
    }

    /**
     * Method for loading a workflow.
     * @param id is the id of the requested workflow.
     * @return workflow is the requested workflow
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     */
    public Role loadRole(String id) throws RoleNotExistentException {
        Role role = null;
        for (Role r : roles) {
            if (r.getId().equals(id)) {
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
     * @exception UserNotExistentException if the requested user is not there
     * @exception RoleNotExistentException if the requested role is not there
     * @throws UserHasAlreadyRoleException
     * @throws UserNotExistentException
     * @throws RoleNotExistentException  
    */
    @Override
    public void addRoleToUser(User user, Role role) throws PersistenceException {
        Role searchedRole = null;
        User searchedUser = null;
        for (Role r : roles) {
            if (r.getId().equals(role.getId())) {
                searchedRole = r;
            }
        }
        if (searchedRole == null) {
            final RoleNotExistentException e = new RoleNotExistentException(role.getRolename());
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                searchedUser = u;
            }
        }
        if (searchedUser == null) {
            final UserNotExistentException e = new UserNotExistentException(user.getUsername());
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        
        
        for (Role r: user.getRoles()) {
            if (role.getId().equals(r.getId())) {
                final UserHasAlreadyRoleException e = new UserHasAlreadyRoleException(role.getRolename());
                this.logger.log(Level.WARNING, e);
                throw e;
            }
        }
        
        user.getRoles().add(role);
    }
    
    /**
     * Method for deleting an existing role.
     * @param rolename is the name of the given role
     * @exception RoleNotExistentException if the given role doesnt exist
     * @throws RoleNotExistentException
     */
    public void deleteRole(String rolename) throws RoleNotExistentException {
        Role roleToRemove = null;
        for (Role r: roles) {
            if (r.getRolename().equals(rolename)) {
                roleToRemove = r;
                break;
            }
        }
        if (roleToRemove != null) {
            for (User u: users) {
                for (Role r: u.getRoles()) {
                    if (r.getRolename().equals(rolename)) {
                        u.getRoles().remove(r);
                    }
                }
            }
            roles.remove(roleToRemove);
        } else {
            final RoleNotExistentException e = new RoleNotExistentException(rolename);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
    }
    
    
}
