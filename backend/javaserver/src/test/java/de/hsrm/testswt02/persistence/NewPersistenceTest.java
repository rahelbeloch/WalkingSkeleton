package de.hsrm.testswt02.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
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
public class NewPersistenceTest {

    // Dependency Injection
    Injector inj = Guice.createInjector(new SingleModule());
    Persistence db = inj.getInstance(Persistence.class);
    
    /**
     * configurate Logger in order to get Logging output
     */
    @BeforeClass
    public static void setup() {
        LogConfigurator.setup();
    }
    
//    @Test
//    public void testIdAllocation() throws PersistenceException {
//        final Workflow workflow1 = new Workflow();
//        final Workflow workflow2 = new Workflow();
//        
//        String workflow1Id = db.storeWorkflow(workflow1);
//        String workflow2Id = db.storeWorkflow(workflow2);
//        
//        final StartStep step1001 = new StartStep();
//        final Action step1002 = new Action();
//        final FinalStep step1003 = new FinalStep();
//        workflow1.addStep(step1001);
//        workflow1.addStep(step1002);
//        workflow1.addStep(step1003);
//        
//        final StartStep step2001 = new StartStep();
//        final Action step2002 = new Action();
//        final FinalStep step2003 = new FinalStep();
//        workflow2.addStep(step2001);
//        workflow2.addStep(step2002);
//        workflow2.addStep(step2003);
//        
//        final Item item1001 = new Item();
//        final Item item1002 = new Item();
//        item1001.setWorkflowId(workflow1Id);
//        item1002.setWorkflowId(workflow1Id);
//        workflow1.addItem(item1001);
//        workflow1.addItem(item1002);
//        
//        final Item item2001 = new Item();
//        final Item item2002 = new Item();
//        item2001.setWorkflowId(workflow2Id);
//        item2002.setWorkflowId(workflow2Id);
//        workflow2.addItem(item2001);
//        workflow2.addItem(item2002);
//        
//        workflow1Id = db.storeWorkflow(workflow1);
//        workflow2Id = db.storeWorkflow(workflow2);
//        
//        assertEquals(db.loadWorkflow(workflow1Id).getId(), "1");
//        assertEquals(db.loadWorkflow(workflow2Id).getId(), "2");
//        
//        assertEquals(db.loadWorkflow(workflow1Id).getItems(), workflow1.getItems());
//        assertEquals(db.loadWorkflow(workflow1Id).getSteps(), workflow1.getSteps());
//        assertEquals(db.loadWorkflow(workflow1Id).getItems(), workflow1.getItems());
//        assertEquals(db.loadWorkflow(workflow1Id).getSteps(), workflow1.getSteps());
//        
//        assertEquals(db.loadWorkflow(workflow1Id).getStepById("1001"), step1001);
//        assertEquals(db.loadWorkflow(workflow1Id).getStepById("1003"), step1002);
//        assertEquals(db.loadWorkflow(workflow1Id).getStepById("1003"), step1003);        
//        assertEquals(db.loadWorkflow(workflow2Id).getStepById("2001"), step1001);
//        assertEquals(db.loadWorkflow(workflow2Id).getStepById("2002"), step1002);
//        assertEquals(db.loadWorkflow(workflow2Id).getStepById("2003"), step1003);
//        assertEquals(db.loadItem("1001"), item1001);
//        assertEquals(db.loadItem("1002"), item1002);
//        assertEquals(db.loadItem("2001"), item2001);
//        assertEquals(db.loadItem("2002"), item2002);
//        
//    }
    
    @Test
    public void testWorkflowStorage() throws PersistenceException {
        final Workflow workflow1 = new Workflow();
        final String workflowId = db.storeWorkflow(workflow1);
        assertEquals(db.loadWorkflow(workflowId), workflow1);
    }
    
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
    
    @Test
    public void testUserStorage() throws PersistenceException {
        final User user007 = new User();
        final String username = "bond";
        user007.setUsername(username);
        db.storeUser(user007);
        
        assertEquals(db.loadUser(username), user007);
    }
    
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
    
//    /**
//     * Method for testing if it's possible to update an User.
//     * @throws UserNotExistentException
//     * @throws UserAlreadyExistsException
//     * @throws PersistenceException 
//     */
//    @Test(expected = UserNotExistentException.class)
//    public void testUpdateOnUser() throws PersistenceException {
//        final User user001 = new User();
//        user001.setUsername("1");
//        db.storeUser(user001);
//
//        assertEquals(user001, db.loadUser(user001.getUsername()));
//
//        // change a users username
//       
//        user001.setUsername("2");
//        db.storeUser(user001);
//
//        assertEquals(user001, db.loadUser(user001.getUsername()));
//        
//        System.out.println(db.loadAllUsers().size());
//        
//        assertEquals(1, db.loadAllUsers().size());
//        assertNotEquals(db.loadUser("1"), user001);
//    }

        
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
        assertEquals(db.loadWorkflow(workflowId).getItems(), workflow1.getItems());
        
        workflow1.removeItem(item1001.getId());
        db.storeWorkflow(workflow1);
        
        assertEquals(db.loadWorkflow(workflowId), workflow1);
        assertEquals(db.loadWorkflow(workflowId).getItems(), workflow1.getItems());
    }
    
}
