package persistence;

import java.util.List;

import model.Item;
import model.MetaEntry;
import model.Step;
import model.User;
import model.Workflow;

public interface Persistence {
	
	/*
	 * store functions to store workflows, items, and users into persistence
	 */
	public void storeWorkflow(Workflow workflow);
	public void storeItem(Item item);
	public void addUser(User user) throws UserAlreadyExistsException;
	public void updateUser(User user) throws UserNotExistentException;
	
	/*
	 * load functions to get workflows, items, and users from persistence
	 */
	public Workflow loadWorkflow(int id);
	public List<Workflow> loadAllWorkflows(); 
	public Item loadItem(int id);
	public User loadUser(String name);
	// later UserNotExistentException could thrown:
	// public AbstractUser loadUser(String name) throws UserNotExistentException;
	
	// will be deleted later on (only for walking sceleton)
	public Step loadStep(int id);
	public MetaEntry loadMetaEntry(String key);

	/*
	 * delete functions to remove workflows, items, and users from persistence
	 */
	public void deleteWorkflow(int id);
	public void deleteItem(int id);
	public void deleteUser(String name);

}
