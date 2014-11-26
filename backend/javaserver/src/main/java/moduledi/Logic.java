package moduledi;

import java.util.List;

import persistence.UserAlreadyExistsException;
import persistence.UserNotExistentException;
import abstractbeans.AbstractStep;
import backingbeans.Action;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;

public interface Logic {
	
	/*
	 * workflow functions
	 */
	public void startWorkflow(int workflowID, User user);
	public void addWorkflow(Workflow workflow); // later a workflows name will be available
	public Workflow getWorkflow(int workflowID);
	public void deleteWorkflow(int workflowID);

	/*
	 * item 
	 */
	public void stepOver(Item item, AbstractStep  step, User user);
	
	/*
	 * step functions
	 */
	public void addStep (int workflowID, AbstractStep step);
	public void deleteStep (int workflowID, int stepID); 
	
	/*
	 * user functions
	 */
	public void addUser(User user) throws UserAlreadyExistsException;
	public User getUser(String username) throws UserNotExistentException; // not attached yet
	//public boolean checkLogIn(String username); // later with password checking
	public void deleteUser(String username);
	
	public List<Workflow> getWorkflowsByUser(User user);
	public List<Item> getOpenItemsByUser(User user);
	public List<Workflow> getStartableWorkflows(User user);
	
	
}
