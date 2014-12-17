package de.hsrm.testswt02.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.RoleHasAlreadyUserException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
import de.hsrm.swt02.persistence.exceptions.UserHasAlreadyRoleException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * Class for testing the persistence functions.
 *
 */
public class PersistenceTest {

    // Dependency Injection
    Injector inj = Guice.createInjector(new SingleModule());
    Persistence db = inj.getInstance(Persistence.class);

    // in order to try out DI the methods loadStep and loadMetaEntry are added
    // to the interface 'Persistence'
    // later the following line would be sufficient:
    // PersistenceImp db = new PersistenceImp();

    /**
     * Testing if it's possible to store a workflow.
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws WorkflowNotExistentException
     */
    
    @BeforeClass
    public static void setup() {
        LogConfigurator.setup();
    }
    
    
    @Test
    public void testWorkflowStorage() throws PersistenceException {
        final Workflow workflow007 = new Workflow();
        final Workflow workflow006 = new Workflow();
        final Workflow workflow005 = new Workflow();

        final String id5 = db.storeWorkflow(workflow005);
        final String id6 = db.storeWorkflow(workflow006);
        final String id7 = db.storeWorkflow(workflow007);

        assertEquals(db.loadWorkflow(id5), workflow005);
        assertEquals(db.loadWorkflow(id6), workflow006);
        assertEquals(db.loadWorkflow(id7), workflow007);
    }


