package de.hsrm.testswt02.abstractbeansTesting;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;

import org.junit.Test;

public class AbstractBeansTest {
	
	@Test
	public void abstractActionTest() {
		Action a = new Action(1, "username", "test");
		assertThat(a, instanceOf(Action.class));
	}
	
	@Test
	public void abstractFinalStepTest() {
		FinalStep fs = new FinalStep();
		assertThat(fs, instanceOf(FinalStep.class));
	}
	@Test
	public void abstractItemTest () {
		Item i = new Item();
		assertThat(i, instanceOf(Item.class));
	}
	
	@Test
	public void abstractMetaEntryTest() {
		MetaEntry me = new MetaEntry();
		assertThat(me, instanceOf(MetaEntry.class));
	}
	
	@Test
	public void abstractMetaStateTest() {
		MetaState ms = MetaState.valueOf("INACTIVE");
		assertTrue(ms instanceof MetaState);
	}
	
	@Test
	public void abstractStartStepTest() {
		StartStep ss = new StartStep();
		assertThat(ss, instanceOf(StartStep.class));
	}
	
	@Test
	public void abstractStepTest() {
		Step s = new Step();
		assertThat(s, instanceOf(Step.class));
	}
	
	@Test
	public void abstractUserTest() {
		User u = new User();
		assertThat(u, instanceOf(User.class));
	}
	
	@Test
	public void abstractWorfklowTest() {
		Workflow wf = new Workflow(0);
		assertThat(wf, instanceOf(Workflow.class));
	}

}
