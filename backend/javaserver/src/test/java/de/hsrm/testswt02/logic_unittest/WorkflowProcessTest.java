package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.ProcessManager;
import de.hsrm.swt02.businesslogic.exceptions.ItemNotForwardableException;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.businesslogic.exceptions.UserHasNoPermissionException;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;

import de.hsrm.swt02.persistence.exceptions.ItemNotExistentException;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.StorageFailedException;

import de.hsrm.swt02.persistence.exceptions.UserAlreadyExistsException;


/**
 * class tests the workflowProcess.
 *
 */
public class WorkflowProcessTest {

    private Workflow myWorkflow;
    private StartStep startStep;
    private Step firstStep;
    private Persistence persistence;
    private ProcessManager processManager;
    private User benni;

    /**
     * build workingset before testing.
<<<<<<< .mine
     * @throws StorageFailedException 
=======
     * @throws PersistenceException 
>>>>>>> .r886
     */
    @Before

    public void setUp() throws PersistenceException {

        final Injector i = Guice.createInjector(new SingleModule());
        processManager = i.getInstance(ProcessManager.class);
        persistence = i.getInstance(Persistence.class);
        
        benni = new User();
        benni.setUsername("benni");
        
        try {
            persistence.addUser(benni);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        }
        
        myWorkflow = new Workflow();
        startStep = new StartStep(benni.getUsername());
        firstStep = new Action(benni.getUsername(), 1 + " Schritt");
        // adding steps in workflow
        myWorkflow.addStep(startStep);
        myWorkflow.addStep(firstStep);

        myWorkflow.addStep(new FinalStep());
        // generates straight neighbors for steps in steplist
        

        persistence.storeWorkflow(myWorkflow);
        
    }

    /**
     * test add an Item in Workflow.
     */
    @Test
    public void addItem() {
        final Item item = new Item();
      //setting an id is definitely wrong, but here's no connection to the persistence (not via logic) therefore the ids have to be manually added
        item.setId("itemId");
        myWorkflow.addItem(item);
        assertEquals(item, myWorkflow.getItems().get(0));
    }

    /**
     * test start a workflow.
     * @throws PersistenceException 
     */
    @Test
    public void startWorkflow() throws PersistenceException {

        processManager.startWorkflow(myWorkflow, benni.getUsername());
        final Item item = (Item) myWorkflow.getItems().get(0);
        assertTrue(item.getStepState(firstStep.getId()) == MetaState.OPEN
                .toString());
    }
    
    /**
     * test start a workflow, without authorization.
     * @throws PersistenceException 
     */
    @Test
    public void startWorkflowWithoutAutohrization() throws PersistenceException {
        
        processManager.startWorkflow(myWorkflow, "ez");
        assertTrue(myWorkflow.getItems().size() == 0);
        
    }

    /**
     * test states in Item via stepId, it checks the "third" entry of the
     * workflow steplist. The first one is a startStep and isn't available in an
     * item. The second one has to be open because it the workflow was recently
     * started so the next one will be inactive.
     * @throws PersistenceException 
     */
    @Test
    public void checkStateInaktive() throws PersistenceException {
        String stepId;
        processManager.startWorkflow(myWorkflow, benni.getUsername());
        final Item item = (Item) myWorkflow.getItems().get(0);
        stepId = myWorkflow.getSteps().get(1).getId();
        assertTrue(item.getStepState(stepId).equals(MetaState.OPEN.toString()));
        stepId = myWorkflow.getSteps().get(2).getId();
        assertTrue(item.getStepState(stepId).equals(MetaState.INACTIVE.toString()));
    }

    /**
     * test handle an step in an Item.
     * @throws LogicException 
     */
    @Test
    public void handleFirstStep() throws LogicException {
        processManager.startWorkflow(myWorkflow, "benni");
        final Item item = (Item) myWorkflow.getItems().get(0);
        processManager.executeStep(firstStep, item, benni);
        processManager.executeStep(firstStep, item, benni);
        assertTrue(item.getStepState(firstStep.getId()) == MetaState.DONE
                .toString());
    }
    /**
     * 
     * @throws LogicException 
     * @throws ItemNotForwardableException .
     * @throws UserHasNoPermissionException .
     */
    @Test
    public void finishItemTest() throws LogicException {
        processManager.startWorkflow(myWorkflow, benni.getUsername());
        processManager.executeStep(myWorkflow.getStepByPos(1), myWorkflow.getItems().get(0), benni);
        processManager.executeStep(myWorkflow.getStepByPos(1), myWorkflow.getItems().get(0), benni);
        assertTrue(myWorkflow.getItems().get(0).isFinished() == true);
        
    }
}
