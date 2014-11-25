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
	 * store functions for workflow, item, user store functions for step,
	 * metaEntry
	 */
	@Override
	public void storeWorkflow(AbstractWorkflow workflow) {
		for (AbstractWorkflow wf : workflows) {
			if (wf.getId() == workflow.getId()) {
				workflows.remove(wf);
			}
		}
		workflows.add((AbstractWorkflow) workflow);

		// a workflows steps are resolved and stored one by one
		List<AbstractStep> workflowsSteps = workflow.getStep();
		for (AbstractStep step : workflowsSteps) {
			storeStep(step);
		}
	}

	@Override
	public void storeItem(AbstractItem item) {
		for (AbstractItem i : items) {
			if (i.getId() == item.getId()) {
				items.remove(i);
			}
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
	public void updateUser(AbstractUser user) throws UserNotExistantException {
		boolean userExists = false;
		for (AbstractUser u : users) {
			if (u.getName().equals(user.getName())) {
				userExists = true;
				users.remove(u);
			}
		}
		if (userExists) {
			users.add(user);
		} else {
			throw new UserNotExistantException();
		}
	}

	public void storeStep(AbstractStep step) {
		for (AbstractStep s : steps) {
			if (s.getId() == step.getId()) {
				steps.remove(s);
			}
		}
		steps.add((AbstractStep) step);
		// TODO: need to distinguish between Action/FirstStep/StartStep?
	}

	public void storeMetaEntry(AbstractMetaEntry metaEntry) {
		for (AbstractMetaEntry me : metaEntries) {
			// assumption that MetaEntries have keys that are unique
			if (me.getKey().equals(metaEntry.getKey())) {
				metaEntries.remove(me);
			}
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
		for (AbstractWorkflow wf : workflows) {
			if (wf.getId() == id) {
				// a workflows steps are resolved and deleted one by one
				List<AbstractStep> workflowsSteps = wf.getStep();
				for (AbstractStep step : workflowsSteps) {
					deleteStep(step.getId());
				}
				workflows.remove(wf);
			}
		}
	}

	@Override
	public void deleteItem(int id) {
		for (AbstractItem i : items) {
			if (i.getId() == id) {

				// an items metaData are deleted as well
				List<AbstractMetaEntry> itemsMetaData = i.getMetadata();
				for (AbstractMetaEntry metaEntry : itemsMetaData) {
					deleteMetaEntry(metaEntry.getKey());
				}
				items.remove(i);
			}
		}
	}

	@Override
	public void deleteUser(String name) {
		for (AbstractUser u : users) {
			if (u.getName().equals(name)) {
				users.remove(u);
			}
		}
	}

	public void deleteStep(int id) {
		for (AbstractStep s : steps) {
			if (s.getId() == id) {
				steps.remove(s);
			}
		}
	}

	public void deleteMetaEntry(String key) {
		for (AbstractMetaEntry me : metaEntries) {
			if (me.getKey().equals(key)) {
				metaEntries.remove(me);
			}
		}
	}
}
