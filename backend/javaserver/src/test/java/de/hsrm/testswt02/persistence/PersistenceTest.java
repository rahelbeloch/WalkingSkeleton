package de.hsrm.testswt02.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.RoleHasAlreadyUserException;
import de.hsrm.swt02.persistence.exceptions.RoleNotExistentException;
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
    @Test
    public void testWorkflowStorage() throws WorkflowNotExistentException {
        final Workflow workflow007 = new Workflow();
        final Workflow workflow006 = new Workflow();
        final Workflow workflow005 = new Workflow();

        db.storeWorkflow(workflow005);
        db.storeWorkflow(workflow006);
        db.storeWorkflow(workflow007);

        assertEquals(db.loadWorkflow(workflow005.getId()), workflow005);
        assertEquals(db.loadWorkflow(workflow006.getId()), workflow006);
        assertEquals(db.loadWorkflow(workflow007.getId()), workflow007);
    }

    /**
     * Class for testing the storage of multiple duplicate workflows.
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws WorkflowNotExistentException
     */
    @Test
    public void testDuplicateWorkflowStorage() throws WorkflowNotExistentException {
        final Workflow workflow001 = new Workflow();
        final Workflow workflow002 = new Workflow();
        

        db.storeWorkflow(workflow001);
        db.storeWorkflow(workflow002);
        

        // current assumption: second workflow entry should have overwritten the
        // first one
        assertNotEquals(workflow001, db.loadWorkflow(workflow002.getId()));
        assertEquals(workflow002, db.loadWorkflow(workflow002.getId()));
    }

    /**
     * Method for testing if it's possible to delete a workflow.
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws WorkflowNotExistentException
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowDeletion() throws WorkflowNotExistentException {
        final Workflow wf001 = new Workflow();
        final Workflow wf002 = new Workflow();
        db.storeWorkflow(wf001);
        db.storeWorkflow(wf002);

        db.deleteWorkflow(wf001.getId());

        assertEquals(db.loadWorkflow(2), wf002);
        assertEquals(db.loadWorkflow(1), null);
    }

    /**
     * Method for testing if it's possible to store a workflow with a few steps in the steplist.
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws WorkflowNotExistentException
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowStorageIncludingSteps() throws WorkflowNotExistentException {
        final Workflow workflow007 = new Workflow();

        final StartStep step1 = new StartStep("username");
        final Action step2 = new Action("username", "main action");
        final FinalStep step3 = new FinalStep();
        

        workflow007.addStep(step1);
        workflow007.addStep(step2);
        workflow007.addStep(step3);

        db.storeWorkflow(workflow007);

        // step2, the action of workflow007, should be persistent
        // step1 and step2 cannot be tested because their ID is not yet
        // implemented
        assertEquals(step2, db.loadStep(step2.getId()));

        // a workflows steps should be the same before and after storage
        assertEquals(workflow007.getSteps(), db.loadWorkflow(workflow007.getId()).getSteps());
        // no specific functions of a step can be tested, because db only
        // returns an AbstractWorkflow
        // assertEquals(workflow007.getStepByPos(2),
        // db.loadWorkflow(7).getStepByPos(2));

        // workflow instances should still be equal
        assertEquals(db.loadWorkflow(workflow007.getId()), workflow007);

        db.deleteWorkflow(workflow007.getId());

        // a workflows steps must not be existent on databse anymore
        assertEquals(db.loadStep(2), null);
        
        // workflow must not be existent on database anymore, WorkflowNotExistentException is expected here!
        assertEquals(db.loadWorkflow(workflow007.getId()), null);
    }

    /**
     * Method for testing if it's possible to store an item.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    @Test
    public void testItemStorage() throws ItemNotExistentException, WorkflowNotExistentException {
        final Workflow wf000 = new Workflow();
        final Item item001 = new Item();
        final Item item002 = new Item();
        final Item item003 = new Item();
        
        db.storeWorkflow(wf000);
        
        item001.setWorkflowId(wf000.getId());
        item002.setWorkflowId(wf000.getId());
        item003.setWorkflowId(wf000.getId());
        
        wf000.addItem(item001);
        db.storeWorkflow(wf000);
        db.storeItem(item001);
        
        wf000.addItem(item002);
        db.storeWorkflow(wf000);
        db.storeItem(item002);
        
        wf000.addItem(item003);
        db.storeWorkflow(wf000);
        db.storeItem(item003);
        
        assertEquals(item001, db.loadItem(item001.getId()));
        assertEquals(item002, db.loadItem(item002.getId()));
        assertEquals(item003, db.loadItem(item003.getId()));
    }

 
    /**
     * Method for testing if it's possible to store an item with a few metadata entries.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    @Test(expected = ItemNotExistentException.class)
    public void testItemStorageIncludingMetaData() throws ItemNotExistentException, WorkflowNotExistentException {
        final Workflow wf000 = new Workflow();
        final Item item001 = new Item();
        
        db.storeWorkflow(wf000);
        item001.setWorkflowId(wf000.getId());
        item001.set("key1", "group1", "value1");
        item001.set("key2", "group2", "value2");

        wf000.addItem(item001);
        db.storeWorkflow(wf000);
        db.storeItem(item001);

        item001.set("key1", "group1", "value1");
        item001.set("key3", "group1", "value1");

        db.storeItem(item001);

        // MetaEntries should be persistent
        assertEquals(item001.getMetadata().get(0), db.loadMetaEntry("key1"));
        assertEquals(item001.getMetadata().get(1), db.loadMetaEntry("key2"));

        // an items metaData should be the same before and after storage
        assertEquals(item001.getMetadata(), db.loadItem(item001.getId()).getMetadata());
        assertEquals(item001.getMetadata().get(0).getValue(),
                db.loadMetaEntry("key1").getValue());

        // item instances should still be the same
        assertEquals(db.loadItem(item001.getId()), item001);

        db.deleteItem(item001.getId());

        // an items metaData must not be existent anymore
        assertEquals(db.loadMetaEntry("key1"), null);
        
        // item must not be existent anymore, ItemNotExistentException is expected here!
        assertEquals(db.loadItem(item001.getId()), null);
    }

    /**
     * Method for testing if it's possible to store a user.
     * @exception UserAlreadyExistsException if the requested user is already there
     * @exception UserNotExistentException if the requested user is not there
     * @throws UserAlreadyExistsException
     * @throws UserNotExistentException
     */
    @Test
    public void testUserStorage() throws UserAlreadyExistsException, UserNotExistentException {
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
     * @exception UserAlreadyExistsException if the requested user is already there
     * @throws UserAlreadyExistsException
     */
    @Test(expected = UserAlreadyExistsException.class)
    public void testUserAlreadyExistsException() throws UserAlreadyExistsException {
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
     * @exception UserNotExistentException if the requested user is not there
     * @throws UserNotExistentException
     */
    @Test
    public void testDuplicateUserStorage() throws UserNotExistentException {
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
     * @exception UserAlreadyExistsException if the requested user is already there
     * @exception UserNotExistentException if the requested user is not there
     * @throws UserAlreadyExistsException
     * @throws UserNotExistentException
     */
    @Test(expected = UserNotExistentException.class) 
    public void testUserDeletion() throws UserAlreadyExistsException, UserNotExistentException {
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
     * @exception UserNotExistentException if the requested user is not there
     * @exception UserAlreadyExistsException if the requested user is already there
     * @throws UserNotExistentException
     * @throws UserAlreadyExistsException
     */
    @Test
    public void testUpdateOnUser() throws UserNotExistentException, UserAlreadyExistsException {
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
     * @exception UserNotExistentException if the requested user is not there
     */
    @Test(expected = UserNotExistentException.class)
    public void testUpdateOnNonExistentUser() throws UserNotExistentException {
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

        db.storeRole(role007);
        db.storeRole(role008);
        db.storeRole(role009);

        assertEquals(db.loadRole(role007.getId()), role007);
        assertEquals(db.loadRole(role008.getId()), role008);
        assertEquals(db.loadRole(role009.getId()), role009);
    }
    
    /**
     * Method for testing if it's possible to assign roles to users and vice-versa.
     * @exception RoleNotExistentException if the requested role is not there
     * @exception UserHasAlreadyRoleException if we want to assign a role to a user and the user has it already
     * @exception RoleHasAlreadyUserException if we want to assign a user to a role and the role has him already
     * @exception UserNotExistentException if the requested user is not there
     * @exception RoleNotExistentException if the requested role is not there
     * @throws UserHasAlreadyRoleException
     * @throws RoleHasAlreadyRoleException
     * @throws UserNotExistentException
     * @throws RoleNotExistentException 
     */
    @Test
    public void testRoleToUserAssignement() throws UserAlreadyExistsException, RoleNotExistentException, UserNotExistentException, RoleHasAlreadyUserException, UserHasAlreadyRoleException {
        final Role roletest = new Role();
        final User usertest = new User();
        usertest.setUsername("name");
        
        db.storeRole(roletest);
        db.addUser(usertest);
        
        db.addRoleToUser(usertest, roletest);
        
        assertTrue(db.loadUser("name").getRoles().contains(roletest));
    }

}
