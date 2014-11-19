package persistence;

import java.util.List;
import java.util.LinkedList;

import beans.Item;
import beans.User;
import beans.Workflow;

public class Persistence {
	
	private List<Workflow> workflows = new LinkedList<Workflow>();
	private List<User> users = new LinkedList<User>();
	private List<Item> items = new LinkedList<Item>();
	
	/*
	 * store functions for items, users, workflows
	 */
	public void storeItem(Item item) {
		for(Item i: items) {
			if(i.getId() == item.getId()) {
				items.remove(i);
			}
		}
		items.add(item);
	}
	
	public void storeUser(User user) {
		for(User u: users) {
			if(u.getId() == user.getId()) {
				users.remove(u);
			}
		}
		users.add(user);
	}
	
	public void storeWorkflow(Workflow workflow) {
		for(Workflow wf: workflows) {
			if (wf.getId() == workflow.getId()) {
				workflows.remove(wf);
			}
		}
		workflows.add(workflow);
	}

	/*
	 * load functions for items, users, workflows
	 */
	public Item loadItem(int id) {
		Item item = null;
		for(Item i: items) {
			if(i.getId() == id) {
				item = i;
			}
		}
		return item;
	}
	
	public User loadUser(int id) {
		User user = null;
		for(User u: users) {
			if(u.getId() == id) {
				user = u;
			}
		}
		return user;
	}
	
	public Workflow loadWorkflow(int id) {
		Workflow workflow = null;
		for(Workflow wf: workflows){
			if(wf.getId() == id) {
				workflow = wf;
			}
		}
		return workflow;
	}

}
