package de.hsrm.testswt02.logic_unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import de.hsrm.swt02.manager.ProcessManager;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaState;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.moduledi.SingleModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.processors.StartTrigger;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class WorkflowProcessTest {

	private Workflow myWorkflow;
	private Step firstStep;
	private Persistence persistence;
	private ProcessManager processManager;
	
	@Before
	public void setUp() {
		Injector i = Guice.createInjector(new SingleModule());
		processManager = i.getInstance(ProcessManager.class);
		persistence = i.getInstance(Persistence.class);
		myWorkflow= new Workflow(1);
		firstStep = new Action(0, "username", 0 + " Schritt");
		//adding steps in workflow
		myWorkflow.addStep(firstStep);
		myWorkflow.addStep(new Action(1*1000, "username", 1 + " Schritt"));
		myWorkflow.addStep(new FinalStep());
		myWorkflow.getStepByPos(2).setId(9999);
		//generates straight neighbors for steps in steplist
		myWorkflow.connectSteps();
	}
	
	@After
	public void tearDown() {
		processManager.stopBroker();
	}
	
	@Test
	public void addItem() {	
		Item item = new Item();
		myWorkflow.addItem(item);
		assertEquals(item, myWorkflow.getItems().get(0));
	}
	
	@Test
	public void startWorkflow() {
		StartTrigger start = new StartTrigger(myWorkflow, processManager, persistence);
		start.startWorkflow();
		Item item = (Item) myWorkflow.getItems().get(0);
		assertTrue(item.getStepState(firstStep.getId()) == MetaState.OPEN.toString());
	}

	@Test
	public void checkStateInaktive() {
		StartTrigger start = new StartTrigger(myWorkflow, processManager, persistence);
		start.startWorkflow();
		Item item = (Item) myWorkflow.getItems().get(0);
		assertTrue(item.getStepState(1000) == MetaState.INACTIVE.toString());
	}
	
	@Test
	public void handleFirstStep() {
		StartTrigger start = new StartTrigger(myWorkflow, processManager, persistence);
		start.startWorkflow();
		User benni = new User();
		benni.setUsername("benni");
		benni.setId(23);
		Item item = (Item) myWorkflow.getItems().get(0);
		processManager.selectProcessor(firstStep, item, benni);	
		assertTrue(item.getStepState(firstStep.getId()) == MetaState.DONE.toString());
	}
}