    /**
     * Method for testing if it's possible to delete a workflow.
     * @throws WorkflowNotExistentException
     * @throws PersistenceException 
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowDeletion() throws PersistenceException {
        final Workflow wf001 = new Workflow();
        final Workflow wf002 = new Workflow();
        final String id_1 = db.storeWorkflow(wf001);
        final String id_2 = db.storeWorkflow(wf002);

        db.deleteWorkflow(id_1);

        assertEquals(db.loadWorkflow(id_2), wf002);
        assertEquals(db.loadWorkflow(id_1), wf001);
    }

    /**
     * Method for testing if it's possible to store a workflow with a few steps in the steplist.
     * @throws WorkflowNotExistentException
     * @throws PersistenceException 
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowStorageIncludingSteps() throws PersistenceException {
        final Workflow workflow007 = new Workflow();

        final StartStep step1 = new StartStep("username");
        final Action step2 = new Action("username", "main action");
        final FinalStep step3 = new FinalStep();
        

        workflow007.addStep(step1);
        workflow007.addStep(step2);
        workflow007.addStep(step3);

        final String id7 = db.storeWorkflow(workflow007);

        // step2, the action of workflow007, should be persistent
        // step1 and step2 cannot be tested because their ID is not yet
        // implemented
        // assertEquals(step2, db.loadStep(step2.getId()));

        // a workflows steps should be the same before and after storage
        assertEquals(workflow007.getSteps(), db.loadWorkflow(id7).getSteps());
        
        // no specific functions of a step can be tested, because db only
        // returns an AbstractWorkflow
        assertEquals(workflow007.getStepByPos(2), db.loadWorkflow(id7).getStepByPos(2));

        // workflow instances should still be equal
        assertEquals(db.loadWorkflow(workflow007.getId()), workflow007);

        db.deleteWorkflow(workflow007.getId());

        // a workflows steps must not be existent on databse anymore
        // assertEquals(db.loadStep(2), null);
        
        // workflow must not be existent on database anymore, WorkflowNotExistentException is expected here!
        assertEquals(db.loadWorkflow(workflow007.getId()), null);
    }

    /**
     * Method for testing if it's possible to store an item.
     * @throws ItemNotExistentException
     * @throws PersistenceException 
     */
    @Test
    public void testItemStorage() throws PersistenceException {
        final Workflow wf000 = new Workflow();
        final Item item001 = new Item();
        final Item item002 = new Item();
        final Item item003 = new Item();
        final List<Item> itemList = new LinkedList<>();
        
        String workflowId = db.storeWorkflow(wf000);
        
        item001.setWorkflowId(workflowId);
        item002.setWorkflowId(workflowId);
        item003.setWorkflowId(workflowId);
        
        
        wf000.addItem(item001);
        itemList.add(item001);
        workflowId = db.storeWorkflow(wf000);
        
        wf000.addItem(item002);
        itemList.add(item002);
        workflowId = db.storeWorkflow(wf000);
        
        wf000.addItem(item003);
        itemList.add(item003);
        workflowId = db.storeWorkflow(wf000);
        
        assertEquals(wf000, db.loadWorkflow(workflowId));
        assertEquals(itemList.get(0), db.loadWorkflow(workflowId).getItems().get(0));
        assertEquals(itemList.get(1), db.loadWorkflow(workflowId).getItems().get(1));
        assertEquals(itemList.get(2), db.loadWorkflow(workflowId).getItems().get(2));
    }

 
    /**
     * Method for testing if it's possible to store an item with a few metadata entries.
     * @throws ItemNotExistentException
     * @throws PersistenceException 
     */
    @Test
    public void testItemStorageIncludingMetaData() throws PersistenceException {
        final Workflow wf000 = new Workflow();
        final Item item001 = new Item();
        
        final String wfid = db.storeWorkflow(wf000);
        item001.setWorkflowId(wfid);
        item001.set("key1", "group1", "value1");
        item001.set("key2", "group2", "value2");

        wf000.addItem(item001);
        db.storeWorkflow(wf000);
        final String itemId = db.storeItem(item001);
        item001.setId(itemId);

//        item001.set("key1", "group1", "value1");
//        item001.set("key3", "group3", "value3");
//        db.storeItem(item001);
        
        assertEquals(wf000, db.loadWorkflow(wfid));
        assertEquals(item001, db.loadItem(itemId));
        
        // MetaEntries should be persistent
        //assertEquals(item001.getMetadata().get(0), db.loadMetaEntry("key1"));
        //assertEquals(item001.getMetadata().get(1), db.loadMetaEntry("key2"));

        // an items metaData should be the same before and after storage
        assertEquals(item001.getMetadata(), db.loadItem(itemId).getMetadata());
        //assertEquals(item001.getMetadata().get(0).getValue(),db.loadMetaEntry("key1").getValue());

        // item instances should still be the same
        assertEquals(db.loadItem(item001.getId()), item001);

        db.deleteItem(item001.getId());

        // an items metaData must not be existent anymore
        // assertEquals(db.loadMetaEntry("key1"), null);
        
        // item must not be existent anymore, ItemNotExistentException is expected here!
        // assertEquals(db.loadItem(item001.getId()), null);
    }

    /**
     * Method for testing if it's possible to store a user.
     * @throws UserAlreadyExistsException
     * @throws UserNotExistentException
     * @throws PersistenceException 
     */
    @Test
    public void testUserStorage() throws PersistenceException {
        final User user001 = new User();
        final User user002 = new User();
        final User user003 = new User();
        user001.setUsername("1");
        user002.setUsername("2");
        user003.setUsername("3");

        db.addUser(user001);
        db.addUser(user002);
        db.addUser(user003);

        assertEquals(user001, db.loadUser("1"));
        assertEquals(user002, db.loadUser("2"));
        assertEquals(user003, db.loadUser("3"));
    }

    /**
     * Method for testing if a user is already there.
     * @throws UserAlreadyExistsException
     * @throws PersistenceException 
     */
    @Test(expected = UserAlreadyExistsException.class)
    public void testUserAlreadyExistsException() throws PersistenceException {
        final User user001 = new User();
        final User user002 = new User();
        user001.setUsername("17");
        user002.setUsername("17");

        // first user is stored, second one should be rejected by
        // UserAlreadyExistsException
        db.addUser(user001);
        db.addUser(user002);
    }

