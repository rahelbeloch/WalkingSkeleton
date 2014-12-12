package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;

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

        final Workflow myWorkflow = new Workflow();

        assertNotEquals(myWorkflow, null);
    }

    /**
     * test the initialization of Action and belonging user.
     */
    @Test
    public void actionCheckUser() {
        final Action step = new Action("username", 0 + " Schritt");

        assertTrue(step.getUsername().equals("username"));
    }

    /**
     * test the initialization of Action and Actionname.
     */
    @Test
    public void actionCheckName() {
        final Action step = new Action("username", 0 + " Schritt");

        assertEquals(step.getDescription(), 0 + " Schritt");
    }

    /**
     * test add Steps into an existing workflow.
     */
    @Test
    public void addStep() {
        final Workflow myWorkflow = new Workflow();
        final Step step = new Action("username", 0 + " Schritt");
        myWorkflow.addStep(step);

        assertEquals(step, myWorkflow.getStepById(0));
    }

    /**
     * test right connection.
     */
    @Test
    public void connectSteps() {
        final Workflow myWorkflow = new Workflow();
        final Step firstStep = new Action("username", 0 + " Schritt");
        final Step secondStep = new Action("username", 1 + " Schritt");

        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);


        assertEquals(secondStep, firstStep.getNextSteps().get(0));
    }

    /**
     * test right connection FinalStep.
     */
    @Test
    public void connectFinalStep() {
        final Workflow myWorkflow = new Workflow();
        final Step firstStep = new Action("username", 0 + " Schritt");
        final Step secondStep = new Action("username", 1 + " Schritt");
        final Step finalStep = new FinalStep();

        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);
        myWorkflow.addStep(finalStep);


        assertTrue(firstStep.getNextSteps().get(0).getNextSteps().get(0) instanceof FinalStep);
    }
}
