package de.hsrm.swt02.persistence;

import java.util.List;

import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;


/**
 * Interface for the dependency injection of the persistence implementation.
 */
public interface Persistence {

    /**
     * store functions to store workflows, items, and users into persistence.
     * @param workflow is a workflow for storing
     * @exception PersistenceException if the storing process failed
     * @throws PersistenceException
     * @return id of stored workflow
     */
    String storeWorkflow(Workflow workflow) throws PersistenceException;


//    /**
//     * store functions to store an item.
//     * @param item is an item for storing
//     * @exception WorkflowNotExistentException if the requested workflow is not there
//     * @throws WorkflowNotExistentException
//     */
//    String storeItem(Item item) throws PersistenceException;
    
    /**
     * Method for adding a new user.
     * @param user is the needed user
     * @exception PersistenceException if the user to store is already there.
     * @throws PersistenceException
     */
    void storeUser(User user) throws PersistenceException;

    /**
     * Method for loading all workflows into a list of workflows.
     * @return List<Workflow> is the list we want to load
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws WorkflowNotExistentException
     */
    List<Workflow> loadAllWorkflows() throws WorkflowNotExistentException;
    
    /**
     * Method for loading a workflow.
     * @param id is the id of the requested workflow.
     * @return workflow is the requested workflow
     * @exception PersistenceException if the requested workflow is not there.
     * @throws PersistenceException
     */
    Workflow loadWorkflow(String id) throws PersistenceException;

    /**
     * Method for loading an item.
     * @param id is the id of the requested item.
     * @return Item is the requested item.
     * @exception PersistenceException if the requested item is not there.
     * @throws PersistenceException
     */
    Item loadItem(String id) throws PersistenceException;

    /**
     * Method for loading an user.
     * @param username is the name of the requested user.
     * @return User is the requested user.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     */
    User loadUser(String username) throws UserNotExistentException;

    /**
     * Only for the walking skeleton:  method for loading a step.
     * @param itemId is the id of the item.
     * @param stepId is the id of the step.
     * @return step is the requested step
     * @exception PersistenceException if the loading process failed
     * @throws PersistenceException 
     */
    Step loadStep(String itemId, String stepId) throws PersistenceException;
//
//    /**
//     * Method for loading a requested MetaEntry.
//     * @param key is the key string.
//     * @return MetaEntry is the requested MetaEntry of the list with the right key string.
//     */
//    MetaEntry loadMetaEntry(String key);
//
    /**
     * delete functions to remove workflows, items, and users from persistence.
     * @param id is the id of the requested workflow
     * @exception WorkflowNotExistentException if the requested workflow is not there.
     * @throws WorkflowNotExistentException
     */
    void deleteWorkflow(String id) throws WorkflowNotExistentException;

    /**
     * Method for function to delete an item.
     * @param id is the id of the requested item.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    void deleteItem(String id) throws ItemNotExistentException;

    /**
     * Method for the function of deleting an user.
     * @param name is the name of the requested user.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     */
    void deleteUser(String name) throws UserNotExistentException;
    
    // Sprint 2 Persistence
    
    /**
    * store function to store a role.
    * @param role is the role for storing
    * @exception PersistenceException if the user to store is already there.
    * @throws PersistenceExceptio
    * @return roleId is the id of the role
    */
    String storeRole(Role role) throws PersistenceException;
    
    /**
     * Method for loading all existing roles.
     * @exception RoleNotExistentException if the requested role is not there.
     * @throws RoleNotExistentException
     * @return roles is the list of all existing roles
     */
    List<Role> loadAllRoles() throws RoleNotExistentException;
    
    /**
     * Method for loading all existing users.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     * @return users is the list of all existing users
     */
    List<User> loadAllUsers() throws UserNotExistentException;
    
    /**
     * Method for loading a role.
     * @param id is the id of the requested role.
     * @return role is the requested role
     * @exception RoleNotExistentException if the requested role is not there.
     * @throws RoleNotExistentException
     */
    Role loadRole(String id) throws RoleNotExistentException;
    
    /**
     * Method for adding a new user to a role.
     * @param user is the needed user
     * @param role is the needed role
     * @exception PersistenceException if there is a persistence exception
     * @throws PersistenceException
     */
    void addRoleToUser(User user, Role role) throws PersistenceException;  
    
    /**
     * Method for removing a role from datbase.
     * @param rolename is the name of the role
     * @exception RoleNotExistentException if the role doesnt exist in the persistence
     * @throws RoleNotExistentException
     */
    void deleteRole(String rolename) throws RoleNotExistentException;

}

