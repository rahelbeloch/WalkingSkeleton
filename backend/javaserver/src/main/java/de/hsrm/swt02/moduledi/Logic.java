package de.hsrm.swt02.moduledi;

import java.util.List;

import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.UserNotExistentException;

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
	public void stepOver(Item item, Step  step, User user);
	
	/*
	 * step functions
	 */
	public void addStep (int workflowID, Step step);
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
