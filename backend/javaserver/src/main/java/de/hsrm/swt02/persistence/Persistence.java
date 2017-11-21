package de.hsrm.swt02.persistence;

import java.util.List;

import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;

/**
 * Interface for the dependency injection of the persistence implementation.
 */
public interface Persistence {

    // Workflow Operations
    
    /**
     * store functions to store workflows, items, and users into persistence.
     * @param workflow is a workflow for storing
     * @exception PersistenceException if the storing process failed
     * @throws PersistenceException
     * @return id of stored workflow
     */
    String storeWorkflow(Workflow workflow) throws PersistenceException;
    
    /**
     * Method for loading a workflow.
     * @param id is the id of the requested workflow.
     * @return workflow is the requested workflow
     * @exception PersistenceException if any error in database occurs.
     * @throws PersistenceException
     */
    Workflow loadWorkflow(String id) throws PersistenceException;
    
    /**
     * Method for loading all workflows into a list of workflows.
     * @return List<Workflow> is the list we want to load
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws PersistenceException if any error in database occurs
     */
    List<Workflow> loadAllWorkflows() throws PersistenceException;
    
    /**
     * delete functions to remove workflows, items, and users from persistence.
     * @param id is the id of the requested workflow
     * @exception PersistenceException if the loading process failed
     * @throws PersistenceException if any error in database occurs 
     */
    void deleteWorkflow(String id) throws PersistenceException;
    
    // Item Operations
    
    /**
     * Method for loading an item.
     * @param id is the id of the requested item.
     * @return Item is the requested item.
     * @exception PersistenceException if the requested item is not there.
     * @throws PersistenceException if any error in database occurs
     */
    Item loadItem(String id) throws PersistenceException;
    
    /**
     * Method for function to delete an item.
     * @param id is the id of the requested item.
     * @throws PersistenceException if any error in database occurs 
     */
    void deleteItem(String id) throws PersistenceException;

    // Step Operations
    
    /**
     * Only for the walking skeleton:  method for loading a step.
     * @param itemId is the id of the item.
     * @param stepId is the id of the step.
     * @return step is the requested step
     * @throws PersistenceException if any error in database occurs 
     */
    Step loadStep(String itemId, String stepId) throws PersistenceException;
    
    // User Operations
    
    /**
     * Method for adding a new user.
     * @param user is the needed user
     * @exception PersistenceException if the user to store is already there.
     * @throws PersistenceException
     */
    void storeUser(User user) throws PersistenceException;
    
    /**
     * Method for loading an user.
     * @param username is the name of the requested user.
     * @return User is the requested user.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws PersistenceException if any error in database occurs
     */
    User loadUser(String username) throws PersistenceException;

    /**
     * Method for loading all existing users.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws PersistenceException if any error in database occurs
     * @return users is the list of all existing users
     */
    List<User> loadAllUsers() throws PersistenceException;
    
    /**
     * Method for the function of deleting an user.
     * @param name is the name of the requested user.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     */
    void deleteUser(String name) throws UserNotExistentException;
    
    // Role Operations
    
    /**
    * store function to store a role.
    * @param role is the role for storing
    * @exception PersistenceException if the user to store is already there.
    * @throws PersistenceExceptiom
    */
    void storeRole(Role role) throws PersistenceException;
    
    /**
     * Method for loading a role.
     * @param id is the id of the requested role.
     * @return role is the requested role
     * @exception PersistenceException if the requested role is not there.
     */
    Role loadRole(String id) throws PersistenceException;

    /**
     * Method for removing a role from database.
     * @param rolename is the name of the role
     * @exception RoleNotExistentException if the role doesn't exist in the persistence
     * @throws RoleNotExistentException
     */
    void deleteRole(String rolename) throws RoleNotExistentException;
    
    /**
     * Method for loading all existing roles.
     * @exception RoleNotExistentException if the requested role is not there.
     * @throws PersistenceException if any error in database occurs
     * @return roles is the list of all existing roles
     */
    List<Role> loadAllRoles() throws PersistenceException;
    
    // Form Operations
    /**
     * stores a new form to database.
     * 
     * @param form to store
     * @throws PersistenceException if an error in persistence occurs
     */
    void storeForm(Form form) throws PersistenceException;

    /**
     * deletes a form, given by the form name, from database.
     * 
     * @param formname of form to be deleted
     * @throws PersistenceException if an error in persistence occurs
     */
    void deleteForm(String formname) throws PersistenceException;
    
    /**
     * loads form from database.
     * 
     * @param formname of form to be loaded
     * @return the requested form
     * @throws PersistenceException if an error in persistence occurs
     */
    Form loadForm(String formname) throws PersistenceException;
    
    /**
     * getter for all forms.
     * 
     * @return List of all forms
     * @throws PersistenceException if an error in persistence occurs
     */
    List<Form> loadAllForms() throws PersistenceException;

    /**
     * Method for saving all relevant data on server during shutdown. All DataModels are
     * serialized in a file which path is configured in server configuration file.
     */
    void save();
    
    /**
     * Method to load the data model from file system. 
     * File is read, in case of reading errors or missing file some default test data 
     * is load.
     */
    void load();

    /**
     * Sets the storage path from logic to persistence.
     * @param storagePath the path for persist the data model.
     */
    void setStoragePath(String storagePath);
}