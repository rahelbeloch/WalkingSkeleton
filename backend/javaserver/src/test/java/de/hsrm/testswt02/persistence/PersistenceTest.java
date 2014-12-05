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
        final Workflow workflow007 = new Workflow(7);
        final Workflow workflow006 = new Workflow(6);
        final Workflow workflow005 = new Workflow(5);
        
        final int id5 = 5;
        final int id6 = 6;
        final int id7 = 7;

        db.storeWorkflow(workflow005);
        db.storeWorkflow(workflow006);
        db.storeWorkflow(workflow007);

        assertEquals(db.loadWorkflow(id5), workflow005);
        assertEquals(db.loadWorkflow(id6), workflow006);
        assertEquals(db.loadWorkflow(id7), workflow007);
    }

    /**
     * Class for testing the storage of multiple duplicate workflows.
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws WorkflowNotExistentException
     */
    @Test
    public void testDuplicateWorkflowStorage() throws WorkflowNotExistentException {
        final Workflow workflow001 = new Workflow(17);
        final Workflow workflow002 = new Workflow(17);
        
        final int id17 = 17;

        db.storeWorkflow(workflow001);
        db.storeWorkflow(workflow002);
        

        // current assumption: second workflow entry should have overwritten the
        // first one
        assertNotEquals(workflow001, db.loadWorkflow(id17));
        assertEquals(workflow002, db.loadWorkflow(id17));
    }

    /**
     * Method for testing if it's possible to delete a workflow.
     * @exception WorkflowNotExistentException if the requested workflow is not there
     * @throws WorkflowNotExistentException
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowDeletion() throws WorkflowNotExistentException {
        final Workflow wf001 = new Workflow(1);
        final Workflow wf002 = new Workflow(2);
        db.storeWorkflow(wf001);
        db.storeWorkflow(wf002);

        db.deleteWorkflow(1);

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
        final Workflow workflow007 = new Workflow(7);

        final StartStep step1 = new StartStep("username");
        final Action step2 = new Action(2, "username", "main action");
        final FinalStep step3 = new FinalStep();
        
        final int id7 = 7;

        workflow007.addStep(step1);
        workflow007.addStep(step2);
        workflow007.addStep(step3);

        db.storeWorkflow(workflow007);

        // step2, the action of workflow007, should be persistent
        // step1 and step2 cannot be tested because their ID is not yet
        // implemented
        assertEquals(step2, db.loadStep(2));

        // a workflows steps should be the same before and after storage
        assertEquals(workflow007.getSteps(), db.loadWorkflow(id7).getSteps());
        // no specific functions of a step can be tested, because db only
        // returns an AbstractWorkflow
        // assertEquals(workflow007.getStepByPos(2),
        // db.loadWorkflow(7).getStepByPos(2));

        // workflow instances should still be equal
        assertEquals(db.loadWorkflow(id7), workflow007);

        db.deleteWorkflow(id7);

        // a workflows steps must not be existent on databse anymore
        assertEquals(db.loadStep(2), null);
        
        // workflow must not be existent on database anymore, WorkflowNotExistentException is expected here!
        assertEquals(db.loadWorkflow(id7), null);
    }

    /**
     * Method for testing if it's possible to store an item.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    @Test
    public void testItemStorage() throws ItemNotExistentException, WorkflowNotExistentException {
        final Item item001 = new Item();
        final Item item002 = new Item();
        final Item item003 = new Item();
        final int id1 = 1;
        final int id2 = 2;
        final int id3 = 3;
        item001.setId(id1);
        item002.setId(id2);
        item003.setId(id3);

        db.storeItem(item001);
        db.storeItem(item002);
        db.storeItem(item003);

        assertEquals(item001, db.loadItem(id1));
        assertEquals(item002, db.loadItem(id2));
        assertEquals(item003, db.loadItem(id3));
    }

    /**
     * Method for testing if it's possible to store multiple items of the same id.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    @Test
    public void testDuplicateItemStorage() throws ItemNotExistentException, WorkflowNotExistentException {
        final Item item001 = new Item();
        final Item item002 = new Item();
        final int id17 = 17;
        item001.setId(id17);
        item002.setId(id17);

        db.storeItem(item001);
        db.storeItem(item002);

        // current assumption: second item entry should have overwritten the
        // first one
        assertNotEquals(item001, db.loadItem(id17));
        assertEquals(item002, db.loadItem(id17));
    }

    /**
     * Method for testing if it's possible to store an item with a few metadata entries.
     * @exception ItemNotExistentException if the requested item is not there
     * @throws ItemNotExistentException
     */
    @Test(expected = ItemNotExistentException.class)
    public void testItemStorageIncludingMetaData() throws ItemNotExistentException, WorkflowNotExistentException {
        final Item item001 = new Item();
        item001.setId(1);
        item001.set("key1", "group1", "value1");
        item001.set("key2", "group2", "value2");

        db.storeItem(item001);

        item001.set("key1", "group1", "value1");
        item001.set("key3", "group1", "value1");

        db.storeItem(item001);

        // MetaEntries should be persistent
        assertEquals(item001.getMetadata().get(0), db.loadMetaEntry("key1"));
        assertEquals(item001.getMetadata().get(1), db.loadMetaEntry("key2"));

        // an items metaData should be the same before and after storage
        assertEquals(item001.getMetadata(), db.loadItem(1).getMetadata());
        assertEquals(item001.getMetadata().get(0).getValue(),
                db.loadMetaEntry("key1").getValue());

        // item instances should still be the same
        assertEquals(db.loadItem(1), item001);

        db.deleteItem(1);

        // an items metaData must not be existent anymore
        assertEquals(db.loadMetaEntry("key1"), null);
        
        // item must not be existent anymore, ItemNotExistentException is expected here!
        //assertEquals(db.loadItem(1), null);
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
        final int id17 = 17;
        final int id71 = 71;
        user001.setId(id71);
        db.addUser(user001);

        assertEquals(user001, db.loadUser(user001.getUsername()));
        assertEquals(user001.getId(), db.loadUser(user001.getUsername())
                .getId());

        // modification on users ID
        user001.setId(id17);

        // TODO:
        // assertNotEquals(user001, db.loadUser(user001.getName()));
        // assertNotEquals(user001.getId(),
        // db.loadUser(user001.getName()).getId());

        // update on existing user
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
        final Role role007 = new Role(7);
        final Role role008 = new Role(8);
        final Role role009 = new Role(9);
        
        final int id7 = 7;
        final int id8 = 8;
        final int id9 = 9;

        db.storeRole(role007);
        db.storeRole(role008);
        db.storeRole(role009);

        assertEquals(db.loadRole(id7), role007);
        assertEquals(db.loadRole(id8), role008);
        assertEquals(db.loadRole(id9), role009);
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
        final Role roletest = new Role(17);
        final User usertest = new User();
        usertest.setId(71);
        
        db.storeRole(roletest);
        db.addUser(usertest);
        
        db.addUserToRole(usertest, roletest);
        
        System.out.println(usertest.getRoles().get(0).getId());
        System.out.println(roletest.getUsers().get(0).getId());
        
        assertTrue(db.loadRole(17).getUsers().contains(usertest));
    }

}
