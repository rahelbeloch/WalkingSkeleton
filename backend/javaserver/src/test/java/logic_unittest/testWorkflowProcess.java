package logic_unittest;

import static org.junit.Assert.*;
import manager.ProcessManager;
import moduledi.SingleModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import persistence.Persistence;
import processors.StartTrigger;
import abstractbeans.AbstractMetaState;
import abstractbeans.AbstractStep;
import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;

public class testWorkflowProcess {

	private Workflow myWorkflow;
	private AbstractStep firstStep;
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
		assertEquals(item, myWorkflow.getItem().get(0));
	}
	
	@Test
	public void startWorkflow() {
		StartTrigger start = new StartTrigger(myWorkflow, processManager, persistence);
		start.startWorkflow();
		Item item = (Item) myWorkflow.getItem().get(0);
		assertTrue(item.getStepState(firstStep.getId()) == AbstractMetaState.OPEN.toString());
	}

	@Test
	public void checkStateInaktive() {
		StartTrigger start = new StartTrigger(myWorkflow, processManager, persistence);
		start.startWorkflow();
		Item item = (Item) myWorkflow.getItem().get(0);
		assertTrue(item.getStepState(1000) == AbstractMetaState.INACTIVE.toString());
	}
	
	@Test
	public void handleFirstStep() {
		StartTrigger start = new StartTrigger(myWorkflow, processManager, persistence);
		start.startWorkflow();
		User benni = new User();
		benni.setName("benni");
		benni.setId(23);
		Item item = (Item) myWorkflow.getItem().get(0);
		processManager.selectProcessor(firstStep, item, benni);	
		assertTrue(item.getStepState(firstStep.getId()) == AbstractMetaState.DONE.toString());
	}
}
