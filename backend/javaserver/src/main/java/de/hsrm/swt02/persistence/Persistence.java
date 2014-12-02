package de.hsrm.swt02.persistence;

import java.util.List;

import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * Interface for the dependency injection of the persistence implementation.
 */
public interface Persistence {

    /**
     * store functions to store workflows, items, and users into persistence.
     * @param workflow is a workflow for storing
     */
    void storeWorkflow(Workflow workflow);

    /**
     * store functions to store an item.
     * @param item is an item for storing
     */
    void storeItem(Item item);

    /**
     * Method for adding a new user.
     * @param user is the needed user
     * @exception UserAlreadyExistsException if the user to store is already there.
     * @throws UserAlreadyExistsException
     */
    void addUser(User user) throws UserAlreadyExistsException;

    /**
     * Method for updating an already existing user.
     * @param user is the user we need
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     */
    void updateUser(User user) throws UserNotExistentException;

    /**
     * Method for loeading all workflows into a list of workflows.
     * @return List<Workflow> is the list we want to load
     */
    List<Workflow> loadAllWorkflows();
    
    /**
     * Method for loading a workflow.
     * @param id is the id of the requested workflow.
     * @return workflow is the requested workflow
     * @exception WorkflowNotExistentException if the requested workflow is not there.
     * @throws WorkflowNotExistentException
     */
    Workflow loadWorkflow(int id) throws WorkflowNotExistentException;

    /**
     * Method for loading an item.
     * @param id is the id of the requested item.
     * @return Item is the requested item.
     * @exception ItemNotExistentException if the requested item is not there.
     * @throws ItemNotExistentException
     */
    Item loadItem(int id) throws ItemNotExistentException;

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
     * @param id is the id of the step.
     * @return step is the requested step
     */
    Step loadStep(int id);

    /**
     * Method for loading a requested MetaEntry.
     * @param key is the key string.
     * @return MetaEntry is the requested MetaEntry of the list with the right key string.
     */
    MetaEntry loadMetaEntry(String key);

    /**
     * delete functions to remove workflows, items, and users from persistence.
     * @param id is the id of the requested workflow
     * @exception WorkflowNotExistentException if the requested workflow is not there.
     * @throws WorkflowNotExistentException
     */
    void deleteWorkflow(int id) throws WorkflowNotExistentException;

    /**
     * Method for function to delete an item.
     * @param id is the id of the requested item.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    void deleteItem(int id) throws ItemNotExistentException;

    /**
     * Method for the function of deleting an user.
     * @param name is the name of the requested user.
     * @exception UserNotExistentException if the requested user is not there.
     * @throws UserNotExistentException
     */
    void deleteUser(String name) throws UserNotExistentException;

}
