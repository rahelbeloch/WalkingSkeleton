package consoleTest;


import processors.StartTrigger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.Item;



import abstractbeans.AbstractItem;
import abstractbeans.AbstractMetaEntry;
import abstractbeans.AbstractStep;
import backingbeans.User;
import backingbeans.Workflow;
import manager.ProcessManager;


public class Logic {
	
	public static void main(String args[]){
		
		Workflow myWorkflow = createWorkflow();
		
		//Output of all steps in a workflow
		for (AbstractStep s : myWorkflow.getStep()){
			System.out.println(s.getId());
		}
		
		//This object will be assigned to a specific user who can execute the workflow (startWorkflow)
		StartTrigger start = new StartTrigger(myWorkflow);
		start.startWorkflow();
		
		User benni = new User();
		benni.setName("benni");
		benni.setId(23);
		
		//This manager calls the appropriate processor for an step
		ProcessManager manager = new ProcessManager();
		
		//Output of all items in a workflow
		for (AbstractItem ai : myWorkflow.getItem()){
			System.out.println(ai.getId());
		}
		
		Item item = (Item)myWorkflow.getItemByPos(0);
		AbstractStep step = myWorkflow.getStepByPos(0);
		
		BufferedReader inputSteam = new  BufferedReader(new InputStreamReader(System.in));

		boolean work = true;
		while(work){
			System.out.println("Nehmen Sie den Step an?" + benni.getName().toString());
			
			
			String input;
			try {
				input = inputSteam.readLine();
				
				if(!(input == "")){
					work= true;
				}
				else{
					work = false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			manager.selectProcessor(step, (Item)item, benni);		
			System.out.println("nach dem ersten Schritt "+ myWorkflow.getId());
			System.out.println("Das Item heißt: "+ item.getId());
			//System.out.println(item.getMetadata());
			for(AbstractMetaEntry ame : item.getMetadata()){
				System.out.print("1: " + ame.getGroupId() +" " +ame.getKey()+" " + ame.getValue() +"\n");
				System.out.println();
			}
			
			if(step.getNextSteps().size() != 0){
				step = step.getNextSteps().get(0);
			}
			else{
				work = false;
			}
			
		
		}
		System.out.println("ende");
	}
	
	public static Workflow createWorkflow(){
		Workflow myWorkflow= new Workflow(1);
		
		//adding steps in workflow
		myWorkflow.addStep(new Action(0*1000, 0*100, 0 + " Schritt"));
		myWorkflow.addStep(new Action(1*1000, 1*100, 1 + " Schritt"));
		myWorkflow.addStep(new FinalStep());
		myWorkflow.getStepByPos(2).setId(9999);

		//this method generates straight neighbors for steps in steplist
		myWorkflow.connectSteps();
		
		return myWorkflow;
	}
}
