package abstractbeansTesting;

import static org.junit.Assert.*;

import org.junit.Test;

import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;
import static org.hamcrest.CoreMatchers.instanceOf;
import abstractbeans.AbstractAction;
import abstractbeans.AbstractFinalStep;
import abstractbeans.AbstractItem;
import abstractbeans.AbstractMetaEntry;
import abstractbeans.AbstractMetaState;
import abstractbeans.AbstractStartStep;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;
import abstractbeans.AbstractWorkflow;

public class abstractbeansTest {
	
	@Test
	public void abstractActionTest() {
		Action a = new Action(1, "username", "test");
		assertThat(a, instanceOf(AbstractAction.class));
	}
	
	@Test
	public void abstractFinalStepTest() {
		FinalStep fs = new FinalStep();
		assertThat(fs, instanceOf(AbstractFinalStep.class));
	}
	@Test
	public void abstractItemTest () {
		Item i = new Item();
		assertThat(i, instanceOf(AbstractItem.class));
	}
	
	@Test
	public void abstractMetaEntryTest() {
		AbstractMetaEntry me = new AbstractMetaEntry();
		assertThat(me, instanceOf(AbstractMetaEntry.class));
	}
	
	@Test
	public void abstractMetaStateTest() {
		AbstractMetaState ms = AbstractMetaState.valueOf("INACTIVE");
		assertTrue(ms instanceof AbstractMetaState);
	}
	
	@Test
	public void abstractStartStepTest() {
		AbstractStartStep ss = new AbstractStartStep();
		assertThat(ss, instanceOf(AbstractStartStep.class));
	}
	
	@Test
	public void abstractStepTest() {
		AbstractStep s = new AbstractStep();
		assertThat(s, instanceOf(AbstractStep.class));
	}
	
	@Test
	public void abstractUserTest() {
		User u = new User();
		assertThat(u, instanceOf(AbstractUser.class));
	}
	
	@Test
	public void abstractWorfklowTest() {
		Workflow wf = new Workflow(0);
		assertThat(wf, instanceOf(AbstractWorkflow.class));
	}

}