    /**
     * Method for testing if it's possible to store duplicate user.
     * @throws UserNotExistentException
     * @throws PersistenceException 
     */
    @Test
    public void testDuplicateUserStorage() throws PersistenceException {
        final User user001 = new User();
        final User user002 = new User();
        user001.setUsername("17");
        user002.setUsername("17");

        // first user is stored, second one should be rejected
        try {
            db.addUser(user001);
            db.addUser(user002);
        } catch (UserAlreadyExistsException e) {
            // Exception is caught manually in order to test if
            // addUser(user002) was rejected
            e.printStackTrace();
        }

        assertEquals(user001, db.loadUser("17"));
        assertNotEquals(user002, db.loadUser("17"));
    }

    /**
     * Method for testing if it's possible to delete an user.
     * @throws UserAlreadyExistsException
     * @throws UserNotExistentException
     * @throws PersistenceException 
     */
    @Test(expected = UserNotExistentException.class) 
    public void testUserDeletion() throws PersistenceException {
        final User user001 = new User();
        final User user002 = new User();
        user001.setUsername("1");
        user002.setUsername("2");

        db.addUser(user001);
        db.addUser(user002);

        db.deleteUser("1");
        
        // user001 should habe been overwritten by user002
        assertEquals(db.loadUser("2"), user002);
        // UserNotExistentException is expected here!
        assertEquals(db.loadUser("1"), null);
    }

    /**
     * Method for testing if it's possible to update an User.
     * @throws UserNotExistentException
     * @throws UserAlreadyExistsException
     * @throws PersistenceException 
     */
    @Test
    public void testUpdateOnUser() throws PersistenceException {
        final User user001 = new User();
        user001.setUsername("1");
        db.addUser(user001);

        assertEquals(user001, db.loadUser(user001.getUsername()));
        assertEquals(user001.getId(), db.loadUser(user001.getUsername())
                .getId());

        // TODO:
        // assertNotEquals(user001, db.loadUser(user001.getName()));
        // assertNotEquals(user001.getId(),
        // db.loadUser(user001.getName()).getId());

        // update on existing user
        user001.setUsername("2");
        db.updateUser(user001);

        assertEquals(user001, db.loadUser(user001.getUsername()));
        assertEquals(user001.getId(), db.loadUser(user001.getUsername())
                .getId());
    }

    /**
     * Method for testing of updating a nonexistent user.
     * @throws PersistenceException 
     */
    @Test(expected = UserNotExistentException.class)
    public void testUpdateOnNonExistentUser() throws PersistenceException {
        final User user001 = new User();
        user001.setUsername("1");

        db.updateUser(user001);
    }
    
    /**
     * Method for testing if storage of roles works.
     * @exception RoleNotExistentException if the requested role is not there
     * @throws RoleNotExistentException
     */
    @Test
    public void testRoleStorage() throws RoleNotExistentException {
        final Role role007 = new Role();
        final Role role008 = new Role();
        final Role role009 = new Role();

        final String id7 = db.storeRole(role007);
        final String id8 = db.storeRole(role008);
        final String id9 = db.storeRole(role009);

        assertEquals(db.loadRole(id7), role007);
        assertEquals(db.loadRole(id8), role008);
        assertEquals(db.loadRole(id9), role009);
    }
    
    /**
     * Method for testing if it's possible to assign roles to users and vice-versa.
     * @exception RoleNotExistentException if the requested role is not there
     * @throws UserHasAlreadyRoleException
     * @throws RoleHasAlreadyRoleException
     * @throws UserNotExistentException
     * @throws RoleNotExistentException 
     * @throws PersistenceException 
     */
    @Test
    public void testRoleToUserAssignement() throws PersistenceException {
        final Role roletest = new Role();
        final User usertest = new User();
        usertest.setUsername("name");
        
        db.storeRole(roletest);
        db.addUser(usertest);
        
        db.addRoleToUser(usertest, roletest);
        
        assertTrue(db.loadUser("name").getRoles().contains(roletest));
    }

}
