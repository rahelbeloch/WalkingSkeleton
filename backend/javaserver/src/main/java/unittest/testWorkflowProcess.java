package unittest;

import static org.junit.Assert.*;
import manager.ProcessManager;

import org.junit.Test;

import processors.StartTrigger;
import abstractbeans.AbstractMetaState;
import abstractbeans.AbstractStep;
import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;

public class testWorkflowProcess {

	
	public Workflow myWorkflow;
	public AbstractStep firstStep;
	
	@Test
	public void addItem() {
		init();
		
		Item item = new Item();
		myWorkflow.addItem(item);
		
		assertEquals(item, myWorkflow.getItem().get(0));
	}
	
	@Test
	public void startWorkflow() {
		init();
		ProcessManager manager = new ProcessManager();
		StartTrigger start = new StartTrigger(myWorkflow, manager);
		start.startWorkflow();
		Item item = (Item) myWorkflow.getItem().get(0);
		
		assertTrue(item.getStepState(firstStep.getId()) == AbstractMetaState.OPEN.toString());
	}

	@Test
	public void checkStateInaktive() {
		init();
		ProcessManager manager = new ProcessManager();
		StartTrigger start = new StartTrigger(myWorkflow, manager);
		start.startWorkflow();
		Item item = (Item) myWorkflow.getItem().get(0);
		
		assertTrue(item.getStepState(1000) == AbstractMetaState.INACTIVE.toString());
	}
	
	@Test
	public void handleFirstStep() {
		init();
		ProcessManager manager = new ProcessManager();
		StartTrigger start = new StartTrigger(myWorkflow, manager);
		start.startWorkflow();
		
		User benni = new User();
		benni.setName("benni");
		benni.setId(23);
		
		
		Item item = (Item) myWorkflow.getItem().get(0);
		manager.selectProcessor(firstStep, item, benni);	
		
		assertTrue(item.getStepState(firstStep.getId()) == AbstractMetaState.DONE.toString());
	}
	
	
	public void init(){
		myWorkflow= new Workflow(1);
		firstStep = new Action(0, 0, 0 + " Schritt");
		//adding steps in workflow
		myWorkflow.addStep(firstStep);
		myWorkflow.addStep(new Action(1*1000, 1*100, 1 + " Schritt"));
		myWorkflow.addStep(new FinalStep());
		myWorkflow.getStepByPos(2).setId(9999);

		//this method generates straight neighbors for steps in steplist
		myWorkflow.connectSteps();
	}

}
