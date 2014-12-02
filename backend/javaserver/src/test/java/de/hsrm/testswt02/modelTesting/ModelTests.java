package de.hsrm.testswt02.modelTesting;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.RootElement;
import de.hsrm.swt02.model.RootElementList;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;

import org.junit.Test;

/**
 * Class for Testing the models.
 *
 */
public class ModelTests {

    /**
    * method for testing Action: a new Action is initializated and checked.
    */
    @Test
    public void actionTest() {
        final Action a = new Action(0, "usernametest", "desctest");
        assertThat(a, instanceOf(Action.class));
    }

    /**
     * method for testing FinalStep: a new FinalStep is initializated and checked.
     */
    @Test
    public void finalStepTest() {
        final FinalStep fs = new FinalStep();
        assertThat(fs, instanceOf(FinalStep.class));
    }

    /**
     * method for testing Item: a new Item is initializated and checked.
     */
    @Test
    public void itemTest() {
        final Item i = new Item();
        assertThat(i, instanceOf(Item.class));
    }

    /**
     * method for testing MetaEntry: a new MetaEntry is initializated and checked.
     */
    @Test
    public void metaEntryTest() {
        final MetaEntry me = new MetaEntry();
        assertThat(me, instanceOf(MetaEntry.class));
    }

    /**
     * method for testing MetaState: a new MetaState is initializated and checked.
     */
    @Test
    public void metaStateTest() {
        final MetaState ms = MetaState.valueOf("INACTIVE");
        assertTrue(ms instanceof MetaState);
    }

    /**
     * method for testing RootElement: a new RootElement is initializated and checked.
     */
    @Test
    public void rootElementTest() {
        final RootElement re = new RootElement();
        assertTrue(re instanceof RootElement);
    }

    /**
     * method for testing RootElementList: a new RootElementList is initializated and checked.
     */
    @Test
    public void rootElementListTest() {
        final RootElementList re = new RootElementList();
        assertTrue(re instanceof RootElementList);
    }

    /**
     * method for testing StartStep: a new StartStep is initializated and checked.
     */
    @Test
    public void startStepTest() {
        final StartStep ss = new StartStep("usernametest");
        assertThat(ss, instanceOf(StartStep.class));
    }

    /**
     * method for testing Step: a new Step is initializated and checked.
     */
    @Test
    public void stepTest() {
        final Step s = new Step();
        assertThat(s, instanceOf(Step.class));
    }

    /**
     * method for testing User: a new User is initializated and checked.
     */
    @Test
    public void userTest() {
        final User u = new User();
        assertThat(u, instanceOf(User.class));
    }

    /**
     * method for testing Workflow: a new Workflow is initializated and checked.
     */
    @Test
    public void worfklowTest() {
        final Workflow wf = new Workflow(0);
        assertThat(wf, instanceOf(Workflow.class));
    }

}
