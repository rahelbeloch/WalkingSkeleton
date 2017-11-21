package de.hsrm.testswt02.modelTesting;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Fork;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.RootElement;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;

/**
 * Class for Testing the models.
 *
 */
public class ModelTest {

    /**
    * method for testing Action: a new Action is initializated and checked.
    */
    @Test
    public void actionTest() {
        final Role role = new Role();
        role.setRolename("testrole");
        final ArrayList<String> roleList = new ArrayList<>();
        roleList.add(role.getRolename());
        final Action a = new Action(roleList, "description");
        assertThat(a, instanceOf(Action.class));
    }

    /**
     * method for testing FinalStep: a new FinalStep is initialized and checked.
     */
    @Test 
    public void finalStepTest() {
        final FinalStep fs = new FinalStep();
        assertThat(fs, instanceOf(FinalStep.class));
    }
    
    @Test
    public void testForkBranching() {
        final Fork fork = new Fork();
        final Step s1 = new Step();
        final Step s2 = new Step();
        fork.getNextSteps().add(s1);
        fork.getNextSteps().add(s2);
        
        assertEquals(fork.getFalseBranch(), fork.getNextSteps().get(1));
        assertEquals(fork.getTrueBranch(), fork.getNextSteps().get(0));
    }

    /**
     * method for testing Fork: a new Fork is initialized checked.
     */
    @Test
    public void testFork() {
        final Fork fork = new Fork();
        assertThat(fork, instanceOf(Fork.class));
    }
    
    /**
     * method for testing Item: a new Item is initialized and checked.
     */
    @Test
    public void itemTest() {
        final Item i = new Item();
        assertThat(i, instanceOf(Item.class));
    }

    /**
     * method for testing MetaEntry: a new MetaEntry is initialized and checked.
     */
    @Test
    public void metaEntryTest() {
        final MetaEntry me = new MetaEntry();
        assertThat(me, instanceOf(MetaEntry.class));
    }

    /**
     * method for testing MetaState: a new MetaState is initialized and checked.
     */
    @Test
    public void metaStateTest() {
        final MetaState ms = MetaState.valueOf("INACTIVE");
        assertTrue(ms instanceof MetaState);
    }

    /**
     * method for testing RootElement: a new RootElement is initialized and checked.
     */
    @Test
    public void rootElementTest() {
        final RootElement re = new RootElement();
        assertTrue(re instanceof RootElement);
    }

    /**
     * method for testing StartStep: a new StartStep is initialized and checked.
     */
    @Test
    public void startStepTest() {
        final Role role = new Role();
        role.setRolename("testrole");
        final ArrayList<String> roleList = new ArrayList<>();
        roleList.add(role.getRolename());
        final StartStep ss = new StartStep(roleList);
        assertThat(ss, instanceOf(StartStep.class));
    }

    /**
     * method for testing Step: a new Step is initialized and checked.
     */
    @Test
    public void stepTest() {
        final Step s = new Step();
        assertThat(s, instanceOf(Step.class));
    }

    /**
     * method for testing User: a new User is initialized and checked.
     */
    @Test
    public void userTest() {
        final User u = new User();
        assertThat(u, instanceOf(User.class));
    }

    /**
     * method for testing Workflow: a new Workflow is initialized and checked.
     */
    @Test
    public void worfklowTest() {
        final Workflow wf = new Workflow();
        assertThat(wf, instanceOf(Workflow.class));
    }
    
    /**
     * method for testing Workflow: a new Workflow is initialized and checked.
     */
    @Test
    public void roleTest() {
        final Role r = new Role();
        assertThat(r, instanceOf(Role.class));
    }

}
