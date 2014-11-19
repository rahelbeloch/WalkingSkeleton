package consoleTest;

import beans.Action;
import beans.FinalStep;
import beans.Step;
import beans.User;
import beans.Workflow;
import processor.StartTrigger;


public class Logic {
	
	public static void main(String args[]){
		Workflow myWorkflow = createWorkflow();
		
		StartTrigger start = new StartTrigger(myWorkflow);
		
		User max = new User();
		
		
	}
	
	public static Workflow createWorkflow(){
		Workflow myWorkflow= new Workflow();
		
		Step [] steps = new Step[4];
		
		//create Steps
		for(int i=0; i<3; i++ ){
			steps[i] = new Action();
		}
		steps[3]= new FinalStep();
		//connecting steps
		for(int i=0; i<3; i++){
			steps[i].setNextStep(steps[i+1]);
		}
		
		myWorkflow.setSteps(steps);
		
		return myWorkflow;
	}
}
