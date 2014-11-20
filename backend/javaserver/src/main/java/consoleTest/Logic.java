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
		
		myWorkflow.setId(1);
		//System.out.println("In der Logic="+myWorkflow.getStep());
		for (AbstractStep s : myWorkflow.getStep()){
			System.out.println(s.getId());
		}
		
		
		StartTrigger start = new StartTrigger(myWorkflow);
		start.startWorkflow();
		
		User benni = new User();
		benni.setName("benni");
		benni.setId(23);
		
		ProcessManager manager = new ProcessManager();
		
		//System.out.println(myWorkflow.getItem());
		for (AbstractItem ai : myWorkflow.getItem()){
			System.out.println(ai.getId());
		}
		
		Item item = (Item)myWorkflow.getItem().get(0);
		AbstractStep step = myWorkflow.getStep().get(0);
		
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
		Workflow myWorkflow= new Workflow();
		
		AbstractStep [] steps = new AbstractStep[4];
		
		//create Steps
		for(int i=0; i<3; i++ ){
			Action a = new Action();
			steps[i] = a;
			a.setUserId(i*100);
			a.setName(i+"Schritt");
			a.setId(i*1000);
			
		}
		steps[3]= new FinalStep();
		steps[3].setId(4000);
		//connecting steps
		for(int i=0; i<3; i++){
			steps[i].getNextSteps().add(steps[i+1]);
		}
		for(int i=0; i<4; i++){
			myWorkflow.getStep().add(steps[i]);
		}
		
		
		return myWorkflow;
	}
}
