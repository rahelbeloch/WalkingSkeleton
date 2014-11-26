package persistence;

import java.util.List;

import abstractbeans.AbstractItem;
import abstractbeans.AbstractMetaEntry;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;
import abstractbeans.AbstractWorkflow;

public interface Persistence {
	
	/*
	 * store functions to store workflows, items, and users into persistence
	 */
	public void storeWorkflow(AbstractWorkflow workflow);
	public void storeItem(AbstractItem item);
	public void addUser(AbstractUser user) throws UserAlreadyExistsException;
	public void updateUser(AbstractUser user) throws UserNotExistentException;
	
	/*
	 * load functions to get workflows, items, and users from persistence
	 */
	public AbstractWorkflow loadWorkflow(int id);
	public List<AbstractWorkflow> loadAllWorkflows(); 
	public AbstractItem loadItem(int id);
	public AbstractUser loadUser(String name);
	// later UserNotExistentException could thrown:
	// public AbstractUser loadUser(String name) throws UserNotExistentException;
	
	// will be deleted later on (only for walking sceleton)
	public AbstractStep loadStep(int id);
	public AbstractMetaEntry loadMetaEntry(String key);

	/*
	 * delete functions to remove workflows, items, and users from persistence
	 */
	public void deleteWorkflow(int id);
	public void deleteItem(int id);
	public void deleteUser(String name);

}
