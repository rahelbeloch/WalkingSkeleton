package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;

import org.junit.Test;

/**
 * This Class tests the initialization of a workflow with Steps.
 *
 */
public class InitWorkflowTest {

    /**
     * test the initialization of a workflow.
     */
    @Test
    public void initWorkflow() {
        
        final Workflow myWorkflow = new Workflow(1);

        assertEquals(myWorkflow.getId(), 1);
    }
    
    /**
     * test the initialization of a step.
     */
    @Test
    public void stepCheckId() {
        final Step step = new Action(1, "username", 0 + " Schritt");

        assertTrue(step.getId() == 1);
    }

    /**
     * test the initialization of Action and belonging user.
     */
    @Test
    public void actionCheckUser() {
        final Action step = new Action(1, "username", 0 + " Schritt");

        assertTrue(step.getUsername().equals("username"));
    }

    /**
     * test the initialization of Action and Actionname.
     */
    @Test
    public void actionCheckName() {
        final Action step = new Action(1, "username", 0 + " Schritt");

        assertEquals(step.getDescription(), 0 + " Schritt");
    }

    /**
     * test add Steps into an existing workflow.
     */
    @Test
    public void addStep() {
        final Workflow myWorkflow = new Workflow(1);
        final Step step = new Action(0 * 1000, "username", 0 + " Schritt");
        myWorkflow.addStep(step);

        assertEquals(step, myWorkflow.getStepById(0));
    }

    /**
     * test right connection.
     */
    @Test
    public void connectSteps() {
        final Workflow myWorkflow = new Workflow(1);
        final Step firstStep = new Action(0 * 1000, "username", 0 + " Schritt");
        final Step secondStep = new Action(1 * 1000, "username", 1 + " Schritt");

        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);

        myWorkflow.connectSteps();

        assertEquals(secondStep, firstStep.getNextSteps().get(0));
    }

    /**
     * test right connection FinalStep.
     */
    @Test
    public void connectFinalStep() {
        final Workflow myWorkflow = new Workflow(1);
        final Step firstStep = new Action(0 * 1000, "username", 0 + " Schritt");
        final Step secondStep = new Action(1 * 1000, "username", 1 + " Schritt");
        final Step finalStep = new FinalStep();

        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);
        myWorkflow.addStep(finalStep);

        myWorkflow.connectSteps();

        assertTrue(firstStep.getNextSteps().get(0).getNextSteps().get(0) instanceof FinalStep);
    }
}
