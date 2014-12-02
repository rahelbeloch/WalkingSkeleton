package de.hsrm.swt02.persistence;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * @author Dominik
 *
 */
@Singleton
public class PersistenceImp implements Persistence {

    /** The logger. */
    private UseLogger logger;
    /*
     * abstraction of a database, that persists the data objects workflow, item,
     * user, step, metaEntry
     */
    private List<Workflow> workflows = new LinkedList<>();
    private List<Item> items = new LinkedList<>();
    private List<User> users = new LinkedList<>();

    private List<Step> steps = new LinkedList<>();
    private List<MetaEntry> metaEntries = new LinkedList<>();

    @Inject
    public PersistenceImp(UseLogger logger) {
        this.logger = logger;
    }

    /*
     * store functions for workflow, item, user store functions for step,
     * metaEntry
     */
    @Override
    public void storeWorkflow(Workflow workflow) {
        workflow.setId(workflows.size());
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == workflow.getId()) {
                workflowToRemove = wf;
                break;
            }
        }
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
            this.logger.log(Level.INFO, "[persistence] removed existing workflow "
                    + workflowToRemove.getId() + ".");
        }
        workflows.add((Workflow) workflow);
        this.logger.log(Level.INFO, "[persistence] successfully stored workflow " + workflow.getId()
                + ".");

        // a workflows steps are resolved and stored one by one
        final List<Step> workflowsSteps = workflow.getSteps();
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
            this.logger.log(Level.INFO, "[persistence] removing exisiting item "
                    + itemToRemove.getId() + ".");
        }
        items.add((Item) item);
        this.logger.log(Level.INFO, "[persistence] successfully stored item " + item.getId() + ".");

        // items include information of type MetaEntry which have to be stored
        // separately
        final List<MetaEntry> itemsMetadata = item.getMetadata();
        for (MetaEntry metaEntry : itemsMetadata) {
            storeMetaEntry(metaEntry);
        }
    }

    @Override
    public void addUser(User user) throws UserAlreadyExistsException {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                final UserAlreadyExistsException e = new UserAlreadyExistsException(
                        user.getUsername());
                this.logger.log(Level.WARNING, e);
                throw e;
            }
        }
        users.add((User) user);
        this.logger
                .log(Level.INFO, "[persistence] adding user '" + user.getUsername() + "'.");
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
            this.logger.log(Level.INFO, "[persistence] removing existing user '"
                    + userToRemove.getUsername() + "'.");
            users.add(user);
            this.logger.log(Level.INFO,
                    "[persistence] successfully stored user '" + user.getUsername() + "'.");
        } else {
            final UserNotExistentException e = new UserNotExistentException(
                    user.getUsername());
            this.logger.log(Level.WARNING, e);
            throw e;
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
        //TODO: need to distinguish between Action/FirstStep/StartStep?
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
    public Workflow loadWorkflow(int id) throws WorkflowNotExistentException {
        Workflow workflow = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
                workflow = wf;
            }
        }
        if (workflow != null) {
            return workflow;
        } else {
            final WorkflowNotExistentException e = new WorkflowNotExistentException(
                    "" + id);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        // TODO: return steps of a workflow as well! - can be done as soon as a
        // unique and combined ID for steps and workflows is given
    }

    @Override
    public Item loadItem(int id) throws ItemNotExistentException {
        Item item = null;
        for (Item i : items) {
            if (i.getId() == id) {
                item = i;
            }
        }
        if (item != null) {
            return item;
        } else {
            final ItemNotExistentException e = new ItemNotExistentException("" + id);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
        // TODO: return MetaData of an item as well!
    }

    @Override
    public User loadUser(String name) throws UserNotExistentException {
        User user = null;
        for (User u : users) {
            if (u.getUsername().equals(name)) {
                user = u;
            }
        }
        if (user != null) {
            return user;
        } else {
            final UserNotExistentException e = new UserNotExistentException(name);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
    }

    /* (non-Javadoc)
     * @see de.hsrm.swt02.persistence.Persistence#loadStep(int)
     */
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
    public void deleteWorkflow(int id) throws WorkflowNotExistentException {
        Workflow workflowToRemove = null;
        for (Workflow wf : workflows) {
            if (wf.getId() == id) {
                // a workflows steps are resolved and deleted one by one
                final List<Step> workflowsSteps = wf.getSteps();
                for (Step step : workflowsSteps) {
                    deleteStep(step.getId());
                }
                workflowToRemove = wf;
                break;
            }
        }
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
            this.logger.log(Level.INFO,
                    "[persistence] removed workflow " + workflowToRemove.getId() + ".");
        } else {
            final WorkflowNotExistentException e = new WorkflowNotExistentException(
                    "" + id);
            this.logger.log(Level.WARNING, e);
            throw e;
        }
    }

    @Override
    public void deleteItem(int id) throws ItemNotExistentException {
        Item itemToRemove = null;
        for (Item i : items) {
            if (i.getId() == id) {

                // an items metaData are deleted as well
                final List<MetaEntry> itemsMetaData = i.getMetadata();
                for (MetaEntry metaEntry : itemsMetaData) {
                    deleteMetaEntry(metaEntry.getKey());
                }
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
            this.logger.log(Level.INFO, "[persistence] removed item " + itemToRemove.getId()
                    + ".");
        } else {
            final ItemNotExistentException e = new ItemNotExistentException("" + id);
            this.logger.log(Level.WARNING, e);
            throw e;

        }
    }

    @Override
    public void deleteUser(String name) throws UserNotExistentException {
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(name)) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
        } else {
            final UserNotExistentException e = new UserNotExistentException(name);
            this.logger.log(Level.WARNING, e);
            throw e;
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
