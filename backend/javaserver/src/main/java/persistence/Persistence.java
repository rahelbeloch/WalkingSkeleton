package persistence;

import abstractbeans.AbstractItem;
import abstractbeans.AbstractUser;
import abstractbeans.AbstractWorkflow;

public interface Persistence {
	
	/*
	 * store functions to store workflows, items, and users into persistence
	 */
	public void storeWorkflow(AbstractWorkflow workflow);
	public void storeItem(AbstractItem item);
	public void addUser(AbstractUser user) throws UserAlreadyExistsEcxeption;
	public void updateUser(AbstractUser user) throws UserNotExistantException;
	
	/*
	 * load functions to get workflows, items, and users from persistence
	 */
	public AbstractWorkflow loadWorkflow(int id);
	public AbstractItem loadItem(int id);
	public AbstractUser loadUser(int id);

	/*
	 * delete functions to remove workflows, items, and users from persistence
	 */
	public void deleteWorkflow(int id);
	public void deleteItem(int id);
	public void deleteUser(int id);

}
