package de.hsrm.swt02.persistence;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Singleton;

import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;

@Singleton
public class PersistenceImp implements Persistence {

    /*
     * abstraction of a database, that persists object in form Abstract-*
     */
    private List<Workflow> workflows = new LinkedList<>();
    private List<Item> items = new LinkedList<>();
    private List<User> users = new LinkedList<>();

    private List<Step> steps = new LinkedList<>();
    private List<MetaEntry> metaEntries = new LinkedList<>();

    /*
     * store functions for workflow, item, user store functions for step,
     * metaEntry
     */
    @Override
    public void storeWorkflow(Workflow workflow) {
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == workflow.getId()) {
                workflowToRemove = wf;
                break;
            }
        }
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
        }
        workflows.add((Workflow) workflow);

        // a workflows steps are resolved and stored one by one
        List<Step> workflowsSteps = workflow.getSteps();
        for (Step step : workflowsSteps) {
            storeStep(step);
        }
    }

    @Override
    public List<Workflow> loadAllWorkflows() {
        return workflows;
    }

    @Override
    public void storeItem(Item item) {
        Item itemToRemove = null;
        for (Item i : items) {
            if (i.getId() == item.getId()) {
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
        }
        items.add((Item) item);

        // items include information of type MetaEntry which have to be stored
        // separately
        List<MetaEntry> itemsMetadata = item.getMetadata();
        for (MetaEntry metaEntry : itemsMetadata) {
            storeMetaEntry(metaEntry);
        }
    }

    @Override
    public void addUser(User user) throws UserAlreadyExistsException {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                throw new UserAlreadyExistsException();
            }
        }
        users.add((User) user);
    }

    @Override
    public void updateUser(User user) throws UserNotExistentException {
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                userToRemove = u;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
            users.add(user);
        } else {
            throw new UserNotExistentException();
        }
    }

    public void storeStep(Step step) {
        Step stepToRemove = null;
        for (Step s : steps) {
            if (s.getId() == step.getId()) {
                stepToRemove = s;
                break;
            }
        }
        if (stepToRemove != null) {
            steps.remove(stepToRemove);
        }
        steps.add((Step) step);
        // TODO: need to distinguish between Action/FirstStep/StartStep?
    }

    public void storeMetaEntry(MetaEntry metaEntry) {
        MetaEntry metaEntryToRemove = null;
        for (MetaEntry me : metaEntries) {
            // assumption that MetaEntries have keys that are unique
            if (me.getKey().equals(metaEntry.getKey())) {
                metaEntryToRemove = me;
                break;
            }
        }
        if (metaEntryToRemove != null) {
            metaEntries.remove(metaEntryToRemove);
        }
        metaEntries.add((MetaEntry) metaEntry);
    }

    /*
     * load functions for workflow, item, user load functions for step,
     * metaEntry
     */
    @Override
    public Workflow loadWorkflow(int id) {
        Workflow workflow = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
                workflow = wf;
            }
        }
        return workflow;
        // TODO: return steps of a workflow as well! - can be done as soon as a
        // unique and combined ID for steps and workflows is given
    }

    @Override
    public Item loadItem(int id) {
        Item item = null;
        for (Item i : items) {
            if (i.getId() == id) {
                item = i;
            }
        }
        return item;
        // TODO: return MetaData of an item as well!
    }

    @Override
    public User loadUser(String name) {
        User user = null;
        for (User u : users) {
            if (u.getUsername().equals(name)) {
                user = u;
            }
        }
        // later UserNotExistantException could be thrown instead of returning
        // 'null', but that has to be conform with all other load function
        return user;
    }

    public Step loadStep(int id) {
        Step step = null;
        for (Step s : steps) {
            if (s.getId() == id) {
                step = s;
            }
        }
        return step;
    }

    public MetaEntry loadMetaEntry(String key) {
        MetaEntry metaEntry = null;
        for (MetaEntry me : metaEntries) {
            if (me.getKey().equals(key)) {
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
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
                // a workflows steps are resolved and deleted one by one
                List<Step> workflowsSteps = wf.getSteps();
                for (Step step : workflowsSteps) {
                    deleteStep(step.getId());
                }
                workflowToRemove = wf;
                break;
            }
        }
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
        }
    }

    @Override
    public void deleteItem(int id) {
        Item itemToRemove = null;
        for (Item i : items) {
            if (i.getId() == id) {

                // an items metaData are deleted as well
                List<MetaEntry> itemsMetaData = i.getMetadata();
                for (MetaEntry metaEntry : itemsMetaData) {
                    deleteMetaEntry(metaEntry.getKey());
                }
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
        }
    }

    @Override
    public void deleteUser(String name) {
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(name)) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
        }
    }

    public void deleteStep(int id) {
        Step stepToRemove = null;
        for (Step s : steps) {
            if (s.getId() == id) {
                stepToRemove = s;
                break;
            }
        }
        if (stepToRemove != null) {
            steps.remove(stepToRemove);
        }
    }

    public void deleteMetaEntry(String key) {
        MetaEntry metaEntryToRemove = null;
        for (MetaEntry me : metaEntries) {
            if (me.getKey().equals(key)) {
                metaEntryToRemove = me;
                break;
            }
        }
        if (metaEntryToRemove != null) {
            metaEntries.remove(metaEntryToRemove);
        }
    }
}
