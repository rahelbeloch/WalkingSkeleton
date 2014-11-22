package persistence;

import java.util.LinkedList;
import java.util.List;

import abstractbeans.AbstractItem;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;
import abstractbeans.AbstractWorkflow;

public class Persistence {
	
	/*
	 * Abstraktion einer Datenbank, die Objekte in der Form Abstract-* persistiert
	 */
	private List<AbstractWorkflow> workflows = new LinkedList<>();
	private List<AbstractUser> users = new LinkedList<>();
	private List<AbstractItem> items = new LinkedList<>();
	private List<AbstractStep> steps = new LinkedList<>();

//	public AbstractItem resolveMap(Item item) {
//		AbstractItem tempItem = null;
//		AbstractMap tempMap = null;
//		
//		/*
//		 * following code does not work because structure will be redefined by Jerome/Jane
//		 * tempMap.getKey().addAll(item.getMetaData().getKeys());
//		 * tempMap.getValue().addAll(item.getMetaData().getValues());
//		 */
//		tempItem.setMetadata(tempMap);
//		return tempItem;
//	}
	

	/*
	 * store functions for steps, items, users, workflows
	 */	
	public void storeStep(AbstractStep step) {
		for(AbstractStep s: steps) {
			if(s.getId() == step.getId()) {
				steps.remove(s);
			}
		}
		steps.add((AbstractStep)step);
	}
	
	public void storeItem(AbstractItem item) {
		for(AbstractItem i: items) {
			if(i.getId() == item.getId()) {
				items.remove(i);
			}
		}
		// items includes a HashMap that has to be transfered into lists (Map)
		items.add((AbstractItem)item);
	}
	
	public void storeUser(AbstractUser user) {
		for(AbstractUser u: users) {
			if(u.getId() == user.getId()) {
				users.remove(u);
			}
		}
		users.add((AbstractUser)user);
	}
	
	public void storeWorkflow(AbstractWorkflow workflow) {
		for(AbstractWorkflow wf: workflows) {
			if (wf.getId() == workflow.getId()) {
				workflows.remove(wf);
			}
		}
		workflows.add((AbstractWorkflow)workflow);
	}

	/*
	 * load functions for steps, items, users, workflows
	 */
	
	public AbstractStep loadStep(int id) {
		AbstractStep step = null;
		for(AbstractStep s: steps) {
			if(s.getId() == id) {
				step = s;
			}
		}
		return step;
	}
	
	public AbstractItem loadItem(int id) {
		AbstractItem item = null;
		for(AbstractItem i: items) {
			if(i.getId() == id) {
				item = i;
			}
		}
		return item;
	}
	
	public AbstractUser loadUser(int id) {
		AbstractUser user = null;
		for(AbstractUser u: users) {
			if(u.getId() == id) {
				user = u;
			}
		}
		return user;
	}
	
	public AbstractWorkflow loadWorkflow(int id) {
		AbstractWorkflow workflow = null;
		for(AbstractWorkflow wf: workflows){
			if(wf.getId() == id) {
				workflow = wf;
			}
		}
		return workflow;
	}
	
	/*
	 * delete functions for steps, items, users, workflows
	 */
	public void deleteStep(int id) {
		for(AbstractStep s: steps) {
			if(s.getId() == id) {
				steps.remove(s);
			}
		}
	}
	
	public void deleteItem(int id) {
		for(AbstractItem i: items) {
			if(i.getId() == id) {
				items.remove(i);
			}
		}
	}
	
	public void deleteUser(int id) {
		for(AbstractUser u: users) {
			if(u.getId() == id) {
				users.remove(u);
			}
		}
	}
	
	public void deleteWorkflow(int id) {
		for(AbstractWorkflow wf: workflows) {
			if(wf.getId() == id) {
				workflows.remove(wf);
			}
		}
	}

}
