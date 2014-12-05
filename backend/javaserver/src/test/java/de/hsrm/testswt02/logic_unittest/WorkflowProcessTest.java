package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.businesslogic.ProcessManager;
import de.hsrm.swt02.businesslogic.processors.StartTrigger;
import de.hsrm.swt02.constructionfactory.SingleModule;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.Persistence;

/**
 * class tests the workflowProcess.
 *
 */
public class WorkflowProcessTest {

    private Workflow myWorkflow;
    private Step firstStep;
    private Persistence persistence;
    private ProcessManager processManager;

    /**
     * build workingset before testing.
     */
    @Before
    public void setUp() {
        final Injector i = Guice.createInjector(new SingleModule());
        final int c1 = 1000;
        final int c2 = 9999;
        processManager = i.getInstance(ProcessManager.class);
        persistence = i.getInstance(Persistence.class);
        myWorkflow = new Workflow(1);
        firstStep = new Action(0, "username", 0 + " Schritt");
        // adding steps in workflow
        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(new Action(1 * c1, "username", 1 + " Schritt"));
        myWorkflow.addStep(new FinalStep());
        myWorkflow.getStepByPos(2).setId(c2);
        // generates straight neighbors for steps in steplist
        myWorkflow.connectSteps();
    }

    /**
     * test add an Item in Workflow.
     */
    @Test
    public void addItem() {
        final Item item = new Item();
        myWorkflow.addItem(item);
        assertEquals(item, myWorkflow.getItems().get(0));
    }

    /**
     * test start a workflow.
     */
    @Test
    public void startWorkflow() {
        final StartTrigger start = new StartTrigger(myWorkflow, processManager,
                persistence);
        start.startWorkflow();
        final Item item = (Item) myWorkflow.getItems().get(0);
        assertTrue(item.getStepState(firstStep.getId()) == MetaState.OPEN
                .toString());
    }

    /**
     * test states in Item.
     */
    @Test
    public void checkStateInaktive() {
        final int c = 1000;
        final StartTrigger start = new StartTrigger(myWorkflow, processManager,
                persistence);
        start.startWorkflow();
        final Item item = (Item) myWorkflow.getItems().get(0);
        assertTrue(item.getStepState(c) == MetaState.INACTIVE.toString());
    }

    /**
     * test handle an step in an Item.
     */
    @Test
    public void handleFirstStep() {
        final StartTrigger start = new StartTrigger(myWorkflow, processManager,
                persistence);
        start.startWorkflow();
        final User benni = new User();
        benni.setUsername("benni");
        final int c = 23;
        benni.setId(c);
        final Item item = (Item) myWorkflow.getItems().get(0);
        processManager.selectProcessor(firstStep, item, benni);
        processManager.selectProcessor(firstStep, item, benni);
        assertTrue(item.getStepState(firstStep.getId()) == MetaState.DONE
                .toString());
    }
}
