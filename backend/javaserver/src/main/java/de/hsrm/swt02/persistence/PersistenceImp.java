package de.hsrm.swt02.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.DataStorage;
import de.hsrm.swt02.model.DataType;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.FormEntry;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.FormNotExistentException;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StepNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.properties.ConfigProperties;

/**
 * @author Dominik
 *
 */

@Singleton
public class PersistenceImp implements Persistence {

    /** The logger. */
    private UseLogger logger;
    private static final int ID_MULTIPLICATOR = 1000;

    /**
     * abstraction of a database, that persists the basic data mdoels.
     */
    private List<Workflow> workflows = new LinkedList<>();

    private List<User> users = new LinkedList<>();

    private List<Role> roles = new LinkedList<>();

    private List<Form> forms = new LinkedList<>();

    private String storagePath;

    /**
     * Constructor for PersistenceImp.
     * 
     * @param logger is the logger for logging.
     */
    @Inject
    public PersistenceImp(UseLogger logger) {
        this.logger = logger;
        this.storagePath = ConfigProperties.getInstance().getProperties()
                .getProperty("StoragePath");
    }

    // Workflow Operations

    @Override
    public String storeWorkflow(Workflow workflow) throws PersistenceException {
        Workflow workflowToRemove = null;
        int stepIdx = 0;
        int itemIdx = 1;
        final int idDivider = 10;
        if (workflow.getId() == null || workflow.getId().equals("")) {
            workflow.setId(workflows.size() + 1 + "");
        } else {
            workflowToRemove = loadWorkflow(workflow.getId());
        }
        if (workflowToRemove != null) {
            try {
                this.deleteWorkflow(workflowToRemove.getId());
                this.logger.log(Level.FINE,
                        "[persistence] overwriting workflow "
                                + workflowToRemove.getId() + "...");
            } catch (WorkflowNotExistentException e) {
                throw new StorageFailedException("storage of workflow"
                        + workflow.getId() + "failed.");
            }
        }

        if (workflow.getItems().size() > 0) {
            String maxId = "0";
            for (Item item : workflow.getItems()) {
                if (!(item.getId() == null || item.getId().equals(""))) {
                    if (Integer.parseInt(item.getId()) > Integer
                            .parseInt(maxId))
                    {
                        maxId = item.getId();
                    }
                }
            }

            for (Item item : workflow.getItems()) {
                if (item.getId() == null || item.getId().equals("")) {
                    item.setId((Integer.parseInt(workflow.getId())
                            * ID_MULTIPLICATOR
                            + (Integer.parseInt(maxId) % (ID_MULTIPLICATOR / idDivider)) + itemIdx)
                            + "");
                    item.setWorkflowId(workflow.getId());
                    itemIdx++;
                }
            }
        }

        if (workflow.getSteps().size() > 0) {
            for (Step step : workflow.getSteps()) {
                if (step.getId() == null || step.getId().equals("")) {
                    step.setId((Integer.parseInt(workflow.getId())
                            * ID_MULTIPLICATOR + stepIdx + 1)
                            + "");
                    stepIdx++;
                }
            }
        }

        Workflow workflowToStore;
        try {
            workflowToStore = (Workflow) workflow.clone();
        } catch (CloneNotSupportedException e) {
            throw new PersistenceException(
                    "Storage failed - Cloning did not work.");
        }

        workflows.add(workflowToStore);

        this.logger.log(Level.INFO,
                "[persistence] successfully stored/updated workflow "
                        + workflow.getId() + ".");
        return workflow.getId();
    }

    @Override
    public void deleteWorkflow(String id) throws PersistenceException {
        final Workflow workflowToRemove = loadWorkflow(id);
        if (workflowToRemove != null) {
            workflows.remove(workflowToRemove);
            this.logger.log(Level.INFO,
                    "[persistence] successfully removed workflow "
                            + workflowToRemove.getId() + ".");
        } else {
            throw new WorkflowNotExistentException("database has no workflow "
                    + id);
        }
        assert (workflows.contains(workflowToRemove) == false);
    }

