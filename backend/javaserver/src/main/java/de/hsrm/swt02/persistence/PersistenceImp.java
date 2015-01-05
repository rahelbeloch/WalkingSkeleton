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
    public String storeWorkflow(Workflow workflow) throws PersistenceException {
        Workflow workflowToRemove = null;
        if (workflow.getId() == null || workflow.getId().equals("")) {
            workflow.setId(workflows.size() + 1 + "");
        } else {
            workflowToRemove = loadWorkflow(workflow.getId());
        }
        if (workflowToRemove != null) {
            try {
                this.deleteWorkflow(workflowToRemove.getId());
                this.logger.log(Level.INFO, "[persistence] overwriting workflow " + workflowToRemove.getId() + "...");
            } catch (WorkflowNotExistentException e) {
                throw new StorageFailedException("storage of workflow" + workflow.getId() + "failed.");
            }
        }
        
        final Workflow workflowToStore = workflow;
        if (workflow.getItems().size() > 0) {
            for (Item item : workflow.getItems()) {
                if (item.getId() == null || item.getId().equals("")) {
                    item.setId((Integer.parseInt(workflow.getId()) * ID_MULTIPLICATOR + workflow.getItems().size() + 1) + "");
                    item.setWorkflowId(workflow.getId());
                }
            }
        }
        
        if (workflow.getSteps().size() > 0) {
            for (Step step: workflow.getSteps()) {
                if (step.getId() == null || step.getId().equals("")) {
                    step.setId((Integer.parseInt(workflow.getId()) * ID_MULTIPLICATOR + workflow.getSteps().size() + 1) + "");
                }
            }
        }
        
        workflows.add(workflowToStore);
        
        this.logger.log(Level.INFO, "[persistence] successfully stored workflow " + workflow.getId() + ".");
        return workflow.getId();
    }
    
    @Override
    public void deleteWorkflow(String id) throws PersistenceException {
        final Workflow workflowToRemove = loadWorkflow(id);
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
    
//    /**
//     * Method for adding an item to the workflow.
//     * @param workflowId is the needed workflowId
//     * @param item is the needed item
//     * @return itemId is the stored item
//     * @exception PersistenceException indicates errors in storage methods
//     * @throws PersistenceException 
//     */
//    public Workflow addItemToWorkflow(Workflow workflow, Item item) throws PersistenceException {
//        Item itemToRemove = null;
//        boolean itemExists = false;
//        
//        if(item.getId() == null){
//            item.setId((Integer.parseInt(workflow.getId()) * ID_MULTIPLICATOR + workflow.getItems().size() + 1) + "");
//            item.setWorkflowId(workflow.getId());
//            itemExists = true;
//        }
//        if (!itemExists){
//            for (Item i : workflow.getItems()) {
//                if (i.getId().equals(item.getId())) {
//                    itemToRemove = i; 
//                    itemExists = true;
//                    break;
//                }
//            }
//        }
//        if (itemToRemove != null) {
//            this.deleteItem(itemToRemove.getId());
//            this.logger.log(Level.INFO, "[persistence] overwriting item " + itemToRemove.getId() + "...");
//        }
//        
//        try {
//            workflow.addItem((Item)item.clone());
//        } catch (CloneNotSupportedException e) {
//            throw new StorageFailedException("storage of item" + item.getId() + "to workflow " + workflow.getId() + "failed."); 
//        }
//        return workflow;
//    }
    
    /**
     * Method for getting the parentworkflow of an item.
     * @param itemId is the id of the item
     * @return parentWorkflow
     * @throws PersistenceException 
     */
    public Workflow getParentWorkflow(String itemId) throws PersistenceException {
        final int integerItemId = Integer.parseInt(itemId);
        final int idDivider = 10;
        final int eliminatedItemId = integerItemId % (ID_MULTIPLICATOR / idDivider);
        final String parentWorkflowId = ((integerItemId - eliminatedItemId) / ID_MULTIPLICATOR) + "";        
        final Workflow parentWorkflow = loadWorkflow(parentWorkflowId);
        return parentWorkflow;
    }
    
    @Override
    public void deleteItem(String itemId) throws PersistenceException {
        final Workflow parentWorkflow = getParentWorkflow(itemId);
        
        
        Item itemToRemove = null;
        for (Item item: parentWorkflow.getItems()) {
            if (item.getId().equals(itemId)) {
                itemToRemove = item;
            }
        }
        
        if (itemToRemove != null) {
            parentWorkflow.getItems().remove(itemToRemove);
            this.logger.log(Level.INFO,"[persistence] successfully removed item " + itemToRemove.getId() + ".");
        } else {
            throw new ItemNotExistentException("database has no item " + itemId);
        }
    }

    @Override
    public Item loadItem(String itemId) throws PersistenceException {
        final Workflow parentWorkflow = getParentWorkflow(itemId);
        Item itemToReturn = null;
        if (parentWorkflow == null) {
            throw new WorkflowNotExistentException("there is no parent workflow for item " + itemId);
        }
        for (Item item : parentWorkflow.getItems()) {
            if (item.getId().equals(itemId)) {
                try {
                    itemToReturn = (Item)item.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of item" + itemId + "failed."); 
                }
            }
        }
        if (itemToReturn != null) {
            return itemToReturn;
        } else {
            throw new ItemNotExistentException("database has no item " + itemId);
        }
    }
    
    // Step Operations
    
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

    // User Operations
    
    @Override
    public void storeUser(User user) throws PersistenceException {
        User userToRemove = null;
        for (User u: users) {
            if (u.getUsername().equals(user.getUsername())) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            this.deleteUser(userToRemove.getUsername());
            this.logger.log(Level.INFO, "[persistence] overwriting user " + user.getUsername() + "...");
        }
        User userToStore;
        try {
            userToStore = (User)user.clone();
            
        } catch (CloneNotSupportedException e) {
            throw new StorageFailedException("storage of user" + user.getUsername() + "failed.");
        }
        for (Role role: userToStore.getRoles()) {
            this.loadRole(role.getRolename());
        }
        users.add(userToStore);
        this.logger.log(Level.INFO, "[persistence] successfully stored user " + user.getUsername() + ".");
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
    
    // Role Operations
    
    /**
     * Method for storing a role.
     * @param role is the role to store
     * @return roleId is the Id of the stored role
     * @throws RoleNotExistentException 
     */
    public void storeRole(Role role) throws RoleNotExistentException {
        if (role.getId() == null) {
            role.setId(roles.size() + 1 + "");
        }
        Role roleToRemove = null;
        for (Role r : roles) {
            if (r.getRolename().equals(role.getRolename())) {
                roleToRemove = r;
                break;
            }
        }
        if (roleToRemove != null) {
            this.logger.log(Level.INFO, "[persistence] overwriting role "
                    + roleToRemove.getRolename() + "...");
            this.deleteRole(roleToRemove.getRolename());
        }
        roles.add((Role) role);
        this.logger.log(Level.INFO, "[persistence] successfully stored role " + role.getId()
                + ".");
    }

    /**
     * Method for loading a workflow.
     * @param rolename is the name of the requested role
     * @return workflow is the requested workflow
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     */
    public Role loadRole(String rolename) throws RoleNotExistentException {
        Role roleToReturn = null;
        for (Role r : roles) {
            if (r.getRolename().equals(rolename)) {
                roleToReturn = r;
            }
        }
        if (roleToReturn != null) {
            return roleToReturn;
        } else {
            throw new RoleNotExistentException("[persistence] database has no role '" + rolename + "'.");
        }
    }
    
    /**
     * Method for deleting an existing role.
     * @param rolename is the name of the given role
     * @exception RoleNotExistentException if the given role doesnt exist
     * @throws RoleNotExistentException
     */
    public void deleteRole(String rolename) throws RoleNotExistentException {       
        Role roleToRemove = null;
        for (Role r : roles) {
            if (r.getRolename().equals(rolename)) {
                roleToRemove = r;
                break;
            }
        }
        if (roleToRemove != null) {
            roles.remove(roleToRemove);
            this.logger.log(Level.INFO,"[persistence] successfully removed role " + roleToRemove.getId() + ".");
        } else {
            throw new RoleNotExistentException("database has no role '" + rolename + "'.");
        }
    }
    
    // General Operations on database
    
    @Override
    public List<Workflow> loadAllWorkflows() throws WorkflowNotExistentException {
        return workflows;
    }
    
    /**
     * Method for loading all roles into a list of roles.
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     * @return List<Workflow> is the list we want to load
     */
    public List<Role> loadAllRoles() throws RoleNotExistentException {
        return this.roles;
    }
    
    /**
     * Method for loeading all users into a list of users.
     * @exception UserNotExistentException if the requested user is not there
     * @throws UserNotExistentException
     * @return List<User> is the list we want to load
     */
    @Override
    public List<User> loadAllUsers() throws UserNotExistentException {
        return this.users;
    }
    
    
}
