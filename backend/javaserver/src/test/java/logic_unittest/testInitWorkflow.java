package logic_unittest;

import static org.junit.Assert.*;

import org.junit.Test;

import abstractbeans.AbstractStep;
import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.Workflow;

public class testInitWorkflow {

	@Test
	public void initWorkflow() {
		Workflow myWorkflow = new Workflow(1);
		
		assertEquals(myWorkflow.getId(), 1);
	}
	
	@Test
	public void StepCheckId(){
		AbstractStep step = new Action(1, "username", 0 + " Schritt");
		
		assertTrue(step.getId()==1);
	}
	
	@Test
	public void ActionCheckUser(){
		Action step = new Action(1, "username", 0 + " Schritt");
		
		assertTrue(step.getUsername().equals("username"));
	}
	
	@Test
	public void ActionCheckName(){
		Action step = new Action(1, "username", 0 + " Schritt");
		
		assertEquals(step.getName(), 0 + " Schritt");
	}
	
	@Test
	public void addStep(){
		Workflow myWorkflow = new Workflow(1);
		AbstractStep step = new Action(0*1000, "username", 0 + " Schritt");
		myWorkflow.addStep(step);
		
		assertEquals(step, myWorkflow.getStepById(0));
	}
	
	@Test
	public void connectSteps(){
		Workflow myWorkflow = new Workflow(1);
		AbstractStep firstStep = new Action(0*1000, "username", 0 + " Schritt");
		AbstractStep secondStep = new Action(1*1000, "username", 1 + " Schritt");
		
		myWorkflow.addStep(firstStep);
		myWorkflow.addStep(secondStep);
				
		myWorkflow.connectSteps();
		
		assertEquals(secondStep, firstStep.getNextSteps().get(0));
	}
	
	@Test
	public void connectFinalStep(){
		Workflow myWorkflow = new Workflow(1);
		AbstractStep firstStep = new Action(0*1000, "username", 0 + " Schritt");
		AbstractStep secondStep = new Action(1*1000, "username", 1 + " Schritt");
		AbstractStep finalStep = new FinalStep();
		
		myWorkflow.addStep(firstStep);
		myWorkflow.addStep(secondStep);
		myWorkflow.addStep(finalStep);
		
		myWorkflow.connectSteps();
		
		assertTrue(firstStep.getNextSteps().get(0).getNextSteps().get(0) instanceof FinalStep);	
	}
}
