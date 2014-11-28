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

public class ModelTests {

    @Test
    public void ActionTest() {
        Action a = new Action(0, "usernametest", "desctest");
        assertThat(a, instanceOf(Action.class));
    }

    @Test
    public void FinalStepTest() {
        FinalStep fs = new FinalStep();
        assertThat(fs, instanceOf(FinalStep.class));
    }

    @Test
    public void ItemTest() {
        Item i = new Item();
        assertThat(i, instanceOf(Item.class));
    }

    @Test
    public void MetaEntryTest() {
        MetaEntry me = new MetaEntry();
        assertThat(me, instanceOf(MetaEntry.class));
    }

    @Test
    public void MetaStateTest() {
        MetaState ms = MetaState.valueOf("INACTIVE");
        assertTrue(ms instanceof MetaState);
    }

    @Test
    public void RootElementTest() {
        RootElement re = new RootElement();
        assertTrue(re instanceof RootElement);
    }

    @Test
    public void RootElementListTest() {
        RootElementList re = new RootElementList();
        assertTrue(re instanceof RootElementList);
    }

    @Test
    public void StartStepTest() {
        StartStep ss = new StartStep("usernametest");
        assertThat(ss, instanceOf(StartStep.class));
    }

    @Test
    public void StepTest() {
        Step s = new Step();
        assertThat(s, instanceOf(Step.class));
    }

    @Test
    public void UserTest() {
        User u = new User();
        assertThat(u, instanceOf(User.class));
    }

    @Test
    public void WorfklowTest() {
        Workflow wf = new Workflow(0);
        assertThat(wf, instanceOf(Workflow.class));
    }

}
