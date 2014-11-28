package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;

import org.junit.Test;

public class InitWorkflowTest {

    @Test
    public void initWorkflow() {
        Workflow myWorkflow = new Workflow(1);

        assertEquals(myWorkflow.getId(), 1);
    }

    @Test
    public void StepCheckId() {
        Step step = new Action(1, "username", 0 + " Schritt");

        assertTrue(step.getId() == 1);
    }

    @Test
    public void ActionCheckUser() {
        Action step = new Action(1, "username", 0 + " Schritt");

        assertTrue(step.getUsername().equals("username"));
    }

    @Test
    public void ActionCheckName() {
        Action step = new Action(1, "username", 0 + " Schritt");

        assertEquals(step.getDescription(), 0 + " Schritt");
    }

    @Test
    public void addStep() {
        Workflow myWorkflow = new Workflow(1);
        Step step = new Action(0 * 1000, "username", 0 + " Schritt");
        myWorkflow.addStep(step);

        assertEquals(step, myWorkflow.getStepById(0));
    }

    @Test
    public void connectSteps() {
        Workflow myWorkflow = new Workflow(1);
        Step firstStep = new Action(0 * 1000, "username", 0 + " Schritt");
        Step secondStep = new Action(1 * 1000, "username", 1 + " Schritt");

        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);

        myWorkflow.connectSteps();

        assertEquals(secondStep, firstStep.getNextSteps().get(0));
    }

    @Test
    public void connectFinalStep() {
        Workflow myWorkflow = new Workflow(1);
        Step firstStep = new Action(0 * 1000, "username", 0 + " Schritt");
        Step secondStep = new Action(1 * 1000, "username", 1 + " Schritt");
        Step finalStep = new FinalStep();

        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);
        myWorkflow.addStep(finalStep);

        myWorkflow.connectSteps();

        assertTrue(firstStep.getNextSteps().get(0).getNextSteps().get(0) instanceof FinalStep);
    }
}
