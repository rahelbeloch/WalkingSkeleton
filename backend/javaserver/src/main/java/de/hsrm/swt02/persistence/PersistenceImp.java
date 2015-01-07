package de.hsrm.swt02.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Form;
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
        int idx = 0;
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
                    step.setId((Integer.parseInt(workflow.getId()) * ID_MULTIPLICATOR + idx + 1) + "");
                    idx++;
                }
            }
        }
        
        Workflow workflowToStore;
        try {
            workflowToStore = (Workflow)workflow.clone();
        } catch (CloneNotSupportedException e) {
            throw new PersistenceException("Storage failed - Cloning did not work.");
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
                try {
                    step = (Step)s.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of step" + stepId + "failed.");
                }
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
    public User loadUser(String username) throws PersistenceException {
        User userToReturn = null;
        for (User u : users) {
            if (u.getUsername().equals(username)) {            
                try {
                    userToReturn = (User) u.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of user" + username + "failed.");
                }                
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
    @Override
    public void storeRole(Role role) throws PersistenceException {
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
        try {
            roles.add((Role) role.clone());
        } catch (CloneNotSupportedException e) {
            throw new PersistenceException("Storage failed - Cloning did not work.");
        }
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
    @Override
    public Role loadRole(String rolename) throws PersistenceException {
        Role roleToReturn = null;
        for (Role r : roles) {
            if (r.getRolename().equals(rolename)) {
                try {
                    roleToReturn = (Role)r.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of Role" + rolename + "failed.");
                }
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
    @Override
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
    public List<Workflow> loadAllWorkflows() throws PersistenceException {
        List<Workflow> retList = new LinkedList<>();
        for (Workflow wf: this.workflows) {
            retList.add(this.loadWorkflow(wf.getId()));
        }
        return retList;
    }
    
    @Override
    public List<Role> loadAllRoles() throws PersistenceException {
        List<Role> retList = new LinkedList<>();
        for (Role role: this.roles) {
            retList.add(this.loadRole(role.getRolename()));
        }
        return retList;
    }
    @Override
    public List<User> loadAllUsers() throws PersistenceException {
        List<User> retList = new LinkedList<>();
        for (User user: this.users) {
            retList.add(this.loadUser(user.getUsername()));
        }
        return retList;
    }
    
    // Form Operations
    @Override
    public void storeForm() throws PersistenceException {};
    @Override
    public void deleteForm(String formname) throws PersistenceException {};
    @Override
    public Form loadForm(String formname) throws PersistenceException {
        return null;
    };
    @Override
    public List<Form> loadAllForms() throws PersistenceException {
        return null;
    };
    
}