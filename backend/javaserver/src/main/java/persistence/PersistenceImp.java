package persistence;

import java.util.LinkedList;
import java.util.List;

import backingbeans.Workflow;
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
	 * store functions for workflow, item, user store functions for step,
	 * metaEntry
	 */
	@Override
	public void storeWorkflow(AbstractWorkflow workflow) {
		AbstractWorkflow workflowToRemove = null;
		for (AbstractWorkflow wf : workflows) {
			if (wf.getId() == workflow.getId()) {
				workflowToRemove = wf;
				break;
			}
		}
		if(workflowToRemove != null) {
			workflows.remove(workflowToRemove);
		}
		workflows.add((AbstractWorkflow) workflow);

		// a workflows steps are resolved and stored one by one
		List<AbstractStep> workflowsSteps = workflow.getStep();
		for (AbstractStep step : workflowsSteps) {
			storeStep(step);
		}
	}
	
	@Override
	public List<AbstractWorkflow> loadAllWorkflows() {
		return workflows;
	}

	@Override
	public void storeItem(AbstractItem item) {
		AbstractItem itemToRemove = null;
		for (AbstractItem i : items) {
			if (i.getId() == item.getId()) {
				itemToRemove = i;
				break;
			}
		}
		if(itemToRemove != null) {
			items.remove(itemToRemove);
		}
		items.add((AbstractItem) item);

		// items include information of type MetaEntry which have to be stored
		// separately
		List<AbstractMetaEntry> itemsMetadata = item.getMetadata();
		for (AbstractMetaEntry metaEntry : itemsMetadata) {
			storeMetaEntry(metaEntry);
		}
	}

	@Override
	public void addUser(AbstractUser user) throws UserAlreadyExistsException {
		for (AbstractUser u : users) {
			if (u.getName().equals(user.getName())) {
				throw new UserAlreadyExistsException();
			}
		}
		users.add((AbstractUser) user);
	}

	@Override
	public void updateUser(AbstractUser user) throws UserNotExistentException {
		AbstractUser userToRemove = null;
		for (AbstractUser u : users) {
			if (u.getName().equals(user.getName())) {
				userToRemove = u;
			}
		}
		if(userToRemove != null) {
			users.remove(userToRemove);
			users.add(user);
		} else {
			throw new UserNotExistentException();
		}
	}

	public void storeStep(AbstractStep step) {
		AbstractStep stepToRemove = null;
		for (AbstractStep s : steps) {
			if (s.getId() == step.getId()) {
				stepToRemove = s;
				break;
			}
		}
		if(stepToRemove != null) {
			steps.remove(stepToRemove);
		}
		steps.add((AbstractStep) step);
		// TODO: need to distinguish between Action/FirstStep/StartStep?
	}

	public void storeMetaEntry(AbstractMetaEntry metaEntry) {
		AbstractMetaEntry metaEntryToRemove = null;
		for (AbstractMetaEntry me : metaEntries) {
			// assumption that MetaEntries have keys that are unique
			if (me.getKey().equals(metaEntry.getKey())) {
				metaEntryToRemove = me;
				break;
			}
		}
		if (metaEntryToRemove != null){
			metaEntries.remove(metaEntryToRemove);
		}
		metaEntries.add((AbstractMetaEntry) metaEntry);
	}

	/*
	 * load functions for workflow, item, user load functions for step,
	 * metaEntry
	 */
	@Override
	public AbstractWorkflow loadWorkflow(int id) {
		AbstractWorkflow workflow = null;
		for (AbstractWorkflow wf : workflows) {
			if (wf.getId() == id) {
				workflow = wf;
			}
		}
		return workflow;
		// TODO: return steps of a workflow as well! - can be done as soon as a unique and combined ID for steps and workflows is given
	}

	@Override
	public AbstractItem loadItem(int id) {
		AbstractItem item = null;
		for (AbstractItem i : items) {
			if (i.getId() == id) {
				item = i;
			}
		}
		return item;
		// TODO: return MetaData of an item as well!
	}

	@Override
	public AbstractUser loadUser(String name) {
		AbstractUser user = null;
		for (AbstractUser u : users) {
			if (u.getName().equals(name)) {
				user = u;
			}
		}
		// later UserNotExistantException could be thrown instead of returning
		// 'null', but that has to be conform with all other load function
		return user;
	}
	
	public AbstractStep loadStep(int id) {
		AbstractStep step = null;
		for(AbstractStep s: steps) {
			if(s.getId() == id) {
				step = s;
			}
		}
		return step;
	}
	
	public AbstractMetaEntry loadMetaEntry(String key) {
		AbstractMetaEntry metaEntry = null;
		for(AbstractMetaEntry me: metaEntries) {
			if(me.getKey().equals(key)) {
				metaEntry = me;
			}
		}
		return metaEntry;
	}

	/*
	 * delete functions for workflow, item, user delete functions for step,
	 * metaEntry
	 */
	@Override
	public void deleteWorkflow(int id) {
		AbstractWorkflow workflowToRemove = null;
		for (AbstractWorkflow wf : workflows) {
			if (wf.getId() == id) {
				// a workflows steps are resolved and deleted one by one
				List<AbstractStep> workflowsSteps = wf.getStep();
				for (AbstractStep step : workflowsSteps) {
					deleteStep(step.getId());
				}
				workflowToRemove = wf;
				break;
			}
		}
		if(workflowToRemove != null) {
			workflows.remove(workflowToRemove);
		}
	}

	@Override
	public void deleteItem(int id) {
		AbstractItem itemToRemove = null;
		for (AbstractItem i : items) {
			if (i.getId() == id) {

				// an items metaData are deleted as well
				List<AbstractMetaEntry> itemsMetaData = i.getMetadata();
				for (AbstractMetaEntry metaEntry : itemsMetaData) {
					deleteMetaEntry(metaEntry.getKey());
				}
				itemToRemove = i;
				break;
			}
		}
		if(itemToRemove != null) {
			items.remove(itemToRemove);
		}
	}

	@Override
	public void deleteUser(String name) {
		AbstractUser userToRemove = null;
		for (AbstractUser u : users) {
			if (u.getName().equals(name)) {
				userToRemove = u;
				break;
			}
		}
		if(userToRemove != null) {
			users.remove(userToRemove);
		}
	}

	public void deleteStep(int id) {
		AbstractStep stepToRemove = null;
		for (AbstractStep s : steps) {
			if (s.getId() == id) {
				stepToRemove = s;
				break;
			}
		}
		if(stepToRemove != null) {
			steps.remove(stepToRemove);			
		}
	}

	public void deleteMetaEntry(String key) {
		AbstractMetaEntry metaEntryToRemove = null;
		for (AbstractMetaEntry me : metaEntries) {
			if (me.getKey().equals(key)) {
				metaEntryToRemove = me;
				break;
			}
		}
		if(metaEntryToRemove != null) {
			metaEntries.remove(metaEntryToRemove);			
		}
	}
}
