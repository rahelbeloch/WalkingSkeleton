package persistence;

import java.util.LinkedList;
import java.util.List;

import abstractbeans.AbstractItem;
import abstractbeans.AbstractMetaEntry;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;
import abstractbeans.AbstractWorkflow;

public class PersistenceImp implements Persistence {

	/*
	 * abstraction of a database, that persists object in form Abstract-*
	 */
	private List<AbstractWorkflow> workflows = new LinkedList<>();
	private List<AbstractItem> items = new LinkedList<>();
	private List<AbstractUser> users = new LinkedList<>();
	
	private List<AbstractStep> steps = new LinkedList<>();
	private List<AbstractMetaEntry> metaEntries = new LinkedList<>();
	
	/*
	 * store functions for workflow, item, user
	 * store functions for step, metaEntry
	 */	
	@Override
	public void storeWorkflow(AbstractWorkflow workflow) {
		for(AbstractWorkflow wf: workflows) {
			if (wf.getId() == workflow.getId()) {
				workflows.remove(wf);
			}
		}
		workflows.add((AbstractWorkflow)workflow);
		
		// a workflows steps are resolved and stored one by one
		List<AbstractStep> workflowsSteps = workflow.getStep();
		for(AbstractStep step: workflowsSteps) {
			storeStep(step);
		}
	}

	@Override
	public void storeItem(AbstractItem item) {
		for(AbstractItem i: items) {
			if(i.getId() == item.getId()) {
				items.remove(i);
			}
		}
		items.add((AbstractItem)item);
		
		// items include type MetaEntry that has to be stored separately
		List<AbstractMetaEntry> itemsMetadata = item.getMetadata();
		for(AbstractMetaEntry metaEntry: itemsMetadata) {
			storeMetaEntry(metaEntry);
		}
	}

	@Override
	public void storeUser(AbstractUser user) {
		for(AbstractUser u: users) {
			if(u.getId() == user.getId()) {
				users.remove(u);
			}
		}
		users.add((AbstractUser)user);
	}
	
	public void storeStep(AbstractStep step) {
		for(AbstractStep s: steps) {
			if(s.getId() == step.getId()) {
				steps.remove(s);
			}
		}
		steps.add((AbstractStep)step);
		// need to distinguish between Action/FirstStep/StartStep?
	}
	
	public void storeMetaEntry(AbstractMetaEntry metaEntry) {
		for(AbstractMetaEntry me: metaEntries) {
			// assumption that MetaEntries have keys that are unique
			if(me.getKey() == metaEntry.getKey()) {
				metaEntries.remove(me);
			}
		}
		metaEntries.add((AbstractMetaEntry)metaEntry);
	}
	
	
	/*
	 * load functions for workflow, item, user
	 * load functions for step, metaEntry
	 */
	@Override
	public AbstractWorkflow loadWorkflow(int id) {
		AbstractWorkflow workflow = null;
		for(AbstractWorkflow wf: workflows){
			if(wf.getId() == id) {
				workflow = wf;
			}
		}
		return workflow;
	}

	@Override
	public AbstractItem loadItem(int id) {
		AbstractItem item = null;
		for(AbstractItem i: items) {
			if(i.getId() == id) {
				item = i;
			}
		}
		return item;
	}

	@Override
	public AbstractUser loadUser(int id) {
		AbstractUser user = null;
		for(AbstractUser u: users) {
			if(u.getId() == id) {
				user = u;
			}
		}
		return user;
	}
	
	
	/*
	 * delete functions for workflow, item, user
	 * delete functions for step, metaEntry
	 */	
	@Override
	public void deleteWorkflow(int id) {
		for(AbstractWorkflow wf: workflows) {
			if(wf.getId() == id) {
				workflows.remove(wf);
			}
		}
	}
	
	@Override
	public void deleteItem(int id) {
		for(AbstractItem i: items) {
			if(i.getId() == id) {
				items.remove(i);
			}
		}
	}
	
	@Override
	public void deleteUser(int id) {
		for(AbstractUser u: users) {
			if(u.getId() == id) {
				users.remove(u);
			}
		}
	}
	
	public void deleteStep(int id) {
		for(AbstractStep s: steps) {
			if(s.getId() == id) {
				steps.remove(s);
			}
		}
	}
	
	public void deleteMetaEntry(String key) {
		for(AbstractMetaEntry me: metaEntries) {
			if(me.getKey() == key) {
				metaEntries.remove(me);
			}
		}
	}
}
