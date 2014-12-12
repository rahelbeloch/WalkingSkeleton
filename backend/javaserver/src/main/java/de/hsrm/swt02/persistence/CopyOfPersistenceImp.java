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
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * @author Dominik
 *
 */

@Singleton
public class CopyOfPersistenceImp implements CopyOfPersistence {

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
    public CopyOfPersistenceImp(UseLogger logger) {
        this.logger = logger;
    }
    
    
    // Workflow Operations
    
    @Override
    public int storeWorkflow(Workflow workflow) throws StorageFailedException {
        Workflow workflowToRemove = null;
        if (workflow.getId() <= 0) {
            workflow.setId(workflows.size() + 1);
        } else {
            for (Workflow wf: workflows) {
                if (wf.getId() == workflow.getId()) {
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
    public void deleteWorkflow(int id) throws WorkflowNotExistentException {
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
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
    public Workflow loadWorkflow(int id) throws WorkflowNotExistentException, StorageFailedException {
        Workflow workflowToReturn = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
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
    public void storeItem(Item item) throws WorkflowNotExistentException, StorageFailedException, ItemNotExistentException {
        Workflow parentWorkflow = null;
        Item itemToRemove = null;
        
        if (item.getId() <= 0) {
            for (Workflow wf: workflows) {
                if (item.getWorkflowId() == wf.getId()) {
                    parentWorkflow = wf;
                    break;
                }
            }
            if (parentWorkflow != null) {
                item.setId(parentWorkflow.getId() * ID_MULTIPLICATOR + parentWorkflow.getItems().size());
            } else {
                throw new WorkflowNotExistentException("invalid parent workflow id in item " + item.getId());
            }
        }
        
        for (Item i : items) {
            if (i.getId() == item.getId()) {
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
    }
    
    @Override
    public void deleteItem(int id) throws ItemNotExistentException {
        Item itemToRemove = null;
        for (Item item : items) {
            if (item.getId() == id) {
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
    public Item loadItem(int id) throws ItemNotExistentException, StorageFailedException {
        Item itemToReturn = null;
        for (Item item : items) {
            if (item.getId() == id) {
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
    public void addUser(User user) throws UserAlreadyExistsException, StorageFailedException {
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
    public void updateUser(User user) throws UserNotExistentException, StorageFailedException {        
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


    @Override
    public List<User> loadAllUsers() throws UserNotExistentException {
        if (users.size() > 0) {
            return users;
        }
        else {
            throw new UserNotExistentException("no stored users in database");
        }
    }
    
}