    @Override
    public Workflow loadWorkflow(String id) throws PersistenceException {
        Workflow workflowToReturn = null;
        for (Workflow wf : workflows) {
            if (wf.getId().equals(id)) {
                try {
                    workflowToReturn = (Workflow) wf.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of workflow" + id
                            + "failed.");
                }
            }
        }
        if (workflowToReturn != null) {
            return workflowToReturn;
        } else {
            throw new WorkflowNotExistentException("database has no workflow "
                    + id);
        }
    }

    /**
     * Method for getting the parentworkflow of an item.
     * 
     * @param itemId is the id of the item
     * @return parentWorkflow
     * @throws PersistenceException 
     */
    public Workflow getParentWorkflow(String itemId)
            throws PersistenceException
    {
        final int integerItemId = Integer.parseInt(itemId);
        final int idDivider = 10;

        final int eliminatedItemId = integerItemId
                % (ID_MULTIPLICATOR / idDivider);
        final String parentWorkflowId = ((integerItemId - eliminatedItemId) / ID_MULTIPLICATOR)
                + "";
        final Workflow parentWorkflow = loadWorkflow(parentWorkflowId);
        return parentWorkflow;
    }

    @Override
    public void deleteItem(String itemId) throws PersistenceException {
        final Workflow parentWorkflow = getParentWorkflow(itemId);

        Item itemToRemove = null;
        for (Item item : parentWorkflow.getItems()) {
            if (item.getId().equals(itemId)) {
                itemToRemove = item;
            }
        }

        if (itemToRemove != null) {
            parentWorkflow.getItems().remove(itemToRemove);
            this.logger.log(
                    Level.INFO,
                    "[persistence] successfully removed item "
                            + itemToRemove.getId() + ".");
        } else {
            throw new ItemNotExistentException("database has no item " + itemId);
        }
    }

    @Override
    public Item loadItem(String itemId) throws PersistenceException {
        final Workflow parentWorkflow = getParentWorkflow(itemId);
        Item itemToReturn = null;
        if (parentWorkflow == null) {
            throw new WorkflowNotExistentException(
                    "there is no parent workflow for item " + itemId);
        }
        for (Item item : parentWorkflow.getItems()) {
            if (item.getId().equals(itemId)) {
                try {
                    itemToReturn = (Item) item.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of item" + itemId
                            + "failed.");
                }
            }
        }
        if (itemToReturn != null) {
            return itemToReturn;
        } else {
            throw new ItemNotExistentException("database has no item " + itemId);
        }
    }

    // Step Operations

    @Override
    public Step loadStep(String itemId, String stepId)
            throws PersistenceException
    {
        final Item item = loadItem(itemId);
        final String workflowId = item.getWorkflowId();
        final Workflow workflow = loadWorkflow(workflowId);
        Step step = null;

        for (Step s : workflow.getSteps()) {
            if (s.getId().equals(stepId)) {
                try {
                    step = (Step) s.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of step" + stepId
                            + "failed.");
                }
            }
        }

        if (step != null) {
            return step;
        } else {
            throw new StepNotExistentException("Step " + stepId
                    + " is not existent.");
        }
    }

    // User Operations

    @Override
    public void storeUser(User user) throws PersistenceException {
        assert (user.getUsername() != null);

        User userToStore;
        try {
            userToStore = (User) user.clone();
        } catch (CloneNotSupportedException e) {
            throw new StorageFailedException("storage of user"
                    + user.getUsername() + "failed.");
        }
        for (Role role : userToStore.getRoles()) {
            this.loadRole(role.getRolename());
        }

        try {
            this.deleteUser(user.getUsername());
        } catch (UserNotExistentException e) {
            // Checkstyle expects at least one statement here
            e.getClass();
        }
        users.add(userToStore);
        this.logger.log(
                Level.INFO,
                "[persistence] successfully stored/updated user "
                        + user.getUsername() + ".");
    }

    @Override
    public User loadUser(String username) throws PersistenceException {
        User userToReturn = null;
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                try {
                    userToReturn = (User) u.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of user"
                            + username + "failed.");
                }
            }
        }
        if (userToReturn != null) {
            return userToReturn;
        } else {
            throw new UserNotExistentException("database has no user '"
                    + username + "'.");
        }
    }

    @Override
    public void deleteUser(String username) throws UserNotExistentException {
        User userToRemove = null;
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                userToRemove = u;
                break;
            }
        }
        if (userToRemove != null) {
            users.remove(userToRemove);
            this.logger.log(
                    Level.INFO,
                    "[persistence] successfully removed user "
                            + userToRemove.getId() + ".");
        } else {
            throw new UserNotExistentException("database has no user '"
                    + username + "'.");
        }
    }

    // Role Operations
    @Override
    public void storeRole(Role role) throws PersistenceException {
        if (role.getId() == null || role.getId().equals("")) {
            role.setId(roles.size() + 1 + "");
        }
        Role roleToRemove = null;
        for (Role r : roles) {
            if (r.getRolename().equals(role.getRolename())) {
                roleToRemove = r;
                break;
            }
        }
        if (roleToRemove != null) {
            this.logger.log(Level.FINE, "[persistence] overwriting role "
                    + roleToRemove.getRolename() + "...");
            this.deleteRole(roleToRemove.getRolename());
        }
        try {
            roles.add((Role) role.clone());
        } catch (CloneNotSupportedException e) {
            throw new PersistenceException(
                    "Storage failed - Cloning did not work.");
        }
        this.logger.log(
                Level.INFO,
                "[persistence] successfully stored/updated role "
                        + role.getId() + ".");
    }

    /**
     * Method for loading a workflow.
     * 
     * @param rolename is the name of the requested role
     * @return workflow is the requested workflow
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     */
    @Override
    public Role loadRole(String rolename) throws PersistenceException {
        Role roleToReturn = null;
        for (Role r : roles) {
            if (r.getRolename().equals(rolename)) {
                try {
                    roleToReturn = (Role) r.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of Role"
                            + rolename + "failed.");
                }
            }
        }
        if (roleToReturn != null) {
            return roleToReturn;
        } else {
            throw new RoleNotExistentException(
                    "[persistence] database has no role '" + rolename + "'.");
        }
    }

    /**
     * Method for deleting an existing role.
     * 
     * @param rolename is the name of the given role
     * @exception RoleNotExistentException if the given role doesnt exist
     * @throws RoleNotExistentException
     */
    @Override
    public void deleteRole(String rolename) throws RoleNotExistentException {
        Role roleToRemove = null;
        for (Role r : roles) {
            if (r.getRolename().equals(rolename)) {
                roleToRemove = r;
                break;
            }
        }
        if (roleToRemove != null) {
            roles.remove(roleToRemove);
            this.logger.log(
                    Level.INFO,
                    "[persistence] successfully removed role "
                            + roleToRemove.getId() + ".");
        } else {
            throw new RoleNotExistentException("database has no role '"
                    + rolename + "'.");
        }
    }

    // General Operations on database

    @Override
    public List<Workflow> loadAllWorkflows() throws PersistenceException {
        final List<Workflow> retList = new LinkedList<>();
        for (Workflow wf : this.workflows) {
            retList.add(this.loadWorkflow(wf.getId()));
        }
        return retList;
    }

    @Override
    public List<Role> loadAllRoles() throws PersistenceException {
        final List<Role> retList = new LinkedList<>();
        for (Role role : this.roles) {
            retList.add(this.loadRole(role.getRolename()));
        }
        return retList;
    }

    @Override
    public List<User> loadAllUsers() throws PersistenceException {
        final List<User> retList = new LinkedList<>();
        for (User user : this.users) {
            retList.add(this.loadUser(user.getUsername()));
        }
        return retList;
    }

    @Override
    public List<Form> loadAllForms() throws PersistenceException {
        final List<Form> retList = new LinkedList<>();
        for (Form f : this.forms) {
            retList.add(this.loadForm(f.getId()));
        }
        return retList;
    }

    // Form Operations

    @Override
    public void storeForm(Form form) throws PersistenceException {
        assert (form.getId() != null);

        final List<String> formEntryIds = new LinkedList<>();
        for (FormEntry fe : form.getFormDef()) {

            // check if there are no duplicates in formEntry keys.
            if (formEntryIds.contains(fe.getKey())) {
                throw new StorageFailedException(
                        "[persistence] form storage not possible - duplicating keys");
            }
            formEntryIds.add(fe.getKey());

            // check if all value types are accepted.
            if (DataType.hasType(fe.getDatatype())) {
                throw new StorageFailedException(
                        "[persistence] form storage not possible - unexpected value type");
            }
        }

        Form formToRemove = null;
        for (Form f : forms) {
            if (f.getId().equals(form.getId())) {
                formToRemove = f;
                break;
            }
        }
        if (formToRemove != null) {
            this.deleteForm(formToRemove.getId());
            this.logger.log(Level.FINE, "[persistence] overwriting form "
                    + form.getId() + "...");
        }
        Form formToStore;
        try {
            formToStore = (Form) form.clone();
        } catch (CloneNotSupportedException e) {
            throw new StorageFailedException("storage of form" + form.getId()
                    + "failed.");
        }

        forms.add(formToStore);
        this.logger.log(
                Level.INFO,
                "[persistence] successfully stored/updated form "
                        + form.getId() + ".");
    }

    /**
     * loads a form from database by formId.
     * 
     * @param formId - a forms unique id
     * @return form that was requested
     * @throws PersistenceException if an error in databse occurs
     */
    public Form loadForm(String formId) throws PersistenceException {
        Form formToReturn = null;
        for (Form f : forms) {
            if (f.getId().equals(formId)) {
                try {
                    formToReturn = (Form) f.clone();
                } catch (CloneNotSupportedException e) {
                    throw new StorageFailedException("loading of form" + formId
                            + "failed.");
                }
            }
        }
        if (formToReturn != null) {
            return formToReturn;
        } else {
            throw new FormNotExistentException("database has no form '"
                    + formId + "'.");
        }
    };

    @Override
    public void deleteForm(String formId) throws PersistenceException {
        Form formToRemove = null;
        for (Form f : forms) {
            if (f.getId().equals(formId)) {
                formToRemove = f;
                break;
            }
        }
        if (formToRemove != null) {
            forms.remove(formToRemove);
            this.logger.log(
                    Level.INFO,
                    "[persistence] successfully removed form "
                            + formToRemove.getId() + ".");
        } else {
            throw new FormNotExistentException("database has no form '"
                    + formId + "'.");
        }

        for (Workflow workflow : workflows) {
            if (workflow.getForm() != null
                    && workflow.getForm().getId().equals(formId)) 
            {
                workflow.setForm(null);
                this.logger
                        .log(Level.INFO,
                                "[persistence] successfully removed form from workflow.");
            }
        }
    }

    @Override
    public void save() {
        if (storagePath == null) {
            this.logger.log(Level.INFO,
                    "[persistece] no storage path provided.");
            return;
        }

        // browse through all DataModels and serialize them into a file (path in
        // server configuration file)
        try {
            final FileOutputStream fileOut = new FileOutputStream(storagePath);
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);

            final DataStorage ds = new DataStorage(workflows, users, roles,
                    forms);
            out.writeObject(ds);
            out.close();
            fileOut.close();

            this.logger.log(Level.INFO,
                    "[persistence] successfully saved data models in "
                            + storagePath + ".");
        } catch (IOException e) {
            this.logger.log(Level.WARNING, e);
        }
    }

    @Override
    public void load() {
        boolean dataLoaded = false;

        if (storagePath != null) {
            final File f = new File(storagePath);
            if (storagePath.contains(".ser") && f.exists() && f.isFile()) {

                try {
                    final FileInputStream fileIn = new FileInputStream(
                            storagePath);
                    final ObjectInputStream in = new ObjectInputStream(fileIn);

                    // get lists from DataStorage object
                    final DataStorage newDs = (DataStorage) in.readObject();
                    workflows = newDs.getWorkflows();
                    users = newDs.getUsers();
                    roles = newDs.getRoles();
                    forms = newDs.getForms();
                    dataLoaded = true;
                    in.close();
                    fileIn.close();
                } catch (IOException i) {
                    this.logger.log(Level.WARNING,
                            "[persistence] could not read/load data model file "
                                    + storagePath + ".");
                    this.logger.log(Level.WARNING, i);
                } catch (ClassNotFoundException e) {
                    this.logger.log(Level.WARNING,
                            "[persistence] could not find fitting class"
                                    + " for serialization of file "
                                    + storagePath + ".");
                    this.logger.log(Level.WARNING, e);
                }
            } else {
                this.logger
                        .log(Level.INFO,
                                "[persistence] storage file with suffix .ser does not exists.");
            }
        } else {
            this.logger.log(Level.INFO,
                    "[persistence] no storage path provided.");
        }
        if (!dataLoaded) {
            try {
                this.logger.log(Level.INFO, "[persistence] load test data.");
                initTestdata();
                // additional test data; ATTENTION not validated by logic, might fail in some cases
                //initPitchWorkflow();
            } catch (PersistenceException e) {
                this.logger.log(Level.WARNING, e);
            }
        }
    }

    /**
     * Initialize test datas.
     * 
     * @throws PersistenceException - if an error in persistence occurs
     */
    private void initTestdata() throws PersistenceException {
        final double standardDistance = 100;
        int elementCounter = 1;

        Workflow workflow1;
        User user1, user2, user3, user4;
        StartStep startStep1;
        Action action1, action2;
        FinalStep finalStep;
        Role role1, role2, role3;
        Form form1, form2;

        user1 = new User();
        user1.setUsername("Alex");
        user1.setPassword("");
        user2 = new User();
        user2.setUsername("Dominik");
        user2.setPassword("");
        user3 = new User();
        user3.setUsername("Tilman");
        user3.setPassword("");
        user4 = new User();
        user4.setUsername(ConfigProperties.getInstance().getProperties()
                .getProperty("AdminNameDefinition"));
        user4.setPassword(ConfigProperties.getInstance().getProperties().getProperty("AdminPasswordDefinition"));

        role1 = new Role();
        // role1.setId("1");
        role1.setRolename("Manager");
        storeRole(role1);
        role2 = new Role();
        role2.setRolename("Sachbearbeiter");
        storeRole(role2);
        role3 = new Role();
        role3.setRolename(ConfigProperties.getInstance().getProperties()
                .getProperty("AdminRoleDefinition"));
        storeRole(role3);

        user1.addRole(role1);
        user2.addRole(role1);
        user4.addRole(role3);

        storeUser(user1);
        storeUser(user2);
        storeUser(user3);
        storeUser(user4);

        startStep1 = new StartStep();

        // display formatting
        startStep1.setLeft(elementCounter * standardDistance);
        startStep1.setTop(elementCounter * standardDistance);
        elementCounter++;

        startStep1.getRoleIds().add(role1.getRolename());

        action1 = new Action(new ArrayList<String>(), "Action von "
                + user1.getUsername());

        // display formatting
        action1.setLeft(elementCounter * standardDistance);
        action1.setTop(elementCounter * standardDistance);
        elementCounter++;

        action1.addRole(role1.getRolename());
        action2 = new Action(new ArrayList<String>(), "Action von "
                + user2.getUsername());

        // display formatting
        action2.setLeft(elementCounter * standardDistance);
        action2.setTop(elementCounter * standardDistance);
        elementCounter++;

        action2.addRole(role1.getRolename());

        finalStep = new FinalStep();

        // display formatting
        finalStep.setLeft(elementCounter * standardDistance);
        finalStep.setTop(elementCounter * standardDistance);
        elementCounter++;

        finalStep.getRoleIds().add(role1.getRolename());

        workflow1 = new Workflow();
        workflow1.addStep(startStep1);
        workflow1.addStep(action1);
        workflow1.addStep(action2);
        workflow1.addStep(finalStep);

        form1 = new Form("das ist ein Formular");
        form2 = new Form("FORM");

        final FormEntry fe1 = new FormEntry();
        fe1.setId("nameEntry");
        fe1.setKey("name");
        fe1.setValue("String");
        final FormEntry fe2 = new FormEntry();
        fe2.setId("ageEntry");
        fe2.setKey("age");
        fe2.setValue("int");
        final FormEntry fe3 = new FormEntry();
        fe3.setId("deliveredBookCount");
        fe3.setKey("bookCount");
        fe3.setValue("int");
        final FormEntry fe4 = new FormEntry();
        fe4.setId("deliveryOnTime");
        fe4.setKey("onTime");
        fe4.setValue("String");
        final FormEntry fe5 = new FormEntry();
        fe5.setId("chargedCosts");
        fe5.setKey("cost");
        fe5.setValue("double");

        final List<FormEntry> fd1 = form1.getFormDef();
        fd1.add(fe1);
        fd1.add(fe2);
        fd1.add(fe5);
        form1.setId("form1");

        final List<FormEntry> fd2 = form2.getFormDef();
        fd2.add(fe3);
        fd2.add(fe4);
        fd2.add(fe5);
        form2.setId("form2");

        storeForm(form1);
        storeForm(form2);
        workflow1.setForm(form1);
        storeWorkflow(workflow1);
    }
    
    /**
     * Init testData for the pitch workflow.
     * @throws PersistenceException 
     */
    private void initPitchWorkflow() throws PersistenceException {
        final double standardDistance = 100;
        int elementCounter = 1;

        final Workflow workflow1;
        final User user1, user2, user3, user4, user5, user6;
        final StartStep startStep1;
        final FinalStep finalStep;
        final Role role1, role2, role3, role4, role5;
        final Form form1, form2;

        user1 = new User();
        user1.setUsername("Kalle");
        user2 = new User();
        user2.setUsername("Simon");
        user3 = new User();
        user3.setUsername("Jane");
        user4 = new User();
        user4.setUsername(ConfigProperties.getInstance().getProperties()
                .getProperty("AdminNameDefinition"));
        user4.setPassword("");
        user5 = new User();
        user5.setUsername("Jerome");
        user6 = new User();
        user6.setUsername("Elizabeth");

        role1 = new Role();
        role1.setRolename("Kunde");
        storeRole(role1);
        role2 = new Role();
        role2.setRolename("Agentur");
        storeRole(role2);
        role3 = new Role();
        role3.setRolename(ConfigProperties.getInstance().getProperties()
                .getProperty("AdminRoleDefinition"));
        storeRole(role3);
        role4 = new Role();
        role4.setRolename("Frontend");
        storeRole(role4);
        role5 = new Role();
        role5.setRolename("Backend");
        storeRole(role5);

        // Alex --> Kunde
        user1.addRole(role1);
        // Dominik --> Kunde
        user2.addRole(role1);
        // Tilman --> Agentur, Frontend
        user3.addRole(role2);
        user3.addRole(role4);
        // TestAdmin --> admin
        user4.addRole(role3);
        // Jerome --> Agentur, Backend
        user5.addRole(role2);
        user5.addRole(role5);
        // Elizabeth --> Agentur, Frontend
        user6.addRole(role2);
        user6.addRole(role4);

        storeUser(user1);
        storeUser(user2);
        storeUser(user3);
        storeUser(user4);
        storeUser(user5);

        // create Startstep
        startStep1 = new StartStep();
//        startStep1.setId("zu Pitch einladen");
        startStep1.getRoleIds().add(role1.getRolename());

        // display formatting for StartStep
        startStep1.setLeft(elementCounter * standardDistance);
        startStep1.setTop(elementCounter * standardDistance);
        elementCounter++;

        // create Action
        final Action proofSuccessChance = new Action(
                new ArrayList<String>(),
                "Die Mitarbeiter der Agentur prüfen, ob sie die Anforderungen des Pitchs erfüllen und ob sie überhaupt "
                        + "Erfolgschancen hätten.");
//        proofSuccessChance.setId("Erfolgschancen prüfen");
        proofSuccessChance.addRole(role2.getId());

        // display formatting for Action1
        proofSuccessChance.setLeft(elementCounter * standardDistance);
        proofSuccessChance.setTop(elementCounter * standardDistance);
        elementCounter++;

        final Action informStaff = new Action(new ArrayList<String>(),
                "Informieren der Mitarbeiter über den angenommenen Pitch.");
//        informStaff.setId("Mitarbeiter informieren");
        informStaff.addRole(role2.getId());

        // display formatting for action 2
        informStaff.setLeft(elementCounter * standardDistance);
        informStaff.setTop(elementCounter * standardDistance);
        elementCounter++;

        // create Action Konzept ausarbeiten
        final Action prepareConcept = new Action(new ArrayList<String>(),
                "Das Frontend arbeitet ein Konzept für den Pitch aus.");
//        prepareConcept.setId("Konzept ausarbeiten");
        prepareConcept.addRole(role4.getId());

        // display formatting for action 3
        prepareConcept.setLeft(elementCounter * standardDistance);
        prepareConcept.setTop(elementCounter * standardDistance);
        elementCounter++;

        // create Action Konzept weitergeben
        final Action passForward = new Action(new ArrayList<String>(),
                "Das Frontend gibt das Konzept weiter an das Backend.");
//        passForward.setId("Konzept weitergeben");
        passForward.addRole(role4.getId());

        // display formatting for action 3
        passForward.setLeft(elementCounter * standardDistance);
        passForward.setTop(elementCounter * standardDistance);
        elementCounter++;

        // create Action Konzept evaluieren
        final Action evaluateConcept = new Action(new ArrayList<String>(),
                "Das Backend der Agentur evaluiert das Konzept.");
//        evaluateConcept.setId("Konzept evaluieren");
        evaluateConcept.addRole(role5.getId());

        // display formatting for action 3
        evaluateConcept.setLeft(elementCounter * standardDistance);
        evaluateConcept.setTop(elementCounter * standardDistance);
        elementCounter++;

        // create Action Konzept bestätigen
        final Action confirmConcept = new Action(new ArrayList<String>(),
                "Das Backend der Agentur bestätigt das Konzept.");
//        confirmConcept.setId("Konzept bestätigen");
        confirmConcept.addRole(role5.getId());

        // display formatting for action 3
        confirmConcept.setLeft(elementCounter * standardDistance);
        confirmConcept.setTop(elementCounter * standardDistance);
        elementCounter++;

        // create FinalStep();
        finalStep = new FinalStep();
        finalStep.addRole(role1.getId());

        // display formatting
        finalStep.setLeft(elementCounter * standardDistance);
        finalStep.setTop(elementCounter * standardDistance);
        elementCounter++;

        workflow1 = new Workflow();
        workflow1.setName("Pitch");
        workflow1.addStep(startStep1);
        workflow1.addStep(proofSuccessChance);
        workflow1.addStep(informStaff);
        workflow1.addStep(prepareConcept);
        workflow1.addStep(evaluateConcept);
        workflow1.addStep(confirmConcept);
        workflow1.addStep(finalStep);

        form1 = new Form("Benötigte Unterlagen");
        form2 = new Form("Teilnehmer der Agentur");

        final FormEntry fe1 = new FormEntry();
        fe1.setId("personInPowerEntry");
        fe1.setKey("Verantwortlicher");
        fe1.setValue("String");
        final FormEntry fe2 = new FormEntry();
        fe2.setId("hourEntry");
        fe2.setKey("Aufwand in St.");
        fe2.setValue("int");
        final FormEntry fe3 = new FormEntry();
        fe3.setId("deliveredBookCount");
        fe3.setKey("bookCount");
        fe3.setValue("int");
        final FormEntry fe4 = new FormEntry();
        fe4.setId("deliveryOnTime");
        fe4.setKey("onTime");
        fe4.setValue("boolean");
        final FormEntry fe5 = new FormEntry();
        fe5.setId("chargedCosts");
        fe5.setKey("cost");
        fe5.setValue("double");

        final List<FormEntry> fd1 = form1.getFormDef();
        fd1.add(fe1);
        fd1.add(fe2);
        fd1.add(fe5);
        form1.setId("resources");

        final List<FormEntry> fd2 = form2.getFormDef();
        fd2.add(fe3);
        fd2.add(fe4);
        fd2.add(fe5);
        form2.setId("members");

        storeForm(form1);
        storeForm(form2);
        workflow1.setForm(form1);
        storeWorkflow(workflow1);

    }

    @Override
    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }
}