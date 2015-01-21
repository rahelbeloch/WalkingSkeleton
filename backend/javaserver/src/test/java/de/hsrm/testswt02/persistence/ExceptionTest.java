package de.hsrm.testswt02.persistence;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.logging.LogConfigurator;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StepNotExistentException;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

/**
 * Class for testing the persistence functions.
 *
 */
public class ExceptionTest {

    static Persistence db;
    
    /**
     * configurate Logger in order to get Logging output.
     */
    @BeforeClass
    public static void setup() {
        LogConfigurator.setup();
        db = ConstructionFactory.getTestInstance().getLogic().getPersistence();
    }
    
    /**
     * Tests if loading a non existent workflow will throw an Exception.
     * @throws PersistenceException 
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowNotExistenException() throws PersistenceException {
        db.loadWorkflow("17");
    }
    
    /**
     * Tests if loading a non existent item will throw an exception.
     * @throws PersistenceException 
     */
    @Test(expected = WorkflowNotExistentException.class)
    public void testWorkflowNotExistenExceptionOnItemsParentWorkflow() throws PersistenceException {
        db.loadItem("4711");
    }
    
    /**
     * Tests if loading a non existent user will throw an exception.
     * @throws PersistenceException 
     */
    @Test(expected = UserNotExistentException.class)
    public void testUserNotExistentException() throws PersistenceException {
        db.loadUser("JohnDoe");
    }
    
    /**
     * Tests if loading a nonexistent step will throw an exception.
     * @throws PersistenceException 
     */
    @Test(expected = StepNotExistentException.class)
    public void testStepNotExistentException() throws PersistenceException {
        final Workflow wf = new Workflow();
        final Item item = new Item();
        wf.addItem(item);
        final String wfId = db.storeWorkflow(wf);
        final String itemId = db.loadWorkflow(wfId).getItemByPos(0).getId();
        db.loadStep(itemId, "0815");
    }
    

}
