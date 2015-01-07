package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
        ArrayList<String> roles = new ArrayList<String>();
        roles.add("role");
               
        final Action step = new Action(roles, 0 + " Schritt");

        assertTrue(step.getRoleIDs().get(0).equals("role"));
    }

    /**
     * test the initialization of Action and Actionname.
     */
    @Test
    public void actionCheckRole() {
        ArrayList<String> roles = new ArrayList<String>();
        roles.add("role");
        
        final Action step = new Action(roles, 0 + " Schritt");

        assertEquals(step.getDescription(), 0 + " Schritt");
    }

    /**
     * test add Steps into an existing workflow.
     */
    @Test
    public void addStep() {
        final Workflow myWorkflow = new Workflow();
        ArrayList<String> roles = new ArrayList<String>();
        roles.add("role");
        
        final Step step = new Action(roles, 0 + " Schritt");
        step.setId("XXX"); //This is not possible in the actual run, because ids are given in the persistence, but in this test there isn't a connection to the persistence
        myWorkflow.addStep(step);

        assertEquals(step, myWorkflow.getStepById(step.getId()));
    }

    /**
     * test right connection.
     */
    @Test
    public void connectSteps() {
        final Workflow myWorkflow = new Workflow();
        ArrayList<String> roles = new ArrayList<String>();
        roles.add("role");
        
        final Step firstStep = new Action(roles, 0 + " Schritt");
        //setting an id is definitely wrong, but here's no connection to the persistence therefore the ids have to be manually added
        firstStep.setId("1");
        final Step secondStep = new Action(roles, 1 + " Schritt");
        secondStep.setId("2");
        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);

        System.out.println(secondStep);
        System.out.println(firstStep.getNextSteps().get(0));

        assertEquals(secondStep, firstStep.getNextSteps().get(0));
    }

    /**
     * test right connection FinalStep.
     */
    @Test
    public void connectFinalStep() {
        final Workflow myWorkflow = new Workflow();
        ArrayList<String> roles = new ArrayList<String>();
        roles.add("role");
        
        final Step firstStep = new Action(roles, 0 + " Schritt");
        final Step secondStep = new Action(roles, 1 + " Schritt");
        final Step finalStep = new FinalStep();

        myWorkflow.addStep(firstStep);
        myWorkflow.addStep(secondStep);
        myWorkflow.addStep(finalStep);


        assertTrue(firstStep.getNextSteps().get(0).getNextSteps().get(0) instanceof FinalStep);
    }
}
