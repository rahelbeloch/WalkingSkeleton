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

public interface Persistence {

    /*
     * store functions to store workflows, items, and users into persistence
     */
    void storeWorkflow(Workflow workflow);

    void storeItem(Item item);

    void addUser(User user) throws UserAlreadyExistsException;

    void updateUser(User user) throws UserNotExistentException;

    /*
     * load functions to get workflows, items, and users from persistence
     */
    List<Workflow> loadAllWorkflows();
    
    Workflow loadWorkflow(int id) throws WorkflowNotExistentException;

    Item loadItem(int id) throws ItemNotExistentException;

    User loadUser(String username) throws UserNotExistentException;

    // will be deleted later on (only for walking sceleton)
    Step loadStep(int id);

    MetaEntry loadMetaEntry(String key);

    /*
     * delete functions to remove workflows, items, and users from persistence
     */
    void deleteWorkflow(int id) throws WorkflowNotExistentException;

    void deleteItem(int id) throws ItemNotExistentException;

    void deleteUser(String name) throws UserNotExistentException;

}
