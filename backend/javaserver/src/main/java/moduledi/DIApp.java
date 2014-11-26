package moduledi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import persistence.Persistence;
import processors.StartTrigger;
import manager.ProcessManager;
import abstractbeans.AbstractItem;
import abstractbeans.AbstractMetaEntry;
import abstractbeans.AbstractStep;
import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.Item;
import backingbeans.User;
import backingbeans.Workflow;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DIApp {
	
	public static void main (String args []){
		
		Injector i = Guice.createInjector(new SingleModule());
		
		Persistence p = i.getInstance(Persistence.class);
		ProcessManager pm = i.getInstance(ProcessManager.class);
		
		ProduceData pd= new ProduceData(p);
		System.out.println("User: " + p.loadUser("0"));
		System.out.println("User: " + p.loadUser("1"));
		System.out.println("User: " + p.loadUser("2"));
		System.out.println("User: " + p.loadUser("3"));
		System.out.println("User: " + p.loadUser("4"));
		
		System.out.println("Workflow: " + p.loadWorkflow(0).getStep());
		System.out.println("Workflow: " + p.loadWorkflow(1).getStep());
		System.out.println("Workflow: " + p.loadWorkflow(2).getStep());
		System.out.println("Workflow: " + p.loadWorkflow(3).getStep());
		System.out.println("Workflow: " + p.loadWorkflow(4).getStep());
		
		
		pm.startBroker();
		
		Workflow myWorkflow = createWorkflow();
		
		StartTrigger start = new StartTrigger(myWorkflow, pm, p);
		start.startWorkflow();
		
		User benni = new User();
		benni.setName("benni");
		benni.setId(23);
		
		
		//Output of all items in a workflow
		for (AbstractItem ai : myWorkflow.getItem()){
			System.out.println(ai.getId());
		}
		
		Item item = (Item)myWorkflow.getItemByPos(0);
		AbstractStep step = myWorkflow.getStepByPos(0);
		
		Item pi = (Item) p.loadItem(item.getId());
		
		for(AbstractMetaEntry ame : pi.getMetadata()){
			System.out.print("1: " + ame.getGroupId() +" " +ame.getKey()+" " + ame.getValue() +"\n");
			System.out.println();
		}
		
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
				//Loggin
			}
			
			
			pm.selectProcessor(step, (Item)item, benni);
			
			for(AbstractMetaEntry ame : pi.getMetadata()){
				System.out.print("1: " + ame.getGroupId() +" " +ame.getKey()+" " + ame.getValue() +"\n");
				System.out.println();
			}
			
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
		pm.stopBroker();
		
	}
	public static Workflow createWorkflow(){
		Workflow myWorkflow= new Workflow(1);
		
		//adding steps in workflow
		myWorkflow.addStep(new Action(0*1000, 0*100+"", 0 + " Schritt"));
		myWorkflow.addStep(new Action(1*1000, 1*100+"", 1 + " Schritt"));
		myWorkflow.addStep(new FinalStep());
		myWorkflow.getStepByPos(2).setId(9999);

		//this method generates straight neighbors for steps in steplist
		myWorkflow.connectSteps();
		
		return myWorkflow;
	}

}
