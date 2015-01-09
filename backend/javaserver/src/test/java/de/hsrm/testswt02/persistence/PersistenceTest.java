package de.hsrm.testswt02.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;
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
    
    /**
     * configurate Logger in order to get Logging output.
     */
    @BeforeClass
    public static void setup() {
        LogConfigurator.setup();
    }
    
    /**
     * store a workflow and check persistence functionality.
     * @exception PersistenceException indicates errors in storage methods
     * @throws PersistenceException 
     */
    @Test
    public void testWorkflowStorage() throws PersistenceException {
        final Workflow workflow1 = new Workflow();
        final String workflowId = db.storeWorkflow(workflow1);
        assertEquals(db.loadWorkflow(workflowId), workflow1);
    }
    
    /**
     * delete a workflow and check persistence functionality.
     * @exception PersistenceException indicates errors in storage methods
     * @throws PersistenceException
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowDeletion() throws PersistenceException {
        final Workflow workflow1 = new Workflow();
        final Workflow workflow2 = new Workflow();
        
        final String id1 = db.storeWorkflow(workflow1);
        final String id2 = db.storeWorkflow(workflow2);
        
        assertEquals(db.loadWorkflow(id1), workflow1);
        assertEquals(db.loadWorkflow(id2), workflow2);
        
        db.deleteWorkflow(id2);

        assertEquals(db.loadWorkflow(id1), workflow1);
        assertEquals(db.loadWorkflow(id2), workflow2);
    }
    
    /**
     * store a user and check persistence functionality.
     * @exception PersistenceException indicates errors in storage methods
     * @throws PersistenceException 
     */
    @Test
    public void testUserStorage() throws PersistenceException {
        final User user007 = new User();
        final String username = "bond";
        user007.setUsername(username);
        db.storeUser(user007);
        
        assertEquals(db.loadUser(username), user007);
    }
    
    /**
     * delete a user and check persistence functionality.
     * @exception PersistenceException indicates errors in storage methods
     * @throws PersistenceException 
     */
    @Test(expected = UserNotExistentException.class)
    public void testUserDeletion() throws PersistenceException {
        final User user001 = new User();
        user001.setUsername("hinz");
        final User user002 = new User();
        user002.setUsername("kunz");
        
        db.storeUser(user001);
        db.storeUser(user002);
        
        assertEquals(db.loadUser("hinz"), user001);
        assertEquals(db.loadUser("kunz"), user002);
        
        db.deleteUser("hinz");

        assertEquals(db.loadUser("kunz"), user002);
        assertEquals(db.loadUser("hinz"), user001);
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
            db.storeUser(user001);
            db.storeUser(user002);
        } catch (UserAlreadyExistsException e) {
            // Exception is caught manually in order to test if
            // addUser(user002) was rejected
            e.printStackTrace();
        }
        assertEquals(user001, db.loadUser("17"));
        assertEquals(db.loadAllUsers().size(), 1); //only one user should be existent
    }
        
    /**
     * store items and check persistence functionality.
     * @exception PersistenceException inidcates errors in storage methods
     * @throws PersistenceException 
     */
    @Test
    public void testItemStorage() throws PersistenceException {
        final Workflow workflow1 = new Workflow();
        final String workflowId = db.storeWorkflow(workflow1);
        
        assertEquals(db.loadWorkflow(workflowId).getItems().size(), 0);
        assertEquals(db.loadWorkflow(workflowId), workflow1);
        
        final Item item001 = new Item();
        final Item item002 = new Item();
        final Item item003 = new Item();
        workflow1.addItem(item001);
        workflow1.addItem(item002);
        workflow1.addItem(item003);
        
        db.storeWorkflow(workflow1);
        
        assertEquals(db.loadWorkflow(workflowId), workflow1);
        assertEquals(db.loadWorkflow(workflowId).getItems(), workflow1.getItems());
    }
    
    /**
     * delete item and check persistence functionality.
     * @exception PersistenceException inidcates errors in storage methods
     * @throws PersistenceException 
     */
    @Test
    public void testItemDeletion() throws PersistenceException {
        final Workflow workflow1 = new Workflow();
        String workflowId = db.storeWorkflow(workflow1);
        
        final Item item1001 = new Item();
        final Item item1002 = new Item();
        workflow1.addItem(item1001);
        workflow1.addItem(item1002);
        workflowId = db.storeWorkflow(workflow1);
        
        assertEquals(db.loadWorkflow(workflowId), workflow1);
//      assertEquals(db.loadWorkflow(workflowId).getItems(), workflow1.getItems());
        
        workflow1.removeItem(item1001.getId());
        db.storeWorkflow(workflow1);
        
        assertEquals(db.loadWorkflow(workflowId), workflow1);
        assertEquals(db.loadWorkflow(workflowId).getItems(), workflow1.getItems());
    }
    
    @Test
    public void testContinuityOfWorkflowIds() throws PersistenceException {
        final Workflow wf1 = new Workflow();
        final Workflow wf2 = new Workflow();
        
        final String id1 = db.storeWorkflow(wf1);
        db.deleteWorkflow(id1);
        
        final String id2 = db.storeWorkflow(wf2);
        
        assertEquals(db.loadWorkflow(id2).getId(), id1);
    }
    
    @Test
    public void testUserOverwriting() throws PersistenceException {
        User user = new User();
        user.setUsername("testuser");
        
        db.storeUser(user);
        db.storeUser(db.loadUser(user.getUsername()));
        assertEquals(db.loadUser(user.getUsername()), user);
    }
}
